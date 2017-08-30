package com.arto.cache.local;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arto.cache.common.Constants;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

/**
 * 集群本地缓存同步
 *
 * Created by xiong.jie on 2017-07-13.
 */
@Slf4j
public class JLocalCacheListener extends JedisPubSub {

    @SuppressWarnings("unchecked")
    public void onMessage(String channel, String message) {
        log.info("Received listener, channel:" + channel + " message:" + message);
        try {
            if (channel.endsWith(Constants.LOCAL_CACHE_CLUSTER)) {
                JSONObject jsonObject = JSON.parseObject(message);
                String name = (String)jsonObject.get("name");
                String key = (String)jsonObject.get("key");
                String value = jsonObject.get("value").toString();
                JLocalCache jLocalCache = JLocalCacheMap.getInstance().get(name);
                if (Strings.isNullOrEmpty(value)) {
                    jLocalCache.getCache().invalidate(jsonObject.get("key"));
                } else {
                    jLocalCache.getCache().put(key, JSON.parseObject(value, jLocalCache.getTypeReference()));
                }
            }
        } catch (Exception e){
            log.error("Update local cache failed.", e);
        }
    }

}
