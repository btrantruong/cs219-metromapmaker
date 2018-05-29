package metromapmaker.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.components.AppWorkspaceComponent;
import static djf.language.AppLanguageSettings.FILE_PROTOCOL;
import static djf.language.AppLanguageSettings.PATH_IMAGES;
import djf.ui.AppDialogs;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import javax.json.JsonString;
import metromapmaker.data.Connection;
import metromapmaker.data.m3Data;
import metromapmaker.data.Draggable;
import static metromapmaker.data.Draggable.RECTANGLE;
import metromapmaker.data.DraggableImage;
import metromapmaker.data.DraggableText;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroObject;
import metromapmaker.data.MetroStation;
import metromapmaker.data.m3State;
import metromapmaker.gui.m3Workspace;
import sun.font.FontFamily;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class m3Files1 implements AppFileComponent {

    // FOR JSON LOADING
    static final String JSON_NAME = "name";
    static final String JSON_BG_COLOR = "background";
    static final String JSON_BG_IMAGE = "background_image";
    static final String JSON_COLOR = "color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_LINES = "lines";
    static final String JSON_ELEMENTS = "elements";
    static final String JSON_LABELS = "labels";
    static final String JSON_IMAGES = "images";
    static final String JSON_LABEL = "label";
    static final String JSON_FONT = "font";
    static final String JSON_FONT_FAMILY = "font_family";
    static final String JSON_FONT_SIZE = "font_size";
    static final String JSON_FONT_STYLE = "font_style";
    static final String JSON_BOLD = "bold";
    static final String JSON_ITALIC = "italic";
    static final String JSON_STATIONS = "stations";
    static final String JSON_STATION_NAMES = "station_names";
    static final String JSON_CIRCULAR = "circular";
    static final String JSON_X = "x";
    static final String JSON_ORIENTATION = "orientation";
    static final String JSON_TRANSLATE_X = "label_translation_x";
    static final String JSON_TRANSLATE_Y = "label_translation_y";
    static final String JSON_Y = "y";
    static final String JSON_IMG_PATH = "path";
    static final String JSON_STATION_RADIUS = "radius";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";

    /**
     * This method is for saving user work, which in the case of this
     * application means the data that together draws the logo.
     *
     * @param data The data management component for this application.
     *
     * @param filePath Path (including file name/extension) to where to save the
     * data to.
     *
     * @throws IOException Thrown should there be an error writing out data to
     * the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {

        // GET THE DATA
        m3Data dataManager = (m3Data) data;
        //NAME 
        String mapName = dataManager.getMetroName();
        // FIRST THE BACKGROUND COLOR
        Color bgColor = dataManager.getBackgroundColor();
        JsonObject bgColorJson = makeJsonColorObject(bgColor);

        String backgroundImgPath = "";
        if (dataManager.hasBackgroundImage()) {
            backgroundImgPath = dataManager.getBackgroundImgPath();
        }
        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder stationArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lineArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder imagesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder labelsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Node> mapElements = dataManager.getLogoNodes();
        ObservableList<MetroObject> stations = dataManager.getStationList();
        ObservableList<MetroObject> lines = dataManager.getLineList();

        for (MetroObject station : stations) {
            MetroStation metroStation = (MetroStation) station;
            String name = metroStation.getName();
            double labelOrientation = metroStation.getRotation();
            double labelTranslateX = metroStation.getLabelTranslateX();
            double labelTranslateY = metroStation.getLabelTranslateY();
            //CAREFUL ABOUT THE GETX() METHOD
            double x = metroStation.getX();
            double y = metroStation.getY();
            JsonObject fillColorJson = makeJsonColorObject((Color) metroStation.getFill());
            double radius = metroStation.getRadiusX();
            JsonObject stationJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_ORIENTATION, labelOrientation)
                    .add(JSON_TRANSLATE_X, labelTranslateX)
                    .add(JSON_TRANSLATE_Y, labelTranslateY)
                    .add(JSON_X, x)
                    .add(JSON_Y, y)
                    .add(JSON_COLOR, fillColorJson)
                    .add(JSON_STATION_RADIUS, radius).build();
            stationArrayBuilder.add(stationJson);
        }
        for (MetroObject line : lines) {
            MetroLine metroLine = ((MetroLine) line);
            String name = metroLine.getName();
            boolean circular = metroLine.getCircular();
            //CREATE AN ARRAY OF STATION_NAMES
            ArrayList<String> stationList = metroLine.getStationNames();
            System.out.println("STATIONNAME LIST:" + stationList);
            JsonArrayBuilder stationNamesArrayBuilder = Json.createArrayBuilder();
            for (String s : stationList) {
                stationNamesArrayBuilder.add(s);
            }
            JsonArray stationNamesArray = stationNamesArrayBuilder.build();
            JsonObject lineColorJson = makeJsonColorObject((Color) metroLine.getStroke());
            double outlineThickness = metroLine.getStrokeWidth();
            JsonObject lineJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_CIRCULAR, circular)
                    .add(JSON_COLOR, lineColorJson)
                    .add(JSON_STATION_NAMES, stationNamesArray)
                    .add(JSON_OUTLINE_THICKNESS, outlineThickness).build();
            lineArrayBuilder.add(lineJson);
        }
        for (Node node : mapElements) {
            if (node instanceof DraggableText) {
                DraggableText text = (DraggableText) node;
                if (!text.belongsToMero()) {
                    JsonObject labelJson = makeJsonLabelObject((Text) node);
                    labelsArrayBuilder.add(labelJson);
                }
            }
            if (node instanceof DraggableImage) {
                JsonObject imageJson = makeJsonImageObject((DraggableImage) node);
                imagesArrayBuilder.add(imageJson);
            }
        }
        JsonArray stationsArray = stationArrayBuilder.build();
        JsonArray linesArray = lineArrayBuilder.build();
        JsonArray labelsArray = labelsArrayBuilder.build();
        JsonArray imagesArray = imagesArrayBuilder.build();
        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_NAME, mapName)
                .add(JSON_BG_IMAGE, backgroundImgPath)
                .add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_LINES, linesArray)
                .add(JSON_STATIONS, stationsArray)
                .add(JSON_LABELS, labelsArray)
                .add(JSON_IMAGES, imagesArray).build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    private JsonObject makeJsonColorObject(Color color) {
        JsonObject colorJson = Json.createObjectBuilder()
                .add(JSON_RED, color.getRed())
                .add(JSON_GREEN, color.getGreen())
                .add(JSON_BLUE, color.getBlue())
                .add(JSON_ALPHA, color.getOpacity()).build();
        return colorJson;

    }

    private JsonObject makeJsonFontObject(Font font) {
        boolean italic = font.getStyle().contains("talic");
        boolean bold = font.getStyle().contains("old");
        JsonObject fontJson = Json.createObjectBuilder()
                .add(JSON_FONT_FAMILY, font.getFamily())
                .add(JSON_BOLD, bold)
                .add(JSON_ITALIC, italic)
                .add(JSON_FONT_SIZE, font.getSize()).build();
        return fontJson;

    }

    private JsonObject makeJsonLabelObject(Text text) {
        JsonObject labelFontJson = makeJsonFontObject(text.getFont());
        JsonObject labelColorJson = makeJsonColorObject((Color) text.getFill());
        String content = text.getText();
        double x = text.getX();
        double y = text.getY();
        JsonObject labelJson = Json.createObjectBuilder()
                .add(JSON_NAME, content)
                .add(JSON_X, x)
                .add(JSON_Y, y)
                .add(JSON_FONT, labelFontJson)
                .add(JSON_COLOR, labelColorJson).build();
        return labelJson;

    }

    private JsonObject makeJsonImageObject(DraggableImage img) {
        String path = img.getImagePath();
        double x = img.getX();
        double y = img.getY();
        JsonObject imgJson = Json.createObjectBuilder()
                .add(JSON_X, x)
                .add(JSON_Y, y)
                .add(JSON_IMG_PATH, path).build();
        return imgJson;
    }

    /**
     * This method loads data from a JSON formatted file into the data
     * management component and then forces the updating of the workspace such
     * that the user may edit the data.
     *
     * @param data Data management component where we'll load the file into.
     *
     * @param filePath Path (including file name/extension) to where to load the
     * data from.
     *
     * @throws IOException Thrown should there be an error reading in data from
     * the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
        m3Data dataManager = (m3Data) data;
        dataManager.resetData();
        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);

        // LOAD THE BACKGROUND COLOR
        if (json.containsKey(JSON_BG_COLOR)) {
            Color bgColor = loadColor(json, JSON_BG_COLOR);
            dataManager.setBackgroundColor(bgColor);
        }
        String mapName = "";
        // GET NAME OF MAP 
        if (json.containsKey(JSON_NAME)) {
            mapName = json.getString(JSON_NAME);
        }

        // AND NOW LOAD ALL THE LINES, STATIONS AND CONNECTIONS
        ArrayList<MetroLine> linesData = extractLines(json);
        ArrayList<MetroStation> stationsData = extractStations(json);
        ArrayList<Connection> connectionsData = extractConnections(json);
        //INITIALIZE DATA MANAGER WITH THE DATA
        dataManager.initM3Data(mapName, linesData, stationsData, connectionsData);
        dataManager.renderLines();

        //GET THE OTHER STUFFS 
        if (json.containsKey(JSON_BG_IMAGE)) {
            String path = json.getString(JSON_BG_IMAGE);
            if (!(path.equals(""))) {
                dataManager.setBackgroundImg(path, loadBgImage(path));
            }
        }

        if (json.containsKey(JSON_LABELS)) {
            ArrayList<DraggableText> labelsData = extractLabels(json);
            dataManager.getLogoNodes().addAll(labelsData);
        }
        if (json.containsKey(JSON_IMAGES)) {
            ArrayList<DraggableImage> imagesData = extractImages(json);
            dataManager.getLogoNodes().addAll(imagesData);
        }
        System.out.println("AFTER LOADING:"+ dataManager.getLogoNodes());
        //CHANGE STATE 
        dataManager.setState(m3State.SELECTING_NODE);

        // UPDATE THE UI ROUTE TOOLBAR
//*******      
    }

    private ArrayList<MetroLine> extractLines(JsonObject json) {
        ArrayList<MetroLine> linesArray = new ArrayList();
        JsonArray jsonLineArray = json.getJsonArray(JSON_LINES);
        for (int i = 0; i < jsonLineArray.size(); i++) {
            JsonObject jsonShape = jsonLineArray.getJsonObject(i);
            MetroLine line = loadLine(jsonShape);
            linesArray.add(line);
        }
        return linesArray;
    }

    private ArrayList<MetroStation> extractStations(JsonObject json) {
        ArrayList<MetroStation> stationsArray = new ArrayList();
        JsonArray jsonStationArray = json.getJsonArray(JSON_STATIONS);
        for (int i = 0; i < jsonStationArray.size(); i++) {
            JsonObject jsonShape = jsonStationArray.getJsonObject(i);
            MetroStation station = loadStation(jsonShape);
            stationsArray.add(station);
        }
        return stationsArray;
    }

    private ArrayList<Connection> extractConnections(JsonObject json) {
        ArrayList<Connection> connections = new ArrayList();
        JsonArray jsonLineArray = json.getJsonArray(JSON_LINES);
        for (int i = 0; i < jsonLineArray.size(); i++) {
            JsonObject jsonLine = jsonLineArray.getJsonObject(i);
            String lineName = jsonLine.getString(JSON_NAME);
            JsonArray jsonStationNames = jsonLine.getJsonArray(JSON_STATION_NAMES);
            if (!jsonStationNames.isEmpty()) {
                for (int k = 0; k < jsonStationNames.size(); k++) {
                    String stationName = jsonStationNames.getString(k);
                    Connection connection = new Connection(lineName, stationName);
                    connections.add(connection);
                }
            }
        }
        return connections;
    }

    private ArrayList<DraggableImage> extractImages(JsonObject json) throws IOException {
        ArrayList<DraggableImage> imagesArray = new ArrayList();
        JsonArray jsonImgArray = json.getJsonArray(JSON_IMAGES);
        for (int i = 0; i < jsonImgArray.size(); i++) {
            JsonObject jsonShape = jsonImgArray.getJsonObject(i);
            DraggableImage image = loadImage(jsonShape);
            imagesArray.add(image);
        }
        return imagesArray;
    }

    private ArrayList<DraggableText> extractLabels(JsonObject json) {
        ArrayList<DraggableText> labelsArray = new ArrayList();
        JsonArray jsonLabelsArray = json.getJsonArray(JSON_LABELS);
        for (int i = 0; i < jsonLabelsArray.size(); i++) {
            JsonObject jsonShape = jsonLabelsArray.getJsonObject(i);
            DraggableText label = loadLabel(jsonShape);
            labelsArray.add(label);
        }
        return labelsArray;
    }

    private double getDataAsDouble(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigDecimalValue().doubleValue();
    }

    private MetroStation loadStation(JsonObject jsonShape) {
        // FIRST BUILD THE PROPER SHAPE TYPE
        String name = jsonShape.getString(JSON_NAME);
        MetroStation station = new MetroStation();

        // AND THEN ITS DRAGGABLE PROPERTIES
        double x = getDataAsDouble(jsonShape, JSON_X);
        double y = getDataAsDouble(jsonShape, JSON_Y);
        double labelTranslateX = getDataAsDouble(jsonShape, JSON_TRANSLATE_X);
        double labelTranslateY = getDataAsDouble(jsonShape, JSON_TRANSLATE_Y);
        double orientation = getDataAsDouble(jsonShape, JSON_ORIENTATION);
        station.setRotation(orientation);
        station.setLabelTranslateX(labelTranslateX);
        station.setLabelTranslateY(labelTranslateY);

        if (jsonShape.containsKey(JSON_COLOR) && jsonShape.containsKey(JSON_STATION_RADIUS)) {
            Color fillColor = loadColor(jsonShape, JSON_COLOR);
            double radius = getDataAsDouble(jsonShape, JSON_STATION_RADIUS);
            station.setFill(fillColor);
            station.setRadius(radius);
        }
        station.setLocation(x, y);
        station.setName(name);

        // ALL DONE, RETURN IT
        return station;
    }

    private DraggableImage loadImage(JsonObject jsonShape) throws IOException {
        // FIRST BUILD THE PROPER SHAPE TYPE
        String path = jsonShape.getString(JSON_IMG_PATH);
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + path;
        Image image = new Image(imagePath);

        DraggableImage img = new DraggableImage();

        // AND THEN ITS DRAGGABLE PROPERTIES
        double x = getDataAsDouble(jsonShape, JSON_X);
        double y = getDataAsDouble(jsonShape, JSON_Y);
        // THEN LOAD ITS FILL AND OUTLINE PROPERTIES
        img.start((int) x, (int) y);
        img.setImage(image);

        // ALL DONE, RETURN IT
        return img;
    }

    private ImageView loadBgImage(String path) throws IOException {
        // FIRST BUILD THE PROPER SHAPE TYPE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + path;
        Image image = new Image(imagePath);
        ImageView bg = new ImageView(image);

        // ALL DONE, RETURN IT
        return bg;
    }

    private MetroLine loadLine(JsonObject jsonShape) {
        // FIRST BUILD THE PROPER SHAPE TYPE
        String name = jsonShape.getString(JSON_NAME);
        MetroLine line = new MetroLine(name);

        // AND THEN ITS PROPERTIES
        Color lineColor = loadColor(jsonShape, JSON_COLOR);
        boolean isCircular = jsonShape.getBoolean(JSON_CIRCULAR);
        if (jsonShape.containsKey(JSON_OUTLINE_THICKNESS)) {
            double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
            line.setThickness(outlineThickness);
        }

        line.setColor(lineColor);
        line.setCircular(isCircular);
        // ALL DONE, RETURN IT
        return line;
    }

    private Color loadColor(JsonObject json, String colorToGet) {
        JsonObject jsonColor = json.getJsonObject(colorToGet);
        double red = getDataAsDouble(jsonColor, JSON_RED);
        double green = getDataAsDouble(jsonColor, JSON_GREEN);
        double blue = getDataAsDouble(jsonColor, JSON_BLUE);
        double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
        Color loadedColor = new Color(red, green, blue, alpha);
        return loadedColor;
    }

    private Font loadFont(JsonObject json, String fontToGet) {
        JsonObject jsonFont = json.getJsonObject(fontToGet);
        boolean bold = jsonFont.getBoolean(JSON_BOLD);
        boolean italic = jsonFont.getBoolean(JSON_ITALIC);
        FontPosture posture = FontPosture.REGULAR;
        FontWeight weight = FontWeight.MEDIUM;
        if (italic) {
            posture = FontPosture.ITALIC;
        }
        if (bold) {
            weight = FontWeight.BOLD;
        }
        String fontFamily = jsonFont.getString(JSON_FONT_FAMILY);
        double fontSize = getDataAsDouble(jsonFont, JSON_FONT_SIZE);
        Font loadedFont = Font.font(fontFamily, weight, posture, fontSize);
        return loadedFont;
    }

    private DraggableText loadLabel(JsonObject jsonShape) {
        // FIRST BUILD THE PROPER SHAPE TYPE
        String content = jsonShape.getString(JSON_NAME);

        DraggableText text = new DraggableText(content);

        // AND THEN ITS DRAGGABLE PROPERTIES
        double x = getDataAsDouble(jsonShape, JSON_X);
        double y = getDataAsDouble(jsonShape, JSON_Y);
        // THEN LOAD ITS FILL AND OUTLINE PROPERTIES
        Color textColor = loadColor(jsonShape, JSON_COLOR);
        Font textFont = loadFont(jsonShape, JSON_FONT);
        text.start(x, y);
        text.setFont(textFont);
        text.setFill(textColor);

        // ALL DONE, RETURN IT
        return text;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    /**
     * This method is provided to satisfy the compiler, but it is not used by
     * this application.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        // GET THE DATA
        m3Data dataManager = (m3Data) data;
        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder stationArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lineArrayBuilder = Json.createArrayBuilder();
        ObservableList<MetroObject> stations = dataManager.getStationList();
        ObservableList<MetroObject> lines = dataManager.getLineList();
        for (MetroObject station : stations) {
            MetroStation metroStation = (MetroStation) station;
            String name = metroStation.getName();
            //CAREFUL ABOUT THE GETX() METHOD
            double x = metroStation.getX();
            double y = metroStation.getY();
            JsonObject stationJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_X, x)
                    .add(JSON_Y, y).build();
            stationArrayBuilder.add(stationJson);
        }
        for (MetroObject line : lines) {
            MetroLine metroLine = ((MetroLine) line);
            String name = metroLine.getName();
            boolean circular = metroLine.getCircular();
            //CREATE AN ARRAY OF STATION_NAMES
            ArrayList<String> stationList = metroLine.getStationNames();
            System.out.println("STATIONNAME LIST:" + stationList);
            JsonArrayBuilder stationNamesArrayBuilder = Json.createArrayBuilder();
            for (String s : stationList) {
                stationNamesArrayBuilder.add(s);
            }
            JsonArray stationNamesArray = stationNamesArrayBuilder.build();
            JsonObject lineColorJson = makeJsonColorObject((Color) metroLine.getStroke());
            JsonObject lineJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_CIRCULAR, circular)
                    .add(JSON_COLOR, lineColorJson)
                    .add(JSON_STATION_NAMES, stationNamesArray).build();
            lineArrayBuilder.add(lineJson);
        }
        JsonArray stationsArray = stationArrayBuilder.build();
        JsonArray linesArray = lineArrayBuilder.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_LINES, linesArray)
                .add(JSON_STATIONS, stationsArray)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    public void processSnapshot(AppWorkspaceComponent workspace, String filePath) {
        m3Workspace metroWorkspace = (m3Workspace) workspace;
        Pane canvas = metroWorkspace.getCanvas();
        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        File file = new File(filePath);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This method is provided to satisfy the compiler, but it is not used by
     * this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        // AGAIN, WE ARE NOT USING THIS IN THIS ASSIGNMENT
    }
}
