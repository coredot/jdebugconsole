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

import java.io.IOException;

/**
 * Created by dkstuart on 8/11/15.
 */
public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
        /*TotalStat stat = new TotalStat("count");
        for(int c=0;c<1000000;c++) {
            //Thread.sleep(1);
            Debug.println("Test message " + c);
            Debug.setKeyValue("test", "value " + c);
            stat.add(c);
            Debug.setStatistic(stat);
        }*/

        long start = System.currentTimeMillis();
        RateStat rateStat = new RateStat("rate");
        while (System.currentTimeMillis() < start + 30000) {
            rateStat.add(5);
            Debug.setStatistic(rateStat);
            Thread.sleep(100);
        }


    }

}
