package djf.ui;
import static djf.AppPropertyType.APP_LOGO;
import static djf.language.AppLanguageSettings.FILE_PROTOCOL;
import static djf.language.AppLanguageSettings.PATH_IMAGES;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import properties_manager.PropertiesManager;

/**
 * This class serves to present custom text messages to the user when
 * events occur. Note that it always provides the same controls, a label
 * with a message, and a single Ok button. 
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class WelcomeDialogSingleton extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static WelcomeDialogSingleton singleton = null;
    
    // HERE ARE THE DIALOG COMPONENTS
    BorderPane mainPane;
    VBox recentWorkPane;
    VBox createNewPane;
    HBox messagePane;
    Scene messageScene;
    Label recentWorkLabel;
    Button closeButton;
    String selection;
    /**
     * Initializes this dialog so that it can be used repeatedly
     * for all kinds of messages. Note this is a singleton design
     * pattern so the constructor is private.
     * 
     * @param owner The owner stage of this modal dialoge.
     * 
     * @param closeButtonText Text to appear on the close button.
     */
    private WelcomeDialogSingleton() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static WelcomeDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new WelcomeDialogSingleton();
	return singleton;
    }
    
    /**
     * This function fully initializes the singleton dialog for use.
     * 
     * @param owner The window above which this dialog will be centered.
     */
    public void init(Stage owner) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // MAKE IT MODAL
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        
        // WE'LL PUT EVERYTHING HERE
        mainPane = new BorderPane();
        recentWorkPane = new VBox();
        createNewPane = new VBox();
        messagePane = new HBox();
        messagePane.setAlignment(Pos.CENTER);
        createNewPane.setAlignment(Pos.CENTER);
        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        recentWorkLabel = new Label("Open Current Work");        

        // CLOSE BUTTON
        closeButton = new Button ("Close");
        closeButton.setOnAction(e->{ WelcomeDialogSingleton.this.close(); });
        
        //RECENT WORK 
        Hyperlink map1 = new Hyperlink("Lausanne Map");
        map1.setOnMousePressed(e->{
            WelcomeDialogSingleton.this.selection = "Create New";
            System.out.println("HEYSELECTION:"+selection);
        });
        map1.setOnAction(e->{
            WelcomeDialogSingleton.this.selection = "Create New";
            System.out.println("BLAHSELECTION:"+selection);
        });
        recentWorkPane.getChildren().addAll(recentWorkLabel, map1);
        recentWorkPane.getChildren().add(closeButton);
        
        //CREATE NEW
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        ImageView image = new ImageView(imagePath);
        image.setPreserveRatio(true);
        image.setFitWidth(500);
        Button newWorkButton = new Button("Create New Work");
        newWorkButton.setOnAction(e->{ WelcomeDialogSingleton.this.close(); });
        createNewPane.getChildren().add(image);
        createNewPane.getChildren().add(newWorkButton);

     
        //mainPane.setLeft(recentWorkPane);
        mainPane.setCenter(createNewPane);
        mainPane.setTop(messagePane);
        
        // MAKE IT LOOK NICE
        recentWorkPane.setPadding(new Insets(80, 60, 80, 60));
        recentWorkPane.setSpacing(20);
        createNewPane.setPadding(new Insets(80, 60, 80, 60));
        createNewPane.setSpacing(20);

        // AND PUT IT IN THE WINDOW
        messageScene = new Scene(mainPane);
        this.setScene(messageScene);
    }
    
    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either YES, NO, or CANCEL, depending on which
     * button the user selected when this dialog was presented.
     */
    public String getSelection() {
        System.out.println("SELECTION:"+selection);
        return selection;
    }
 
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param title The title to appear in the dialog window.
     * 
     * @param message Message to appear inside the dialog.
     */
    public void show(String title, String message) {
	// SET THE DIALOG TITLE BAR TITLE
	setTitle(title);
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 24));
        this.messagePane.getChildren().add(messageLabel);
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
     public void setButtonsText(String closeString){
        this.closeButton.setText(closeString);
    }
}