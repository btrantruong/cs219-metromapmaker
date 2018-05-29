package metromapmaker.transactions;

import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class RemoveNode_Transaction implements jTPS_Transaction {
    private m3Data data;
    private Node node;
    private int nodeIndex;
    
    public RemoveNode_Transaction(m3Data initData, Node initNode) {
        data = initData;
        node = initNode;
        nodeIndex = data.getIndexOfNode(node);
    }

    @Override
    public void doTransaction() {
        data.removeNode(node);
    }

    @Override
    public void undoTransaction() {
        data.addNodeAtIndex(node, nodeIndex);
    }
}