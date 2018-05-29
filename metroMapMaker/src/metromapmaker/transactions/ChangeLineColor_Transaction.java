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
public class ChangeLineColor_Transaction implements jTPS_Transaction {
    private m3Data data;
    private MetroLine line;
    private Color color;
    private Color oldColor;
    
    public ChangeLineColor_Transaction(m3Data data, MetroLine initLine, Color initColor) {
        this.data = data;
        line = initLine;
        color = initColor;
        oldColor = line.getColor();
    }

    @Override
    public void doTransaction() {
        line.setColor(color);
        data.renderLines();
    }

    @Override
    public void undoTransaction() {
        line.setColor(oldColor);
        data.renderLines();
    }    
}