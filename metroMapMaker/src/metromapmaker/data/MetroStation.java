/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.data;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import static metromapmaker.data.Draggable.ELLIPSE;
import static metromapmaker.m3PropertyType.DEFAULT_NODE_X;
import static metromapmaker.m3PropertyType.DEFAULT_NODE_Y;
import properties_manager.PropertiesManager;

/**
 *
 * @author Lakmi
 */
public class MetroStation extends Ellipse implements Draggable, MetroObject, Comparable<MetroStation> {

    String name;
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    double defaultX = Double.parseDouble(props.getProperty(DEFAULT_NODE_X));
    double defaultY = Double.parseDouble(props.getProperty(DEFAULT_NODE_Y));
    double startCenterX;
    double startCenterY;
    Text label;
    ArrayList<MetroLine> lines;
    ArrayList<String> lineNames;
    HashMap<String, MetroLine> linesMap;
    ArrayList stationDrawings;
    double rotation; 
    double labelTranslateX;
    double labelTranslateY;
  
    
    public MetroStation() {
        setCenterX(0.0);
        setCenterY(0.0);
        setRadiusX(10);
        setRadiusY(10);
        setOpacity(1.0);
        startCenterX = 0.0;
        startCenterY = 0.0;
        name = "";
        rotation = 0;
        lines = new ArrayList();
        lineNames = new ArrayList();
        linesMap = new HashMap();
        stationDrawings = new ArrayList<>();
        label = new Text(name);
    }

    public void addLine(MetroLine initLine) {
        lines.add(initLine);
        lineNames.add(initLine.getName());
        linesMap.put(initLine.getName(), initLine);
    }
    
    public void removeLine(MetroLine lineToRemove){
        lines.remove(lineToRemove);
        lineNames.remove(lineToRemove.getName());
        linesMap.remove(lineToRemove.getName());
    }
    
    @Override
    public void changeName(String newName){
        for(MetroLine line: this.getLines()){
            line.updateStationsMap(newName, name);
        }
        name = newName;
    }
    
    // HELPER METHOD TO CHANGE HASH MAP WHEN A LINE'S NAME IS CHANGED 
    public void updateLinesMap(String newName, String oldName){
        lineNames.add(newName);
        lineNames.remove(oldName);
        linesMap.put(newName, linesMap.remove(oldName));
    }

    @Override
    public void render() {
        stationDrawings.clear();
        label = new Text(name);
        label.setRotate(rotation);
        label.setTranslateX(labelTranslateX);
        label.setTranslateY(labelTranslateY);
        label.setX(this.getCenterX() + 30);
        label.setY(this.getCenterY() - 20);
        stationDrawings.add(label);
    }
    
    public ArrayList<MetroLine> getLines(){
        return lines;
    }

    public void setRadius(double x) {
        setRadiusX(x);
        setRadiusY(x);
    }

    @Override
    public MetroStation makeClone() {
        MetroStation cloneEllipse = new MetroStation();
        cloneEllipse.setRadiusX(getRadiusX());
        cloneEllipse.setRadiusY(getRadiusY());
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        cloneEllipse.setCenterX(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
        cloneEllipse.setCenterY(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));
        cloneEllipse.setOpacity(getOpacity());
        cloneEllipse.setFill(getFill());
        cloneEllipse.setStroke(getStroke());
        cloneEllipse.setStrokeWidth(getStrokeWidth());
        return cloneEllipse;
    }

    @Override
    public void start(double x, double y) {
        setCenterX(x);
        setCenterY(y);
        startCenterX = x;
        startCenterY = y;
    }

    @Override
    public void drag(int x, int y) {
        double diffX = x - startCenterX;
        double diffY = y - startCenterY;
        double newX = getCenterX() + diffX;
        double newY = getCenterY() + diffY;
        setCenterX(newX);
        setCenterY(newY);
        startCenterX = x;
        startCenterY = y;
        label.setX(newX+30);
        label.setY(newY-20);
    }

    @Override
    public void setStart(int initStartX, int initStartY) {
        startCenterX = initStartX;
        startCenterY = initStartY;
    }

    @Override
    public void size(int x, int y) {
        double width = x - startCenterX;
        double height = y - startCenterY;
        double centerX = startCenterX + (width / 2);
        double centerY = startCenterY + (height / 2);
        setCenterX(centerX);
        setCenterY(centerY);
        setRadiusX(width / 2);
        setRadiusY(height / 2);

    }

    @Override
    public double getX() {
        return getCenterX() - getRadiusX();
    }

    @Override
    public double getY() {
        return getCenterY() - getRadiusY();
    }

    @Override
    public double getWidth() {
        return getRadiusX() * 2;
    }

    @Override
    public double getHeight() {
        return getRadiusY() * 2;
    }

    @Override
    public String getNodeType() {
        return ELLIPSE;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String initName) {
        name = initName;
    }

    public void setLocation(double initX, double initY) {
        setCenterX(initX);
        setCenterY(initY);
        startCenterX = initX;
        startCenterY = initY;
    }

    @Override
    public int compareTo(MetroStation compareStation) {
        double compareX = compareStation.getCenterX();
        double compareY = compareStation.getCenterY();
        if (this.getCenterX() == compareX) {
            //ascending order
            return (int) (this.getCenterY() - compareY);
        } else {
            return (int) (this.getCenterX() - compareX);
        }
    }

    public String toString() {
        return name + ", x: " + getCenterX() + ", y: " + getCenterY();
    }

    @Override
    public ArrayList getDrawings() {
        return stationDrawings;
    }

    public Text getLabel(){
        return label;
    }
    
    public void setLabel(Text initLabel){
        label = initLabel;
    }
    
    public void setRotation(double initRotation){
        rotation = initRotation;
        this.label.setRotate(initRotation);
    }
    
    public void setLabelTranslateX(double x){
        this.label.setTranslateX(x);
        labelTranslateX = x;
    }
    public void setLabelTranslateY(double y){
        this.label.setTranslateY(y);
        labelTranslateY = y;
    }
    
    public double getLabelTranslateX(){
        return label.getTranslateX();
    }
    public double getLabelTranslateY(){
        return label.getTranslateY();
    }
    
    public double getRotation(){
        return rotation;
    }
}
