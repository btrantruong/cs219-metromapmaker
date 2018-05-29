package metromapmaker.transactions;

import javafx.scene.text.Text;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroStation;

/**
 *
 * @author McKillaGorilla
 */
public class RotateLabel_Transaction implements jTPS_Transaction {
    private MetroStation station;
    private double currentRotation;
    public RotateLabel_Transaction(MetroStation station) {
        this.station = station;
        currentRotation = station.getLabel().getRotate();
    }
 
    @Override
    public void doTransaction() {
        station.setRotation(currentRotation+90);
    }

    @Override
    public void undoTransaction() {
        station.setRotation(currentRotation-90);
    }    
}