package io.temco.guhada.blockchain.service;

import io.temco.guhada.framework.model.blockchain.response.TokenAddressResponse;
import io.temco.guhada.framework.model.blockchain.response.TokenTypeResponse;
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
public interface BlockchainWalletService {
    TokenAddressResponse getEthAddress(Long userId, String tokenName) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException;

    void updateToken() throws Exception;

    UserTokenResponse getMyTokenInfo(Long userId,String tokenName ,int page, int unitPerPage) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException;

    List<TokenTypeResponse> getTokenList(Long userId) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException;
}
