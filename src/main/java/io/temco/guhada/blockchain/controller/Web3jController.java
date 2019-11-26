package io.temco.guhada.blockchain.controller;

import io.temco.guhada.blockchain.service.Web3jService;
import io.temco.guhada.framework.controller.BaseController;
import io.temco.guhada.framework.model.response.GuhadaApiResponse;
import io.temco.guhada.framework.model.user.GuhadaUserDetails;
import io.temco.guhada.framework.util.UserDetailsHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
public class Web3jController extends BaseController {

    @Autowired
    private Web3jService web3jService;

    @PostMapping("/generateAddress")
    public ResponseEntity<GuhadaApiResponse> generateAddress() throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException
    {
        GuhadaUserDetails guhadaUserDetails = UserDetailsHolder.getUserDetails();
        Long userId = guhadaUserDetails.getUserId();
        return responseApi(web3jService.generateAddress(userId));
    }

    @GetMapping("/updatetoken") // TEST 용도
    public ResponseEntity<GuhadaApiResponse> updateToken() throws Exception {
        web3jService.updateToken();
        return responseApi(null);
    }

    @GetMapping("/my-token")
    public ResponseEntity<GuhadaApiResponse> getMyToken(@RequestParam(value ="page")int page,
                                                        @RequestParam(value ="unitPerPage", required = false) Integer unitPerPage) throws NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CipherException {
        GuhadaUserDetails guhadaUserDetails = UserDetailsHolder.getUserDetails();
        Long userId = guhadaUserDetails.getUserId();

        if(unitPerPage == null){
            unitPerPage = 10;
        }
        return responseApi(web3jService.getMyTokenInfo(userId,page,unitPerPage));
    }

}
