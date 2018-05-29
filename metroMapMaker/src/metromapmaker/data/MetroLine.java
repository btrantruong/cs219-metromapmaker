/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import static metromapmaker.m3PropertyType.DEFAULT_NODE_X;
import static metromapmaker.m3PropertyType.DEFAULT_NODE_Y;
import properties_manager.PropertiesManager;

/**
 *
 * @author Lakmi This object represents a line on an M3Metro. Note that each
 * line has references to the stations on its line and it knows what lines it
 * can transfer to. ALSO note that some lines are circular, which is important
 * for pathfinding.
 */
public class MetroLine extends Line implements MetroObject {

    double defaultX;
    double defaultY;
    DraggableText startLabel;
    DraggableText endLabel;
    String name;
    MetroStation startStation;
    MetroStation endStation;
    ArrayList<Node> lineDrawings;
    ArrayList<MetroStation> stations;
    ArrayList<String> stationNames;
    HashMap<String, MetroStation> stationsMap;
    HashMap<String, MetroLine> transfersMap;
    ArrayList<MetroLine> transfers;
    ArrayList<String> transferNames;
    boolean isCircular;
    Color color;
    double thickness;

    public MetroLine(String initName) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        defaultX = Double.parseDouble(props.getProperty(DEFAULT_NODE_X));
        defaultY = Double.parseDouble(props.getProperty(DEFAULT_NODE_Y));
        setOpacity(1.0);
        name = initName;
        lineDrawings = new ArrayList();
        stations = new ArrayList();
        stationNames = new ArrayList();
        stationsMap = new HashMap<>();
        transfersMap = new HashMap();
        transfers = new ArrayList();
        transferNames = new ArrayList();
        color = new Color(0, 0, 1, 0);
        thickness = 2;
    }

    public void addStation(MetroStation station) {
        int index = stations.indexOf(station);
        if (index < 0) {
            //ADD THIS STATION TO LINE
            stations.add(station);
            stationsMap.put(station.getName(), station);
            stationNames.add(station.getName());
            //ADD THIS LINE TO STATION
            station.addLine(this);
        }
    }

    /*
        This function adds a transfer line to this line, meaning one can connect
        to that line from this one.
     */
    public void addTransfer(MetroLine transferLine) {
        if (!(transferLine.getName().equals(name))
                && (!(transfersMap.containsValue(transferLine)))) {
            transfersMap.put(transferLine.getName(), transferLine);
            transferNames.add(transferLine.getName());
        }
    }

    /*
     This function searches for the stationName argument in this line's list
     of stations and returns its index number.
     */
    public int getStationIndex(String stationName) {
        // GO THROUGH ALL THE STATIONS
        for (int i = 0; i < stations.size(); i++) {
            // AND FIND THE ONE WITH THE SAME NAME
            if (stationName.equals(stations.get(i).getName())) {
                return i;
            }
        }
        // NOT FOUND
        return -1;
    }

    public void removeStation(MetroStation station) {
        int index = stations.indexOf(station);
        if (index >= 0) {
            stationNames.remove(station.getName());
            stationsMap.remove(station.getName());
            stations.remove(station);
            //REMOVE THE CONNECTION FROM THE STATION TO THIS LINE
            station.removeLine(this);
        }
    }

    public boolean hasStation(String stationName) {
        if (stationsMap.containsKey(stationName)) {
            return true;
        } else {
            return false;
        }
    }

    /*
        This function finds and returns the station where this station intersects
        the intersectingLine argument.
     */
    public MetroStation findIntersectingStation(MetroLine intersectingLine) {
        // GO THROUGH ALL THE STATIONS IN THIS LINE
        for (int i = 0; i < stations.size(); i++) {
            MetroStation station1 = stations.get(i);
            String stationName = station1.getName();
            // FOUND IT
            if (intersectingLine.hasStation(stationName)) {
                return station1;
            }
        }
        // THEY DON'T SHARE A STATION'
        return null;
    }

    public void sortStationsInLine() {
        Collections.sort(stations);
        startStation = stations.get(0);
        endStation = stations.get(stations.size() - 1);
        System.out.println("After Sorting: " + stations);
    }

    @Override
    public void changeName(String newName) {
        for (MetroStation station : this.getStations()) {
            station.updateLinesMap(newName, name);
        }
        for (MetroLine line : this.getTransfers()) {
            line.updateTransfersMap(newName, name);
        }
        name = newName;
    }

    public void updateStationsMap(String newName, String oldName) {
        stationNames.remove(oldName);
        stationNames.add(newName);
        stationsMap.put(newName, stationsMap.remove(oldName));
    }

    public void updateTransfersMap(String newName, String oldName) {
        transferNames.remove(oldName);
        transferNames.add(newName);
        transfersMap.put(newName, transfersMap.remove(oldName));
    }

    @Override
    public void render() {
        startLabel = new DraggableText(name);
        endLabel = new DraggableText(name);
        startLabel.setBelongsToMetro(true);
        endLabel.setBelongsToMetro(true);
        System.out.println("DRAWINGS IN LINE BEFORE RENDER: " + lineDrawings);
        lineDrawings.clear();
        if (stations.isEmpty()) {
            Line defaultLine = new Line(defaultX, defaultY, defaultX, defaultY + 100);
            defaultLine.setStroke(color);
            defaultLine.setStrokeWidth(thickness);
            startLabel.start(defaultLine.getStartX() - 20, defaultLine.getStartY() - 40);
            endLabel.start(defaultLine.getStartX() - 20, defaultLine.getStartY() + 40);
            drawLine(defaultLine);

        }
        System.out.println("DRAWINGS IN LINE BEFORE RENDER: " + lineDrawings);
        if (stations.size() == 1) {
            MetroStation station = stations.get(0);
            Line segment = new Line(station.getCenterX() - 50, station.getCenterY(), station.getCenterX() + 50, station.getCenterY());
            segment.setStroke(color);
            segment.setStrokeWidth(thickness);
            startLabel.start(station.getCenterX() - 50, station.getCenterY());
            endLabel.start(station.getCenterX() + 50, station.getCenterY());
            drawLine(segment);
        }
        if (stations.size() > 1) {
            sortStationsInLine();
            for (int i = 0; i < stations.size() - 1; i++) {
                MetroStation begin = stations.get(i);
                MetroStation end = stations.get(i + 1);
                Line segment = new Line(begin.getCenterX(), begin.getCenterY(), end.getCenterX(), end.getCenterY());
                segment.startXProperty().bind(begin.centerXProperty());
                segment.startYProperty().bind(begin.centerYProperty());
                segment.endXProperty().bind(end.centerXProperty());
                segment.endYProperty().bind(end.centerYProperty());
                segment.setStroke(color);
                segment.setStrokeWidth(thickness);
                drawLine(segment);
            }
            startLabel.start(startStation.getCenterX() - 50, startStation.getCenterY() - 20);
            endLabel.start(startStation.getCenterX() + 50, startStation.getCenterY() + 40);
            System.out.println("DRAWINGS IN LINE AFTER RENDER: " + lineDrawings);
        }
        drawLabel(startLabel);
        drawLabel(endLabel);
    }

    public ArrayList<String> getStationNames() {
        return stationNames;
    }

    public ArrayList<MetroStation> getStations() {
        return stations;
    }

    public ArrayList<MetroLine> getTransfers() {
        return transfers;
    }

    public HashMap<String, MetroStation> getStationsMap() {
        return stationsMap;
    }

    public void setCircular(boolean circular) {
        isCircular = circular;
    }

    public Color getColor() {
        return color;
    }

    public double getThickness() {
        return thickness;
    }

    public boolean getCircular() {
        return isCircular;
    }

    public void setThickness(double initThickness) {
        thickness = initThickness;
    }

    public void setColor(Color initColor) {
        color = initColor;
    }

    public void drawLine(Line line) {
        lineDrawings.add(line);
    }

    public void drawLabel(DraggableText label) {
        lineDrawings.add(label);
    }

    @Override
    public ArrayList<Node> getDrawings() {
        return lineDrawings;
    }

    public DraggableText getStartLabel() {
        return startLabel;
    }

    public DraggableText getEndLabel() {
        return endLabel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String initName) {
        name = initName;
    }

    public String toString() {
        return name + ", station: " + stations.toString();
    }
    
}
