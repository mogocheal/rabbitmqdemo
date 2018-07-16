package com.xiaowo.provider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheInfo<T> {
    private String id;
    private Date time;
    private T data;

}
