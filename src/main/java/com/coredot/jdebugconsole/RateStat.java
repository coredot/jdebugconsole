/*
 * Copyright (c) 2015 Daniel Stuart
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.coredot.jdebugconsole;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class RateStat implements Statistic {
    private final Cache cache;
    private final Mutex mutex = new Mutex();
    @Getter
    private String name;
    private long count = 0l;
    private int seconds;

    public RateStat(String name, int seconds) {
        this.name = name;
        this.seconds = seconds;
        cache = new Cache(CacheBuilder.newBuilder()
                .expireAfterWrite(seconds, TimeUnit.SECONDS)
                .build(new RateCacheLoader()));
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        synchronized (mutex) {
            Collection<Number> set = cache.getCache().asMap().values();
            double total = set.stream().mapToDouble(Number::doubleValue).sum();
            out.append("Rate: ");
            out.append(JMath.round(total / seconds, 2));
            out.append("/sec, Collected: ");
            out.append(count);
        }
        return out.toString();
    }

    public void add(Number value) {
        synchronized (mutex) {
            count++;
            cache.getCache().put(System.currentTimeMillis(), value.doubleValue());
        }
    }

    private static class Cache implements Serializable {
        @Getter
        LoadingCache<Long, Number> cache;

        public Cache(LoadingCache<Long, Number> cache) {
            this.cache = cache;
        }
    }

    private static class RateCacheLoader extends CacheLoader<Long, Number> implements Serializable {
        @Override
        public Number load(Long aLong) throws Exception {
            return new Integer(0);
        }
    }

}
