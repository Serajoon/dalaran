package com.serajoon.dalaran.common.enums;

public enum FileUpLoadEnum {
    FTP(1, "FTP"),
    LOCAL(2, "LOCAL");

    private final int value;
    private final String desc;

    FileUpLoadEnum(int value, String desc) {
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
