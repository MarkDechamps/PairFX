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
import util.PlayerUtil;

/**
 *
 * @author mim
 */
public class RankingCellFactory implements Callback<ListView<Player>, ListCell<Player>> {

    public RankingCellFactory() {
    }

    @Override
    public ListCell<Player> call(ListView<Player> p) {
        return new ListCell<Player>() {
            @Override
            protected void updateItem(Player player, boolean bln) {
                super.updateItem(player, bln);
                if (player != null) {
                    final Model model = Model.getInstance();
                    int nrGames = model.getAantalPartijen(player);
                    String pointsString = model.getPointString(player);
                    String name = String.format("%15s %-15s", player.getFirstname(), player.getLastname());
                    String text = String.format("[%3s/%-3s] %-31s", pointsString, "" + nrGames, name);
                    setText(text);
                    if (player.isAbsent()) {
                        setTextFill(Color.valueOf("GRAY"));
                    }else{
                        setTextFill(Color.valueOf("BLACK"));
                    }
                }
            }
        };
    }
}
