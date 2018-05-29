package metromapmaker.transactions;


import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroStation;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class AddStationToLine_Transaction implements jTPS_Transaction {
    private m3Data data;
    private MetroStation station;
    private MetroLine line;
    
    public AddStationToLine_Transaction(m3Data initData, MetroStation initStation) {
        data = initData;
        line = data.getSelectedLine();
        station = initStation;
    }

    @Override
    public void doTransaction() {
        line.addStation(station);
        data.renderLines();
    }

    @Override
    public void undoTransaction() {
        line.removeStation(station);
        data.renderLines();
    }
}