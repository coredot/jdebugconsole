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

import lombok.Getter;

public class TotalStat implements Statistic {
    private final Mutex mutex = new Mutex();
    @Getter
    private String name;
    private double total = 0l;
    private long count = 0l;

    public TotalStat(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        synchronized (mutex) {
            out.append("Total: ");
            out.append(total);
            out.append(", Mean: ");
            out.append(JMath.round(total / count, 2));
            out.append(", Collected: ");
            out.append(count);
        }
        return out.toString();
    }

    public void add(Number value) {
        synchronized (mutex) {
            count++;
            total += value.doubleValue();
        }
    }

    public void reset() {
        synchronized (mutex) {
            total = 0l;
            count = 0l;
        }
    }
}
