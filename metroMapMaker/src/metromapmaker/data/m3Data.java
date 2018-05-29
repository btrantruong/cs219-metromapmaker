package metromapmaker.data;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import jtps.jTPS;
import javafx.scene.text.Text;
import jtps.jTPS;
import static metromapmaker.data.m3State.SELECTING_NODE;
import metromapmaker.gui.m3Workspace;
import static metromapmaker.m3PropertyType.DEFAULT_NODE_X;
import static metromapmaker.m3PropertyType.DEFAULT_NODE_Y;
import metromapmaker.transactions.AddMetroObject_Transaction;
import metromapmaker.transactions.AddNode_Transaction;
import metromapmaker.transactions.AddStationToLine_Transaction;
import metromapmaker.transactions.RemoveMetroObject_Transaction;
import metromapmaker.transactions.RemoveNode_Transaction;
import metromapmaker.transactions.RemoveStationFromLine_Transaction;
import properties_manager.PropertiesManager;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class m3Data implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES

    // THESE ARE THE THINGS THAT MAKE UP A METRO MAP
    String name;
    String backgroundImgPath;
    m3Path pathShowing;
    ObservableList<Node> logoNodes;
    ObservableList<MetroObject> stations;
    ObservableList<MetroObject> lines;
    HashMap<String, MetroLine> lineNames;
    HashMap<String, MetroStation> stationNames;

    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Shape newShape;

    // THIS IS THE NODE CURRENTLY SELECTED
    Node selectedNode;
    MetroObject selectedStation;
    MetroObject selectedLine;

    // CURRENT STATE OF THE APP
    m3State state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    // USE THIS WHEN THE NODE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    /**
     * This constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public m3Data(AppTemplate initApp) {
        // KEEP THE APP FOR LATER
        app = initApp;

        name = "";

        //INIT STATION AND LINE LIST 
        stationNames = new HashMap();
        lineNames = new HashMap();
        stations = FXCollections.observableArrayList();
        lines = FXCollections.observableArrayList();
        // NO SHAPE STARTS OUT AS SELECTED
        newShape = null;
        selectedNode = null;

        // THIS IS FOR THE SELECTED SHAPE
        DropShadow dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
        dropShadowEffect.setOffsetY(0.0f);
        dropShadowEffect.setSpread(1.0);
        dropShadowEffect.setColor(Color.YELLOW);
        dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
        dropShadowEffect.setRadius(15);
        highlightedEffect = dropShadowEffect;
    }

    public void initM3Data(String initName, ArrayList<MetroLine> linesData, ArrayList<MetroStation> stationsData, ArrayList<Connection> connectionsData) {
        // THIS IS THE NAME OF THE METRO SYSTEM, PROBABLY THE NAME OF THE CITY
        // OR METROPOLITAN AREA IT IS FOUND IN
        name = initName;

        // FIRST ADD ALL THE LINES AND STATIONS TO ASSOCIATIVE ARRAYS
        for (MetroLine line : linesData) {
            lines.add(line);
            lineNames.put(line.getName(), line);
        }
        for (MetroStation station : stationsData) {
            stations.add(station);
            stationNames.put(station.getName(), station);
        }
        // GET ALL THE CONNECTIONS AND HOOK UP ALL 
        for (Connection connection : connectionsData) {
            String lineName = connection.getLineName();
            String stationName = connection.getStationName();
            MetroLine line = lineNames.get(lineName);
            MetroStation station = stationNames.get(stationName);
            line.addStation(station);
            //station.addLine(line);
        }

        // NOW GO THROUGH ALL THE LINES AND TELL EACH ONE WHAT OTHER LINES
        // IT TRANSFERS TO. THIS PRECOMPUTING MAKES PATHFINDING CHEAPER TO
        // COMPUTE AND EASIER TO IMPLEMENT
        for (MetroObject line : lines) {
            ArrayList<MetroStation> stationsList = ((MetroLine) line).getStations();
            for (MetroStation station : stationsList) {
                for (MetroLine transferLine : station.getLines()) {
                    ((MetroLine) line).addTransfer(transferLine);
                }
            }
        }
    }

    /*
    This function finds a minimum transfer path from the start station to the end station.
     */
    public m3Path findMinimumTransferPath(String startStationName, String endStationName) {
        MetroStation startStation = stationNames.get(startStationName);
        MetroStation endStation = stationNames.get(endStationName);
        ArrayList<MetroLine> linesToTest = new ArrayList();
        ArrayList<String> visitedLineNames = new ArrayList();

        // THIS WILL COUNT HOW MANY TRANSFERS
        int numTransfers = 0;

        // THESE WILL BE PATHS THAT WE WILL BUILD TO TEST
        ArrayList<m3Path> testPaths = new ArrayList();

        // START BY PUTTING ALL THE LINES IN THE START STATION
        // IN OUR linesToTest Array
        for (MetroLine line : startStation.getLines()) {
            m3Path path = new m3Path(startStation, endStation);
            testPaths.add(path);
            path.addBoarding(line, startStation);
        }

        boolean found = false;
        boolean morePathsPossible = true;
        ArrayList<m3Path> completedPaths = new ArrayList();
        while (!found && morePathsPossible) {
            ArrayList updatedPaths = new ArrayList();
            for (m3Path testPath : testPaths) {
                // FIRST CHECK TO SEE IF THE DESTINATION IS ALREADY ON THE PATH
                if (testPath.hasLineWithStation(endStationName)) {
                    completedPaths.add(testPath);
                    found = true;
                    morePathsPossible = false;
                } else if (morePathsPossible) {
                    // GET ALL THE LINES CONNECTED TO THE LAST LINE ON THE TEST PATH
                    // THAT HAS NOT YET BEEN VISITED
                    MetroLine lastLine = testPath.tripLines.get(testPath.tripLines.size() - 1);
                    for (String transferName : lastLine.transferNames) {
                        String testLineName = transferName;
                        MetroLine testLine = lineNames.get(testLineName);
                        if (!testPath.hasLine(testLineName)) {
                            m3Path newPath = testPath.makeClone();
                            MetroStation intersectingStation = lastLine.findIntersectingStation(testLine);
                            newPath.addBoarding(testLine, intersectingStation);
                            updatedPaths.add(newPath);
                        }
                        // DEAD ENDS DON'T MAKE IT TO THE NEXT ROUND
                    }
                }
            }
            if (updatedPaths.size() > 0) {
                testPaths = updatedPaths;
                numTransfers++;
            } else {
                morePathsPossible = false;
            }
        }
        // WAS A PATH FOUND?
        if (found) {
            m3Path shortestPath = completedPaths.get(0);
            int shortestTime = shortestPath.calculateTimeOfTrip();
            for (int i = 1; i < completedPaths.size(); i++) {
                m3Path testPath = completedPaths.get(i);
                int timeOfTrip = testPath.calculateTimeOfTrip();
                if (timeOfTrip < shortestTime) {
                    shortestPath = testPath;
                    shortestTime = timeOfTrip;
                }
            }
            // WE NOW KNOW THE SHORTEST PATH, COMPLETE ITS DATA FOR EASY USE
            return shortestPath;
        } // NO PATH FOUND
        else {
            return null;
        }
    }

    public ObservableList<Node> getLogoNodes() {
        return logoNodes;
    }

    public void setLogoNodes(ObservableList<Node> initLogoNodes) {
        logoNodes = initLogoNodes;
    }

    public ObservableList<MetroObject> getLineList() {
        return lines;
    }

    public ObservableList<MetroObject> getStationList() {
        return stations;
    }

    public String getMetroName() {
        return name;
    }

    public void setMetroName(String initName) {
        name = initName;
    }

    public void changeMetroObjectName(String oldName, String newName) {
        if (stationNames.containsKey(oldName)) {
            stationNames.put(newName, stationNames.remove(oldName));
        } else if (lineNames.containsKey(oldName)) {
            lineNames.put(newName, lineNames.remove(oldName));
        }
    }

    public void renderLines() {
        if (!lines.isEmpty()) {
            ArrayList<Node> temp = new ArrayList();
            for (Node node : logoNodes) {
                if (this.hasBackgroundImage()) {
                    temp.add(logoNodes.get(0));
                }
//                if (node instanceof DraggableText) {
//                    DraggableText text = (DraggableText) node;
//                    if (!(text.belongsToMero())) {
//                        temp.add(node);
//                    }
//                }
            }
            logoNodes.clear();
            logoNodes.addAll(temp);
            for (MetroObject line : lines) {
                line.render();
                //NOW THAT WE HAVE A LIST OF CONNECTING LINES, ADD THEM TO THE VERY BEGINNING OF LOGONODES
                ArrayList<Line> connectingLines = line.getDrawings();
                logoNodes.addAll(connectingLines);
            }
            for (MetroObject line : lines) {
                logoNodes.add((Node) line);
            }
            for (MetroObject station : stations) {
                station.render();
                logoNodes.add((Node) station);
                logoNodes.add(((MetroStation)station).getLabel());
            }
        }
    }

    public void renderPath(m3Path path) {
        if (path != null) {
            // THESE ARE THE STATIONS IN THE TRIP
            ArrayList<MetroStation> tripStations = path.getTripStations();
            // FIRST THE LINES
            for (Node node : logoNodes) {
                for (MetroStation station : tripStations) {
                    double x = station.getCenterX();
                    double y = station.getCenterY();
                    if (node.contains(x, y)) {
                        highlightNode(node);
                    }
                }
            }
        }
    }

    public void unRenderPath(m3Path path) {
        if (path != null) {
            // THESE ARE THE STATIONS IN THE TRIP
            ArrayList<MetroStation> tripStations = path.getTripStations();
            // FIRST THE LINES
            for (Node node : logoNodes) {
                for (MetroStation station : tripStations) {
                    double x = station.getCenterX();
                    double y = station.getCenterY();
                    if (node.contains(x, y)) {
                        unhighlightNode(node);
                    }
                }
            }
        }
    }

    public void removeSelectedMetroStation() {
        if (selectedNode != null) {
            // FINALLY, ADD A TRANSACTION FOR REMOVING SELECTED NODE
            jTPS tps = app.getTPS();
            RemoveMetroObject_Transaction newTransaction = new RemoveMetroObject_Transaction(this, (MetroObject) selectedNode);
            tps.addTransaction(newTransaction);
            selectedNode = null;
            //@todo set state
        }
    }

    public void removeSelectedMetroLine() {
        if (selectedLine != null) {
            // FINALLY, ADD A TRANSACTION FOR REMOVING SELECTED NODE
            jTPS tps = app.getTPS();
            RemoveMetroObject_Transaction newTransaction = new RemoveMetroObject_Transaction(this, selectedLine);
            tps.addTransaction(newTransaction);
            selectedNode = null;
            //@todo set state
        }
    }

    public void removeSelectedNode() {
        if (selectedNode != null) {
            if (selectedNode instanceof DraggableText) {
                DraggableText text = (DraggableText) selectedNode;
                if (!text.belongsToMero()) {
                    jTPS tps = app.getTPS();
                    RemoveNode_Transaction newTransaction = new RemoveNode_Transaction(this, selectedNode);
                    tps.addTransaction(newTransaction);
                    selectedNode = null;
                }
            }
            else {
                 // FINALLY, ADD A TRANSACTION FOR REMOVING SELECTED NODE
            jTPS tps = app.getTPS();
            RemoveNode_Transaction newTransaction = new RemoveNode_Transaction(this, selectedNode);
            tps.addTransaction(newTransaction);
            selectedNode = null;
            }
           
            //@todo set state
        }
    }

    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void resetData() {
        setState(SELECTING_NODE);
        newShape = null;
        selectedNode = null;
        //CLEAR CANVAS
        logoNodes.clear();
        stations.clear();
        lines.clear();
        stationNames.clear();
        lineNames.clear();
        ((m3Workspace) app.getWorkspaceComponent()).getCanvas().getChildren().clear();
        ((m3Workspace) app.getWorkspaceComponent()).initDebugText();
        //RELOAD COMBOBOX
    }

    public Color getBackgroundColor() {
        return (Color) ((m3Workspace) app.getWorkspaceComponent()).getCanvas().getBackground().getFills().get(0).getFill();
    }

    public void setBackgroundColor(Color color) {
        Pane canvas = ((m3Workspace) app.getWorkspaceComponent()).getCanvas();
        BackgroundFill fill = new BackgroundFill(color, null, null);
        Background background = new Background(fill);
        canvas.setBackground(background);
    }

    public void selectSizedShape() {
        if (selectedNode != null) {
            unhighlightNode(selectedNode);
        }
        selectedNode = newShape;
        if (newShape instanceof MetroStation) {
            setSelectedStation((MetroStation) newShape);
        }
        if (newShape instanceof MetroLine) {
        }
        //@todo - load into combo box 
        highlightNode(selectedNode);
        newShape = null;
    }

    public Node selectTopNode(int x, int y) {
        Node node = getTopNode(x, y);
        if (node == selectedNode) {
            return node;
        }

        if (selectedNode != null) {
            unhighlightNode(selectedNode);
        }
        if (node != null && (node instanceof Draggable)) {
            highlightNode(node);
            ((Draggable) node).setStart(x, y);
//	    m3Workspace workspace = (m3Workspace)app.getWorkspaceComponent();
//	    workspace.loadSelectedNodeSettings(node);
        }
        selectedNode = node;
        if (node instanceof MetroStation) {
            selectedStation = (MetroStation) node;
        }
        return node;
    }

    public void unhighlightNode(Node node) {
        node.setEffect(null);
    }

    public void highlightNode(Node node) {
        node.setEffect(highlightedEffect);
    }

    public void startNewStation(String initName) {
        MetroStation newStation = new MetroStation();
        newStation.start(500, 400);
        newStation.setName(initName);
        newShape = newStation;
        initNewShape();
    }

    public void addStationToLine(MetroStation station) {
        if (selectedLine != null) {
            //ADD A TRANSACTION FOR ADDING THE NEW SHAPE
            jTPS tps = app.getTPS();
            m3Data data = (m3Data) app.getDataComponent();
            AddStationToLine_Transaction newTransaction = new AddStationToLine_Transaction(data, station);
            tps.addTransaction(newTransaction);
        }
    }

    public void removeStationFromLine(MetroStation station) {
        // FINALLY, ADD A TRANSACTION FOR ADDING THE NEW SHAPE
        jTPS tps = app.getTPS();
        m3Data data = (m3Data) app.getDataComponent();
        RemoveStationFromLine_Transaction newTransaction = new RemoveStationFromLine_Transaction(data, station);
        tps.addTransaction(newTransaction);
    }

    public void startNewLine(String initName) {
        MetroLine newLine = new MetroLine(initName);
        newShape = newLine;
        initNewShape();
    }

    public void initNewShape() {
        // DESELECT THE SELECTED SHAPE IF THERE IS ONE
        if (selectedNode != null) {
            unhighlightNode(selectedNode);
            selectedNode = null;
        }

        // USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
        m3Workspace workspace = (m3Workspace) app.getWorkspaceComponent();
        if (newShape instanceof MetroLine) {
            MetroLine line = (MetroLine) newShape;
            line.setColor(workspace.getLineColorPicker().getValue());
            line.setThickness(workspace.getLineThicknessSlider().getValue());
        } else if (newShape instanceof MetroStation) {
            MetroStation station = (MetroStation) newShape;
            station.setFill(workspace.getStationColorPicker().getValue());
            station.setRadius(workspace.getStationThicknessSlider().getValue());
        }
        ((MetroObject) newShape).render();

        // CHANGE STATE 
        state = m3State.SELECTING_NODE;
        // FINALLY, ADD A TRANSACTION FOR ADDING THE NEW SHAPE
        jTPS tps = app.getTPS();
        m3Data data = (m3Data) app.getDataComponent();
        AddMetroObject_Transaction newTransaction = new AddMetroObject_Transaction(data, (MetroObject) newShape);
        tps.addTransaction(newTransaction);
    }

    public Shape getNewShape() {
        return newShape;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }

    public MetroLine getSelectedLine() {
        return (MetroLine) selectedLine;
    }

    public MetroStation getSelectedStation() {
        return (MetroStation) selectedStation;
    }

    public Node getTopNode(int x, int y) {
        for (int i = logoNodes.size() - 1; i >= 0; i--) {
            Node node = (Node) logoNodes.get(i);
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public void deleteLine(MetroStation stationToDelete) {
        double x = stationToDelete.getCenterX();
        double y = stationToDelete.getCenterY();
        for (int i = logoNodes.size() - 1; i >= 0; i--) {
            Node node = (Node) logoNodes.get(i);
            if (node.contains(x, y) && (node instanceof Line)) {
                removeNode(node);
            }
        }
    }

    public void setSelectedNode(Node initSelectedNode) {
        selectedNode = initSelectedNode;
        if (initSelectedNode instanceof MetroStation) {
            selectedStation = (MetroStation) initSelectedNode;
        }
    }

    public void setSelectedStation(MetroObject station) {
        System.out.println("SELECTED STATION IN DATA BEFORE:" + selectedStation);
        selectedStation = station;
        System.out.println("SELECTED STATION IN DATA:" + selectedStation);
    }

    public void setSelectedLine(MetroObject line) {
        selectedLine = line;
        System.out.println("LINESELECTED: " + line.getName());
    }

    // METHODS NEEDED BY TRANSACTIONS
    public void moveNodeToIndex(Node nodeToMove, int index) {
        int currentIndex = logoNodes.indexOf(nodeToMove);
        int numberOfNodes = logoNodes.size();
        if ((currentIndex >= 0) && (index >= 0) && (index < numberOfNodes)) {
            // IS IT SUPPOSED TO BE THE LAST ONE?
            if (index == (numberOfNodes - 1)) {
                logoNodes.remove(currentIndex);
                logoNodes.add(nodeToMove);
            } else {
                logoNodes.remove(currentIndex);
                logoNodes.add(index, nodeToMove);
            }
        }
    }

    public void removeNode(Node nodeToRemove) {
        int currentIndex = logoNodes.indexOf(nodeToRemove);
        if (currentIndex >= 0) {
            logoNodes.remove(currentIndex);
        }
    }

    public void removeMetroObject(MetroObject objectToRemove) {
        int currentIndex = logoNodes.indexOf(objectToRemove);
        if (currentIndex >= 0) {
            //REMOVE THE OBJECT FROM RESPECTIVE ARRAYS
            logoNodes.remove(currentIndex);
            String objectName = objectToRemove.getName();
            if (lineNames.containsKey(objectName)) {
                MetroLine line = (MetroLine) objectToRemove;
                //UPDATE THE CONNECTIONS BETWEEN METRO OBJECTS
                ArrayList<MetroStation> stationsConnected = line.getStations();
                for (int i = 0; i < stationsConnected.size(); i++) {
                    line.removeStation(stationsConnected.get(i));
                }
                lineNames.remove(objectToRemove.getName());
                lines.remove(objectToRemove);
            }
            if (stationNames.containsKey(objectToRemove.getName())) {
                MetroStation station = (MetroStation) objectToRemove;
                ArrayList<MetroLine> linesConnected = station.getLines();
                for (int i = 0; i < linesConnected.size(); i++) {
                    linesConnected.get(i).removeStation(station);
                }
                stationNames.remove(objectToRemove.getName());
                stations.remove(objectToRemove);
            }

        }
    }

    public void addNode(Node nodeToAdd) {
        int currentIndex = logoNodes.indexOf(nodeToAdd);
        if (currentIndex < 0) {
            logoNodes.add(nodeToAdd);
        }
    }

    //ADD METRO STATION OR METRO LINE INTO THE CANVAS ALONG WITH LINE DRAWINGS AND LABELS
    public void addMetroObject(MetroObject objectToAdd) {
        int currentIndex = logoNodes.indexOf(objectToAdd);
        if (currentIndex < 0) {
            logoNodes.add((Node) objectToAdd);
            ArrayList<Node> objectDrawing = objectToAdd.getDrawings();
            logoNodes.addAll(objectDrawing);
            if (objectToAdd instanceof MetroLine) {
                lineNames.put(objectToAdd.getName(), (MetroLine) objectToAdd);
                lines.add((MetroLine) objectToAdd);
            }
            if (objectToAdd instanceof MetroStation) {
                stationNames.put(objectToAdd.getName(), (MetroStation) objectToAdd);
                stations.add((MetroStation) objectToAdd);
            }
        }
    }

    //CHECK IF STATION OR LINE NAME IS LEGAL
    public boolean isNameAvailable(String name) {
        boolean isAvailable = true;
        for (MetroObject object : stations) {
            if (name.equals(object.getName())) {
                isAvailable = false;
            }
            break;
        }
        for (MetroObject object : lines) {
            if (name.equals(object.getName())) {
                isAvailable = false;
            }
            break;
        }
        return isAvailable;
    }

    //** TO ADD ALL THE CONNECTING LINES TO THE BEGINNING OF THE LIST
    public void addConnectingLine(Node lineToAdd) {
        int currentIndex = logoNodes.indexOf(lineToAdd);
        if (currentIndex < 0) {
            logoNodes.add(lineToAdd);
        }
    }

    public int getIndexOfNode(Node node) {
        return logoNodes.indexOf(node);
    }

    public void addNodeAtIndex(Node node, int nodeIndex) {
        logoNodes.add(nodeIndex, node);
    }

    public boolean isShape(Draggable node) {
        return ((node.getNodeType() == Draggable.ELLIPSE)
                || (node.getNodeType() == Draggable.TEXT));
    }

    public boolean isTextSelected() {
        if (selectedNode == null) {
            return false;
        } else {
            return (selectedNode instanceof Text);
        }
    }

    public boolean hasBackgroundImage() {
        if (logoNodes.get(0) instanceof ImageView || backgroundImgPath != null) {
            return true;
        } else {
            return false;
        }
    }

    public m3State getState() {
        return state;
    }

    public void setState(m3State initState) {
        state = initState;
    }

    public boolean isInState(m3State testState) {
        return state == testState;
    }

    public void setBackgroundImg(String initPath, ImageView img) {
        backgroundImgPath = initPath;
        logoNodes.add(0, img);
    }

    public String getBackgroundImgPath() {
        return backgroundImgPath;
    }

    public void setPathShowing(m3Path pathToShow) {
        pathShowing = pathToShow;
    }

    public m3Path getPathShowing() {
        return pathShowing;
    }
}
