/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import importXLS.Import;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.I18n;
import model.Player;
import model.Model;
import model.Screens;
import screensframework.cellfactory.FileCellFactory;
import util.PFXUtil;

/**
 *
 * @author mim
 */
public class NieuwToernooiController extends AbstractController {

    @FXML
    private TextField naamToernooi;
    @FXML
    private ComboBox<File> toernooien;

    @FXML
    public void maakNieuwToernooiAan() {

        if (naamToernooi.getText().isEmpty()) {
            Stage stage = (Stage) naamToernooi.getScene().getWindow();
            Dialogs.showErrorDialog(stage, I18n.get(Messages.naamVoorToernooi));
        } else {
            final Model model = Model.getInstance();
            File gebruikSpelersUit = toernooien.getSelectionModel().getSelectedItem();
            List<Player> startSpelers = Model.parsePlayersFrom(gebruikSpelersUit);
            model.maakToernooi(naamToernooi.getText(), startSpelers);
            controller().setScreen(Screens.toernooiOverzicht);


        }
    }

    @FXML
    public void handleTerugAction() {
        controller().setScreen(Screens.startScreen);
    }

    @Override
    public void init() {
        toernooien.getSelectionModel().clearSelection();
        toernooien.getItems().clear();
        toernooien.setCellFactory(new FileCellFactory());

        toernooien.setButtonCell(new ListCell<File>() {
            @Override
            protected void updateItem(File t, boolean bln) {
                super.updateItem(t, bln);
                if (t == null) {
                    setText("");
                    toernooien.getSelectionModel().clearSelection();
                } else {
                    setText(t.getName());
                }

            }
        });

        List<File> toernooiFiles = new ArrayList<>();
        toernooiFiles.add(null);
        toernooiFiles.addAll(Model.getInstance().getToernooiBestanden());
        toernooien.getItems().addAll(toernooiFiles);
        toernooien.getSelectionModel().select(0);

    }

    @Override
    public boolean onLoad() {
        init();
        return super.onLoad();
    }

    @FXML
    public void importeerVanExcel() {
        if (naamToernooi.getText().isEmpty()) {
            Stage stage = (Stage) naamToernooi.getScene().getWindow();
            Dialogs.showErrorDialog(stage, I18n.get(Messages.naamVoorToernooi));
        } else {
            List<Player> result = new ArrayList<>();
            File importVan = PFX.chooseLoadFile(toernooien, Messages.importeerVanXLS);
            if (PFXUtil.isNull(importVan)) {
                return;
            }


            try {
                if (importVan.getName().endsWith("xlsx")) {
                    result = new Import().importFromXLSXFile(importVan);
                } else {
                    result = new Import().importFromXLSFile(importVan);

                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NieuwToernooiController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(NieuwToernooiController.class.getName()).log(Level.SEVERE, null, ex);
            }

            final Model model = Model.getInstance();

            model.maakToernooi(naamToernooi.getText(), result);
            controller().setScreen(Screens.toernooiOverzicht);
        }

    }
}