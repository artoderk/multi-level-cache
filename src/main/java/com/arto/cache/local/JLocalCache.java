package com.arto.cache.local;

import com.google.common.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xiong.jie on 2017-07-13.
 */
@Setter
@Getter
@AllArgsConstructor
public class JLocalCache<V1> {

    private Cache<String, V1> cache;

    private String name;

    private int expireAfterWrite;

    private boolean spreadToCluster;

    private com.alibaba.fastjson.TypeReference typeReference;

}
