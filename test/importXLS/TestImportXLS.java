/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importXLS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import model.Player;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Mark Dechamps
 */
public class TestImportXLS {

    @Test
    public void testMaakSpelerUit() {

        assertPlayer("dechamps mark", "mark", "dechamps");
        assertPlayer("mark", "mark", "");
        assertPlayer("Van Den Broucke J", "J", "Van Den Broucke");

    }

    private void assertPlayer(String value, String voornaam, String naam) {
        Import ntc = new Import();
        Player p = new Player();
        ntc.zetNaamEnVoornaamOp(p, value);
        assertEquals(voornaam, p.getFirstname());
        assertEquals(naam, p.getLastname());
    }

    @Test
    public void testValidatePlayer_naam_is_leeg() {
        Import imp = new Import();
        Player validNaam = Player.createPlayerWithFirstnameLastname("voornaam", null);
        validNaam.setId(0);
        assertTrue(imp.isValidPlayer(validNaam));
    }

    @Test
    public void testValidatePlayer_voornaam_is_leeg() {
        Import imp = new Import();
        Player validNaam = Player.createPlayerWithFirstnameLastname(null, "naam");
        validNaam.setId(0);
        assertTrue(imp.isValidPlayer(validNaam));
    }

    @Test
    public void testValidatePlayer_id_is_leeg() {
        Import imp = new Import();
        Player validNaam = Player.createPlayerWithFirstnameLastname("voornaam", "naam");
        validNaam.setId(-1);
        assertTrue(imp.isValidPlayer(validNaam));
    }

    @Test
    public void testValidatePlayer_alles_is_leeg() {
        Import imp = new Import();
        Player validNaam = Player.createPlayerWithFirstnameLastname(null, null);
        validNaam.setId(-1);
        assertFalse(imp.isValidPlayer(validNaam));
    }

    @Test
    public void testValidatePlayer_naam_en_voornaam_is_leeg() {
        Import imp = new Import();
        Player validNaam = Player.createPlayerWithFirstnameLastname(null, null);
        validNaam.setId(0);
        assertFalse(imp.isValidPlayer(validNaam));
    }

    @Test
    public void parseXLS() throws FileNotFoundException, IOException {
        Import im = new Import();
        final File file = new File("test/testxls.xls");
        List<Player> result = im.importFromXLSFile(file);
        assertResult(result);
    }
    @Test
    public void parseXLSX() throws FileNotFoundException, IOException {
        Import im = new Import();
        final File file = new File("test/test.xlsx");
        List<Player> result = im.importFromXLSXFile(file);
        assertResult(result);
    }

    private void assertResult(List<Player> result) {
        assertTrue(result.size() == 2);        
        Player p1 = result.get(0);
        assertEquals(p1.getFirstname(), "Azra");
        assertEquals(p1.getLastname(), "Agdere");
        assertEquals(p1.getId(), 1);
        
        Player p2 = result.get(1);
        assertEquals(p2.getFirstname(), "Kiara");
        assertEquals(p2.getLastname(), "Bammens");
        assertEquals(p2.getId(), 2);
        
        
    }
}
