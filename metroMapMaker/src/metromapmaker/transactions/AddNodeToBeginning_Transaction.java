package metromapmaker.transactions;


import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class AddNodeToBeginning_Transaction implements jTPS_Transaction {
    private m3Data data;
    private Node node;
    
    public AddNodeToBeginning_Transaction(m3Data initData, Node initNode) {
        data = initData;
        node = initNode;
    }

    @Override
    public void doTransaction() {
        data.addNodeAtIndex(node,0);
    }

    @Override
    public void undoTransaction() {
        data.removeNode(node);
    }
}