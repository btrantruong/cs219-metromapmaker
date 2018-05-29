package djf.ui;

import static djf.AppPropertyType.*;
import djf.AppTemplate;
import djf.language.LanguageException;
import properties_manager.PropertiesManager;

public class SettingsController {
    private AppTemplate app;
    
    public SettingsController(AppTemplate initApp) {
        app = initApp;
    }
    
    public void processLanguageRequest() {
        try {
            app.getLanguageSettings().promptForLanguage();
            app.getLanguageSettings().resetLanguage();
        }
        catch(LanguageException le) {
            System.out.println("Error Loading Laguage into UI");
        }
    }    
    
    public void processHelpRequest() {
        // GET THE PATH OF THE HTML DOCUMENT THAT
        // CONTAINS THE APPLICATION HELP AND DISPLAY IT
        
    }
    
    public void processAboutRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppDialogs.showMessageDialog(app.getGUI().getWindow(), ABOUT_TITLE, ABOUT_CONTENT);
    }    
}
