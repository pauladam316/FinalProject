/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Adam
 */
public class Block {
    public Box box;
    public Geometry geom;  // create cube geometry from the shape
    public BoxCollisionShape collider;
    /**create a block in the board
     * 
     * @param scale the scale of the block 
     * @param mat material to use for the block
     */
    public void createBlock(Vector3f scale, Material mat) {
        Box b = new Box(scale.x,scale.y,scale.z); // create cube shape
        //b.scaleTextureCoordinates(new Vector2f(.0001f,.0001f)); //scale the texture NOT WORKING!!! PLS FIX
        geom = new Geometry("Box", b);  // create cube geometry from the shape   
        geom.setMaterial(mat); // set the cube's material
        collider = new BoxCollisionShape (new Vector3f(scale.x,scale.y,scale.z));
    }
}
