package com.xiaowo.commoninfo.basicEnums;

import lombok.Data;

public enum ExchangeEnum {

    HELLO("1", "hello");
    private String id;
    private String name;

    ExchangeEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
