/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework.cellfactory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.Pairing;
import model.Player;
import model.Model;
import util.PlayerUtil;

/**
 *
 * @author Mark
 */
public class PairingCellFactory implements Callback<ListView<Pairing>, ListCell<Pairing>> {

  
            @Override
            public ListCell<Pairing> call(ListView<Pairing> p) {
                ListCell<Pairing> result = new ListCell<Pairing>() {
                   

                    @Override
                    protected void updateItem(Pairing pairing, boolean bln) {
                        super.updateItem(pairing, bln);

                        if (pairing != null) {
                            String txt = PlayerUtil.buildPairingStringFor(pairing);
                            if(pairing.isPlayed()){
                                setTextFill(Color.GREEN);
                            }
                            else{                                                               
                                if(Model.getInstance().isNewPairing(pairing)){
                                    setTextFill(Color.BLUE);
                                }else{
                                    setTextFill(Color.BLACK);
                                }
                            }
                            setText(txt.toString());
                        }
                    }

                    
                    
                    
                };
                return result;
            }
        
    
}
