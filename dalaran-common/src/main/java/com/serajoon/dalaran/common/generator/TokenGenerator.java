package com.serajoon.dalaran.common.generator;


import com.serajoon.dalaran.common.util.MyStringUtils;

public abstract class TokenGenerator {

    /**
     * 根据id和当前时间生成token
     * @param id
     * @return
     */
    public static String generate(String id) {
        return MyStringUtils.md5(id + System.currentTimeMillis());
    }
}
