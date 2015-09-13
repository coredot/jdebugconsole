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

public class Example {

    public static void main(String[] args) throws InterruptedException {

        // RateStat Example
        RateStat rateStat = new RateStat("My Rate", 10);
        for (int c = 0; c < 300; c++) {
            rateStat.add(5);
            Debug.setStatistic(rateStat);
            Thread.sleep(100);
        }

        // Total Example
        TotalStat totalStat = new TotalStat("My Total");
        for (int c = 0; c < 300; c++) {
            totalStat.add(1);
            Debug.setStatistic(totalStat);
        }

        // TimeStat Example
        TimeStat timeStat = new TimeStat("My Time", 10);
        for (int c = 0; c < 300; c++) {
            StopWatch watch = timeStat.start();
            Thread.sleep(100);
            watch.stop();
            Debug.setStatistic(timeStat);
        }

        // Debug Message Example
        Debug.println("A debug message");
    }

}
