/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import importXLS.TestImportXLS;
import model.TestTournament;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import schoolpairingfx.TestModel;

/**
 *
 * @author mim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestFormat.class, TestPairing.class,TestModel.class,TestTournament.class,TestImportXLS.class})
public class AllTests {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
