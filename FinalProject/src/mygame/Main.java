package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    Player mainPlayer = new Player();
    GameBoard mainBoard = new GameBoard();
    String boardPath = "Models/board.scene"; //this is a change
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        try{
            mainBoard.board = assetManager.loadModel(boardPath);
            rootNode.attachChild( mainBoard.board);
        }
        catch (com.jme3.asset.AssetNotFoundException e){
            System.out.println("The model: " + boardPath + " could not be found");
        } 
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
