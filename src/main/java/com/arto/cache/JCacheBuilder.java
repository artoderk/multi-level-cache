package com.arto.cache;

import com.alibaba.fastjson.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.arto.cache.local.JLocalCache;
import com.arto.cache.local.JLocalCacheMap;

import java.util.concurrent.TimeUnit;

/**
 * 多级缓存模板生成器.
 *
 * Created by xiong.jie on 2017-07-10.
 */
public class JCacheBuilder<V> {

    /** 本地缓存大小 */
    private int maximumSize;

    /** 是否使用远程缓存 */
    private boolean useRemote;

    /** 本地缓存是否扩散到集群 */
    private boolean spreadToCluster;

    /** 扩散到集群时的泛型类型 */
    private TypeReference typeReference;

    /** 过期时间(距上次写入N秒) */
    private int expireAfterWrite;

    /** 远程缓存过期时间 */
    private int remoteExpireTime;

    /** 本地缓存名 */
    private String name;

    private JCacheBuilder(String name, TypeReference typeReference) {
        this.name = name;
        this.typeReference = typeReference;
    }

    /**
     * 新建缓存实例
     *
     * @param name 缓存名，不要重复
     * @return 缓存模板生成器
     */
    public static JCacheBuilder<Object> newBuilder(String name, TypeReference typeReference) {
        return new JCacheBuilder<Object>(name, typeReference);
    }

    /**
     * 增加远程缓存
     *
     * @param remoteExpireTime 过期时间
     * @return 缓存模板生成器
     */
    public JCacheBuilder<V> useRemote(int remoteExpireTime) {
        this.useRemote = true;
        this.remoteExpireTime = remoteExpireTime;
        return this;
    }

    /**
     * 增加远程缓存(过期时间=本地缓存过期时间*2)
     *
     * @return 缓存模板生成器
     */
    public JCacheBuilder<V> useRemote() {
        this.useRemote = true;
        return this;
    }

    /**
     * 设置本地缓存最大数量
     *
     * @param maximumSize 数量
     * @return 缓存模板生成器
     */
    public JCacheBuilder<V> maximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    /**
     * 本地缓存过期策略
     *
     * @param expireAfterWrite 过期时间
     * @return 缓存模板生成器
     */
    public JCacheBuilder<V> expireAfterWrite(int expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
        return this;
    }

    /**
     * 本地缓存更新是否更新集群本地缓存
     *
     * @return 缓存模板生成器
     */
    public JCacheBuilder<V> spreadToCluster() {
        this.spreadToCluster = true;
        return this;
    }

    /**
     * 根据配置生成多级缓存实例
     *
     * @return 多级缓存
     * @throws Exception
     */
    public <V1 extends V> JCache<V1> build() throws Exception {
        Cache<String, V1> cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES).build();
        JLocalCache<V1> localCache = new JLocalCache<V1>(cache, name, expireAfterWrite, spreadToCluster, typeReference);
        if (spreadToCluster) {
            JLocalCacheMap.getInstance().put(name, localCache);
        }

        return new JCacheTemplate<V1>(localCache, useRemote, remoteExpireTime);
    }

    public static void main(String args[]){
        /*LoadingCache<String, String> graphs = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build(
        new CacheLoader<String, String>() {
            public String load(String key) throws Exception {
                return "test";
            }
        });*/

        Cache<String, String> graphs = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }
}
