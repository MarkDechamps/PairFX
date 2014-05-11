/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework.cellfactory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.Player;
import model.Model;

/**
 *
 * @author mim
 */
public class PlayerCellFactory implements Callback<ListView<Player>, ListCell<Player>> {

    public PlayerCellFactory() {
    }

    @Override
    public ListCell<Player> call(ListView<Player> p) {
        ListCell<Player> result = new ListCell<Player>() {
           

            @Override
            protected void updateItem(Player player, boolean bln) {
                super.updateItem(player, bln);
                if (player != null) {
                    String name=player.getFirstname() + " " + player.getLastname();
                    if(Model.getInstance().isSelectedForPairing(player)){
                     setText("<< "+name+" >>");   
                     setTextFill(Color.valueOf("BLUE"));
                    }else{
                    setText(name);
                    setTextFill(Color.valueOf("BLACK"));
                    }
                }
            }
        };
        return result;
    }
}
