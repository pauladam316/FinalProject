package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    Player mainPlayer = new Player();
    GameBoard mainBoard = new GameBoard();
    
    static String boardPath = "Models/board.scene";
    static String playerPath = "Models/marble.scene";
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        try{ // init models
            mainBoard.board = assetManager.loadModel(boardPath);
            mainPlayer.player = assetManager.loadModel(playerPath);
        }
        catch (com.jme3.asset.AssetNotFoundException e){
            //System.out.println("The model: " + boardPath + " could not be found");
        }
        //add models to root node
        rootNode.attachChild( mainBoard.board);
        rootNode.attachChild( mainPlayer.player);
        
        //Setup the models
        mainBoard.setBoard();
        mainPlayer.setPlayer();
        
        //initiate the camera position
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f( 0.0f, 2.0f, 0.0f ));
        Quaternion q = new Quaternion();
        cam.setRotation(q.fromAngles(1.5708f, 0f, 0f)); //radians, not degrees :(
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
