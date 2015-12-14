/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author Adam
 */
public class GameBoard{
    Spatial board;
    
    /**sets the location of the board.
    */
    public void setBoard() {
        board.setLocalTranslation( new Vector3f( 0.0f, 0.0f, 0.0f ) );
    }
}
