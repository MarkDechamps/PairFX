/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.I18n;
import model.Model;
import model.Screens;
import screensframework.cellfactory.FileCellFactory;
import util.PFXUtil;

/**
 *
 * @author mim
 */
public class StartScreenController extends AbstractController {

    @FXML
    private ListView<File> files;
    @FXML
    private Button administrator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshFileList();
    }

    @FXML
    public void handleNieuwToernooiAction(ActionEvent event) {
        controller().setScreen(Screens.nieuwToernooi);
    }

    private List<File> getFiles() {
        return Model.getInstance().getToernooiBestanden();
    }

    public void clicked() {
        File file = files.getSelectionModel().getSelectedItem();
        if (file != null && file.isFile()) {
            Model.getInstance().laadToernooi(file);
            System.out.println("Geselecteerde file:" + file.getAbsolutePath());
            controller().setScreen(Screens.toernooiOverzicht);
        }
    }

    @Override
    public boolean onLoad() {
        refreshFileList();
        administrator.setDisable(Model.getInstance().noTournamentChosen());
        return true;
    }

    @FXML
    public void admin() {
        if (Model.getInstance().noTournamentChosen()) {
            PFX.showInfoDialog(I18n.get(Messages.chooseTournament), files);
        } else {
            controller().setScreen(Screens.administrator);
        }
    }

    public void refreshFileList() {
        final FileCellFactory fileCellFactory = new FileCellFactory();
        files.getItems().clear();
        files.setCellFactory(fileCellFactory);
        files.getItems().addAll(getFiles());
    }
}
