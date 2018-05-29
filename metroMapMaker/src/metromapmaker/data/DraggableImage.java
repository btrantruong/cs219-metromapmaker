package metromapmaker.data;
import javafx.scene.image.ImageView;
import static metromapmaker.m3PropertyType.DEFAULT_NODE_X;
import static metromapmaker.m3PropertyType.DEFAULT_NODE_Y;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class DraggableImage extends ImageView implements Draggable {
    double startX;
    double startY;
    String path;
    
    public DraggableImage() {
	setX(0.0);
	setY(0.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
    }
    
    @Override
    public DraggableImage makeClone() {
        DraggableImage cloneImage = new DraggableImage();
        cloneImage.setImage(getImage());
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        cloneImage.setX(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
        cloneImage.setY(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));
        cloneImage.setOpacity(getOpacity());
        return cloneImage;
    }
  
    public void start(int x, int y) {
	startX = x;
	startY = y;
	setX(x);
	setY(y);
    }
    
    @Override
    public void setStart(int initStartX, int initStartY) {
        startX = initStartX;
        startY = initStartY;
    }
    
    @Override
    public void drag(int x, int y) {
	//double diffX = x - (getX() + (getWidth()/2));
	//double diffY = y - (getY() + (getHeight()/2));
        double diffX = x - startX;
        double diffY = y - startY;
	double newX = getX() + diffX;
	double newY = getY() + diffY;
	xProperty().set(newX);
	yProperty().set(newY);
	startX = x;
	startY = y;
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
	// WE DON'T RESIZE THE IMAGE	
    }
    
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
	// WE DON'T RESIZE THE IMAGE
    }
    
    @Override
    public String getNodeType() {
	return IMAGE;
    }

    @Override
    public double getWidth() {
        return super.getImage().getWidth();
    }

    @Override
    public double getHeight() {
        return super.getImage().getHeight();
    }
    
    public void setImagePath(String initPath){
        path = initPath;
    }
    
    public String getImagePath(){
        return path;
    }

    @Override
    public void start(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}