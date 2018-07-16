package com.xiaowo.provider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg  implements Serializable {
    private String id;
    private String time;
    private String data;
}
