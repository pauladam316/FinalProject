/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.HullCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Adam
 */
public class GameBoard{
    Spatial board;
    RigidBodyControl boardPhy;
    
    /**sets the location of the board.
    */
    public void setBoard() {
        board.setLocalTranslation( new Vector3f( 0, 0, 0 ) );
        Quaternion q = new Quaternion();
        board.setLocalRotation( q.fromAngles(1.5708f, 0f, 0f) );
        
        MeshCollisionShape   boardShape = new MeshCollisionShape  (((Geometry)((Node)board).getChild("Wood")).getMesh());
        boardPhy = new RigidBodyControl(boardShape, 0);
        board.addControl(boardPhy);
    }
}
