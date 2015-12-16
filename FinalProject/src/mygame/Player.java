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
        player.setLocalTranslation( new Vector3f( 0.1f, 1f, -0.45f ) );
        
        SphereCollisionShape playerShape = new SphereCollisionShape(0.03f);	
        playerPhy = new RigidBodyControl(playerShape, .1f);
        //playerPhy.setAngularDamping(1);
        //playerPhy.setCcdMotionThreshold(0.02f);
        //playerPhy.setCcdSweptSphereRadius(.1f);
        player.addControl(playerPhy);
    }
}
