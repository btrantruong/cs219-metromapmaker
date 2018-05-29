/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.gui;

import djf.AppTemplate;
import djf.ui.AppDialogs;
import static djf.ui.AppDialogs.showYesNoCancelDialog;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import metromapmaker.data.Draggable;
import metromapmaker.data.DraggableText;
import metromapmaker.data.MetroStation;
import metromapmaker.data.m3Data;
import metromapmaker.data.m3State;
import static metromapmaker.data.m3State.ADDING_STATION_TO_LINE;
import static metromapmaker.data.m3State.DRAGGING_NODE;
import static metromapmaker.data.m3State.DRAGGING_NOTHING;
import static metromapmaker.data.m3State.REMOVING_STATION_FROM_LINE;
import static metromapmaker.data.m3State.SELECTING_NODE;
import static metromapmaker.data.m3State.SHOWING_PATH;
import static metromapmaker.m3PropertyType.CONFIRM_REMOVE_CONTENT_TEXT;
import static metromapmaker.m3PropertyType.CONFIRM_REMOVE_LINE_CONTENT_TEXT;
import static metromapmaker.m3PropertyType.CONFIRM_REMOVE_STATION_CONTENT_TEXT;
import static metromapmaker.m3PropertyType.CONFIRM_REMOVE_TITLE;
import static metromapmaker.m3PropertyType.INPUT_DIALOG_CONTENT_TEXT;
import static metromapmaker.m3PropertyType.INPUT_DIALOG_ERROR_TEXT;
import static metromapmaker.m3PropertyType.INPUT_DIALOG_HEADER_TEXT;
import static metromapmaker.m3PropertyType.INPUT_DIALOG_TITLE;

/**
 *
 * @author Lakmi
 */
public class ElementController {

    AppTemplate app;
    m3Data dataManager;

    public ElementController(AppTemplate initApp) {
        app = initApp;
        dataManager = (m3Data) app.getDataComponent();

    }

    public void processAddLine() {
        String textInput = AppDialogs.showTextInputDialog(app.getGUI().getWindow(), INPUT_DIALOG_TITLE, INPUT_DIALOG_HEADER_TEXT, INPUT_DIALOG_CONTENT_TEXT);
        dataManager.startNewLine(textInput);
        dataManager.selectSizedShape();
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    public void processRemoveSelectedMetroLine() {
        ButtonType confirmAction = showYesNoCancelDialog(app.getGUI().getWindow(), CONFIRM_REMOVE_TITLE, CONFIRM_REMOVE_LINE_CONTENT_TEXT);
        if(confirmAction.equals(ButtonType.YES)){
        // REMOVE THE SELECTED NODE IF THERE IS ONE
        dataManager.removeSelectedMetroLine();
        // ENABLE/DISABLE THE PROPER BUTTONS
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
        }
    }
    
    public void processRemoveSelectedMetroStation() {
        ButtonType confirmAction = showYesNoCancelDialog(app.getGUI().getWindow(), CONFIRM_REMOVE_TITLE, CONFIRM_REMOVE_STATION_CONTENT_TEXT);
        if(confirmAction.equals(ButtonType.YES)){
        // REMOVE THE SELECTED NODE IF THERE IS ONE
        dataManager.removeSelectedMetroStation();
        // ENABLE/DISABLE THE PROPER BUTTONS
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
        }
    }

    public void processAddStation() {
        String textInput = AppDialogs.showTextInputDialog(app.getGUI().getWindow(), INPUT_DIALOG_TITLE, INPUT_DIALOG_HEADER_TEXT, INPUT_DIALOG_CONTENT_TEXT);
        if (dataManager.isNameAvailable(textInput)) {
            dataManager.startNewStation(textInput);
            dataManager.selectSizedShape();
        } else {
            String textInput1 = AppDialogs.showTextInputDialog(app.getGUI().getWindow(), INPUT_DIALOG_TITLE, INPUT_DIALOG_ERROR_TEXT, INPUT_DIALOG_CONTENT_TEXT);
            processAddStation();
        }
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        //workspace.reloadWorkspace(dataManager);
    }

    /**
     * This method handles a user request to remove the selected node.
     */
    public void processRemoveSelectedNode() {
        // REMOVE THE SELECTED NODE IF THERE IS ONE
        dataManager.removeSelectedNode();
        // ENABLE/DISABLE THE PROPER BUTTONS
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }

    /**
     * This method handles the response for selecting either the selection or
     * removal tool.
     */
    public void processSelectSelectionTool() {
        // CHANGE THE CURSOR
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);

        // CHANGE THE STATE
        dataManager.setState(m3State.SELECTING_NODE);

        // ENABLE/DISABLE THE PROPER BUTTONS
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * This method processes a user request to start drawing a rectangle.
     */
    public void processSelectStationToRemove() {
        if (dataManager.isInState(SELECTING_NODE)) {
            MetroStation stationToRemove = dataManager.getSelectedStation();
            if (stationToRemove != null) {
                    //IF THERES A SELECTED LINE THEN DO
                    dataManager.removeStationFromLine(stationToRemove);
                    System.out.println("Station removed" + stationToRemove);
                //ELSE PROMPT TO SELECT A LINE 
                // dataManager.setState(m3State.DRAGGING_NOTHING);
            }
        }
        // CHANGE THE CURSOR
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.HAND);

        // CHANGE THE STATE
        dataManager.setState(m3State.REMOVING_STATION_FROM_LINE);

        // ENABLE/DISABLE THE PROPER BUTTONS
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * This method provides a response to the user requesting to select a
     * station and add to line.
     */
    public void processSelectStationToAdd() {
        if (dataManager.isInState(SELECTING_NODE)) {
            MetroStation stationToAdd = dataManager.getSelectedStation();
            if (stationToAdd != null) {
                //IF THERES A SELECTED LINE THEN DO
                dataManager.addStationToLine(stationToAdd);
                System.out.println("Station added" + stationToAdd);
                //ELSE PROMPT TO SELECT A LINE 
                // dataManager.setState(m3State.DRAGGING_NOTHING);
            }
        }
        // CHANGE THE CURSOR
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.HAND);

        // CHANGE THE STATE
        dataManager.setState(m3State.ADDING_STATION_TO_LINE);
        // ENABLE/DISABLE THE PROPER BUTTONS
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
//    public void processCanvasMousePress(int x, int y) {
//        System.out.println("State before press:"+dataManager.getState());
//        if (dataManager.isInState(SELECTING_NODE)||dataManager.isInState(REMOVING_STATION_FROM_LINE)
//                ||dataManager.isInState(ADDING_STATION_TO_LINE)) {
//            // SELECT THE TOP NODE
//            Node node = dataManager.selectTopNode(x, y);
//            Scene scene = app.getGUI().getPrimaryScene();
//            System.out.println("Selected node is: "+node);
//
//            // AND START DRAGGING IT
//            if (node != null) {
//                if (dataManager.isInState(SELECTING_NODE)){
//                    scene.setCursor(Cursor.MOVE);
//                    if(node instanceof MetroStation){
//                        dataManager.setSelectedStation((MetroStation)node);
//                        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
//                        workspace.updateStationComboBox();
//                        System.out.println("Combo Box should be updated");
//                    }
//                dataManager.setState(m3State.DRAGGING_NODE);
//                app.getGUI().updateToolbarControls(false);
//                }
//                if (dataManager.isInState(REMOVING_STATION_FROM_LINE)){
//                     if(node instanceof MetroStation){
//                       dataManager.removeStationFromLine((MetroStation)node);
//                   }
//                }
//                if(dataManager.isInState(ADDING_STATION_TO_LINE)){
//                   if(node instanceof MetroStation){
//                       dataManager.addStationToLine((MetroStation)node);
//                   }
//                }
//                }
//            } else {
//                dataManager.setState(DRAGGING_NOTHING);
//                app.getWorkspaceComponent().reloadWorkspace(dataManager);
//            }
//         System.out.println("State after before press:"+dataManager.getState());
//        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
//        workspace.reloadWorkspace(dataManager);
//    }
    public void processCanvasMousePress(int x, int y) {
        m3Data dataManager = (m3Data) app.getDataComponent();
        if (dataManager.isInState(SELECTING_NODE) || dataManager.isInState(ADDING_STATION_TO_LINE)
                || dataManager.isInState(REMOVING_STATION_FROM_LINE)) {
            // SELECT THE TOP NODE
            Node node = dataManager.selectTopNode(x, y);
            Scene scene = app.getGUI().getPrimaryScene();

            // AND START DRAGGING IT
            if (node != null) {
                if (dataManager.isInState(SELECTING_NODE)) {
                    scene.setCursor(Cursor.MOVE);
                    dataManager.setState(m3State.DRAGGING_NODE);
                    app.getGUI().updateToolbarControls(false);
                } else if (dataManager.isInState(ADDING_STATION_TO_LINE)) {
                    if (dataManager.getSelectedStation() == null) {
                        System.out.println("CHOOSE A STATION!!");
                    } else if (node instanceof MetroStation) {
                        dataManager.addStationToLine((MetroStation) node);
                    }
                } else if (dataManager.isInState(REMOVING_STATION_FROM_LINE)) {
                    if (dataManager.getSelectedStation() == null) {
                        System.out.println("CHOOSE A STATION!!");
                    } else if (node instanceof MetroStation) {
                        dataManager.removeStationFromLine((MetroStation) node);
                    }
                }
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(dataManager);
            }
           
        }
        else if(dataManager.isInState(SHOWING_PATH)){
            dataManager.unRenderPath(dataManager.getPathShowing());
            dataManager.setState(SELECTING_NODE);
        }
         m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
            workspace.updateStationComboBox();
            workspace.reloadWorkspace(dataManager);

    }

    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */

    public void processCanvasMouseDragged(int x, int y) {
        if (dataManager.isInState(DRAGGING_NODE)) {
            if(dataManager.getSelectedNode() instanceof Draggable){
            Draggable selectedDraggableNode = (Draggable) dataManager.getSelectedNode();
            selectedDraggableNode.drag(x, y);
            app.getGUI().updateToolbarControls(false);
            }
        }
    }

    /**
     * Respond to mouse button release on the rendering surface, which we call
     * canvas, but is actually a Pane.
     */
    public void processCanvasMouseRelease(int x, int y) {
        if (dataManager.isInState(m3State.DRAGGING_NODE)) {
            dataManager.setState(SELECTING_NODE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(m3State.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_NODE);
        }
    }

}
