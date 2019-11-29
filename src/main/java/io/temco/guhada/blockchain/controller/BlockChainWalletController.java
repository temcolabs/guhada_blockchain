package io.temco.guhada.blockchain.controller;

import io.swagger.annotations.ApiOperation;
import io.temco.guhada.blockchain.service.BlockchainWalletService;
import io.temco.guhada.framework.controller.BaseController;
import io.temco.guhada.framework.model.blockchain.response.TokenAddressResponse;
import io.temco.guhada.framework.model.blockchain.response.TokenTypeResponse;
import io.temco.guhada.framework.model.blockchain.response.UserTokenResponse;
import io.temco.guhada.framework.model.response.GuhadaApiResponse;
import io.temco.guhada.framework.model.user.GuhadaUserDetails;
import io.temco.guhada.framework.util.UserDetailsHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.CipherException;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by Shin Han
 * Since 2019-11-20
 */
@Slf4j
@RequestMapping("/blockchain-wallet")
@RestController
public class BlockChainWalletController extends BaseController {

    @Autowired
    private BlockchainWalletService web3jService;

    @GetMapping("/token-list")
    @ApiOperation(value = "token-list", notes = "구하다의 전체 토큰리스트와 내가 가진 토큰 보유량", response = TokenTypeResponse.class)
    public ResponseEntity<GuhadaApiResponse> getTokenList() throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException
    {
        GuhadaUserDetails guhadaUserDetails = UserDetailsHolder.getUserDetails();
        Long userId = guhadaUserDetails.getUserId();
        return responseApi(web3jService.getTokenList(userId));
    }

    @GetMapping("/token-address")
    @ApiOperation(value = "token-address", notes = "해당하는 토큰의 입금 계좌관련된 정보", response = TokenAddressResponse.class)
    public ResponseEntity<GuhadaApiResponse> getTokenAddress(@RequestParam String tokenName) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException
    {
        GuhadaUserDetails guhadaUserDetails = UserDetailsHolder.getUserDetails();
        Long userId = guhadaUserDetails.getUserId();
        return responseApi(web3jService.getEthAddress(userId,tokenName));
    }

//    @Deprecated
//    @GetMapping("/updatetoken") // TEST 용도
//    public ResponseEntity<GuhadaApiResponse> updateToken() throws Exception {
//        web3jService.updateToken();
//        return responseApi(null);
//    }
//
//    @Deprecated
//    @GetMapping("/tokentransfer") // TEST 용도
//    public ResponseEntity<GuhadaApiResponse> tokenTransfer() throws Exception {
//        web3jService.tokenTransfer();
//        return responseApi(null);
//    }

    @GetMapping("/my-token-history")
    @ApiOperation(value = "my-token-history", notes = "해당하는 토큰의 입금 및 출금 상세정보", response = UserTokenResponse.class)
    public ResponseEntity<GuhadaApiResponse> getMyTokenHistory(@RequestParam(value="tokenName") String tokenName,
                                                               @RequestParam(value ="page")int page,
                                                               @RequestParam(value ="unitPerPage", required = false) Integer unitPerPage) throws NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CipherException {
        GuhadaUserDetails guhadaUserDetails = UserDetailsHolder.getUserDetails();
        Long userId = guhadaUserDetails.getUserId();

        if(unitPerPage == null){
            unitPerPage = 10;
        }
        return responseApi(web3jService.getMyTokenInfo(userId,tokenName,page,unitPerPage));
    }

}
