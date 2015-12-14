/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author Adam
 */
public class Player {
    Spatial player;
    
    public void setPlayer() {
        player.setLocalTranslation( new Vector3f( 0.0f, 0.0f, 0.0f ) );
    }
}
