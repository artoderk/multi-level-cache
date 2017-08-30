package com.arto.cache.remote;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.arto.cache.JCasCallable;
import com.arto.cache.common.Constants;
import com.google.common.base.Strings;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import redis.clients.jedis.JedisPubSub;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by xiong.jie on 2017-07-11.
 */
@Slf4j
@Setter
@Component("jRemoteCache")
public class JRemoteCacheImpl implements JRemoteCache {
    @Override
    public boolean set(String key, Object obj) {
        return false;
    }

    @Override
    public boolean set(String key, Object obj, int secs) {
        return false;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T get(String key, TypeReference typeReference) {
        return null;
    }

    @Override
    public boolean hashSet(String hashKey, String valueKey, Object obj) {
        return false;
    }

    @Override
    public String hashGet(String hashKey, String valueKey) {
        return null;
    }

    @Override
    public <T> T hashGet(String hashKey, String valueKey, Class<T> clazz) {
        return null;
    }

    @Override
    public Map<String, String> hashGetAll(String hashKey) {
        return null;
    }

    @Override
    public <T> List<T> hashGetAll(String hashKey, Class<T> clazz) {
        return null;
    }

    @Override
    public Set<String> smembers(String key) {
        return null;
    }

    @Override
    public boolean isMember(String key, String value) {
        return false;
    }

    @Override
    public boolean expire(String key, int secs) {
        return false;
    }

    @Override
    public List<String> getAll(String pattern) {
        return null;
    }

    @Override
    public boolean hasKey(String key) throws Exception {
        return false;
    }

    @Override
    public boolean del(String key) {
        return false;
    }

    @Override
    public boolean hashDel(String hashKey, String valueKey) {
        return false;
    }

    @Override
    public long increase(String key) {
        return 0;
    }

    @Override
    public long decrease(String key) {
        return 0;
    }

    @Override
    public <V> boolean setDC(String key, Callable<V> callable, int expireTime) throws Exception {
        return false;
    }

    @Override
    public <V> boolean compareAndSet(String key, JCasCallable<V> callable, int expireTime) throws Exception {
        return false;
    }


    @Override
    public <V> void publish(String key, V value) throws Exception {

    }


    @Override
    public <V> void publishToCluster(V value) throws Exception {

    }

   /* @Autowired
    private JedisDirectClient jedisxClient;

    *//** 生成随机码 *//*
    private static Random random = new Random();

    *//**
     * 设置缓存(k,v)
     *
     * @param key 键
     * @param obj 值
     * @return 结果
     *//*
    public boolean set(String key, Object obj) {
        return setString(key, obj, -1);
    }

    *//**
     * 设置缓存带超时(k,v)
     *
     * @param key 键
     * @param obj 值
     * @param secs 缓存失效时间，单位秒，0代表不失效
     * @return 结果
     *//*
    public boolean set(String key, Object obj, int secs) {
        return setString(key, obj, secs);
    }

    *//**
     * 根据key，获取value
     *
     * @param key 键
     * @return 结果
     *//*
    public String get(String key) {
        return getString(key, null);
    }

    *//**
     * 根据key，获取value
     *
     * @param key 键
     * @param clazz 返回的类型
     * @param <T> 根据泛型返回对应的类型对象
     * @return 结果
     *//*
    public <T> T get(String key, Class<T> clazz) {
        return parseFromJson(getString(key, null), clazz);
    }

    *//**
     * 根据key，获取value
     *
     * @param key 键
     * @param typeReference 返回的泛型类型
     * @param <T> 根据泛型返回对应的类型对象
     * @return 结果
     *//*
    public <T> T get(String key, TypeReference typeReference) {
        return parseFromJson(getString(key, null), typeReference);
    }

    *//**
     * 设置缓存(k1,k2,v)
     *
     * @param hashKey 键1
     * @param valueKey 键2
     * @param obj 值
     * @return 结果
     *//*
    public boolean hashSet(String hashKey, String valueKey, Object obj) {
        if (obj == null || Strings.isNullOrEmpty(hashKey) || Strings.isNullOrEmpty(valueKey)) {
            log.warn("hashSet operation failed, hashKey or valueKey or value is null");
            return false;
        }
        String val;
        try {
            if (obj instanceof String) {
                val = (String)obj;
            } else {
                val = toJsonString(obj);
            }
            jedisxClient.hset(hashKey, valueKey, val);
        } catch (Exception e) {
            log.error("hashSet operation failed, hashKey=" + hashKey + ", valueKey=" + valueKey + ", value=" + obj,
                    " error:", e);
            return false;
        }
        return true;
    }

    *//**
     * 根据key，获取value
     *
     * @param hashKey 键
     * @param valueKey 值
     * @return 结果
     *//*
    public String hashGet(String hashKey, String valueKey) {
        return getString(hashKey, valueKey);
    }

    *//**
     * 根据key，获取value
     *
     * @param hashKey 键
     * @param valueKey 值
     * @param clazz 返回的类型
     * @param <T> 根据泛型返回对应的类型对象
     * @return 结果
     *//*
    public <T> T hashGet(String hashKey, String valueKey, Class<T> clazz) {
        return parseFromJson(getString(hashKey, valueKey), clazz);
    }

    *//**
     * 根据key，获取整个HashMap
     *
     * @param hashKey 键
     * @return 结果
     *//*
    public Map<String, String> hashGetAll(String hashKey) {
        try {
            return jedisxClient.hgetAll(hashKey);
        } catch (Exception e) {
            log.error("hgetAll operation failed, key=" + hashKey, e);
            return null;
        }
    }

    *//**
     * 根据key，获取整个HashMap的值
     *
     * @param hashKey 键
     * @param clazz 返回的类型
     * @param <T> 根据泛型返回对应的类型对象
     * @return 结果
     *//*
    public <T> List<T> hashGetAll(String hashKey, Class<T> clazz) {
        try {
            Map<String, String> map = jedisxClient.hgetAll(hashKey);
            List<T> list = new ArrayList<T>(map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                list.add(parseFromJson(entry.getValue(), clazz));
            }
            return list;
        } catch (Exception e) {
            log.error("hgetAll operation failed, key=" + hashKey, e);
            return null;
        }
    }

	*//*@Override
	public boolean sSet(String key, String value) {
		try {
			return jedisxClient.add(key, value) > 0 ? true : false;
		} catch (Exception e) {
			log.error("Set value to redis error, key = " + key + ", val=" + value, e);
			return false;
		}
	}*//*

    *//**
     * 获取缓存Set(k)所有的v
     *
     * @param key 键
     * @return 结果
     *//*
    public Set<String> smembers(String key) {
        try {
            return jedisxClient.smembers(key);
        } catch (Exception e) {
            log.error("smembers operation failed, key =" + key, e);
            return null;
        }
    }

    *//**
     * 根据key，判断value是否存在set中，O(1)
     *
     * @param key 键
     * @param value 值
     * @return 结果
     *//*
    public boolean isMember(String key, String value) {
        try {
            return jedisxClient.sismember(key, value);
        } catch (Exception e) {
            log.error("sismember operation failed, key =" + key + ", val=" + value, e);
            return false;
        }
    }
    *//**
     * 设置Key的超时时间
     *
     * @param key 键
     * @param secs 过期秒数
     * @return 结果
     *//*
    public boolean expire(String key, int secs) {
        try {
            jedisxClient.expire(key, secs);
            return true;
        } catch (Exception e) {
            log.error("expire operation failed, key =" + key, e);
            return false;
        }
    }

    *//**
     * 批量查询
     *
     * @param pattern
     * @return 结果
     *//*
    @Deprecated
    public List<String> getAll(String pattern) {
        List<String> values = null;
        // TODO 等待Jedisx新版本
		*//*try {
			Set<String> keys = stringRedisTemplate.keys(pattern);
			values = new ArrayList<String>();
			for (String key : keys) {
				String value = jedisxClient..get(key);
				values.add(value);
			}
		} catch (Exception e) {
			log.error("Get all value from redis error, key=" + pattern, e);
		}*//*
        return values;
    }

    *//**
     * 指定Key是否存在于缓存中
     *
     * @param key 键
     * @return 结果
     *//*
    public boolean hasKey(String key) throws Exception {
        try {
            return jedisxClient.exists(key);
        } catch (Exception e) {
            log.error("exists operation failed, key=" + key, e);
            throw e;
        }
    }

    *//**
     * 删除缓存
     *
     * @param key 键
     *//*
    public boolean del(String key) {
        try {
            jedisxClient.delete(key);
        } catch (Exception e) {
            log.error("delete operation failed, key=" + key, e);
            return false;
        }
        return true;
    }

    *//**
     * 删除缓存
     *
     * @param hashKey 键1
     * @param valueKey 键2
     * @return 结果
     *//*
    public boolean hashDel(String hashKey, String valueKey) {
        try {
            jedisxClient.hdel(hashKey, valueKey);
        } catch (Exception e) {
            log.error("hdel operation failed, hashKey=" + hashKey + ", valueKey=" + valueKey, e);
            return false;
        }
        return true;
    }

    *//**
     * 原子自增
     *
     * @param key 键
     * @return 结果
     *//*
    public long increase(String key) {
        try {
            return jedisxClient.incr(key);
        } catch (Exception e) {
            log.error("increase operation failed, key=" + key, e);
        }
        return -1L;
    }

    *//**
     * 原子自减
     *
     * @param key 键
     * @return 结果
     *//*
    public long decrease(String key) {
        try {
            return jedisxClient.decr(key);
        } catch (Exception e) {
            log.error("decrease operation failed, key=" + key, e);
        }
        return -1L;
    }

    *//**
     * 强一致性更新DB与缓存
     * 1.调用此方法前需使用悲观锁锁住更新资源，callable中只实现更新DB方法，逻辑处理在调用前处理。
     * 2.请确保更新SQL的轻量，目前Redis中占位10秒，超过10秒可能出现不一致。
     * 3.所有涉及此Key的操作需要确保都使用setDC。
     *
     * @param key 主键
     * @param callable DB更新回调
     * @param expireTime 过期时间
     *//*
    public <V> boolean setDC(String key, Callable<V> callable, int expireTime) throws Exception {
        int version = random.nextInt(100000);

        TransactionManager tm = jedisxClient.getTransactionManager(key);
        tm.watch();
        JTransaction tx = tm.multi();
        while (true) {
            String oldValue = get(key);
            if (oldValue == null || !oldValue.startsWith(Constants.VERSION)) {
                // 占位，固定10秒超时
                tx.setex(10, Constants.VERSION + version);
                List<Object> result = tx.exec();
                if (result != null && !result.isEmpty()) {
                    break;
                }
            }
        }
        // 更新DB
        V value = null;
        try{
            value = callable.call();
        } catch (Exception e){
            // 尝试移除占位
            del(key);
            throw e;
        }
        // 更新远程缓存
        if (value != null) {
            set(key, value, expireTime);
        }
        return true;
    }

    *//**
     * CAS更新缓存。
     *
     * @param key 主键
     * @param callable 值处理回调
     * @param expireTime 过期时间
     *//*
    public <V> boolean compareAndSet(String key, JCasCallable<V> callable, int expireTime) throws Exception {
        TransactionManager tm = jedisxClient.getTransactionManager(key);
        tm.watch();
        JTransaction tx = tm.multi();
        V value = callable.call(jedisxClient.get(key));
        try{
            // 更新远程缓存
            tx.setex(expireTime, toJsonString(value));
            List<Object> result = tx.exec();
            if (result != null && !result.isEmpty()) {
                return true;
            }
        } catch (Throwable t){
            return false;
        }
        return false;
    }

    *//**
     * 获取Jedis客户端
     *
     * @return Jedis客户端
     *//*
    public JedisDirectClient getJedisxClient(){
        return this.jedisxClient;
    }

    *//**
     * 发布消息
     *
     * @param key 队列名
     * @param value 消息
     * @throws Exception
     *//*
    public <V> void publish(String key, V value) throws Exception {
        jedisxClient.publish(key, toJsonString(value));
    }

    *//**
     * 订阅消息
     *
     * @param jedisPubSub 监听器
     * @param key 监听队列
     * @throws Exception
     *//*
    public void subscribe(JedisPubSub jedisPubSub, String key) throws Exception {
        jedisxClient.subscribe(jedisPubSub, key);
        // sleep1秒以便Jedisx线程subscribe住key.
        Thread.sleep(1000);
    }

    public <V> void publishToCluster(V value) throws Exception{
        publish(Constants.LOCAL_CACHE_CLUSTER, value);
    }

    private boolean setString(String key, Object obj, int secs) {
        if (obj == null || Strings.isNullOrEmpty(key)) {
            log.warn("set operation failed, key or value is null.");
            return false;
        }
        String val;
        try {
            if (obj instanceof String) {
                val = (String)obj;
            } else {
                val = toJsonString(obj);
            }
            if (secs == -1) {
                jedisxClient.set(key, val);
            } else {
                jedisxClient.setex(key, secs, val);
            }
        } catch (Exception e) {
            log.error("set operation failed, key=" + key + ", value =" + obj, e);
            return false;
        }
        return true;
    }

    private String getString(String key1, String key2) {
        String result = null;
        try {
            if (Strings.isNullOrEmpty(key2)) {
                result = jedisxClient.get(key1);
            } else {
                result = jedisxClient.hget(key1, key2);
            }
        } catch (Exception e) {
            log.error("get operation failed, key1=" + key1 +", key2=" + key2, e);
        }
        if (!Strings.isNullOrEmpty(result) && result.startsWith(Constants.VERSION)) {
            result = null;
        }
        return result;
    }

    private String toJsonString(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    private <T> T parseFromJson(String val, Class<T> clazz){
        if (Strings.isNullOrEmpty(val)) return null;
        return JSONObject.parseObject(val, clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> T parseFromJson(String val, TypeReference typeReference){
        if (Strings.isNullOrEmpty(val)) return null;
        return (T) JSONObject.parseObject(val, typeReference);
    }

*/
}
