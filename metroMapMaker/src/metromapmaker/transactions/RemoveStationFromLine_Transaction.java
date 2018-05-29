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
public class RemoveStationFromLine_Transaction implements jTPS_Transaction {
    private m3Data data;
    private MetroStation station;
    private MetroLine line;
    
    public RemoveStationFromLine_Transaction(m3Data initData, MetroStation initStation) {
        data = initData;
        line = data.getSelectedLine();
        station = initStation;
    }

    @Override
    public void doTransaction() {
        line.removeStation(station);
        data.renderLines();
    }

    @Override
    public void undoTransaction() {
        line.addStation(station);
        data.renderLines();
    }
}