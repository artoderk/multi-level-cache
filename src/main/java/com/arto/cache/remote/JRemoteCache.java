package com.arto.cache.remote;

import com.alibaba.fastjson.TypeReference;
import com.arto.cache.JCasCallable;
//import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by xiong.jie on 2017-07-11.
 */
public interface JRemoteCache {

    /**
     * 设置缓存(k,v)
     *
     * @param key 键
     * @param obj 值
     * @return 结果
     */
    boolean set(String key, Object obj);

    /**
     * 设置缓存带超时(k,v)
     *
     * @param key 键
     * @param obj 值
     * @param secs 缓存失效时间，单位秒，0代表不失效
     * @return 结果
     */
    boolean set(String key, Object obj, int secs);

    /**
     * 根据key，获取value
     *
     * @param key 键
     * @return 结果
     */
    String get(String key);

    /**
     * 根据key，获取value
     *
     * @param key 键
     * @param clazz 返回的类型
     * @param <T> 根据泛型返回对应的类型对象
     * @return 结果
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 根据key，获取value
     *
     * @param key 键
     * @param typeReference 返回的泛型类型
     * @param <T> 根据泛型返回对应的类型对象
     * @return 结果
     */
    <T> T get(String key, TypeReference typeReference);

    /**
     * 设置缓存(k1,k2,v)
     *
     * @param hashKey 键1
     * @param valueKey 键2
     * @param obj 值
     * @return 结果
     */
    boolean hashSet(String hashKey, String valueKey, Object obj);

    /**
     * 根据key，获取value
     *
     * @param hashKey 键
     * @param valueKey 值
     * @return 结果
     */
    String hashGet(String hashKey, String valueKey);

    /**
     * 根据key，获取value
     *
     * @param hashKey 键
     * @param valueKey 值
     * @param clazz 返回的类型
     * @param <T> 根据泛型返回对应的类型对象
     * @return 结果
     */
    <T> T hashGet(String hashKey, String valueKey, Class<T> clazz);

    /**
     * 根据key，获取整个HashMap
     *
     * @param hashKey 键
     * @return 结果
     */
    Map<String, String> hashGetAll(String hashKey);

    /**
     * 根据key，获取整个HashMap的值
     *
     * @param hashKey 键
     * @param clazz 返回的类型
     * @param <T> 根据泛型返回对应的类型对象
     * @return 结果
     */
    <T> List<T> hashGetAll(String hashKey, Class<T> clazz);

	/*
	boolean sSet(String key, String value);*/

    /**
     * 获取缓存Set(k)所有的v
     *
     * @param key 键
     * @return 结果
     */
    Set<String> smembers(String key);

    /**
     * 根据key，判断value是否存在set中，O(1)
     *
     * @param key 键
     * @param value 值
     * @return 结果
     */
    boolean isMember(String key, String value);
    /**
     * 设置Key的超时时间
     *
     * @param key 键
     * @param secs 过期秒数
     * @return 结果
     */
    boolean expire(String key, int secs);

    /**
     * 批量查询
     *
     * @param pattern
     * @return 结果
     */
    @Deprecated
    List<String> getAll(String pattern);

    /**
     * 指定Key是否存在于缓存中
     *
     * @param key 键
     * @return 结果
     */
    boolean hasKey(String key) throws Exception;

    /**
     * 删除缓存
     *
     * @param key 键
     */
    boolean del(String key);

    /**
     * 删除缓存
     *
     * @param hashKey 键1
     * @param valueKey 键2
     * @return 结果
     */
    boolean hashDel(String hashKey, String valueKey);

    /**
     * 原子自增
     *
     * @param key 键
     * @return 结果
     */
    long increase(String key);

    /**
     * 原子自减
     *
     * @param key 键
     * @return 结果
     */
    long decrease(String key);

    /**
     * 强一致性更新DB与缓存
     * 1.调用此方法前需使用悲观锁锁住更新资源，callable中只实现更新DB方法，逻辑处理在调用前处理。
     * 2.请确保更新SQL的轻量，目前Redis中占位10秒，超过10秒可能出现不一致。
     *
     * @param key 主键
     * @param callable DB更新回调
     * @param expireTime 过期时间
     */
    <V> boolean setDC(String key, Callable<V> callable, int expireTime) throws Exception ;

    /**
     * CAS更新缓存。
     *
     * @param key 主键
     * @param callable 值处理回调
     * @param expireTime 过期时间
     */
    <V> boolean compareAndSet(String key, JCasCallable<V> callable, int expireTime) throws Exception;

    /**
     * 获取Jedis客户端
     *
     * @return Jedis客户端
     */
    //JedisDirectClient getJedisxClient();

    /**
     * 发布消息
     *
     * @param key 队列名
     * @param value 消息
     */
    <V> void publish(String key, V value) throws Exception;
    /**
     * 订阅消息
     *
     * @param jedisPubSub 监听器
     * @param key 监听队列
     * @throws Exception
     */
    //void subscribe(JedisPubSub jedisPubSub, String key) throws Exception;

    <V> void publishToCluster(V value) throws Exception;
}
