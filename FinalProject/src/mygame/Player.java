/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.HullCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
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
        
        SphereCollisionShape playerShape = new SphereCollisionShape(0.02f);	
        playerPhy = new RigidBodyControl(playerShape, 0.01f);
        player.addControl(playerPhy);
    }
}
