/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.gui;

import djf.AppTemplate;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import jtps.jTPS;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroStation;
import metromapmaker.data.m3Data;
import static metromapmaker.m3PropertyType.CONFIRM_REMOVE_TITLE;
import metromapmaker.transactions.ChangeBackgroundColor_Transaction;
import metromapmaker.transactions.ChangeElementName_Transaction;
import metromapmaker.transactions.ChangeLineColor_Transaction;
import metromapmaker.transactions.ChangeLineThickness_Transaction;
import metromapmaker.transactions.ChangeStationFillColor_Transaction;
import metromapmaker.transactions.ChangeStationRadius_Transaction;

/**
 *
 * @author Lakmi
 */
public class StyleController {

    AppTemplate app;
    m3Data data;

    public StyleController(AppTemplate initApp) {
        app = initApp;
        data = (m3Data) app.getDataComponent();

    }
    
    /**
     * This method processes a user request to select the 
     * background color.
     */
    public void processSelectBackgroundColor() {
	m3Workspace workspace = (m3Workspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getBackgroundColorPicker().getValue();
	if (selectedColor != null) {
            Pane canvas = workspace.getCanvas();
	    ChangeBackgroundColor_Transaction transaction = new ChangeBackgroundColor_Transaction(canvas, selectedColor);
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
	    app.getGUI().updateToolbarControls(false);
	}
    }

    public void processLineEditRequest() {
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        MetroLine selectedLine = data.getSelectedLine();
        ButtonType selection = m3Dialogs.showEditDialog(app.getGUI().getWindow(), workspace.getLineColorPicker(), workspace.getLineNameField(), CONFIRM_REMOVE_TITLE, CONFIRM_REMOVE_TITLE);

        if (selection == ButtonType.OK) {
            String lineName = workspace.getLineNameField().getText();
            Color lineColor = workspace.getLineColorPicker().getValue();
            if (selectedLine != null) {
                ChangeLineColor_Transaction colorTransaction = new ChangeLineColor_Transaction(data, selectedLine, lineColor);
                ChangeElementName_Transaction nameTransaction = new ChangeElementName_Transaction(data, selectedLine, lineName);
                jTPS tps = app.getTPS();
                tps.addTransaction(colorTransaction);
                tps.addTransaction(nameTransaction);
                app.getGUI().updateToolbarControls(false);
            }
            workspace.updateLineComboBox();
        }

    }

    public void processStationEditRequest() {
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        MetroStation station = data.getSelectedStation();
        ButtonType selection = m3Dialogs.showEditDialog(app.getGUI().getWindow(), workspace.getStationColorPicker(), workspace.getStationNameField(), CONFIRM_REMOVE_TITLE, CONFIRM_REMOVE_TITLE);

        if (selection == ButtonType.OK) {
            String stationName = workspace.getStationNameField().getText();
            Color stationColor = workspace.getStationColorPicker().getValue();
            if (station != null) {
                ChangeStationFillColor_Transaction colorTransaction = new ChangeStationFillColor_Transaction((Shape) station, stationColor);
                ChangeElementName_Transaction nameTransaction = new ChangeElementName_Transaction(data, station, stationName);
                jTPS tps = app.getTPS();
                tps.addTransaction(colorTransaction);
                tps.addTransaction(nameTransaction);
                app.getGUI().updateToolbarControls(false);
            }
        }

    }

    public void processChangeLineThickness() {
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        int outlineThickness = (int) workspace.getLineThicknessSlider().getValue();
        MetroLine line = data.getSelectedLine();
        if (line != null) {
            ChangeLineThickness_Transaction transaction = new ChangeLineThickness_Transaction(data, line, outlineThickness);
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
            app.getGUI().updateToolbarControls(false);
        }
    }

    public void processChangeStationRadius() {
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        double radius = workspace.getStationThicknessSlider().getValue();
        MetroStation station = data.getSelectedStation();
        if (station != null) {
            ChangeStationRadius_Transaction transaction = new ChangeStationRadius_Transaction(station, radius);
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
            app.getGUI().updateToolbarControls(false);
        }
    }
}
