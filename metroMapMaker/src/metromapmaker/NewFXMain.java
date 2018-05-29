/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author Lakmi
 */
public class NewFXMain extends Application {
    public void start(Stage primaryStage) throws Exception {
        WebView webView = new WebView();
        Slider slider = new Slider(0.5,2,1);
        webView.scaleXProperty().bind(slider.valueProperty());
        webView.scaleYProperty().bind(slider.valueProperty());
        primaryStage.setScene(new Scene(new BorderPane(webView, null, null, slider, null)));
        webView.getEngine().load("http://www.google.com");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

