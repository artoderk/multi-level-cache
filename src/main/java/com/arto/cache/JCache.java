package com.arto.cache;

import java.util.concurrent.Callable;

/**
 * Created by xiong.jie on 2017-07-10.
 */
public interface JCache<V> {

    /**
     * 更新多级缓存。
     *
     * @param key 主键
     * @throws Exception
     */
    void set(String key, V value) throws Exception;

    /**
     * (Redis.HashMap)更新多级缓存。
     *
     * @param key 主键1
     * @param valueKey 主键2
     * @param value 值
     * @throws Exception
     */
    void hSet(String key, String valueKey, V value) throws Exception;

    /**
     * 使用CAS策略更新多级缓存(需要强一致性时使用此方法)。
     *
     * @param key 主键
     * @throws Exception
     */
    boolean compareAndSet(String key, JCasCallable<V> value) throws Exception;

    /**
     * 强一致性更新DB与缓存(调用此方法前需使用悲观锁，callable中只实现更新DB方法，逻辑处理在调用前处理)。
     * 1.调用此方法前需使用悲观锁锁住更新资源，callable中只实现更新DB方法，逻辑处理在调用前处理。
     * 2.请确保更新SQL的轻量，目前Redis中占位10秒，超过10秒可能出现不一致。
     * 3.所有涉及此Key的操作需要确保都使用setDC。
     *
     * @param key 主键
     * @param callable 回调
     * @throws Exception
     */
    boolean setDC(String key, Callable<V> callable) throws Exception;

    /**
     * 多级缓存获取数据
     *
     * @param key 主键
     * @return 结果
     * @throws Exception
     */
    V get(String key) throws Exception;

    /**
     * 从多级缓存中获取指定对象。
     *
     * @param key 主键
     * @param clazz 类
     * @return 对象
     * @throws Exception
     */
    /*V get(String key, Class<V> clazz) throws Exception;*/

    /**
     * 从多级缓存中获取指定对象，如果对象不存在，根据回调方法获取对象并加载到缓存。
     *
     * @param key 主键
     * @param clazz 类
     * @param callable 回调
     * @return 对象
     * @throws Exception
     */
    V get(String key, Class<V> clazz, Callable<V> callable) throws Exception;

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
    V hGet(String key, String valueKey, Class<V> clazz, Callable<V> callable) throws Exception;

    /**
     * 从多级缓存中删除指定对象
     *
     * @param key 主键
     */
    void del(String... key) throws Exception;

    /**
     * (Redis.HashMap)从多级缓存中删除指定对象
     *
     * @param key 主键1
     * @param valueKey 主键2
     */
    void hDel(String key, String valueKey) throws Exception;
}
