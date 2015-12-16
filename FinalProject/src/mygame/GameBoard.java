/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.GImpactCollisionShape;
import com.jme3.bullet.collision.shapes.HullCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Adam
 */
public class GameBoard{
    Node board = new Node();
    RigidBodyControl boardPhy;
    public Material mat;
    
    /**sets the location of the board.
    */
    public void setBoard() {
        Block block = new Block();
        block.createBlock(Vector3f.ZERO, new Vector3f(1,1,1), mat);
        board.attachChild(block.geom); 
        BoxCollisionShape boardShape = new BoxCollisionShape( new Vector3f(1,1,1));
        boardPhy = new RigidBodyControl(boardShape, 0);
        boardPhy.setKinematic(true);
        //boardPhy.setCcdMotionThreshold(0.001f);
        //boardPhy.setCcdSweptSphereRadius(1);
        board.addControl(boardPhy);
    }
    public void updateRotation(Vector2f mousePos, Vector2f screenSize) {
        Vector2f rotAngle = new Vector2f();   
        
        float [] [] oldRange = new float [2] [2]; //range of numbers in screenPos
        oldRange[0][0] = 1; //range of numbers from 1 to middle of screen in pixels
        oldRange[0][1] = screenSize.x/2;

        oldRange[1][0] = 1; //range of numbers from 1 to middle of screen in pixels
        oldRange[1][1] = screenSize.y/2;
        
        float [] [] newRange = new float [2] [2]; //range of numbers in angles
        newRange[0][0] = 0;
        newRange[0][1] = 45; //max rotation angle of the board.
        
        newRange[1][0] = 0;
        newRange[1][1] = 45; //max rotation angle of the board.
        
        rotAngle.x = (((mousePos.x - oldRange[0][0]) * (newRange[0][0] - newRange[0][1])) / (oldRange[0][1] - oldRange[0][0])) + newRange[0][1];   
        rotAngle.x = (float) Math.toRadians(rotAngle.x);
        
        rotAngle.y = (((mousePos.y - oldRange[1][0]) * (newRange[1][0] - newRange[1][1])) / (oldRange[1][1] - oldRange[1][0])) + newRange[1][1];   
        rotAngle.y = (float) Math.toRadians(rotAngle.y);
        
        Quaternion q = new Quaternion();
        board.setLocalRotation( q.fromAngles((float)Math.PI/2 + -rotAngle.y , 0f, -rotAngle.x));
        
    }
    
}
