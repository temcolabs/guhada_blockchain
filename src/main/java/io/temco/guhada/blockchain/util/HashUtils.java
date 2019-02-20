package io.temco.guhada.blockchain.util;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class HashUtils {
    private static final HashFunction sha = Hashing.sha512();
    private static final Charset utf8 = Charset.forName("UTF-8");

    public static String getSha(String passwd) {
        return sha.hashString(passwd, utf8).toString();
    }
}