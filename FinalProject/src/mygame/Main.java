package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private BulletAppState bulletAppState;
    
    Player mainPlayer = new Player();
    GameBoard mainBoard = new GameBoard();
    
    static String playerPath = "Models/marble.j3o";
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //init physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        try{ // init models
            mainPlayer.player = assetManager.loadModel(playerPath);
        }
        catch (com.jme3.asset.AssetNotFoundException e){
            //System.out.println("The model: " + boardPath + " could not be found");
        }
        //init materials
        mainBoard.mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        
        //Setup the models
        mainBoard.setBoard();
        mainPlayer.setPlayer();
        
        //add models to root node
        rootNode.attachChild( mainBoard.board);
        rootNode.attachChild( mainPlayer.player);
        
        //setup model physics
        //PLAYER
        bulletAppState.getPhysicsSpace().add(mainPlayer.playerPhy);
        mainPlayer.playerPhy.setGravity(new Vector3f(0.0f, -1f, 0.0f)); 
        //BOARD
        bulletAppState.getPhysicsSpace().add(mainBoard.boardPhy);
        
        //initiate the camera position
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f( 0.0f, 6.0f, 0.0f ));
        Quaternion q = new Quaternion();
        cam.setRotation(q.fromAngles((float)Math.PI/2, 0f, 0f)); //radians, not degrees :(
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        Vector2f mousePos = inputManager.getCursorPosition();
        Vector2f screenSize = new Vector2f();
        screenSize.x = this.settings.getWidth();
        screenSize.y = this.settings.getHeight();
        
        mainBoard.updateRotation(mousePos, screenSize);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }   
}
