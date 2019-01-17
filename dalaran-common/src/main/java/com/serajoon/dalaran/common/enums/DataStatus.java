package com.serajoon.dalaran.common.enums;

/**
 * 数据状态
 *
 * @author hm 2019/1/15 10:42
 */
public enum DataStatus {
    NORMAL(1, "正常"),
    HIDE(0, "隐藏"),
    DELETE(-1, "删除");

    private final int value;
    private final String desc;

    DataStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return desc;
    }
}
