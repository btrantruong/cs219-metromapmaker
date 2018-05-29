package metromapmaker.transactions;

import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroStation;

/**
 *
 * @author McKillaGorilla
 */
public class MoveLabel_Transaction implements jTPS_Transaction {

    private MetroStation station;
    private double currentTranslateX;
    private double currentTranslateY;

    public MoveLabel_Transaction(MetroStation station) {
        this.station = station;
        currentTranslateX = station.getLabel().getTranslateX();
        currentTranslateY = station.getLabel().getTranslateY();

    }

    @Override
    public void doTransaction() {
        if (currentTranslateY == 0) {
            //WHEN LABEL IS ABOVE PIVOT, TRANSLATE RIGHT OR DOWN
            if (currentTranslateX == 0) {
                station.setLabelTranslateY(currentTranslateY + 30);
            } else {
                station.setLabelTranslateX(currentTranslateX + 150);
            }
        } else {
            //WHEN LABEL IS BELOW PIVOT, TRANSLATE LEFT OR UP
            if (currentTranslateX == 0) {
                station.setLabelTranslateX(currentTranslateX - 100);
            } else {
                station.setLabelTranslateY(currentTranslateY - 30);
            }
        }
    }

    @Override
    public void undoTransaction() {
        station.setLabelTranslateX(currentTranslateX);
        station.setLabelTranslateY(currentTranslateY);
    }

}
