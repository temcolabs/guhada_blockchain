package io.temco.guhada.blockchain.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class HashUtil {

    private HashUtil(){}

//    private static final HashFunction sha = Hashing.sha512();
//    private static final Charset utf8 = Charset.forName("UTF-8");
//
//    public static String getSha(String passwd) {
//        return sha.hashString(passwd, utf8).toString();
//    }

    public static String sha512(String original) {
        return sha512(original, StandardCharsets.UTF_8);
    }

    public static String sha512(String original, Charset charset) {
        return DigestUtils.sha512Hex(StringUtil.getBytes(original, charset));
    }

    public static String sha256(String original) {
        return sha256(original, StandardCharsets.UTF_8);
    }

    public static String sha256(String original, Charset charset) {
        return DigestUtils.sha256Hex(StringUtil.getBytes(original, charset));
    }
}
