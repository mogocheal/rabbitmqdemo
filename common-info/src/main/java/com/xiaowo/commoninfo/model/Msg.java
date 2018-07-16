package com.xiaowo.commoninfo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Msg implements Serializable {
    private String id;
    private String msg;
}
