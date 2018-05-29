/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.gui;

import djf.AppTemplate;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import metromapmaker.data.m3Data;
import metromapmaker.data.m3State;

/**
 *
 * @author Lakmi
 */
public class CanvasController {

    m3Data data;
    AppTemplate app;
    Scale scale;
    double zoomFactor = 0.1;

    public CanvasController(AppTemplate initApp) {
        app = initApp;
        data = (m3Data) app.getDataComponent();
    }
    

    public void processZoomInRequest(Pane canvas) {
        Scale scale = new Scale(1, 1);
        canvas.getTransforms().add(scale);
        scale.setX(1.1);
        scale.setY(1.1);
        canvas.requestLayout();
       // data.setState(m3State.ZOOMING);
    }
    
     public void processZoomOutRequest(Pane canvas) {
        Scale scale = new Scale(1, 1);
        canvas.getTransforms().add(scale);
        scale.setX(0.9);
        scale.setY(0.9);
        canvas.requestLayout();
        //data.setState(m3State.ZOOMING);
    }

    public void processKeyPress(BorderPane parent, Pane canvas, KeyEvent keyEvent) {
        Bounds canvasBound = canvas.getLayoutBounds();
         Bounds canvasBound1 = canvas.getBoundsInLocal();
          Bounds canvasBound2 = canvas.getBoundsInParent();
          
        System.out.println("CANVAS BOND" + canvasBound);
        System.out.println("CANVAS BOND IN LOCAL" + canvasBound1);
        System.out.println("CANVAS BOND IN PARENT" + canvasBound2);
        System.out.println("**BEFORE!! CANVAS X: "+canvas.getTranslateX()+" CANVAS Y: "+canvas.getTranslateY());
        Translate translate = new Translate(0, 0);
        if (keyEvent.getCode() == KeyCode.A) {
            System.out.println("KEY A");
            translate.setX(-10);
             System.out.println("**AFTER!! CANVAS X: "+canvas.getLayoutX()+" CANVAS Y: "+canvas.getLayoutY());
        } else if (keyEvent.getCode() == KeyCode.D) {
            translate.setX(10);
        } else if (keyEvent.getCode() == KeyCode.W) {
            translate.setY(10);
        } else {
            translate.setY(-10);
        }
        canvas.getTransforms().add(translate);
//        
//        
//        orgSceneX = t.getSceneX();
//            orgSceneY = t.getSceneY();
//            orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
//            orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
//
//            double offsetX = t.getSceneX() - orgSceneX;
//            double offsetY = t.getSceneY() - orgSceneY;
//            double newTranslateX = orgTranslateX + offsetX;
//            double newTranslateY = orgTranslateY + offsetY;
//             
//            ((Circle)(t.getSource())).setTranslateX(newTranslateX);
//            ((Circle)(t.getSource())).setTranslateY(newTranslateY);
    }
    
    private boolean outSideParentBounds(BorderPane parent, Bounds childBounds, double newX, double newY) {
        Bounds bounds =  parent.getCenter().getBoundsInParent();
        Bounds bounds1 =  parent.getCenter().getBoundsInLocal();
        Bounds bounds2 =  parent.getCenter().getLayoutBounds();
        System.out.println("BORDERPANE IN PARENT"+bounds);
        System.out.println("BORDERPANE IN LOCAL"+bounds1);
        System.out.println("BORDERPANE LAYOUT BOUNDS"+bounds2);
        
        //check if too left
        if( bounds.getMaxX() <= (newX + childBounds.getMaxX()) ) {
            return true ;
        }

        //check if too right
        if( bounds.getMinX() >= (newX + childBounds.getMinX()) ) {
            return true ;
        }

        //check if too down
        if( bounds.getMaxY() <= (newY + childBounds.getMaxY()) ) {
            return true ;
        }

        //check if too up
        if( bounds.getMinY() >= (newY + childBounds.getMinY()) ) {
            return true ;
        }

        return false;
    }
    
}
