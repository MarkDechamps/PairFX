package export;

import java.util.ArrayList;
import java.util.List;
import model.Player;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import model.Model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mark Dechamps
 */
public class TestExport {

    private Model model;
    private Player mark;
    private Player norah;

    @Before
    public void init() {
        model = Model.getInstance();
        model.init();
        mark = Player.createPlayerWithFirstnameLastname("Mark", "Dechamps");
        norah = Player.createPlayerWithFirstnameLastname("Norah", "Dechamps");
    }

    @Test
    public void testExport() {
        Export export = new Export();
        List<Player> ranking = maakRanking();
        List< String> tableLines = export.exportRankingToHtml(ranking);

        for (String s : tableLines) {
            System.out.println("LOG:" + s);
        }
    }

    private List<Player> maakRanking() {
        List<Player> result = new ArrayList<>();

        mark.setPoints(99);
        norah.setPoints(100);
        result.add(mark);
        result.add(norah);


        model.addPlayer(mark);
        model.addPlayer(norah);
        model.selectPlayerForPairing(mark);
        model.selectPlayerForPairing(norah);
        model.pairSelectedPlayers().get(0).whiteWins();
        return result;
    }

    @Test
    public void testPoints() {
        Export ex = new Export();
        maakRanking();
        String result = ex.getPointsTxt(mark);
       assertEquals("  10/1  ",result);
    }
}
