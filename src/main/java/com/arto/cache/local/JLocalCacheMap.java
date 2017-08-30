package com.arto.cache.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiong.jie on 2017-07-13.
 */
public final class JLocalCacheMap {

    /** 本地缓存集合 */
    private Map<String, JLocalCache> cacheMap;

    private JLocalCacheMap() {
        cacheMap = new ConcurrentHashMap<String, JLocalCache>();
    }

    private static class JLocalCacheMapHolder{
        public static JLocalCacheMap instance = new JLocalCacheMap();
    }

    public static JLocalCacheMap getInstance() {
        return JLocalCacheMapHolder.instance;
    }

    public void put(String key, JLocalCache cache) {
        cacheMap.put(key, cache);
    }

    public JLocalCache get(String key) {
        return cacheMap.get(key);
    }

    public void remove(String key) {
        cacheMap.remove(key);
    }

    public List<JLocalCache> list() {
        List<JLocalCache> list = new ArrayList<JLocalCache>();
        for (Map.Entry<String, JLocalCache> entry : cacheMap.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }
}
