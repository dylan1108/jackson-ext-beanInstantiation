package com.jackson.ext.test.Enum;

public enum MockEnumWithNonDefaultCreator {
    YES(0),NO(1);
    private int code;

    MockEnumWithNonDefaultCreator(int code) {
        this.code = code;
    }
}