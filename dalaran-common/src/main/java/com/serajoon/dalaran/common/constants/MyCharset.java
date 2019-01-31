package com.serajoon.dalaran.common.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 系统默认编码
 *
 * @author hanmeng
 * @since 2019/1/24 11:09
 */
public class MyCharset {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static final String DEFAULT_CHARSET_TEXT = DEFAULT_CHARSET.name();
}
