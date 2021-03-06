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

import javafx.application.Application;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Debug implements Serializable {
    private static final HashMap<String, Object> keyValueMap = new HashMap<>();
    private static DebugApplication instance = null;
    private static Timer timer = new Timer();
    private static int refreshRate = 500;
    private static AtomicBoolean enableGUI = new AtomicBoolean(true);

    public static void setRefreshRate(int refreshRate) {
        Debug.refreshRate = refreshRate;
    }

    public static void enableGUI(boolean enabled) {
        Debug.enableGUI.set(enabled);
    }

    private static DebugApplication getApp() {
        if (instance == null) {
            instance = new DebugApplication();
            new Thread(() -> {
                Application.launch(DebugApplication.class);
            }).start();
            while (DebugApplication.getInstance() == null) try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timer.scheduleAtFixedRate(new Task() {
                @Override
                public void run() {
                    getApp();
                    Platform.runLater(() -> {
                        try {
                            synchronized (keyValueMap) {
                                keyValueMap.forEach((key, value) -> getApp().getController().setKeyValue(key, value));
                                keyValueMap.clear();
                            }
                            getApp().getController().redrawConsole();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }, new Date(), refreshRate);
            instance = DebugApplication.getInstance();
        }
        return instance;
    }

    public static void println(String message) {
        if (enableGUI.get()) {
            getApp();
            Platform.runLater(() -> getApp().getController().println(message));
        }
    }

    public static void setKeyValue(String key, String value) {
        if (enableGUI.get()) {
            getApp();
            synchronized (keyValueMap) {
                keyValueMap.put(key, value);
            }
        }
    }

    public static void setStatistic(Statistic stat) {
        if (enableGUI.get()) {
            getApp();
            synchronized (keyValueMap) {
                keyValueMap.put(stat.getName(), stat);
            }
        }
    }

    private static class DebugTimer extends Timer implements Serializable {}

    private abstract static class Task extends TimerTask implements Serializable {}

}
