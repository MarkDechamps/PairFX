/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.List;
import javafx.collections.ObservableList;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import util.PlayerUtil;

/**
 *
 * @author Mark Dechamps
 */
public class TestTournament {

    @Before
    public void init() {
        Model.getInstance().init();
    }

    @Test
    public void testPercentageRound() {

        Player p = Player.createPlayerWithFirstnameLastname("", "");
        p.addWin();
        p.addWin();
        p.addWin();
        p.addDraw();
        assertEquals(p.getPoints(), 35);
        Tournament t = new Tournament();

        int percentage = t.calculatePercentage((double) p.getPoints() / 10, 7);
        assertEquals(percentage, 50);
    }

    @Test
    public void testPlayerWithNoGamesHas0Percent() {
        Player p = Player.createPlayerWithFirstnameLastname("", "");
        assertEquals(p.getPoints(), 0);
        int percentage = new Tournament().calculatePercentage((double) p.getPoints() / 10, 0);
        assertEquals(percentage, 0);
    }

    @Test
    public void testPercentageDouble() {

        Player p = Player.createPlayerWithFirstnameLastname("", "");
        p.addWin();
        p.addWin();
        p.addWin();
        p.addDraw();
        assertEquals(p.getPoints(), 35);
        double pointsDouble = (double) p.getPoints() / 10;
        assertEquals(pointsDouble, 3.5, 0);
        int percentage = new Tournament().calculatePercentage(pointsDouble, 10);
        assertEquals(percentage, 35);
    }

    @Test
    public void testNumberOfFinishedGame() {
        Player p = Player.createPlayerWithFirstnameLastname("p1", "");
        Player p2 = Player.createPlayerWithFirstnameLastname("p2", "");
        Tournament tournament = new Tournament();
        assertEquals(tournament.getNrFinishedGames(p), 0);
        tournament.addPlayer(p);
        tournament.addPlayer(p2);
        assertEquals(tournament.getNrFinishedGames(p), 0);
        tournament.selectAllAvailablePlayersForPairing();
        List<Pairing> pairings = tournament.pairWithSelectedPlayers();
        assertEquals(pairings.size(), 1);
        pairings.iterator().next().whiteWins();
        assertEquals(tournament.getNrFinishedGames(p), 1);
        assertEquals(tournament.getNrFinishedGames(p2), 1);
        assertEquals(0,tournament.percentage(p2));
        assertEquals(100,tournament.percentage(p));
    }
  
    @Test
    public void bigTournament(){
        File toernooi = new File("test/testcase");
        assertTrue(toernooi.exists());
        Model model = Model.getInstance();
        model.laadToernooi(toernooi);
        for(int i=0;i<100;i++){
            //Player player = model.getRanking().get(0);
            //System.out.println("Ronde 1:"+player.getFirstname()+" "+player.getLastname()+" "+player.getPoints()+" "+PlayerUtil.getPointsString(player));
            System.out.println("-------Ronde "+i+" --------------");            
            selectAll(model);
            model.pairSelectedPlayers();
            printNietSpelende(model.getNietSpelendeSpelers());
            
            
            witWint(model);
        }
    }

    private void selectAll(Model model) {
        for(Player p:model.getPlayers()){
            model.selectPlayerForPairing(p);
        }
    }

    private void witWint(Model model) {
        final List<Pairing> onafgewerktePartijen = model.getOnafgewerktePartijen();
        System.out.println("#Te spelen partijen:"+onafgewerktePartijen.size());
        for(Pairing p: onafgewerktePartijen){
            p.whiteWins();
            
            System.out.println(PlayerUtil.buildPairingStringFor(p));
            System.out.println("   W->"+maakSpelerString(p.getWhite()));
            System.out.println("   Z->"+maakSpelerString(p.getBlack()));
        }
    }

    private void printNietSpelende(List<Player> nietSpelendeSpelers) {
       for(Player p:nietSpelendeSpelers){
           String spelerString = maakSpelerString(p);
           System.out.println("Niet spelend:"+spelerString);
       }
    }

    private String maakSpelerString(Player p) {
        Model model = Model.getInstance();
       return PlayerUtil.toPairingString(p)+" "+model.percentage(p)+"%      "+PlayerUtil.getPointsString(p)+"/"+model.getAantalPartijen(p);
    }
}
