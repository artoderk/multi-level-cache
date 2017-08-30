/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.arto.cache.local;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 参照com.alibaba.fastjson.TypeReference
 *
 * Created by xiong.j on 2017/1/25.
 */
@Slf4j
public class TypeReference {

    private static ConcurrentMap<String, SoftReference<Type>> types = new ConcurrentHashMap<String, SoftReference<Type>>();

    public static Type getType(JLocalCache cache){
        String name = cache.getName();
        if (types.containsKey(name)) {
            if (types.get(name).get() != null) {
                return types.get(name).get();
            }
        }
        try {
            return createType(cache);
        } catch (Exception e) {
            log.warn("Can't found field.", e);
            return null;
        }
    }

    private static synchronized Type createType(JLocalCache cache) throws NoSuchFieldException {
        String name = cache.getName();
        if (types.containsKey(name)) {
            if (types.get(name).get() != null) {
                return types.get(name).get();
            }
        }
        Type type = cache.getClass().getTypeParameters()[0];
        types.put(name, new SoftReference<Type>(type));
        return type;
    }

}
