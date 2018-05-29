package metromapmaker.transactions;

import jtps.jTPS_Transaction;
import metromapmaker.data.MetroStation;
/**
 *
 * @author McKillaGorilla
 */
public class ChangeStationRadius_Transaction implements jTPS_Transaction {
    private MetroStation station;
    private double radius;
    private double oldRadius;
    
    public ChangeStationRadius_Transaction(MetroStation initStation, double initRadius) {
        station = initStation;
        radius = initRadius;
        oldRadius = station.getRadiusX();
    }

    @Override
    public void doTransaction() {
        station.setRadius(radius);
    }

    @Override
    public void undoTransaction() {
        station.setRadius(oldRadius);
    }    
}