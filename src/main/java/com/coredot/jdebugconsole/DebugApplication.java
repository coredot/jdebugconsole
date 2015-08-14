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
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

import java.net.URL;

/**
 * Created by dkstuart on 8/11/15.
 */
public class DebugApplication extends Application {
    private static DebugApplication instance = null;

    @Getter
    private Controller controller;

    public static DebugApplication getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL location = getClass().getResource("/debug.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load(location.openStream());
        primaryStage.setTitle("Debug Console");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
        controller = fxmlLoader.getController();
        instance = this;
    }

}
