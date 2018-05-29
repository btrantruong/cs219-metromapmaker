/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.gui;

import djf.AppTemplate;
import java.util.ArrayList;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import metromapmaker.data.MetroStation;
import metromapmaker.data.m3Data;
import metromapmaker.data.m3Path;
import metromapmaker.data.m3State;
import static metromapmaker.gui.m3Dialogs.showPathDescription;
import static metromapmaker.m3PropertyType.PATH_TITLE;

/**
 *
 * @author sunshinger
 */
public class FindPathController {
    public static final String YELLOW_HEX = "#EEEE00";
    AppTemplate app;
    m3Data data;
    Effect highlightedEffect;
    public FindPathController(AppTemplate initApp){
        app = initApp;
        data = (m3Data)app.getDataComponent();
        // THIS IS FOR THE SELECTED SHAPE
        DropShadow dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
        dropShadowEffect.setOffsetY(0.0f);
        dropShadowEffect.setSpread(1.0);
        dropShadowEffect.setColor(Color.YELLOW);
        dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
        dropShadowEffect.setRadius(15);
        highlightedEffect = dropShadowEffect;
    }
    
    /*
        This function is called when the user asks to find a path from one station to
        another in the currently loaded metro system. It responds by computing the path
        and then highlighting it on the map.
    */
    public void requestFindPath(String startStation, String endStation){
        // COMPUTE THE PATH
        m3Path path = data.findMinimumTransferPath(startStation, endStation);
        // AND UPDATE THE MAP
        data.setPathShowing(path);
        data.renderPath(path);
        // UPDATE THE PATH DESCRIPTION
        showPathDescription(app.getGUI().getWindow(), PATH_TITLE, startStation, endStation, path);
        data.setState(m3State.SHOWING_PATH);
    }
    
    /*
        This function draws a filled circle on top of a station in the metro
        so as to highlight it.
    */
    public void highlightStation(MetroStation station) {
        station.setEffect(highlightedEffect);
    }
}
