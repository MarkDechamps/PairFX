/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework.cellfactory;

import java.io.File;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import screensframework.Messages;

/**
 *
 * @author mim
 */
public class FileCellFactory implements Callback<ListView<File>, ListCell<File>> {

    // private ClickObserver observer;
    public FileCellFactory() {
        //observer = clickObserver;
    }

    @Override
    public ListCell<File> call(ListView<File> p) {
        return new ListCell<File>() {
            @Override
            protected void updateItem(File t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    setText(t.getName());
                }else{
                    setText("");
                }
            }
        };
    }
}
