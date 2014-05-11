package screensframework;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.I18n;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mark Dechamps
 */
public class PFX {

    public static final Logger log = Logger.getAnonymousLogger();

    public static void log(String logMe) {
        log.log(Level.INFO, logMe);
    }

   
    public static void showInfoDialog(String msg, Node node) {
        Stage stage = getStage(node);
        Dialogs.showInformationDialog(stage, msg, "", "PairFX");
    }

    public static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    public static boolean showOkCancelDialog(String msg, Node node) {
        Stage stage = getStage(node);
        DialogResponse response = Dialogs.showWarningDialog(stage, msg, "", "PairFX", Dialogs.DialogOptions.OK_CANCEL);
        return response.equals(DialogResponse.OK);
    }

    public static File chooseSaveFile(Node node, String msgToTranslate) {
        File location;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18n.get(msgToTranslate));
        File export = new File("export");
        if (!export.exists()) {
            export.mkdir();
        }
        fileChooser.setInitialDirectory(export);
        location = fileChooser.showSaveDialog(getStage(node));
        return location;
    }

    public static File chooseLoadFile(Node node, String msgToTranslate) {
        File location;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18n.get(msgToTranslate));
        location = fileChooser.showOpenDialog(getStage(node));
        return location;
    }

  

    public static boolean openBrowserOn(String path) {

        try {
            if (path.startsWith("http:")) {
                Desktop.getDesktop().browse(new URI(path));
                return true;
            }
            Desktop.getDesktop().open(new File(path));
            return true;
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ExportController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
