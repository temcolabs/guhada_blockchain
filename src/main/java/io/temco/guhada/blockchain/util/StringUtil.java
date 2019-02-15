package io.temco.guhada.blockchain.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class StringUtil {

    private StringUtil(){}

    public static boolean isEmpty(String s) {
        return s==null || s.trim().isEmpty();
    }

    public static final String NONE = "none";
    public static String noneIfEmpty(String s) {
        return isEmpty(s) ? NONE : s;
    }

    public static String nullIfEmpty(String s) {
        return isEmpty(s) ? null : s;
    }

    public static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    public static String getLowerCase(String name) {
        return emptyIfNull(name).toLowerCase();
    }


    public static String trim(String s) {
        if(isEmpty(s)) return s;
        return s.trim();
    }

    public static byte[] getBytes(String original, Charset charset) {
        if(original==null) return new byte[0];
        return original.getBytes(charset);
    }
    public static byte[] getBytesUtf8(String orignal) {
        return getBytes(orignal, StandardCharsets.UTF_8);
    }

    public static byte[] getBytesUtf16(String orignal) {
        return getBytes(orignal, StandardCharsets.UTF_16);
    }

    public static long getLongHasCode(String s) {
        String lc = s==null? "\u0000" : s.toLowerCase(); // ignore case
        long firstHash  = lc.hashCode()      & 0xffffffffL;
        long secondHash = secondHashCode(lc) & 0xffffffffL;
        long length     = lc.length()        & 0xffffffffL;
        return ((firstHash) << 32) |               // 32 bit first hashcode
                ((secondHash & 0x3fffff)  << 10) | // 22 bit second hashcode
                (length & 0x3ff);                  // 10 bit length
    }

    // same as java's hashCode except using a different prime number multiplier.
    private static int secondHashCode(String s) {
        int h = 0;
        for (int i=0;i<s.length();i++)
            h = 37*h + s.charAt(i);
        return h;
    }
}
