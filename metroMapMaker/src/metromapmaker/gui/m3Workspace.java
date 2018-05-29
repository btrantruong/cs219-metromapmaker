/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.gui;

import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import djf.language.AppLanguageSettings;
import static djf.language.AppLanguageSettings.FILE_PROTOCOL;
import static djf.language.AppLanguageSettings.PATH_IMAGES;
import static djf.ui.AppDialogs.showYesNoCancelDialog;
import djf.ui.AppGUI;
import static djf.ui.AppGUI.ENABLED;
import static djf.ui.AppGUI.LABELED;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import static metromapmaker.css.m3Style.CLASS_BUTTON;
import static metromapmaker.css.m3Style.CLASS_EDIT_TOOLBAR;
import static metromapmaker.css.m3Style.CLASS_EDIT_TOOLBAR_ROW;
import static metromapmaker.css.m3Style.CLASS_RENDER_CANVAS;
import metromapmaker.data.Draggable;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroObject;
import metromapmaker.data.MetroStation;
import metromapmaker.data.m3Data;
import static metromapmaker.data.m3Data.WHITE_HEX;
import metromapmaker.data.m3Path;
import metromapmaker.data.m3State;
import static metromapmaker.gui.m3Dialogs.showEditDialog;
import static metromapmaker.gui.m3Dialogs.showStationList;
import static metromapmaker.m3PropertyType.ADD_IMAGE;
import static metromapmaker.m3PropertyType.ADD_LABEL;
import static metromapmaker.m3PropertyType.ADD_LINE;
import static metromapmaker.m3PropertyType.ADD_STATION;
import static metromapmaker.m3PropertyType.ADD_STATION_TO_LINE;
import static metromapmaker.m3PropertyType.BOLD_TEXT;
import static metromapmaker.m3PropertyType.COLLAPSE;
import static metromapmaker.m3PropertyType.CONFIRM_REMOVE_CONTENT_TEXT;
import static metromapmaker.m3PropertyType.CONFIRM_REMOVE_TITLE;
import static metromapmaker.m3PropertyType.DECOR_PANE;
import static metromapmaker.m3PropertyType.EDIT_LINE;
import static metromapmaker.m3PropertyType.EDIT_STATION;
import static metromapmaker.m3PropertyType.EXPAND;
import static metromapmaker.m3PropertyType.FIND_ROUTE;
import static metromapmaker.m3PropertyType.FONT_FAMILY_COMBO_BOX_OPTIONS;
import static metromapmaker.m3PropertyType.FONT_PANE;
import static metromapmaker.m3PropertyType.FONT_SIZE_COMBO_BOX_OPTIONS;
import static metromapmaker.m3PropertyType.INPUT_DIALOG_TITLE;
import static metromapmaker.m3PropertyType.ITALICIZE_TEXT;
import static metromapmaker.m3PropertyType.METRO_LINE_PANE;
import static metromapmaker.m3PropertyType.METRO_STATION_PANE;
import static metromapmaker.m3PropertyType.MOVE_LABEL;
import static metromapmaker.m3PropertyType.REMOVE_LINE;
import static metromapmaker.m3PropertyType.REMOVE_STATION;
import static metromapmaker.m3PropertyType.REMOVE_STATION_FROM_LINE;
import static metromapmaker.m3PropertyType.ROTATE_LABEL;
import static metromapmaker.m3PropertyType.SNAP_LABEL;
import static metromapmaker.m3PropertyType.STATION_LIST;
import static metromapmaker.m3PropertyType.REMOVE_ELEMENT;
import static metromapmaker.m3PropertyType.SET_IMAGE_BACKGROUND;
import static metromapmaker.m3PropertyType.STATION_LIST_DIALOG_HEADER;
import static metromapmaker.m3PropertyType.STATION_LIST_DIALOG_TITLE;
import static metromapmaker.m3PropertyType.ZOOM_IN;
import static metromapmaker.m3PropertyType.ZOOM_OUT;
import properties_manager.PropertiesManager;

public class m3Workspace extends AppWorkspaceComponent {
// HERE'S THE APP

    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;
    double toolbarWidth;

    // METRO LINE PANE
    // ComboBox, Color picker, 5 buttons: + - add station remove station, station list, slider
    VBox metroLinePane;
    HBox metroLinePane1;
    HBox metroLinePane2;
    Label metroLineLabel;
    MetroObjectComboBox lineNameComboBox;
    Button addLineButton;
    Button removeLineButton;
    Button addStationtoLineButton;
    Button removeStationfromLineButton;
    Button stationList;
    Button editLineButton;
    Slider lineThicknessSlider;
    ColorPicker lineColorPicker;
    TextField lineNameField;

    // METRO STATION PANE
    // ComboBox, Color picker, 5 buttons: + - add station remove station, station list, slider
    VBox metroStationPane;
    HBox metroStationPane1;
    HBox metroStationPane2;
    Label metroStationLabel;
    ComboBox<MetroObject> stationNameComboBox;
    Button addStationButton;
    Button removeStationButton;
    Button snapLabelButton;
    Button moveLabelButton;
    Button rotateLabelButton;
    Button editStationButton;
    Slider stationThicknessSlider;
    ColorPicker stationColorPicker;
    TextField stationNameField;

    // FINDROUTE PANE
    // 2 comboBoxes, find route button
    HBox findRoutePane;
    VBox findRoutePane1;
    VBox findRoutePane2;
    ComboBox startStationComboBox;
    ComboBox endStationComboBox;
    Button findRouteButton;

    // DECOR PANE
    // ColorPicker, buttons: set imageBackground, addImage, addLabel, removeElement
    VBox decorPane;
    HBox decorPane1;
    HBox decorPane2;
    Label decorPaneLabel;
    Button setImageBackgroundButton;
    Button addImageButton;
    Button addLabelButton;
    Button removeElementButton;
    ColorPicker backgroundColorPicker;

    // FONT PANE
    // Bold, Italic, 2 combo boxes (size, font), color picker
    VBox editTextPane;
    HBox editTextPane1;
    HBox editTextPane2;
    Label fontPaneLabel;
    ComboBox fontFamilyComboBox;
    ComboBox fontSizeComboBox;
    ToggleButton boldButton;
    ToggleButton italicsButton;
    ColorPicker fontColorPicker;

    // NAVIGATION PANE
    // Check box: showGrid, buttons: zoom in, out, decrease, increase map size
    HBox navigationPane;
    Button zoomInButton;
    Button zoomOutButton;
    Button expandButton;
    Button collapseButton;

    // THIS IS WHERE WE'LL RENDER OUR DRAWING, NOTE THAT WE
    // CALL THIS A CANVAS, BUT IT'S REALLY JUST A Pane
    Pane canvas;

    // FOR DISPLAYING DEBUG STUFF
    Text debugText;

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public m3Workspace(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // KEEP THE GUI FOR LATER
        gui = app.getGUI();

        // LAYOUT THE APP
        initLayout();

        // HOOK UP THE CONTROLLERS
        initControllers();

        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();
    }

    /**
     * Note that this is for displaying text during development.
     */
    public void setDebugText(String text) {
        //debugText.setText(text);
    }

    // ACCESSOR METHODS FOR COMPONENTS THAT EVENT HANDLERS
    // MAY NEED TO UPDATE OR ACCESS DATA FROM
    public ColorPicker getFontColorPicker() {
        return fontColorPicker;
    }

    public ColorPicker getBackgroundColorPicker() {
        return backgroundColorPicker;
    }

    public ColorPicker getStationColorPicker() {
        return stationColorPicker;
    }

    public ColorPicker getLineColorPicker() {
        return lineColorPicker;
    }

    public TextField getLineNameField() {
        return lineNameField;
    }

    public TextField getStationNameField() {
        return stationNameField;
    }

    public Slider getLineThicknessSlider() {
        return lineThicknessSlider;
    }

    public Slider getStationThicknessSlider() {
        return stationThicknessSlider;
    }

    public Pane getCanvas() {
        return canvas;
    }

    public void initDebugText() {
        canvas.getChildren().add(debugText);
    }

    // HELPER SETUP METHOD
    private void initLayout() {
        // WE'LL USE THIS TO GET TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
        m3Data data = (m3Data) app.getDataComponent();

        // THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
        editToolbar = new VBox();

        // LINE TOOLBAR
        metroLinePane = new VBox();
        metroLinePane1 = new HBox(28);
        metroLinePane2 = new HBox(8);

        metroLineLabel = new Label(props.getProperty(METRO_LINE_PANE));
        lineNameComboBox = new MetroObjectComboBox(data.getLineList());
        metroLinePane1.getChildren().add(metroLineLabel);
        metroLinePane1.getChildren().add(lineNameComboBox);
        editLineButton = gui.initPaneChildButton2(metroLinePane1, EDIT_LINE.toString(), ENABLED, false);
        lineNameField = new TextField();
        lineColorPicker = new ColorPicker(Color.DARKGRAY);
        lineColorPicker.setMaxSize(50, 50);

        addLineButton = gui.initPaneChildButton2(metroLinePane2, ADD_LINE.toString(), ENABLED, false);
        removeLineButton = gui.initPaneChildButton2(metroLinePane2, REMOVE_LINE.toString(), ENABLED, false);
        addStationtoLineButton = gui.initPaneChildButton2(metroLinePane2, ADD_STATION_TO_LINE.toString(), ENABLED, LABELED);
        removeStationfromLineButton = gui.initPaneChildButton2(metroLinePane2, REMOVE_STATION_FROM_LINE.toString(), ENABLED, LABELED);
        stationList = gui.initPaneChildButton2(metroLinePane2, STATION_LIST.toString(), ENABLED, false);

        lineThicknessSlider = new Slider(0, 10, 1);

        metroLinePane.getChildren().add(metroLinePane1);
        metroLinePane.getChildren().add(metroLinePane2);
        metroLinePane.getChildren().add(lineThicknessSlider);

        // STATION TOOLBAR
        metroStationPane = new VBox();
        metroStationPane1 = new HBox(23);
        metroStationPane2 = new HBox(8);

        stationNameComboBox = new MetroObjectComboBox(data.getStationList());
        stationColorPicker = new ColorPicker(Color.NAVY);
        stationColorPicker.setMaxSize(50, 50);
        stationNameField = new TextField();
        metroStationLabel = new Label(props.getProperty(METRO_STATION_PANE));
        metroStationPane1.getChildren().add(metroStationLabel);
        metroStationPane1.getChildren().add(stationNameComboBox);
        editStationButton = gui.initPaneChildButton2(metroStationPane1, EDIT_STATION.toString(), ENABLED, false);

        addStationButton = gui.initPaneChildButton2(metroStationPane2, ADD_STATION.toString(), ENABLED, false);
        removeStationButton = gui.initPaneChildButton2(metroStationPane2, REMOVE_STATION.toString(), ENABLED, false);
        snapLabelButton = gui.initPaneChildButton2(metroStationPane2, SNAP_LABEL.toString(), ENABLED, LABELED);
        moveLabelButton = gui.initPaneChildButton2(metroStationPane2, MOVE_LABEL.toString(), ENABLED, LABELED);
        rotateLabelButton = gui.initPaneChildButton2(metroStationPane2, ROTATE_LABEL.toString(), ENABLED, false);

        stationThicknessSlider = new Slider(5, 50, 10);

        metroStationPane.getChildren().add(metroStationPane1);
        metroStationPane.getChildren().add(metroStationPane2);
        metroStationPane.getChildren().add(stationThicknessSlider);

        // FIND ROUTE TOOLBAR
        findRoutePane = new HBox();
        findRoutePane1 = new VBox();
        startStationComboBox = new MetroObjectComboBox(data.getStationList());
        endStationComboBox = new MetroObjectComboBox(data.getStationList());
        findRoutePane1.getChildren().add(startStationComboBox);
        findRoutePane1.getChildren().add(endStationComboBox);
        findRoutePane.getChildren().add(findRoutePane1);
        findRouteButton = gui.initPaneChildButton2(findRoutePane, FIND_ROUTE.toString(), ENABLED, false);

        // DECOR TOOLBAR
        decorPane = new VBox();
        decorPane1 = new HBox(35);
        decorPane2 = new HBox();
        decorPaneLabel = new Label(props.getProperty(DECOR_PANE));
        backgroundColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
        CheckBox showGrid = new CheckBox("Show Grid");
        decorPane1.getChildren().add(decorPaneLabel);
        decorPane1.getChildren().add(backgroundColorPicker);
        decorPane1.getChildren().add(showGrid);
        setImageBackgroundButton = gui.initPaneChildButton2(decorPane2, SET_IMAGE_BACKGROUND.toString(), ENABLED, LABELED);
        addImageButton = gui.initPaneChildButton2(decorPane2, ADD_IMAGE.toString(), ENABLED, LABELED);
        addLabelButton = gui.initPaneChildButton2(decorPane2, ADD_LABEL.toString(), ENABLED, LABELED);
        removeElementButton = gui.initPaneChildButton2(decorPane2, REMOVE_ELEMENT.toString(), ENABLED, LABELED);
        decorPane.getChildren().add(decorPane1);
        decorPane.getChildren().add(decorPane2);
        toolbarWidth = decorPane.getWidth();

        // EDIT TEXT TOOLBAR
        editTextPane = new VBox();
        editTextPane1 = new HBox(225);
        editTextPane2 = new HBox(10);
        fontPaneLabel = new Label(props.getProperty(FONT_PANE));
        fontFamilyComboBox = initComboBox(FONT_FAMILY_COMBO_BOX_OPTIONS.toString());
        fontSizeComboBox = initComboBox(FONT_SIZE_COMBO_BOX_OPTIONS.toString());
        editTextPane2.getChildren().add(fontFamilyComboBox);
        editTextPane2.getChildren().add(fontSizeComboBox);
        boldButton = initToggleButton(editTextPane2, BOLD_TEXT.toString(), ENABLED);
        italicsButton = initToggleButton(editTextPane2, ITALICIZE_TEXT.toString(), ENABLED);
        fontColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
        fontColorPicker.setMaxSize(50, 50);
        editTextPane1.getChildren().add(fontPaneLabel);
        editTextPane1.getChildren().add(fontColorPicker);
        editTextPane.getChildren().add(editTextPane1);
        editTextPane.getChildren().add(editTextPane2);

        // SNAPSHOT TOOLBAR
        navigationPane = new HBox(5);
        zoomInButton = gui.initPaneChildButton(navigationPane, ZOOM_IN.toString(), ENABLED);
        zoomOutButton = gui.initPaneChildButton(navigationPane, ZOOM_OUT.toString(), ENABLED);
        collapseButton = gui.initPaneChildButton(navigationPane, COLLAPSE.toString(), ENABLED);
        expandButton = gui.initPaneChildButton(navigationPane, EXPAND.toString(), ENABLED);

        // NOW ORGANIZE THE EDIT TOOLBAR
        editToolbar.getChildren().add(metroLinePane);
        editToolbar.getChildren().add(metroStationPane);
        editToolbar.getChildren().add(findRoutePane);
        editToolbar.getChildren().add(editTextPane);
        editToolbar.getChildren().add(decorPane);
        editToolbar.getChildren().add(navigationPane);

        // WE'LL RENDER OUR STUFF HERE IN THE CANVAS
        canvas = new Pane();
        BackgroundFill fill = new BackgroundFill(Color.WHITE, null, null);
        Background background = new Background(fill);
        canvas.setBackground(background);
        debugText = new Text();
        debugText.setText("");
        debugText.setStroke(Color.BLUE);
        canvas.getChildren().add(debugText);
        debugText.setX(500);
        debugText.setY(500);
        MetroLine debugLine = new MetroLine("DEBUG1");
        canvas.getChildren().add(debugLine);
        Line debug = new Line(200, 200, 300, 300);
        canvas.getChildren().add(debug);
//	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
//        m3Data data = (m3Data)app.getDataComponent();
//	data.setLogoNodes(canvas.getChildren());
        data.setLogoNodes(canvas.getChildren());

        // AND NOW SETUP THE WORKSPACE
        workspace = new BorderPane();
        ((BorderPane) workspace).setLeft(editToolbar);
        ((BorderPane) workspace).setCenter(canvas);
//        Rectangle outputClip = new Rectangle(1000,1000);
//        outputClip.setX(0);
//        outputClip.setY(0);
//        canvas.setClip(outputClip);
//
//    canvas.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
//        outputClip.setWidth(newValue.getWidth());
//        outputClip.setHeight(newValue.getHeight());
//    });
    }

    public ToggleButton initToggleButton(Pane parent, String name, boolean enabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // LOAD THE ICON FROM THE PROVIDED FILE
        String iconProperty = name + "_ICON";
        String tooltipProperty = name + "_TOOLTIP";
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(iconProperty);
        Image buttonImage = new Image(imagePath);

        // NOW MAKE THE BUTTON
        ToggleButton button = new ToggleButton();
        button.setDisable(!enabled);
        button.setGraphic(new ImageView(buttonImage));
        String tooltipText = props.getProperty(tooltipProperty);
        Tooltip buttonTooltip = new Tooltip(tooltipText);
        button.setTooltip(buttonTooltip);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        AppLanguageSettings languageSettings = app.getLanguageSettings();
        languageSettings.addLabeledControl(name, button);

        // ADD IT TO THE PANE
        parent.getChildren().add(button);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }

    private ComboBox initComboBox(String comboPropertyList) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> comboOptions = props.getPropertyOptionsList(comboPropertyList);
        ObservableList oList = FXCollections.observableList(comboOptions);
        ComboBox cBox = new ComboBox(oList);
        cBox.getSelectionModel().selectFirst();
        return cBox;
    }

    // HELPER SETUP METHOD
    private void initControllers() {
        // NOW CONNECT THE BUTTONS TO THEIR HANDLERS
        // FIRST THE LINE TOOLBAR
        ElementController elementController = new ElementController(app);
        StyleController styleController = new StyleController(app);
        FontController fontController = new FontController(app);
        m3Data data = (m3Data) app.getDataComponent();

        // CanvasController canvasController = new CanvasController(app);
        editLineButton.setOnAction(e -> {
            styleController.processLineEditRequest();
        });
        addLineButton.setOnAction(e -> {
            elementController.processAddLine();
        });
        removeLineButton.setOnAction(e -> {
            elementController.processRemoveSelectedMetroLine();
        });

        addStationtoLineButton.setOnAction(e -> {
            System.out.println("Curent State: " + data.getState());
            elementController.processSelectStationToAdd();
        });
        removeStationfromLineButton.setOnAction(e -> {
            elementController.processSelectStationToRemove();
        });
        stationList.setOnAction(e -> {
            MetroLine line = (MetroLine) lineNameComboBox.getSelectionModel().getSelectedItem();
            System.out.println("SELECTED LINE IS: " + line.toString());
            showStationList(app.getGUI().getWindow(), STATION_LIST_DIALOG_TITLE, line.getName(), line.getStationNames());
        });
        lineThicknessSlider.valueProperty().addListener(e -> {
            styleController.processChangeLineThickness();
        });

        //THE STATION TOOLBAR
        editStationButton.setOnAction(e -> {
            styleController.processStationEditRequest();
        });
        addStationButton.setOnAction(e -> {
            elementController.processAddStation();
        });
        removeStationButton.setOnAction(e -> {
            elementController.processRemoveSelectedMetroStation();
        });
        snapLabelButton.setOnAction(e -> {
        });

        moveLabelButton.setOnAction(e -> {
            fontController.processMoveLabel();
        });
        rotateLabelButton.setOnAction(e -> {
            fontController.processRotateLabel();
        });

        stationThicknessSlider.valueProperty().addListener(e -> {
            styleController.processChangeStationRadius();
        });

        //FIND ROUTE TOOLBAR
        FindPathController findPathController = new FindPathController(app);
        findRouteButton.setOnAction(e -> {
            MetroStation startStation = (MetroStation) startStationComboBox.getSelectionModel().getSelectedItem();
            MetroStation endStation = (MetroStation) endStationComboBox.getSelectionModel().getSelectedItem();
            findPathController.requestFindPath(startStation.getName(), endStation.getName());
        });

        // AND THEN THE FONT TOOLBAR
        fontFamilyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                fontController.processChangeFont();
            }
        });

        fontSizeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                fontController.processChangeFont();
            }
        });
        boldButton.setOnAction(e -> {
            fontController.processChangeFont();
        });
        italicsButton.setOnAction(e -> {
            fontController.processChangeFont();
        });
        fontColorPicker.setOnAction(e -> {
            fontController.processSelectFontColor();
        });

        //DECOR TOOLBAR
        AddImageAndLabelController decorController = new AddImageAndLabelController(app);
        setImageBackgroundButton.setOnAction(e -> {
            decorController.processAddBackgroundImage();
        });
        addImageButton.setOnAction(e -> {
            decorController.processAddImage();
            System.out.println("**STATE: " + data.getState());
        });
        addLabelButton.setOnAction(e -> {
            decorController.processAddText();
        });
        removeElementButton.setOnAction(e -> {
            elementController.processRemoveSelectedNode();
        });

        backgroundColorPicker.setOnAction(e -> {
            styleController.processSelectBackgroundColor();
        });

        // ZOOM FUNCTIONALITIES
        CanvasController canvasController = new CanvasController(app);
        zoomInButton.setOnAction(e -> {
            canvasController.processZoomInRequest(canvas);
        });
        zoomOutButton.setOnAction(e -> {
            canvasController.processZoomOutRequest(canvas);
        });

        app.getGUI().getWindow().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                canvasController.processKeyPress((BorderPane) workspace, canvas, keyEvent);
            }
        }
        );

        //THE CANVAS
        canvas.setOnMousePressed(e -> {
            elementController.processCanvasMousePress((int) e.getX(), (int) e.getY());
        });
        canvas.setOnMouseReleased(e -> {
            elementController.processCanvasMouseRelease((int) e.getX(), (int) e.getY());
        });
        canvas.setOnMouseDragged(e -> {
            elementController.processCanvasMouseDragged((int) e.getX(), (int) e.getY());
        });

        // COMBO BOXES
        ComboBoxController comboBoxController = new ComboBoxController(app);

        lineNameComboBox.getSelectionModel()
                .selectedItemProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue
                    ) {
                        comboBoxController.processSelectLine();
                    }
                }
                );
        stationNameComboBox.getSelectionModel()
                .selectedItemProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue
                    ) {
                        comboBoxController.processSelectStation();
                    }
                }
                );

    }

    // HELPER METHOD
    public void loadSelectedNodeSettings(Node node) {
        if (node != null) {
            m3Data data = (m3Data) app.getDataComponent();
            if (data.getSelectedNode() instanceof Text) {
                Text selectedText = (Text) node;
                Color color = (Color) selectedText.getFill();
                Font font = selectedText.getFont();
                if (font != null) {
                    fontSizeComboBox.getSelectionModel().select(font.getSize());
                    fontFamilyComboBox.getSelectionModel().select(font.getFamily());
                }
                fontColorPicker.setValue(color);
            } else if (data.getSelectedNode() instanceof MetroStation) {
                MetroStation station = (MetroStation) node;
                Color color = (Color) station.getFill();
                double thickness = station.getRadiusX();
                stationColorPicker.setValue(color);
                stationThicknessSlider.setValue(thickness);
            }
        }
    }

    public void loadSelectedLineSettings() {
        m3Data data = (m3Data) app.getDataComponent();
        MetroLine current = data.getSelectedLine();
        lineColorPicker.setValue((Color) current.getColor());
        lineThicknessSlider.setValue(current.getThickness());
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle() {
        // NOTE THAT EACH CLASS SHOULD CORRESPOND TO
        // A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
        // CSS FILE
        canvas.getStyleClass().add(CLASS_RENDER_CANVAS);

        // COLOR PICKER STYLE
        lineColorPicker.getStyleClass().add(CLASS_BUTTON);
        stationColorPicker.getStyleClass().add(CLASS_BUTTON);
        backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);
        editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
        metroLinePane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        metroStationPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        editTextPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        findRoutePane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        decorPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        navigationPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    @Override
    public void resetLanguage() {
        //
    }

    public void updateStationComboBox() {
        m3Data data = (m3Data) app.getDataComponent();
        MetroStation selected = data.getSelectedStation();
        if (selected != null) {
            stationNameComboBox.setValue(selected);
        }
    }

    public void updateLineComboBox() {
        m3Data data = (m3Data) app.getDataComponent();
        lineNameComboBox.setItems(data.getLineList());
        MetroLine selected = data.getSelectedLine();
        if (selected != null) {
            lineNameComboBox.setValue(selected);
        }
    }

    @Override
    public void reloadWorkspace(AppDataComponent data) {
        m3Data dataManager = (m3Data) data;
//        if (dataManager.isInState(m3State.STATION_LABEL_SELECTED)) {
//            removeElementButton.setDisable(true);
//        } 
//        else 
            if (dataManager.isInState(m3State.SELECTING_NODE)
                ) {
            boolean nodeIsNotSelected = dataManager.getSelectedNode() == null;
            addLineButton.setDisable(true);
            removeLineButton.setDisable(nodeIsNotSelected);
            addStationtoLineButton.setDisable(false);
            removeStationfromLineButton.setDisable(false);
            findRouteButton.setDisable(nodeIsNotSelected);
        }
           else if(dataManager.isInState(m3State.REMOVING_STATION_FROM_LINE)){
            addLineButton.setDisable(true);
            addStationtoLineButton.setDisable(false);
            removeStationfromLineButton.setDisable(true);
            findRouteButton.setDisable(true);
            }
            else if(dataManager.isInState(m3State.ADDING_STATION_TO_LINE)){
            addLineButton.setDisable(true);
            addStationtoLineButton.setDisable(false);
            removeStationfromLineButton.setDisable(true);
            findRouteButton.setDisable(true);
            }
        loadSelectedNodeSettings(dataManager.getSelectedNode());
        removeLineButton.setDisable(dataManager.getSelectedLine() == null);
        removeStationButton.setDisable(dataManager.getSelectedStation() == null);
        rotateLabelButton.setDisable(dataManager.getSelectedStation() == null);
        moveLabelButton.setDisable(dataManager.getSelectedStation() == null);
        backgroundColorPicker.setValue(dataManager.getBackgroundColor());
    }

    /**
     * Clips the children of the specified {@link Region} to its current size.
     * This requires attaching a change listener to the region’s layout bounds,
     * as JavaFX does not currently provide any built-in way to clip children.
     *
     * @param region the {@link Region} whose children to clip
     * @param arc the {@link Rectangle#arcWidth} and {@link Rectangle#arcHeight}
     * of the clipping {@link Rectangle}
     * @throws NullPointerException if {@code region} is {@code null}
     */
    static void clipChildren(Region region, double arc) {

        final Rectangle outputClip = new Rectangle();
        outputClip.setX(380);
        outputClip.setY(0);
        outputClip.setArcWidth(arc);
        outputClip.setArcHeight(arc);
        region.setClip(outputClip);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

    public MetroStation getStartStation() {
        MetroStation startStation = (MetroStation) startStationComboBox.getSelectionModel().getSelectedItem();
        return startStation;
    }

    public MetroStation getEndStation() {
        MetroStation endStation = (MetroStation) endStationComboBox.getSelectionModel().getSelectedItem();
        return endStation;
    }

    public MetroObject getCurrentLine() {
        MetroObject line = (MetroObject) lineNameComboBox.getSelectionModel().getSelectedItem();
        return line;
    }

    public MetroObject getCurrentStation() {
        MetroObject station = (MetroObject) stationNameComboBox.getSelectionModel().getSelectedItem();
        return station;
    }

    public Font getCurrentFontSettings() {
        String fontFamily = fontFamilyComboBox.getSelectionModel().getSelectedItem().toString();
        int fontSize = Integer.valueOf(fontSizeComboBox.getSelectionModel().getSelectedItem().toString().replace(".0", ""));
        FontWeight weight = FontWeight.NORMAL;
        if (boldButton.isSelected()) {
            weight = FontWeight.BOLD;
        }
        FontPosture posture = FontPosture.REGULAR;
        if (italicsButton.isSelected()) {
            posture = FontPosture.ITALIC;
        }
        Font newFont = Font.font(fontFamily, weight, posture, fontSize);
        return newFont;
    }

}
