package metromapmaker.gui;

import djf.AppTemplate;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import jtps.jTPS;
import metromapmaker.data.m3Data;
import metromapmaker.transactions.ChangeBackgroundColor_Transaction;
import metromapmaker.transactions.ChangeFontColor_Transaction;
import metromapmaker.transactions.ChangeTextFont_Transaction;
import metromapmaker.transactions.MoveLabel_Transaction;
import metromapmaker.transactions.RotateLabel_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class FontController {
    private AppTemplate app;
    
    public FontController(AppTemplate initApp) {
        app = initApp;
    }

    public void processChangeFont() {
        m3Data data = (m3Data)app.getDataComponent();
        if (data.isTextSelected()) {
            Text selectedText = (Text)data.getSelectedNode();
            m3Workspace workspace = (m3Workspace)app.getWorkspaceComponent();
            Font currentFont = workspace.getCurrentFontSettings();
            ChangeTextFont_Transaction transaction = new ChangeTextFont_Transaction(selectedText, currentFont);
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
        }
    }
    
    public void processRotateLabel(){
        m3Data data = (m3Data)app.getDataComponent();
        if(data.getSelectedStation()!=null){
            RotateLabel_Transaction transaction = new RotateLabel_Transaction(data.getSelectedStation());
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
        }
    }
    
    public void processMoveLabel(){
        m3Data data = (m3Data)app.getDataComponent();
        if (data.getSelectedStation()!=null){
            MoveLabel_Transaction transaction = new MoveLabel_Transaction(data.getSelectedStation());
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
        }
    }
    
     /**
     * This method processes a user request to select the 
     * background color.
     */
    public void processSelectFontColor() {
        m3Data data = (m3Data)app.getDataComponent();
	m3Workspace workspace = (m3Workspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getFontColorPicker().getValue();
	if (selectedColor != null) {
            Text selectedText = (Text)data.getSelectedNode();
	    ChangeFontColor_Transaction transaction = new ChangeFontColor_Transaction(selectedText, selectedColor);
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
	    app.getGUI().updateToolbarControls(false);
	}
    }
}