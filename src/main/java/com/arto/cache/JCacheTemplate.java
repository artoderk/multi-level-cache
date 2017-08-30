package com.arto.cache;

import com.arto.cache.common.Constants;
import com.arto.cache.local.JLocalCache;
import com.arto.cache.local.JLocalCacheCluster;
import com.arto.cache.local.JLocalCacheListener;
import com.arto.cache.local.JLocalCacheMap;
import com.arto.cache.remote.JRemoteCache;
import com.arto.cache.tool.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * Created by xiong.jie on 2017-07-11.
 */
@Slf4j
public class JCacheTemplate<V> implements JCache<V>{

    /** 本地缓存 */
    private JLocalCache<V> localCache;

    /** 远程缓存 */
    private volatile static JRemoteCache remoteCache;

    /** 本地缓存是否集群更新 */
    private volatile static boolean localCacheListenerFlag;

    /** 是否使用远程缓存 */
    private boolean useRemote;

    /** 远程缓存过期时间 */
    private int expireTime;


    /**
     * 使用多级缓存
     *
     * @param localCache 本地缓存
     * @param useRemote 是否使用远程缓存
     */
    protected <V1 extends V> JCacheTemplate(JLocalCache<V> localCache, boolean useRemote) throws Exception {
        this(localCache, useRemote, localCache.getExpireAfterWrite() * 2);
    }

    /**
     * 使用多级缓存
     *
     * @param localCache 本地缓存
     * @param useRemote 是否使用远程缓存
     * @param remoteExpireTime 远程缓存过期时间
     */
    protected <V1 extends V> JCacheTemplate(JLocalCache<V> localCache, boolean useRemote, int remoteExpireTime) throws Exception {
        this.localCache = localCache;
        this.expireTime = remoteExpireTime;
        this.useRemote = useRemote;
        if (useRemote) initRemoteCache();

        // 如果选择扩散到集群，则利用Redis的pub/sub
        if (localCache.isSpreadToCluster()) {
            JLocalCacheMap.getInstance().put(localCache.getName(), localCache);
            if (!localCacheListenerFlag) {
                synchronized (JCacheTemplate.class) {
                    if (!localCacheListenerFlag) {
                        if (remoteCache == null) initRemoteCache();
                        remoteCache.subscribe(new JLocalCacheListener(), Constants.LOCAL_CACHE_CLUSTER);
                        localCacheListenerFlag = true;
                    }
                }
            }
            remoteCache.subscribe(new JLocalCacheListener(), "");
        }
    }

    /**
     * 更新多级缓存。
     *
     * @param key 主键
     * @throws Exception
     */
    public void set(String key, V value) throws Exception {
        if (key == null || value == null) {
            throw new Exception("Parameter can't be null.");
        }
        updateCache(key, null, value);
    }

    /**
     * (Redis.HashMap)更新多级缓存。
     *
     * @param key 主键1
     * @param valueKey 主键2
     * @param value 值
     * @throws Exception
     */
    public void hSet(String key, String valueKey, V value) throws Exception {
        if (key == null || valueKey == null || value == null) {
            throw new Exception("Parameter can't be null.");
        }
        updateCache(key, valueKey, value);
    }

    /**
     * 使用CAS策略更新多级缓存(需要强一致性时使用此方法)。
     *
     * @param key 主键
     * @throws Exception
     */
    @Deprecated
    public boolean compareAndSet(String key, JCasCallable<V> value) throws Exception {
        if (key == null || value == null) {
            throw new Exception("Parameter can't be null.");
        }
        // 更新DB与远程缓存
        if (remoteCache.compareAndSet(key, value, expireTime)){
            return true;
        }

        localCacheIsSpreadToCluster(key, null);
        return false;
    }

    /**
     * 强一致性更新DB与缓存(调用此方法前需使用悲观锁，callable中只实现更新DB方法，逻辑处理在调用前处理)。
     *
     * @param key 主键
     * @param callable 回调
     * @throws Exception
     */
    @Deprecated
    public boolean setDC(String key, Callable<V> callable) throws Exception {
        if (key == null || callable == null) {
            throw new Exception("Parameter can't be null.");
        }
        // 更新DB与远程缓存
        if (remoteCache.setDC(key, callable, expireTime)){
            localCacheIsSpreadToCluster(key, null);
            return true;
        }

        return false;
    }

    /**
     * 多级缓存获取数据
     *
     * @param key 主键
     * @return 结果
     * @throws Exception
     */
    public V get(String key) throws Exception {
        if (key == null) {
            throw new Exception("Parameter can't be null.");
        }
        V value = localCache.getCache().getIfPresent(key);
        if (value == null) {
            if (useRemote) {
                value = remoteCache.get(key, localCache.getTypeReference());
            }
        }
        return value;
    }

    /**
     * 从多级缓存中获取指定对象。
     *
     * @param key 主键
     * @param clazz 类
     * @return 对象
     * @throws Exception
     */
    public V get(String key, Class<V> clazz) throws Exception {
        if (key == null || clazz == null) {
            throw new Exception("Parameter can't be null.");
        }
        V value = localCache.getCache().getIfPresent(key);
        if (value == null && useRemote) {
            value = remoteCache.get(key, clazz);
        }
        return value;
    }

    /**
     * 从多级缓存中获取指定对象，如果对象不存在，根据回调方法获取对象并加载到缓存。
     *
     * @param key 主键
     * @param clazz 类
     * @param callable 回调
     * @return 对象
     * @throws Exception
     */
    public V get(String key, Class<V> clazz, Callable<V> callable) throws Exception {
        if (key == null || clazz == null || callable == null) {
            throw new Exception("Parameter can't be null.");
        }
        V value = localCache.getCache().getIfPresent(key);
        if (value == null && useRemote) {
            value = remoteCache.get(key, clazz);
        }
        if (value == null){
            synchronized (this) {
                try{
                    value = callable.call();
                    updateCache(key, null, value);
                } catch (Exception e){
                    log.error("Set value to remoteCache failed. key=" + key + ", value=" + value, e);
                }
            }
        }
        return value;
    }

    /**
     * (Redis.HashMap)从多级缓存中获取指定对象，如果对象不存在，根据回调方法获取对象并加载到缓存。
     *
     * @param key 主键1
     * @param valueKey 主键2
     * @param clazz 类
     * @param callable 回调
     * @return 对象
     * @throws Exception
     */
    public V hGet(String key, String valueKey, Class<V> clazz, Callable<V> callable) throws Exception {
        if (key == null || valueKey == null || clazz == null || callable == null) {
            throw new Exception("Parameter can't be null.");
        }
        V value = localCache.getCache().getIfPresent(valueKey);
        if (value == null && useRemote) {
            value = remoteCache.hashGet(key, valueKey, clazz);
        }
        if (value == null){
            try{
                value = callable.call();
                updateCache(key, valueKey, value);
            } catch (Exception e){
                log.error("Set value to cache failed. key=" + key + ", value=" + value, e);
                // 因为hashmap没有单个key的过期设置，为了避免redis和db长期不一致，抛出错误
                throw e;
            }
        }
        return value;
    }

    /**
     * 从多级缓存中删除指定对象
     *
     * @param key 主键
     */
    @Override
    public void del(String... key) throws Exception{
        for (String k : key){
            delCache(k, null);
        }
    }

    /**
     * (Redis.HashMap)从多级缓存中删除指定对象
     *
     * @param key 主键1
     * @param valueKey 主键2
     */
    public void hDel(String key, String valueKey) throws Exception {
        delCache(key, valueKey);
    }

    private void updateCache(String key, String valueKey, V value) throws Exception {
        updateRemoteCache(key, valueKey, value);
        localCacheIsSpreadToCluster(key, value);
    }

    private void localCacheIsSpreadToCluster(String key, V value) throws Exception {
        if (localCache.isSpreadToCluster()) {
            remoteCache.publishToCluster(new JLocalCacheCluster<V>(localCache.getName(), key, value));
        } else {
            localCache.getCache().put(key, value);
        }
    }

    private void updateRemoteCache(String key, String valueKey, V value){
        if (useRemote) {
            if (valueKey != null){
                remoteCache.hashSet(key, valueKey, value);
            } else {
                remoteCache.set(key, value, expireTime);
            }
        }
    }

    private static synchronized void initRemoteCache(){
        if (remoteCache == null) {
            remoteCache = SpringContextHolder.getBean("jRemoteCache");
        }
    }

    private void delCache(String key, String valueKey) throws Exception {
        if (useRemote) {
            if (valueKey != null) {
                remoteCache.hashDel(key, valueKey);
            } else {
                remoteCache.del(key);
            }
        }
        if (localCache.isSpreadToCluster()) {
            remoteCache.publishToCluster(new JLocalCacheCluster<V>(localCache.getName(), key, null));
        } else {
            localCache.getCache().invalidate(key);
        }
    }
}
