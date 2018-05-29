/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import metromapmaker.data.MetroObject;

/**
 *
 * @author Lakmi
 */
public class MetroObjectComboBox extends ComboBox {

    ObservableList<MetroObject> selection;

    public MetroObjectComboBox(ObservableList<MetroObject> initList) {
        selection = initList;
        this.setItems(selection);
        Callback<ListView<MetroObject>, ListCell<MetroObject>> factory = lv -> new ListCell<MetroObject>() {

            @Override
            protected void updateItem(MetroObject item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        };
        this.setCellFactory(factory);
        this.setButtonCell(factory.call(null));
    }

    public MetroObject isClicked() {
        MetroObject selected = (MetroObject) this.getSelectionModel().getSelectedItem();
        this.setValue(selected);
        return selected;
    }
}
