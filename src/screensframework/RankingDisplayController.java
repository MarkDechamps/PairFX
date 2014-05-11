/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Pairing;
import model.Screens;

/**
 *
 * @author Mark Dechamps
 */
public class RankingDisplayController extends AbstractController {

    @FXML
    private ListView<Pairing> paringen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
    public void hoofdscherm() {
        controller().setScreen(Screens.startScreen);
    }
}
