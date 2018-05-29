package metromapmaker.transactions;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroLine;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeLineThickness_Transaction implements jTPS_Transaction {
    private m3Data data;
    private MetroLine line;
    private double outlineThickness;
    private double oldOutlineThickness;
    
    public ChangeLineThickness_Transaction(m3Data data, MetroLine initLine, double initOutlineThickness) {
        this.data = data;
        line = initLine;
        outlineThickness = initOutlineThickness;
        oldOutlineThickness = initLine.getThickness();
    }

    @Override
    public void doTransaction() {
        line.setThickness(outlineThickness);
        data.renderLines();
    }

    @Override
    public void undoTransaction() {
        line.setThickness(oldOutlineThickness);
        data.renderLines();
    }    
}