/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.data;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.text.Font;

/**
 *
 * @author Lakmi
 */
public interface MetroObject {
    public static final String LINE = "LINE";
    public static final String STATION = "STATION";
    public String getName();
    public void setName(String initName);
    public void changeName(String newName);
    public void render();
    public ArrayList getDrawings();
}
