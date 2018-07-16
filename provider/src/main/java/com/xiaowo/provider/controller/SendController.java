package com.xiaowo.provider.controller;

import com.xiaowo.provider.cache.RedisCacheService;
import com.xiaowo.provider.model.Msg;
import com.xiaowo.provider.service.MsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author EDZ
 */
@RestController
public class SendController {

    @Autowired
    private MsgSender msgSender;
    @Autowired
    private RedisCacheService redisCacheService;

    @PostMapping("sendMsgExample")
    public void send(@RequestBody Msg msg) {
        System.out.println(123);
        msgSender.sendMsgExample(msg);
    }
}
