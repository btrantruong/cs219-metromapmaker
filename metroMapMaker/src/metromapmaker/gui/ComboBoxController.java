/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.gui;

import djf.AppTemplate;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jtps.jTPS;
import metromapmaker.data.MetroObject;
import metromapmaker.data.MetroStation;
import metromapmaker.data.m3Data;

/**
 *
 * @author Lakmi
 */
public class ComboBoxController {
    AppTemplate app;
    m3Data data;
    public ComboBoxController(AppTemplate initApp){
        app = initApp;
        data = (m3Data)app.getDataComponent();
    }
    public ComboBox initComboBox(ObservableList<MetroObject> metroObjectList){
        ComboBox<MetroObject> comboBox = new ComboBox<MetroObject>();
        comboBox.setItems(metroObjectList);
        comboBox.setCellFactory(new Callback<ListView<MetroObject>, ListCell<MetroObject>>(){
          @Override
          public ListCell<MetroObject> call(ListView<MetroObject> param) {
            final ListCell<MetroObject> cell = new ListCell<MetroObject>() {
              {
                super.setPrefWidth(100);
              }

              @Override
              public void updateItem(MetroObject item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                  setText(item.getName());
                } else {
                  setText(null);
                }
              }
            };
            return cell;
          }
        });
        comboBox.getSelectionModel().selectFirst();
       return comboBox; 
    }
       public void processSelectStation() {
        m3Workspace workspace = (m3Workspace)app.getWorkspaceComponent();
        data.setSelectedStation(workspace.getCurrentStation());
        workspace.getStationNameField().setText(workspace.getCurrentStation().getName());
        }
       
        public void processSelectLine() {
        m3Workspace workspace = (m3Workspace)app.getWorkspaceComponent();
        data.setSelectedLine(workspace.getCurrentLine());
        workspace.getLineNameField().setText(workspace.getCurrentLine().getName());
        }
    public void updateComboBox(MetroObjectComboBox comboBox){
           MetroStation selected = data.getSelectedStation();
           comboBox.setValue(selected);
    }
    
}
