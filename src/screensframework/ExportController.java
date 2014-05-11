/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import export.Export;
import export.ExportFlags;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialogs;
import javafx.stage.Stage;
import model.I18n;
import model.Screens;

/**
 *
 * @author Mark Dechamps
 */
public class ExportController implements Initializable, ControlledScreen {

    @FXML
    private CheckBox openFileBox;
    @FXML
    private CheckBox tabel;
    @FXML
    private CheckBox rankingByPercentage;
    @FXML
    private CheckBox rankingByPoints;
    @FXML
    private CheckBox activePairings;//the non finished pairings
    @FXML
    private CheckBox latestPairings;//the most recent click on the pairing button's result
    @FXML
    private CheckBox autoRefresh;
    @FXML
    private CheckBox allFinishedPairings;
    private ScreensController myController;
    @FXML
    private Button back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public boolean onLoad() {
        return true;
    }

    @Override
    public void onUnload() {
    }

    @FXML
    public void exportToHtml() throws FileNotFoundException {
        File location = PFX.chooseSaveFile(tabel,Messages.export_save_locatie);
        if (location == null) {
            //cancel pressed
            return;
        }
        ExportFlags flags = getExportFlags();
        String path = new Export().exportToHtml(location, flags);
        handleOpeningOfFile(path);
    }

    @FXML
    public void exportToText() throws FileNotFoundException {
        File location = PFX.chooseSaveFile(tabel,Messages.export_save_locatie);
        if (location == null) {
            //cancel pressed
            return;
        }
        ExportFlags flags = getExportFlags();
        String path = new Export().exportToTxt(location, flags);
        handleOpeningOfFile(path);
    }

    private Stage getStage() {
        Stage stage = (Stage) back.getScene().getWindow();
        return stage;
    }

    @FXML
    public void hoofdscherm() {
        myController.setScreen(Screens.toernooiOverzicht);
    }

   

   

    private void handleOpeningOfFile(String path) {
        if (openFileBox.isSelected()) {
            PFX.openBrowserOn(path);
        } else {
            Dialogs.showConfirmDialog(getStage(), I18n.get(Messages.export) + " " + path, "", I18n.get(Messages.exportTitle), Dialogs.DialogOptions.OK);
        }
    }

    private ExportFlags getExportFlags() {
        ExportFlags flags = new ExportFlags();
        flags.includeTable = tabel.isSelected();
        flags.includeRankingByPercentage = rankingByPercentage.isSelected();
        flags.includeRankingByPoints = rankingByPoints.isSelected();
        flags.includeActivePairings = activePairings.isSelected();
        flags.includeFinishedPairings = allFinishedPairings.isSelected();
        flags.latestPairings = latestPairings.isSelected();
        flags.autoRefresh = autoRefresh.isSelected();
        return flags;
    }
}
