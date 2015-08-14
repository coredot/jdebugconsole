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


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.HashMap;


public class Controller {
    private final ObservableList<KeyValue> data = FXCollections.observableArrayList();
    private final HashMap<String, Integer> indexes = new HashMap<>();
    private ArrayList<String> consoleBuffer = new ArrayList<>();
    @FXML
    private TableView table;

    @FXML
    private TextArea console;

    public void initialize() {
        setTableData(data);
    }

    public void setTableData(ObservableList<KeyValue> data) {
        table.setItems(data);
    }

    public void println(String message) {
        consoleBuffer.add(message);
        while (consoleBuffer.size() > 500) consoleBuffer.remove(0);
    }

    public void redrawConsole() {
        StringBuilder out = new StringBuilder();
        consoleBuffer.forEach(s -> out.append(s + "\n"));
        console.setText(out.toString());
        console.setScrollTop(100000000);
    }

    public void setKeyValue(String key, Object value) {
        Integer index = indexes.get(key);
        if (index == null) {
            indexes.put(key, data.size());
            data.add(new KeyValue(key, value));
        } else {
            KeyValue val = data.get(index);
            val.setValue(value);
            data.set(index, val);
        }
    }

}
