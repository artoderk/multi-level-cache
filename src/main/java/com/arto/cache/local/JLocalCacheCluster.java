package com.arto.cache.local;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xiong.jie on 2017-07-13.
 */
@Setter
@Getter
@AllArgsConstructor
public class JLocalCacheCluster<V> {

    String name;

    String key;

    V value;
}
