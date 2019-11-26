package io.temco.guhada.blockchain.service;

import io.temco.guhada.framework.model.blockchain.response.UserTokenResponse;
import org.web3j.crypto.CipherException;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

/**
 * Created by Shin Han
 * Since 2019-11-20
 */
public interface Web3jService {
    String generateAddress(Long userId) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException;

    void updateToken() throws Exception;

    UserTokenResponse getMyTokenInfo(Long userId, int page, int unitPerPage) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException;
}
