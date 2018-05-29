/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.data;

import java.util.ArrayList;

/**
 *
 * @author sunshinger
 */
public class m3Path {
    MetroStation startStation;
    MetroStation endStation;
    ArrayList<MetroLine> tripLines;
    ArrayList<String> tripLineNames;
    ArrayList<MetroStation> tripStations;
    ArrayList tripStationNames;
    ArrayList<MetroStation> boardingStations;
    final int STATION_COST;
    final int TRANSFER_COST;
    
    /*
   This object represents a complete path from one station to another in an M3Metro.
*/
public m3Path(MetroStation initStartStation, MetroStation initEndStation) {
    // THESE ARE PROVIDED EXTERNALLY AND ONCE WE HAVE THEM WE CAN BUILD OUR PATH
    startStation = initStartStation;
    endStation = initEndStation;
    STATION_COST = 3;
    TRANSFER_COST = 10;

    // OUR PATH WILL INCLUDE ALL THE DETAILS CONCERNING THE TRIP SUCH THAT IT
    // CAN BE DISPLAYED IN ALL SORTS OF CUSTOMIZED WAYS.

    // THESE ARE ALL THE LINES THAT ARE PART OF THE PATH.
    
    tripLines = new ArrayList();
    tripLineNames = new ArrayList();

    // THESE ARE ALL THE STATIONS VISITED WHEN FOLLOWING THE PATH
    tripStations = new ArrayList();
    tripStationNames = new ArrayList();

    // THESE ARE THE STATIONS AT WHICH TRANSFERS HAPPEN
    boardingStations = new ArrayList();
}

    /*
        clone

        This function clones this path object, returning a path initialized with all the same data.
    */
    public m3Path makeClone(){
        m3Path clonedPath = new m3Path(this.startStation, this.endStation);
        for (int i = 0; i < this.tripLines.size(); i++) {
            clonedPath.tripLines.add(this.tripLines.get(i));
            clonedPath.tripLineNames.add(this.tripLineNames.get(i));
        }
        for (int i = 0; i < this.tripStations.size(); i++) {
            clonedPath.tripStations.add(this.tripStations.get(i));
            clonedPath.tripStationNames.add(this.tripStationNames.get(i));
        }
        for (int i = 0; i < this.boardingStations.size(); i++) {
            clonedPath.boardingStations.add(this.boardingStations.get(i));
        }
        return clonedPath;
    }
    
    

    /*
        addBoarding

        This function adds a boarding to the path. Note there is one boarding per line.
    */
    public void addBoarding(MetroLine boardingLine, MetroStation boardingStation) {
        // WE'LL NEED THE LINE AND GET THE NAMES TOO FOR QUICK LOOKUP
       tripLines.add(boardingLine);
       tripLineNames.add(boardingLine.getName());
       
        // THESE ARE THE STATIONS WHERE A PERSON WOULD BOARD A TRAIN
        boardingStations.add(boardingStation);
    }

    /*
        getTripStations

        This function is for getting an array with a list of all the stations to be
        visited, including boarding and passing through while on a train.
    */
    public ArrayList<MetroStation> getTripStations(){
        // WE'LL RETURN AN ARRAY OF STATIONS AND WE'LL USE THE NAMES
        // FOR A QUICK LOOKUP
        tripStations = new ArrayList();
        tripStationNames = new ArrayList();

        // WE ONLY DO THIS IF WE HAVE A VALID TRIP
        if (this.isCompleteTrip()) {
            // IF WE MADE IT THIS FAR WE KNOW IT'S A GOOD TRIP
            int i = 0;
            while (i < boardingStations.size()-1) {
                ArrayList<MetroStation> stationsToAdd = this.generateStationsForPathOnLine(
                        tripLines.get(i), boardingStations.get(i), boardingStations.get(i+1));
                for (int j = 0; j < stationsToAdd.size(); j++) {
                    MetroStation stationToAdd = stationsToAdd.get(j);
                    if (!tripStationNames.contains(stationToAdd.getName())) {
                        tripStations.add(stationToAdd);
                        //tripStationNames[stationToAdd.name] = stationToAdd.name;
                    }
                }

                // ONTO THE NEXT LINE
                i++;
            }
            // AND NOW FOR THE LAST LINK IN THE CHAIN
            ArrayList<MetroStation> stationsToAdd = this.generateStationsForPathOnLine(
                    tripLines.get(i), boardingStations.get(i), endStation);
            for (int j = 0; j < stationsToAdd.size(); j++) {
                MetroStation stationToAdd = stationsToAdd.get(i);
                tripStations.add(stationToAdd);
            }
        }

        // RETURN THE STATIONS
        return this.tripStations;
    }

    /*
        isCompleteTrip

        This function tests to see if this path is complete, meaning one can get from its
        start station toe its end station using the boarding stops and trip lines.
    */
    public boolean isCompleteTrip() {
        if (tripLines.isEmpty()) {
            return false;
        }

        // THEN, IS THE END STATION ON THE LAST LINE? IF IT IS NOT THEN THE TRIP IS INCOMPLETE
        if(!tripLines.get(tripLines.size()-1).hasStation(endStation.getName())){
            return false;
        }

        // NOW, ARE ALL THE BOARDING STATIONS ON ALL THE TRIP LINES, IF NOT IT'S INCORRECT
        for (int i = 0; i <boardingStations.size(); i++) {
            if(!tripLines.get(i).hasStation(boardingStations.get(i).getName())){
                return false;
            }
        }

        // IF WE MADE IT THIS FAR WE KNOW IT'S A COMPLETE TRIP'
        return true;
    }

    /*
        generateStationsForPathOnLine

        This function returns a list of all the stations to be visited to get from one station
        to another on the same line.
    */
    public ArrayList<MetroStation> generateStationsForPathOnLine (MetroLine line, MetroStation station1, MetroStation station2) {
        ArrayList<MetroStation> stationsOnPath = new ArrayList();
        int station1Index = line.getStationIndex(station1.getName());
        int station2Index = line.getStationIndex(station2.getName());
        
        // FOR CIRCULAR LINES WE CAN GO IN EITHER DIRECTION
        if (line.isCircular) {
            if (station1Index >= station2Index) {
                int forward = station1Index - station2Index;
                int reverse = station2Index + line.getStations().size() - station1Index;
                if (forward < reverse) {
                    for (int i = station1Index; i >= station2Index; i--) {
                        MetroStation stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                }
                else {
                    for (int i = station1Index; i < line.getStations().size(); i++) {
                        MetroStation stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                    for (int i = 0; i <= station2Index; i++) {
                        MetroStation stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                }
            }
            // STILL CIRCULAR, BUT station1 IS BEFORE station2 IN THE ARRAY
            else {
                int forward = station2Index - station1Index;
                int reverse = station1Index + line.getStations().size() - station2Index;
                if (forward < reverse) {
                    for (int i = station1Index; i <= station2Index; i++) {
                        MetroStation stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                }
                else {
                    for (int i = station1Index; i >= 0; i--) {
                        MetroStation stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                    for (int i = line.stations.size()-1; i >= station2Index; i--) {
                        MetroStation stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                }
            }
        }
        // NOT CIRCULAR
        else {
            if (station1Index >= station2Index) {
                for (int i = station1Index; i >= station2Index; i--) {
                    MetroStation stationToAdd = line.getStations().get(i);
                    stationsOnPath.add(stationToAdd);
                }
            }
            else {
                for (int i = station1Index; i <= station2Index; i++) {
                    MetroStation stationToAdd = line.getStations().get(i);
                    stationsOnPath.add(stationToAdd);
                }
            }
        }
        return stationsOnPath;
    }


    /*
        hasLine

        This function tests to see if this trip includes the testLineName. If it does, true
        is returned, otherwise false.
    */
    public boolean hasLine(String testLineName){
        return tripLineNames.contains(testLineName);
    }
    
    /*
        hasLineWithStation

        This function tests to see if this trip has a line with the testStationName 
        station on it. If so, true is returned, false otherwise.
    */
    public boolean hasLineWithStation(String stationName){
        // GO THROUGH ALL THE LINES AND SEE IF IT'S IN ANY OF THEM'
        for (int i = 0; i < tripLines.size(); i++) {
            if (tripLines.get(i).hasStation(stationName)) {
                // YUP
                return true;
            }
        }
        // NOPE
        return false;
    }
    /*
        calculateTimeOfTrip

        This function calculates and returns the time of this trip taking into account
        the time it takes for a train to go from station to station is constant as is
        the time it takes to transfer lines.
    */
    public int calculateTimeOfTrip(){
        ArrayList stations = this.getTripStations();
        int stationsCost = (stations.size()-1) * this.STATION_COST;
        int transferCost = (tripLines.size()-1) * this.TRANSFER_COST;
        return stationsCost + transferCost;
    }
    
    public String toString(){
        return "MINIMUM TRANSFER PATH IS: "+ tripStations.toString();
    }
    
    public ArrayList<MetroLine> getTripLines(){
        return tripLines;
    }
    
    public ArrayList<MetroStation> getBoardingStations(){
        return boardingStations;
    }
    
    public MetroStation getEndStation(){
        return endStation;
    }
    public MetroStation getStartStation(){
        return startStation;
    }
}

