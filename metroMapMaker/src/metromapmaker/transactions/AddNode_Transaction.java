package metromapmaker.transactions;


import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class AddNode_Transaction implements jTPS_Transaction {
    private m3Data data;
    private Node node;
    
    public AddNode_Transaction(m3Data initData, Node initNode) {
        data = initData;
        node = initNode;
    }

    @Override
    public void doTransaction() {
        data.addNode(node);
        //data.renderLines();
    }

    @Override
    public void undoTransaction() {
        data.removeNode(node);
        //data.renderLines();
    }
}