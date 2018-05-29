package metromapmaker.transactions;


import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroObject;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class RemoveMetroObject_Transaction implements jTPS_Transaction {
    private m3Data data;
    private MetroObject metroObject;
    
    public RemoveMetroObject_Transaction(m3Data initData, MetroObject initMetroObject) {
        data = initData;
        metroObject = initMetroObject;
    }

    @Override
    public void doTransaction() {
        data.removeMetroObject(metroObject);
        data.renderLines();
    }

    @Override
    public void undoTransaction() {
        data.addMetroObject(metroObject);
        data.renderLines();
    }
}