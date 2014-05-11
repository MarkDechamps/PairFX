/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import model.Model;
import model.Screens;
import util.PFXUtil;

/**
 *
 * @author Mark Dechamps
 */
public class AdministratorController extends AbstractController {

    @FXML
    private TextField maxDiffInPercentage;
    @FXML
    private TextField nrRoundsBetween;
    @FXML
    private TextField maxDiffInPoints;
    @FXML
    private RadioButton pairingBypercentage;
    @FXML
    private Label serverStatus;
    @FXML
    private Hyperlink url;
    @FXML
    private CheckBox fading;
    private int maxDiffInPointsToSave;
    private int maxDiffInPercentageToSave;
    private int nrRoundsToSave;

    @FXML
    public void fadingChanged() {
        if (fading.isSelected()) {
            controller().enableFading();
        } else {
            controller().disableFading();
        }

    }

    @FXML
    public void maxPercentageChanged() {
        String txt = maxDiffInPercentage.getText();
        if (PFXUtil.notEmpty(txt)
                && PFXUtil.isInt(txt)) {
            maxDiffInPercentageToSave = Integer.parseInt(txt);
        }
    }

    @FXML
    public void nrRoundsChanged() {
        String txt = nrRoundsBetween.getText();
        if (PFXUtil.notEmpty(txt)
                && PFXUtil.isInt(txt)) {
            nrRoundsToSave = Integer.parseInt(txt);
        }
    }

    @FXML
    public void maxDiffInPointsChanged() {
        String txt = maxDiffInPoints.getText();
        if (PFXUtil.notEmpty(txt)
                && PFXUtil.isDouble(txt)) {
            Double d = Double.parseDouble(txt);
            d *= 10;
            d -= (d % 5);
            maxDiffInPointsToSave = d.intValue();
            maxDiffInPoints.setText("" + (double) maxDiffInPointsToSave / 10);
        }
    }

    @FXML
    public void save() {
        nrRoundsChanged();
        maxDiffInPointsChanged();
        maxPercentageChanged();
        Model m = Model.getInstance();
        m.setMaxDiffInPercentage(maxDiffInPercentageToSave);
        m.setMaxDifferenceInPoints(maxDiffInPointsToSave);
        m.setNrRoundsPlayersMayNotHavePlayed(nrRoundsToSave);
        m.bewaarToernooi();
    }

    @Override
    public boolean onLoad() {

        if (Model.getInstance().noTournamentChosen()) {
            return false;
        }
        Model model = Model.getInstance();
        maxDiffInPercentageToSave = model.maxDiffInPercentage();
        maxDiffInPercentage.setText("" + maxDiffInPercentageToSave);
        nrRoundsToSave = model.nrRoundsPlayerMayNotHavePlayed();
        nrRoundsBetween.setText("" + nrRoundsToSave);
        maxDiffInPointsToSave = model.maxDiffInPoints();
        maxDiffInPoints.setText("" + (double) maxDiffInPointsToSave / 10);

        pairingBypercentage.setSelected(Model.getInstance().usesPercentageStrategy());
        
        fading.setSelected(controller().isFadingEnabled());
        setServerStatus();
        return true;
    }

    @FXML
    public void handleTerugAction() {
        controller().setScreen(Screens.startScreen);
    }

    @FXML
    public void log() {
        PFX.log("KLIK!");
    }

    @FXML
    public void rankingByPercentage(ActionEvent event) {
        Model model = Model.getInstance();
        if (pairingBypercentage.isSelected()) {
            model.setPairingStrategyByPercentage();
        } else {
            model.setPairingStrategyByPoints();
        }

        save();
    }

    @FXML
    public void naarHetToernooi() {
        controller().setScreen(Screens.toernooiOverzicht);
    }

    @FXML
    public void startServer() {
        Model.getInstance().startWebServer();
        setServerStatus();
    }

    @FXML
    public void stopServer() {
        Model.getInstance().stopWebServer();
        setServerStatus();
    }

    private void setServerStatus() {
        serverStatus.setText("Server on:" + Model.getInstance().isOnline());
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            url.setText("http://" + ip + ":8080/export.html");
        } catch (UnknownHostException ex) {
            url.setText("http://127.0.0.1:8080/export.html");
            Logger.getLogger(AdministratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void openBrowser() {
        if (url.getText().startsWith("http:")) {
            PFX.openBrowserOn(url.getText());
        }
    }
}
