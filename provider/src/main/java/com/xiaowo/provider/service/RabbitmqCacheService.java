package com.xiaowo.provider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaowo.provider.cache.RedisCacheService;
import com.xiaowo.provider.model.CacheInfo;
import com.xiaowo.provider.model.Constants;
import com.xiaowo.provider.model.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
public class RabbitmqCacheService {
    private boolean flag = true;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private MsgSender msgSender;

    /**
     * @param msg
     * @param correlationData
     */
    public void addCache(String msg, CorrelationData correlationData) {
        Set<String> rabbitmqCache = redisCacheService.getSet(Constants.CACHENAME);
        CacheInfo cacheInfo = new CacheInfo(correlationData.getId(), new Date(), msg);
        rabbitmqCache.add(JSON.toJSONString(cacheInfo));
        redisCacheService.addSet(Constants.CACHENAME, rabbitmqCache);
    }

    /**
     * @param delId
     */
    public void del(String delId) {
        Set<String> rabbitmqCache = redisCacheService.getSet(Constants.CACHENAME);
        Iterator<String> iterator = rabbitmqCache.iterator();
        while (iterator.hasNext()) {
            CacheInfo cacheInfo = (CacheInfo) JSONObject.parse(iterator.next());
            String id = cacheInfo.getId();
            if (Objects.equals(delId, id)) {
                iterator.remove();
                break;
            }
        }
        rabbitmqCache.add(iterator.toString());
        redisCacheService.addSet(Constants.CACHENAME, rabbitmqCache);
    }

    public void start() {
        new Thread(() -> {
            while (flag) {
                try {
//                    Thread.sleep(Constants.RETRY_TIME_INTERVAL);
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long now = System.currentTimeMillis();
                Set<String> cacheInfos = redisCacheService.getSet(Constants.CACHENAME);
                cacheInfos.forEach(s -> {
//                    CacheInfo cacheInfo = (CacheInfo) JSONObject.parse(s);
                    CacheInfo cacheInfo = JSON.parseObject(s, CacheInfo.class);
                    if (null != cacheInfo) {
                        if (cacheInfo.getTime().getTime() + 3 * Constants.VALID_TIME < now) {
                            log.info("send message {} failed after 3 min ", cacheInfo);
                            this.del(cacheInfo.getId());
                        } else if (cacheInfo.getTime().getTime() + Constants.VALID_TIME < now) {
                            //switch  转换调用服务
                            msgSender.sendMsgExample(JSON.parseObject(cacheInfo.getData().toString(), Msg.class));

                        }
                    }
                });
            }
        }).start();
    }
}
