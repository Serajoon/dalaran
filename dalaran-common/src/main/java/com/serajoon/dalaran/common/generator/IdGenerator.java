package com.serajoon.dalaran.common.generator;

import java.util.UUID;


public abstract class IdGenerator {

    /**
     * 生成32位随机主键
     * serajoon-2019/1/6 8:50
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
