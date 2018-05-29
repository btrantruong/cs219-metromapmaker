package metromapmaker.transactions;


import javafx.scene.Node;
import javafx.scene.image.ImageView;
import jtps.jTPS_Transaction;
import metromapmaker.data.m3Data;

/**
 *
 * @author McKillaGorilla
 */
public class AddBackgroundImage_Transaction implements jTPS_Transaction {
    private m3Data data;
    private ImageView img;
    private ImageView oldImg;
    private String path;
    private String oldPath;
    
    public AddBackgroundImage_Transaction(m3Data initData, ImageView initImg, ImageView oldImg, String path) {
        data = initData;
        img = initImg;
        this.oldImg = oldImg;
        this.path = path;
        oldPath = data.getBackgroundImgPath();
    }

    @Override
    public void doTransaction() {
        data.setBackgroundImg(path, img);
    }

    @Override
    public void undoTransaction() {
        data.setBackgroundImg(oldPath, oldImg);
    }
}