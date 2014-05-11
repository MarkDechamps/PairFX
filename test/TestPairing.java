
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Pairing;
import model.PairingImpl;
import model.Player;
import model.Tournament;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import model.Model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mim
 */
public class TestPairing {

    private Player pieter;
    private Player mark;
    private Player nele;
    private Player jp;
    private Player herman;
    private Player roland;
    private Player didier;
    private Player yenka;
    private Player marcel;
    private List<Player> players;

    private void print(List<Pairing> nextRound) {
        for (Pairing p : nextRound) {
            System.out.println(p.toString());
        }
    }

    enum Result {

        WHITE_WINS, BLACK_WINS, DRAW
    };

    @Before
    public void init() {
        pieter = Player.createPlayerWithFirstnameLastname("Pieter", "Truwant");
        mark = Player.createPlayerWithFirstnameLastname("Mark", "Dechamps");
        nele = Player.createPlayerWithFirstnameLastname("Nele", "Vanhuyse");
        jp = Player.createPlayerWithFirstnameLastname("JP", "Blondel");
        herman = Player.createPlayerWithFirstnameLastname("Herman", "Ottevaere");
        roland = Player.createPlayerWithFirstnameLastname("Roland", "Seynaeve");
        didier = Player.createPlayerWithFirstnameLastname("Didier", "Dalschaert");
        yenka = Player.createPlayerWithFirstnameLastname("Yenka", "Vanderkerckhove");
        marcel = Player.createPlayerWithFirstnameLastname("Marcel", "Demaertelaere");
        players = Arrays.asList(new Player[]{pieter, mark, nele, jp, herman, roland, didier, yenka, marcel});
        Model model =Model.getInstance();
        model.init();
        model.setPairingStrategyByPercentage();
    }

    @Test
    public void testPairing() {
        
        Tournament t = new Tournament();
        List<Pairing> pairings = maakParingen();
        for (Player p : players) {
            t.addPlayer(p);
        }
        t.initializeWith(pairings);

        t.selectPlayersForPairing(players);
        List<Pairing> nextRound = t.pairWithSelectedPlayers();
        print(nextRound);
        for (Pairing p : nextRound) {
            org.junit.Assert.assertFalse(p.existsOfPlayers(herman, roland));
        }
    }

    private List<Pairing> maakParingen() {

        List<Pairing> result = new ArrayList<>();
        addTo(result, mark, pieter, Result.WHITE_WINS);
        addTo(result, nele, didier, Result.WHITE_WINS);
        addTo(result, herman, roland, Result.DRAW);
        addTo(result, yenka, jp, Result.WHITE_WINS);
        return result;
    }

    private void addTo(List<Pairing> result, Player white, Player black, Result r) {
        Pairing p = PairingImpl.createPairing(white, black);
        switch (r) {
            case BLACK_WINS:
                p.blackWins();
                break;
            case WHITE_WINS:
                p.whiteWins();
                break;
            case DRAW:
                p.draw();
                break;
            default:
                throw new IllegalStateException("unknown result");
        }
        result.add(p);

    }

    @Test
    public void testUndoPairingLeavesPointsAloneWhenGameNotPlayed() {
        Tournament t = new Tournament();
        mark.setPoints(3);
        assertTrue(mark.getPoints() == 3);

        t.addPlayer(pieter);
        t.addPlayer(mark);
        t.selectPlayerForPairing(pieter);
        t.selectPlayerForPairing(mark);
        List<Pairing> result = t.pairWithSelectedPlayers();

        assertTrue(result.size()==1);
        t.removePairing(result.get(0));
         assertTrue(mark.getPoints() == 3);
    }
    
    @Test
    public void testUndoPairingWithPointsAdapted() {
        Tournament t = new Tournament();
        mark.setPoints(3);
        assertTrue(mark.getPoints() == 3);

        t.addPlayer(pieter);
        t.addPlayer(mark);
        t.selectPlayerForPairing(pieter);
        t.selectPlayerForPairing(mark);
        List<Pairing> result = t.pairWithSelectedPlayers();
        mark.setPoints(0);
        assertTrue(mark.getPoints() == 0);
        assertTrue(result.size()==1);
        t.removePairing(result.get(0));
         assertTrue(mark.getPoints() == 0);
    }
    
    @Test
    public void testPairingWithMaxPercentage(){
        Model model =Model.getInstance();
                model.init();
        Tournament t = model.getTournament();
        assertTrue(mark.getPoints() == 0);
        t.addPlayer(pieter);
        t.addPlayer(mark);
        t.selectPlayerForPairing(pieter);
        t.selectPlayerForPairing(mark);
        List<Pairing> result = t.pairWithSelectedPlayers();
        assertTrue(result.size()==1);
        Pairing p = result.iterator().next();
        if(mark.equals(p.getWhite())) {
            p.whiteWins();
        }else {
            p.blackWins();
        }
        assertEquals(t.percentage(pieter),0);
        assertEquals(t.percentage(mark),100);
        
        t.addPlayer(jp);
        assertEquals(t.percentage(jp),0);
        
        t.selectPlayerForPairing(mark);
        t.selectPlayerForPairing(jp);
        
        result = t.pairWithSelectedPlayers();
        assertTrue(result.size()==1);//altijd geforceerde paring tussen 2 spelers toelaten
        
    }
}
