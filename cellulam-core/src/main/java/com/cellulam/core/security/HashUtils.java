package com.cellulam.core.security;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author eric.li
 * @date 2022-06-06 13:35
 */
public abstract class HashUtils {

    public final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static String sha32(String s) {
        return Hashing.goodFastHash(32).hashString(s, DEFAULT_CHARSET).toString();
    }

    public static String sha128(String s) {
        return Hashing.goodFastHash(128).hashString(s, DEFAULT_CHARSET).toString();
    }

    public static String sha256(String s) {
        return Hashing.sha256().hashString(s, DEFAULT_CHARSET).toString();
    }

    public static String sha512(String s) {
        return Hashing.sha512().hashString(s, DEFAULT_CHARSET).toString();
    }
}
