package metromapmaker;

/**
 * This class provides the properties that are needed to be loaded for
 * setting up goLogoLo workspace controls including language-dependent
 * text.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public enum m3PropertyType {
    EDIT_LINE,
    EDIT_STATION,
    ADD_LINE,
    REMOVE_LINE,
    ADD_STATION_TO_LINE,
    REMOVE_STATION_FROM_LINE,
    STATION_LIST,
    ADD_STATION,
    REMOVE_STATION,
    SNAP_LABEL,
    MOVE_LABEL,
    ROTATE_LABEL,
    FIND_ROUTE,
    SET_IMAGE_BACKGROUND,
    ADD_IMAGE,
    ADD_LABEL,
    REMOVE_ELEMENT,
    BOLD_TEXT,
    ITALICIZE_TEXT,
    ZOOM_IN,
    ZOOM_OUT,
    COLLAPSE,
    EXPAND,
    
    /* m3 WORKSPACE ICONS */
    EDIT_LINE_ICON,
    EDIT_STATION_ICON,
    ADD_ICON,    
    REMOVE_ICON,    
    STATION_LIST_ICON,    
    ROTATE_LABEL_ICON,
    FIND_ROUTE_ICON,  
    BOLD_TEXT_ICON,    
    ITALICIZE_TEXT_ICON,    
    COLLAPSE_ICON,
    EXPAND_ICON,
    ZOOM_IN_ICON,
    ZOOM_OUT_ICON,
    
    /* m3 WORKSPACE TOOLTIPS */
    EDIT_LINE_TOOLTIP,
    EDIT_STATION_TOOLTIP,
    SELECT_TOOLTIP,
    REMOVE_TOOLTIP,
    ADD_RECTANGLE_TOOLTIP,
    ADD_ELLIPSE_TOOLTIP,
    ADD_IMAGE_TOOLTIP,
    ADD_TEXT_TOOLTIP,
    BOLD_TEXT_TOOLTIP,
    ITALICIZE_TEXT_TOOLTIP,
    MOVE_TO_BACK_TOOLTIP,
    MOVE_TO_FRONT_TOOLTIP,
    BACKGROUND_TOOLTIP,
    FILL_TOOLTIP,
    OUTLINE_TOOLTIP,
    SNAPSHOT_TOOLTIP,

    /* THESE ARE LABELS THAT WILL NEED LANGUAGE SPECIFIC CONTENT */
    METRO_LINE_PANE,
    METRO_STATION_PANE,
    DECOR_PANE,
    FONT_PANE,
    NAVIGATION_PANE,

    /* m3 WORKSPACE OPTIONS */
    START_STATION_OPTIONS,
    END_STATION_OPTIONS,
    FONT_FAMILY_COMBO_BOX_OPTIONS,
    FONT_SIZE_COMBO_BOX_OPTIONS,
    
    /* LANGUAGE CONTENT FOR DIALOGS */
    INPUT_DIALOG_TITLE, 
    INPUT_DIALOG_HEADER_TEXT, 
    INPUT_DIALOG_ERROR_TEXT, 
    INPUT_DIALOG_CONTENT_TEXT,
    CONFIRM_REMOVE_TITLE,
    CONFIRM_REMOVE_CONTENT_TEXT,
    CONFIRM_REMOVE_LINE_CONTENT_TEXT,
    CONFIRM_REMOVE_STATION_CONTENT_TEXT,
    
    STATION_LIST_DIALOG_TITLE,
    STATION_LIST_DIALOG_HEADER,
    PATH_TITLE,
    /* DEFAULT INITIAL POSITIONS WHEN ADDING IMAGES AND TEXT */
    DEFAULT_NODE_X,
    DEFAULT_NODE_Y,
    
    WELCOME_MESSAGE
    
}