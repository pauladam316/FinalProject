/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Adam
 */
public class Block {
    public Box box;
    public Geometry geom;  // create cube geometry from the shape
    
    public void createBlock(Vector3f position, Vector3f scale, Material mat) {
        Box b = new Box(scale.x,scale.y,scale.z); // create cube shape
        geom = new Geometry("Box", b);  // create cube geometry from the shape
        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
        geom.setMaterial(mat);                   // set the cube's material
    }
}
