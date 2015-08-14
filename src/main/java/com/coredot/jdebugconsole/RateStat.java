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

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by dkstuart on 8/11/15.
 */
public class RateStat implements Statistic {
    private final LoadingCache<Long, Number> values;
    private Object mutex = new Object();
    @Getter
    private String name;
    private long count = 0l;

    public RateStat(String name) {
        this.name = name;
        values = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, Number>() {
                    @Override
                    public Number load(Long key) throws Exception {
                        return new Integer(0);
                    }
                });
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        synchronized (mutex) {
            Collection<Number> set = values.asMap().values();
            double total = set.stream().mapToDouble(value -> value.doubleValue()).sum();
            out.append("Rate: ");
            out.append(total / 10);
            out.append(", Mean: ");
            out.append(total / set.size());
            out.append(", Hits: ");
            out.append(count);
        }
        return out.toString();
    }

    public void add(Number value) {
        synchronized (mutex) {
            count++;
            values.put(System.currentTimeMillis(), value.doubleValue());
        }
    }

}
