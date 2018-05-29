/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.data;

/**
 *
 * @author sunshinger
 */
public class Connection {
    MetroLine line;
    MetroStation station;
    String lineName;
    String stationName;
          
    public Connection(MetroLine initLine, MetroStation initStation){
        line = initLine;
        station = initStation;
    }
    public Connection(String initLineName, String initStationName){
        lineName = initLineName;
        stationName = initStationName;
    }
    public MetroStation getStation(){
        return station;
    }
    
    public MetroLine getLine(){
        return line;
    }
    public String getStationName(){
        return stationName;
    }
    
    public String getLineName(){
        return lineName;
    }
}
