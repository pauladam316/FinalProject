/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;

/**
 *
 * @author Adam
 */
public class GameBoard extends SimpleApplication{
    Material mat;
    Spatial board;
    
    public void setMaterial(String fileLocation) {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setTexture("ColorMap", assetManager.loadTexture(fileLocation)); // with Unshaded.j3md
    }

    public Spatial makeBoard(String modelLocation) {

        
        setMaterial("test");
        board.setMaterial(mat);
        return board;
    }

    @Override
    public void simpleInitApp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
