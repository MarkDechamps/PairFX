/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.util.List;
import model.Pairing;

/**
 *
 * @author mim
 */
interface DisplayListener {
    void PairingChanged(List<Pairing>pairings);
}
