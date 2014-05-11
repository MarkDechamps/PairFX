/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schoolpairingfx;

import model.Screens;
import model.Model;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import screensframework.ScreensController;


/**
 *
 * @author mim
 */
public class SchoolPairingFX extends Application {
     
   
    @Override
    public void start(Stage primaryStage) {
        Model.getInstance().init();        
                
        primaryStage.setTitle(Model.getApplicationTitle());
        primaryStage.setResizable(false);
        ScreensController mainContainer = new ScreensController();
       
        String locale =System.getProperty("locale","nl");
         Locale.setDefault(new Locale(locale));
        ResourceBundle rb= ResourceBundle.getBundle("i18n.application");
        Model.getInstance().setResourceBundle(rb);
        mainContainer.loadScreen(Screens.startScreen, Screens.screen1File,rb);
        mainContainer.setScreen(Screens.startScreen);
        mainContainer.loadScreen(Screens.nieuwToernooi, Screens.nieuwToernooiFile,rb);
        mainContainer.loadScreen(Screens.toernooiOverzicht, Screens.toernooiOverzichtFile,rb);
        mainContainer.loadScreen(Screens.export, Screens.exportFile,rb);
        mainContainer.loadScreen(Screens.rankingDisplay, Screens.rankingDisplayFile,rb);
        mainContainer.loadScreen(Screens.administrator, Screens.administratorFile,rb);
        
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
       
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        Model.getInstance().stopWebServer();
               
        super.stop();
    }

    
    
}

