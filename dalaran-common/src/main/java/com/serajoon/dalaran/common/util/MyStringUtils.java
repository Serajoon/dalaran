package com.serajoon.dalaran.common.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public abstract class MyStringUtils {
    /**
     * The empty string.
     */
    public static final String EMPTY = "";

    /**
     *
     * @param input
     * @return
     */
    @SuppressWarnings("all")
    public static String md5(String input) {
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(input, Charsets.UTF_8);
        return hasher.hash().toString();
    }


}
