package metromapmaker.transactions;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroObject;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeElementName_Transaction implements jTPS_Transaction {
    private m3Data data;
    private MetroObject metroObject;
    private String oldName;
    private String name;
    
    public ChangeElementName_Transaction(m3Data data, MetroObject object, String initName) {
        this.data = data;
        metroObject = object;
        name = initName;
        oldName = metroObject.getName();
    }

    @Override
    public void doTransaction() {
        metroObject.changeName(name);
        data.changeMetroObjectName(name, oldName);
        data.renderLines();
        System.out.println("LINE LIST: " + data.getLineList());
    }

    @Override
    public void undoTransaction() {
        metroObject.changeName(oldName);
        data.changeMetroObjectName(oldName, name);
        data.renderLines();
    }    
}