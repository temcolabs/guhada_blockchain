package io.temco.guhada.blockchain.service.impl;

import io.temco.guhada.blockchain.mapper.UserTokenMapper;
import io.temco.guhada.blockchain.model.UserTokenAccount;
import io.temco.guhada.blockchain.model.UserTokenHistory;
import io.temco.guhada.blockchain.model.request.PointRequest;
import io.temco.guhada.blockchain.repository.UserTokenAccountRepository;
import io.temco.guhada.blockchain.repository.UserTokenHistoryRepository;
import io.temco.guhada.blockchain.service.Web3jService;
import io.temco.guhada.blockchain.service.retrofit.BenefitApiService;
import io.temco.guhada.framework.model.benefit.enums.ServiceType;
import io.temco.guhada.framework.model.blockchain.response.UserTokenItemResponse;
import io.temco.guhada.framework.model.blockchain.response.UserTokenResponse;
import io.temco.guhada.framework.model.point.enums.PointType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Shin Han
 * Since 2019-11-20
 */
@Slf4j
@Service
public class Web3jServiceImpl implements Web3jService {

    @Value("${smart-contract.chain-url}")
    private String chianUrl;

    private Web3j web3j;

    @Autowired
    private UserTokenAccountRepository userTokenAccountRepository;

    @Autowired
    private UserTokenHistoryRepository userTokenHistoryRepository;

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private BenefitApiService benefitApiService;

    @Value("${smart-contract.guhada-token-contract}")
    private String guhadaTokencontract;

    @PostConstruct
    private void initWeb3j(){
        web3j = Web3j.build(new HttpService(chianUrl));
    }

    @Override
    public String generateAddress(Long userId) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException {
        Optional<UserTokenAccount> optionalUserTokenAccount = userTokenAccountRepository.findById(userId);
        if(optionalUserTokenAccount.isPresent()){
            return optionalUserTokenAccount.get().getPublicKey();
        }
        StringBuffer seed = new StringBuffer(userId.toString());
        Random rnd = new Random();
        for(int i = 0; i < 20; i++)
        {
            int rIndex = rnd.nextInt(3);
            switch(rIndex)
            {
                case 0: // '\0'
                    seed.append((char)(rnd.nextInt(26) + 97));
                    break;

                case 1: // '\001'
                    seed.append((char)(rnd.nextInt(26) + 65));
                    break;

                case 2: // '\002'
                    seed.append(rnd.nextInt(10));
                    break;
            }
        }

        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
        String sPrivatekeyInHex = privateKeyInDec.toString(16);
        WalletFile aWallet = Wallet.createLight(seed.toString(), ecKeyPair);
        String sAddress = aWallet.getAddress();
        UserTokenAccount userTokenAccount = new UserTokenAccount();
        userTokenAccount.setUserId(userId);
        userTokenAccount.setPublicKey((new StringBuilder()).append("0x").append(sAddress).toString());
        userTokenAccount.setPrivateKey(sPrivatekeyInHex);
        userTokenAccountRepository.save(userTokenAccount);
        return userTokenAccount.getPublicKey();
    }

    @Override
    public void updateToken() throws Exception {
        List<UserTokenAccount> userTokenAccountList = userTokenAccountRepository.findAll();
        for (UserTokenAccount userTokenAccount: userTokenAccountList) {
            TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, userTokenAccount.getPublicKey());
            ERC20 guhadaToken = ERC20.load(guhadaTokencontract, web3j, transactionManager, new DefaultGasProvider());

            BigInteger balance = guhadaToken.balanceOf(userTokenAccount.getPublicKey()).send();
            if (userTokenAccount.getCurrentBalance().compareTo(balance) != 0) { // 기존이랑 차이가 있는 경우
                BigInteger subtractBalance = balance.subtract(userTokenAccount.getCurrentBalance());
                UserTokenHistory userTokenHistory = UserTokenHistory.builder()
                        .userId(userTokenAccount.getUserId())
                        .changedBalance(subtractBalance)
                        .build();
                userTokenHistoryRepository.save(userTokenHistory);
                userTokenAccount.setCurrentBalance(balance);

            }

            if(userTokenAccount.getCurrentBalance().compareTo(userTokenAccount.getTransferPointBalance()) != 0){
                BigInteger requestPointBalance = userTokenAccount.getTransferPointBalance().subtract(userTokenAccount.getCurrentBalance());// TODO : 포인트 변환비율 알아야함
                PointRequest pointRequest = PointRequest.builder().pointType(PointType.TOKEN_POINT).serviceType(ServiceType.SYSTEM).chargePrice(requestPointBalance.longValue()).build();
                benefitApiService.savePoint(pointRequest).execute();
                userTokenAccount.setTransferPointBalance(userTokenAccount.getTransferPointBalance().add(requestPointBalance));
            }

            userTokenAccountRepository.save(userTokenAccount);

        }

    }

    @Override
    public UserTokenResponse getMyTokenInfo(Long userId, int page, int unitPerPage) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException {
        Optional<UserTokenAccount> optionalUserTokenAccount = userTokenAccountRepository.findById(userId);
        if(!optionalUserTokenAccount.isPresent()){
            generateAddress(userId);
        }
        BigInteger currentTokenBalance = userTokenAccountRepository.getOne(userId).getCurrentBalance();
        int startIndex = page - 1; // cause : mysql zero base
        int myTokenInfoListCount = userTokenMapper.getMyTokenInfoCount(userId);
        int totalPage = (myTokenInfoListCount % unitPerPage) == 0 ? (myTokenInfoListCount / unitPerPage) : (myTokenInfoListCount / unitPerPage) + 1;
        List<UserTokenItemResponse> myTokenInfoList = userTokenMapper.getMyTokenInfo(userId, startIndex, unitPerPage);

        return UserTokenResponse.of(currentTokenBalance,page,myTokenInfoListCount, totalPage, myTokenInfoList);
    }

}
