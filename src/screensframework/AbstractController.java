/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package screensframework;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author Mark Dechamps
 */
abstract class AbstractController implements Initializable, ControlledScreen{

    private   ScreensController myController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    }

    public void init(){
        
    }
    
    @Override
    public void setScreenParent(ScreensController screenController) {
       this.myController=screenController;
    }

    protected  ScreensController controller(){
        return myController;
                
    }
    
    @Override
    public boolean onLoad() {
        return true;
    }

    @Override
    public void onUnload() {
        
    }

}
