package com.arto.cache;

/**
 * Created by xiong.jie on 2017-07-14.
 */
public interface JCasCallable<V> {

    V call(String value);
}
