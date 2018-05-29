package metromapmaker.data;

/**
 * This interface represents a family of draggable shapes.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public interface Draggable {
    public static final String RECTANGLE = "RECTANGLE";
    public static final String ELLIPSE = "ELLIPSE";
    public static final String TEXT = "TEXT";
    public static final String IMAGE = "IMAGE";
    public Draggable makeClone();
    public void start(double x, double y);
    public void setStart(int x, int y);
    public void drag(int x, int y);
    public void size(int x, int y);
    public double getX();
    public double getY();
    public double getWidth();
    public double getHeight();
    public String getNodeType();
}
