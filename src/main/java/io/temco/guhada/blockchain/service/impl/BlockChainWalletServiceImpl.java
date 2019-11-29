package io.temco.guhada.blockchain.service.impl;

import io.temco.guhada.blockchain.mapper.UserTokenMapper;
import io.temco.guhada.blockchain.model.AirdropUser;
import io.temco.guhada.blockchain.model.UserTokenAccount;
import io.temco.guhada.blockchain.model.UserTokenHistory;
import io.temco.guhada.blockchain.model.request.PointRequest;
import io.temco.guhada.blockchain.repository.UserTokenAccountRepository;
import io.temco.guhada.blockchain.repository.UserTokenHistoryRepository;
import io.temco.guhada.blockchain.service.BlockchainWalletService;
import io.temco.guhada.blockchain.service.retrofit.BenefitApiService;
import io.temco.guhada.framework.exception.GuhadaApiRuntimeException;
import io.temco.guhada.framework.model.benefit.enums.ServiceType;
import io.temco.guhada.framework.model.blockchain.enums.TokenActionType;
import io.temco.guhada.framework.model.blockchain.enums.TokenType;
import io.temco.guhada.framework.model.blockchain.response.TokenAddressResponse;
import io.temco.guhada.framework.model.blockchain.response.TokenTypeResponse;
import io.temco.guhada.framework.model.blockchain.response.UserTokenItemResponse;
import io.temco.guhada.framework.model.blockchain.response.UserTokenResponse;
import io.temco.guhada.framework.model.point.enums.PointType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Shin Han
 * Since 2019-11-20
 */
@Slf4j
@Service
public class BlockChainWalletServiceImpl implements BlockchainWalletService {

    @Value("${smart-contract.chain-url}")
    private String chainUrl;

    private Web3j web3j;

    private final BigInteger ether = new BigInteger("1000000000000000000");

    @Autowired
    private UserTokenAccountRepository userTokenAccountRepository;

    @Autowired
    private UserTokenHistoryRepository userTokenHistoryRepository;

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private BenefitApiService benefitApiService;

    @Value("${smart-contract.guhada-token-contract}")
    private String guhadaTokenContract;

    @PostConstruct
    private void initWeb3j(){
        web3j = Web3j.build(new HttpService(chainUrl));
    }

    @Override
    public TokenAddressResponse getEthAddress(Long userId, String tokenName) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException {
        TokenType tokenType = TokenType.valueOf(tokenName);
        if(ObjectUtils.isEmpty(tokenType)) {
            throw new GuhadaApiRuntimeException("TokenName error"); // TODO : 임시로 처리
        }
        Optional<UserTokenAccount> optionalUserTokenAccount = userTokenAccountRepository.findById(userId);
        UserTokenAccount userTokenAccount;
        if(optionalUserTokenAccount.isPresent()){
            userTokenAccount = optionalUserTokenAccount.get();
        }else {
            StringBuffer seed = new StringBuffer(userId.toString());
            Random rnd = new Random();
            for (int i = 0; i < 20; i++) {
                int rIndex = rnd.nextInt(3);
                switch (rIndex) {
                    case 0: // '\0'
                        seed.append((char) (rnd.nextInt(26) + 97));
                        break;

                    case 1: // '\001'
                        seed.append((char) (rnd.nextInt(26) + 65));
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
            userTokenAccount = new UserTokenAccount();
            userTokenAccount.setUserId(userId);
            userTokenAccount.setPublicKey((new StringBuilder()).append("0x").append(sAddress).toString());
            userTokenAccount.setPrivateKey(sPrivatekeyInHex);
            userTokenAccount.setCurrentBalance(BigInteger.ZERO);
            userTokenAccount.setTransferPointBalance(BigInteger.ZERO);
            userTokenAccountRepository.save(userTokenAccount);
        }

        String qrImageUrl = "";

        TokenAddressResponse tokenAddressResponse = TokenAddressResponse.builder()
                .tokenName(tokenType.name())
                .tokenNameText(tokenType.getTokenText())
                .publicKey(userTokenAccount.getPublicKey())
                .tokenImageUrl(tokenType.getImageUrl())
                .tokenRatio(tokenType.getTokenRatio())
                .pointRatio(tokenType.getPointRatio())
                .qrImageUrl(qrImageUrl)
                .build();

        return tokenAddressResponse;
    }

    @Override
    public void updateToken() throws Exception {
        List<UserTokenAccount> userTokenAccountList = userTokenAccountRepository.findAll();
        for (UserTokenAccount userTokenAccount: userTokenAccountList) {
            TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, userTokenAccount.getPublicKey());
            ERC20 guhadaToken = ERC20.load(guhadaTokenContract, web3j, transactionManager, new DefaultGasProvider());

            BigInteger balance = guhadaToken.balanceOf(userTokenAccount.getPublicKey()).send().divide(ether);
            if (userTokenAccount.getCurrentBalance().compareTo(balance) != 0) { // 기존이랑 차이가 있는 경우
                BigInteger subtractBalance = balance.subtract(userTokenAccount.getCurrentBalance());
                UserTokenHistory userTokenHistory = UserTokenHistory.builder()
                        .userId(userTokenAccount.getUserId())
                        .changedBalance(subtractBalance)
                        .actionType(TokenActionType.SAVE.name())
                        .tokenName(TokenType.GUHADA.name())
                        .build();
                userTokenHistoryRepository.save(userTokenHistory);
                userTokenAccount.setCurrentBalance(balance);

            }

            if(userTokenAccount.getCurrentBalance().compareTo(userTokenAccount.getTransferPointBalance()) != 0){
                BigInteger transferPointBalance = userTokenAccount.getCurrentBalance().subtract(userTokenAccount.getTransferPointBalance());
                TokenType guhadaAirdrop = TokenType.GUHADA;
                PointRequest pointRequest = PointRequest.builder().pointType(PointType.TOKEN_POINT).serviceType(ServiceType.SYSTEM).chargePrice(transferPointBalance.longValue()).build();
                benefitApiService.savePoint(pointRequest, userTokenAccount.getUserId()).execute();
                UserTokenHistory userTokenHistory = UserTokenHistory.builder()
                        .userId(userTokenAccount.getUserId())
                        .changedBalance(BigInteger.valueOf(pointRequest.getChargePrice()))
                        .actionType(TokenActionType.CHANGE_POINT.name())
                        .tokenName(guhadaAirdrop.name())
                        .build();
                userTokenAccount.setTransferPointBalance(userTokenAccount.getTransferPointBalance().add(transferPointBalance));
                userTokenHistoryRepository.save(userTokenHistory);
            }

            userTokenAccountRepository.save(userTokenAccount);

        }

    }

    @Override
    public UserTokenResponse getMyTokenInfo(Long userId,String tokenName, int page, int unitPerPage) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException {

        if(ObjectUtils.isEmpty(TokenType.valueOf(tokenName))) {
            throw new GuhadaApiRuntimeException("TokenName error"); // TODO : 임시로 처리
        }
        TokenType tokenType = TokenType.valueOf(tokenName);
        Optional<UserTokenAccount> optionalUserTokenAccount = userTokenAccountRepository.findById(userId);
        if(!optionalUserTokenAccount.isPresent()){
            getEthAddress(userId,tokenName);
        }
        UserTokenAccount userTokenAccount = userTokenAccountRepository.getOne(userId);
        BigInteger currentTokenBalance = userTokenAccount.getCurrentBalance().subtract(userTokenAccount.getTransferPointBalance());
        int startIndex = page - 1; // cause : mysql zero base
        int myTokenInfoListCount = userTokenMapper.getMyTokenInfoCount(userId);
        int totalPage = (myTokenInfoListCount % unitPerPage) == 0 ? (myTokenInfoListCount / unitPerPage) : (myTokenInfoListCount / unitPerPage) + 1;
        List<UserTokenItemResponse> myTokenInfoList = userTokenMapper.getMyTokenInfo(userId,tokenName, startIndex * unitPerPage, unitPerPage);

        return UserTokenResponse.of(tokenName,tokenType.getImageUrl(), currentTokenBalance,page,myTokenInfoListCount, totalPage, myTokenInfoList);
    }

    @Override
    public List<TokenTypeResponse> getTokenList(Long userId) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException {

        Optional<UserTokenAccount> optionalUserTokenAccount = userTokenAccountRepository.findById(userId);
        if(!optionalUserTokenAccount.isPresent()){
            getEthAddress(userId,TokenType.GUHADA.name());
        }
        UserTokenAccount userTokenAccount = userTokenAccountRepository.getOne(userId);
        BigInteger currentTokenBalance = userTokenAccount.getCurrentBalance().subtract(userTokenAccount.getTransferPointBalance());
        List<TokenTypeResponse> tokenTypeList = new ArrayList<>();

        TokenTypeResponse tokenTypeResponse = TokenTypeResponse.builder()
                .tokenName(TokenType.GUHADA.name())
                .tokenNameText(TokenType.GUHADA.getTokenText())
                .tokenImageUrl(TokenType.GUHADA.getImageUrl())
                .balance(currentTokenBalance)
                .build();
        tokenTypeList.add(tokenTypeResponse);
        return tokenTypeList;
    }

    @Override
    public void tokenTransfer() throws Exception {
        Credentials credentials = Credentials.create("");

        List<AirdropUser> airdropUsers = userTokenMapper.getAirdropUsers();
        for (AirdropUser airdropUser: airdropUsers) {
            ERC20 guhadaToken = ERC20.load(guhadaTokenContract, web3j, credentials, new DefaultGasProvider());


            TransactionReceipt send = guhadaToken.transfer(airdropUser.getAddress(), new BigInteger(airdropUser.getGuhadaAmount())).send();


            if(send.getStatus().equals("0x1")) {

                userTokenMapper.updateSuccess(airdropUser.getId());
            }
        }

    }

}
