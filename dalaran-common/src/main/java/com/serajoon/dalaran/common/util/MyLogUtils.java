package com.serajoon.dalaran.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具
 *
 * @author hm 2019/1/13 9:29
 */
public abstract class MyLogUtils {
    public static Logger log(Class c) {
        return LoggerFactory.getLogger(c);
    }
}
