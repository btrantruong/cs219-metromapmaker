package metromapmaker.transactions;


import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroObject;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class AddMetroObject_Transaction implements jTPS_Transaction {
    private m3Data data;
    private MetroObject metroObject;
    
    public AddMetroObject_Transaction(m3Data initData, MetroObject initMetroObject) {
        data = initData;
        metroObject = initMetroObject;
    }

    @Override
    public void doTransaction() {
        data.addMetroObject(metroObject);
    }

    @Override
    public void undoTransaction() {
        data.removeMetroObject(metroObject);
    }
}