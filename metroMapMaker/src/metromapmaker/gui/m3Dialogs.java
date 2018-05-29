package metromapmaker.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroStation;
import metromapmaker.data.m3Path;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class m3Dialogs {
    public static ButtonType showEditDialog(Stage parent, ColorPicker picker, TextField textField, Object titleProperty, Object headerProperty) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(titleProperty);
        String headerText = props.getProperty(headerProperty);
        Alert elementEditDialog = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = elementEditDialog.getDialogPane();
        final Label textLabel1 = new Label("Enter Line/Station Name:");
        final Label textLabel2 = new Label("Set Line/Station Color:");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.add(textLabel1, 0, 1);
        gridPane.add(textField, 1, 1);
        gridPane.add(textLabel2, 0, 2);
        gridPane.add(picker, 1, 2);
        
        elementEditDialog.setResizable(false);
        //dialog.setIconifiable(false);
        dialogPane.setContent(gridPane);
        elementEditDialog.setTitle(title);
        elementEditDialog.setHeaderText(headerText);
        elementEditDialog.initOwner(parent);
        elementEditDialog.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = elementEditDialog.showAndWait();
        if (result.isPresent()) {
            textField.getText();
            return result.get();
        }
            return ButtonType.APPLY;
    }    
     public static void showStationList(Stage parent, Object titleProperty,String lineName, ArrayList<String> list) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(titleProperty);
        String header = lineName +" Line Stops: ";
        String contentText = "";
        for(String s: list){
            contentText += "\n" + s;
        }
        Alert messageDialog = new Alert(Alert.AlertType.INFORMATION);
        messageDialog.initOwner(parent);
        messageDialog.initModality(Modality.APPLICATION_MODAL);
        messageDialog.setTitle(title);
        messageDialog.setHeaderText(header);
        messageDialog.setWidth(400);
        Label label = new Label(contentText);
       // label.setWrapText(true);
        messageDialog.getDialogPane().setContent(label);
        messageDialog.showAndWait();
    }
     
     public static void showPathDescription(Stage parent, Object titleProperty, String startStation, String endStation, m3Path path) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(titleProperty);
        String header = "Path found to get from "+startStation+" to "+endStation+" : ";
        String contentText = "";
        // ONLY DO THIS IF THERE ACTUALLY IS A PATH
        if (path != null) {
            MetroLine line = path.getTripLines().get(0);
            ArrayList<MetroLine> tripLines = path.getTripLines();
            int i = 0;
            for (; i < tripLines.size(); i++) {
                line = tripLines.get(i);
                MetroStation boardingStation = path.getBoardingStations().get(i);
                contentText += (i+1) + ". Board " + line.getName() + " at " +  boardingStation.getName() + "\n";
            }
            contentText += (i+1) + ". Disembark " + line.getName() + " at " + path.getEndStation().getName() + "\n";
        }
        
        Alert messageDialog = new Alert(Alert.AlertType.INFORMATION);
        messageDialog.initOwner(parent);
        messageDialog.initModality(Modality.APPLICATION_MODAL);
        messageDialog.setTitle(title);
        messageDialog.setHeaderText(header);
        messageDialog.setWidth(400);
        Label label = new Label(contentText);
        
       // label.setWrapText(true);
        messageDialog.getDialogPane().setContent(label);
        messageDialog.showAndWait();
    }
}
