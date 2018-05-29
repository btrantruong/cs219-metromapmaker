package metromapmaker.transactions;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import jtps.jTPS_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class ChangeFontColor_Transaction implements jTPS_Transaction {
    private Text text;
    private Color color;
    private Color oldColor;
    
    public ChangeFontColor_Transaction(Text initText, Color initColor) {
        text = initText;
        color = initColor;
        oldColor = (Color)text.getFill();
    }

    @Override
    public void doTransaction() {
        text.setFill(color);
    }

    @Override
    public void undoTransaction() {
        text.setFill(oldColor);
    }    
}