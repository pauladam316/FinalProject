/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author Adam
 */
public class Player {
    Spatial player;
    RigidBodyControl playerPhy;
   
    /**sets the location of the player.
    */
    public void setPlayer() {
        
        SphereCollisionShape playerShape = new SphereCollisionShape(0.03f);	
        playerPhy = new RigidBodyControl(playerShape, 5f);
        
        player.addControl(playerPhy);
    }
    public boolean isDead () {
        if (playerPhy.getPhysicsLocation().y < -1) {
            return true;
        }
        return false;
    }
}
