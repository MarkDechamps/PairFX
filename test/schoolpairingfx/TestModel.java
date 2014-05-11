package schoolpairingfx;

import model.Model;
import java.util.List;
import model.Pairing;
import model.Player;
import model.Tournament;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mark Dechamps
 */
public class TestModel {

    private Player mark;
    private Player pieter;
    private Player norah;
    private Player bart;
    private Player jan;
    private Model model;

    @Before
    public void init() {
        pieter = Player.createPlayerWithFirstnameLastname("Pieter", "Truwant");
        mark = Player.createPlayerWithFirstnameLastname("Mark", "Dechamps");
        norah = Player.createPlayerWithFirstnameLastname("Norah", "Dechamps");
        bart = Player.createPlayerWithFirstnameLastname("Bart", "Marescaux");
        jan = Player.createPlayerWithFirstnameLastname("Jan", "Claesen");

        model = Model.getInstance();
        model.init();
        model.setPairingStrategyByPoints();
    }

   
    @Test
    public void testUndoUnplayedPairingDoesNotModifyPoints() {


        model.addPlayer(mark);
        model.addPlayer(pieter);
        mark.setPoints(3);
        assertTrue(mark.getPoints() == 3);

        model.selectPlayerForPairing(mark);
        model.selectPlayerForPairing(pieter);
        List<Pairing> result = model.pairSelectedPlayers();
        assertTrue(result.size() == 1);
        assertTrue(result.get(0).existsOfPlayers(mark, pieter));
        assertTrue(mark.getPoints() == 3);
        model.removePairing(result.get(0));
        assertTrue(mark.getPoints() == 3);
    }

    @Test
    public void testUndoUnplayedPairingDoesNotModifyPointsWhenPointsAreUpdated() {


        model.addPlayer(mark);
        model.addPlayer(pieter);
        mark.setPoints(3);
        assertTrue(mark.getPoints() == 3);

        model.selectPlayerForPairing(mark);
        model.selectPlayerForPairing(pieter);
        List<Pairing> result = model.pairSelectedPlayers();
        assertTrue(result.size() == 1);
        assertTrue(result.get(0).existsOfPlayers(mark, pieter));
        mark.setPoints(0);
        assertTrue(mark.getPoints() == 0);
        model.removePairing(result.get(0));
        assertTrue(mark.getPoints() == 0);
    }

    @Test
    public void testRankingString() {
        String name = "Mark Dechamps";
        int percentage = 5;
        String points = "5.5";
        String rounds = "10";
        String text = String.format("[%2d%%] %20s (%3s/%-3s)", percentage, name, points, rounds);
        assertEquals("[ 5%]        Mark Dechamps (5.5/10 )", text);
    }

    @Test
    public void testName() {
        String name = String.format("%10s %20s", "v", "naam");
        System.out.println("name:" + name);
        assertTrue(name.length() == 31);
    }

    @Test
    public void testSortByPointsAndByPercentageWithMarkAndPieter() {
        model.addPlayer(mark);
        model.addPlayer(pieter);
        model.addPlayer(norah);
        model.addPlayer(bart);
        model.addPlayer(jan);

        //geef pieter 1 win, 100% 
        model.selectPlayerForPairing(norah);
        model.selectPlayerForPairing(pieter);
        laatSpelerWinnen(pieter);

        //maar geef mark 2 wins en 1 verlies. 
        //Zo zijn marks punten hoger maar percentage lager, dat gaan we aftesten.
        model.selectPlayerForPairing(norah);
        model.selectPlayerForPairing(mark);
        laatSpelerWinnen(mark);

        model.selectPlayerForPairing(bart);
        model.selectPlayerForPairing(mark);
        laatSpelerWinnen(bart);

        //bart zijn 100% afnemen
        model.selectPlayerForPairing(norah);
        model.selectPlayerForPairing(bart);
        laatSpelerWinnen(norah);
        
        model.selectPlayerForPairing(jan);
        model.selectPlayerForPairing(mark);
        laatSpelerWinnen(mark);

        print(model.getRankingByPercentage());
        print(model.getRankingByPoints());

        assertTrue(model.getRankingByPercentage().get(0).equals(pieter));
        assertTrue(model.getRankingByPoints().get(0).equals(mark));
    }

    private void laatSpelerWinnen(Player speler) {
        final Pairing pairing = model.pairSelectedPlayers().iterator().next();
        if (speler.equals(pairing.getBlack())) {
            pairing.blackWins();
        } else {
            pairing.whiteWins();
        }
    }

    private void print(List<Player> printme) {
        System.out.println("--begin--");
        for (Player p : printme) {
            System.out.println("player:" + p.getFirstname() + " " + p.getLastname() + " " + p.getPoints());
        }
        System.out.println("--end--");
    }
}
