package metromapmaker.transactions;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeStationFillColor_Transaction implements jTPS_Transaction {
    private Shape shape;
    private Color color;
    private Color oldColor;
    
    public ChangeStationFillColor_Transaction(Shape initShape, Color initColor) {
        shape = initShape;
        color = initColor;
        oldColor = (Color)shape.getFill();
    }

    @Override
    public void doTransaction() {
        shape.setFill(color);
    }

    @Override
    public void undoTransaction() {
        shape.setFill(oldColor);
    }    
}