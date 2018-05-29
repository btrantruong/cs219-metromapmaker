/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.data;

/**
 *
 * @author sunshinger
 */
public enum m3State {
    ZOOMING,
    REMOVING_STATION_FROM_LINE,
    ADDING_STATION_TO_LINE,
    SELECTING_NODE,
    DRAGGING_NODE,
    DRAGGING_NOTHING,    
    SHOWING_PATH,
    STATION_LABEL_SELECTED
}
