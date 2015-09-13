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

public class TimeStat implements Statistic {
    private final Cache cache;
    private final Mutex mutex = new Mutex();
    @Getter
    private String name;
    private long count = 0l;
    private long total = 0l;

    public TimeStat(String name, int seconds) {
        this.name = name;
        cache = new Cache(CacheBuilder.newBuilder()
                .expireAfterWrite(seconds, TimeUnit.SECONDS)
                .build(new RateCacheLoader()));
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        synchronized (mutex) {
            Collection<Double> set = cache.getCache().asMap().values();
            double total = set.stream().mapToLong(value -> value.longValue()).sum();
            out.append("Mean: ");
            out.append(JMath.round(total / set.size(), 4));
            out.append("ms, Total: ");
            out.append(String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(this.total),
                    TimeUnit.MILLISECONDS.toMinutes(this.total) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this.total)),
                    TimeUnit.MILLISECONDS.toSeconds(this.total) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.total))));
            out.append(", Collected: ");
            out.append(count);
        }
        return out.toString();
    }

    protected void stop(long span) {
        synchronized (mutex) {
            count++;
            double value = span / 1000000d;
            total += value;
            cache.getCache().put(System.currentTimeMillis(), value);
        }
    }

    public StopWatch start() {
        return new StopWatch(this);
    }

    private static class Cache implements Serializable {
        @Getter
        LoadingCache<Long, Double> cache;

        public Cache(LoadingCache<Long, Double> cache) {
            this.cache = cache;
        }
    }

    private static class RateCacheLoader extends CacheLoader<Long, Double> implements Serializable {
        @Override
        public Double load(Long aLong) throws Exception {
            return 0d;
        }
    }

}
