package metromapmaker;

import static djf.AppPropertyType.APP_ERROR_CONTENT;
import static djf.AppPropertyType.APP_ERROR_TITLE;
import static djf.AppPropertyType.APP_TITLE;
import static djf.AppPropertyType.PROPERTIES_ERROR_CONTENT;
import static djf.AppPropertyType.PROPERTIES_ERROR_TITLE;
import java.util.Locale;
import djf.AppTemplate;
import djf.language.AppLanguageSettings;
import static djf.language.AppLanguageSettings.APP_PROPERTIES_FILE_NAME;
import djf.ui.AppDialogs;
import djf.ui.AppGUI;
import djf.ui.FileController;
import djf.ui.WelcomeDialogSingleton;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import jtps.jTPS;
import metromapmaker.data.m3Data;
import metromapmaker.file.m3Files1;
import metromapmaker.gui.m3Workspace;
import static metromapmaker.m3PropertyType.WELCOME_MESSAGE;
import properties_manager.PropertiesManager;

/**
 * This class serves as the application class for our goLogoLoApp program. 
 * Note that much of its behavior is inherited from AppTemplate, as defined in
 * the Desktop Java Framework. This app starts by loading all the app-specific
 * messages like icon files and tooltips and other settings, then the full 
 * User Interface is loaded using those settings. Note that this is a 
 * JavaFX application.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class metroMapMaker extends AppTemplate {
     /**
     * This hook method must initialize all three components in the
     * proper order ensuring proper dependencies are respected, meaning
     * all proper objects are already constructed when they are needed
     * for use, since some may need others for initialization.
     */
    @Override
    public void buildAppComponentsHook() {
        // CONSTRUCT ALL THREE COMPONENTS. NOTE THAT FOR THIS APP
        // THE WORKSPACE NEEDS THE DATA COMPONENT TO EXIST ALREADY
        // WHEN IT IS CONSTRUCTED, AND THE DATA COMPONENT NEEDS THE
        // FILE COMPONENT SO WE MUST BE CAREFUL OF THE ORDER
        fileComponent = new m3Files1();
        dataComponent = new m3Data(this);
        workspaceComponent = new m3Workspace(this);
     //  clipboardComponent = new golClipboard(this);
    }

    @Override
    public void start(Stage primaryStage) {
        // FIRST SETUP THE PropertiesManager WITH
        // IT'S MINIMAL LANGUAGE PROPERTIES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        props.addProperty(APP_ERROR_TITLE, "Application Error");
        props.addProperty(APP_ERROR_CONTENT, "An Error Occured in the Application");
        props.addProperty(PROPERTIES_ERROR_TITLE, "Properties File Error");
        props.addProperty(PROPERTIES_ERROR_CONTENT, "There was an Error Loading a Properties File");        
        
	try {

            // FIRST LOAD THE APP PROPERTIES, WHICH CONTAINS THE UI SETTINGS
            // THAT ARE NOT LANGUAGE-SPECIFIC.
	    boolean success = loadProperties(APP_PROPERTIES_FILE_NAME);
	    
	    if (success) {               
                // THIS IS THE TRANSACTION PROCESSING SYSTEM THAT WE'LL BE USING
                tps = new jTPS();
                
                // INIT THE LANGUAGE SETTINGS
                languageSettings = new AppLanguageSettings(this);
                languageSettings.init();

                // GET THE TITLE FROM THE XML FILE	
		String appTitle = props.getProperty(APP_TITLE);

                // BUILD THE BASIC APP GUI OBJECT FIRST
		gui = new AppGUI(primaryStage, appTitle, this);

                // THIS BUILDS ALL OF THE COMPONENTS, NOTE THAT
                // IT WOULD BE DEFINED IN AN APPLICATION-SPECIFIC
                // CHILD CLASS
		buildAppComponentsHook();                
                
                // SETUP THE CLIPBOARD, IF IT'S BEING USED
                gui.registerClipboardComponent();
                 
                // LOAD ALL THE PROPER TEXT INTO OUR CONTROLS
                languageSettings.resetLanguage();
                WelcomeDialogSingleton welcomeDialog = WelcomeDialogSingleton.getSingleton();
                welcomeDialog.init(primaryStage);
                welcomeDialog.show(props.getProperty(APP_TITLE),props.getProperty(WELCOME_MESSAGE));
                
                primaryStage.show();
                
	    } 
	}catch (Exception e) {
            // THIS TYPE OF ERROR IS LIKELY DUE TO PROGRAMMER ERROR IN
            // THE APP ITSELF SO WE'LL PROVIDE A STACK TRACE DIALOG AND EXIT
            AppDialogs.showStackTraceDialog(gui.getWindow(), e, APP_ERROR_TITLE, APP_ERROR_CONTENT);
            System.exit(0);
	}
    }
    
    /**
     * This is where program execution begins. Since this is a JavaFX app it
     * will simply call launch, which gets JavaFX rolling, resulting in sending
     * the properly initialized Stage (i.e. window) to the start method inherited
     * from AppTemplate, defined in the Desktop Java Framework.
     */
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
}