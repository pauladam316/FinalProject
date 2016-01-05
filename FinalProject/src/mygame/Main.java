package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private BulletAppState bulletAppState;
    private int deaths = 0;
    private boolean doOnce = true;
    
    BitmapText hudText;  
    Player mainPlayer = new Player();
    GameBoard mainBoard = new GameBoard();
    GUI boardGUI = new GUI();
    AudioNode background;
    static String playerPath = "Models/marble.j3o";
    
    public boolean isReady = false;
    
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
        //create GUI
        boardGUI.target.setImage(assetManager, "Textures/Target.png", true);
        boardGUI.target.setWidth(30);
        boardGUI.target.setHeight(30);
        boardGUI.target.setPosition(settings.getWidth()/2 - 15, settings.getHeight()/2 - 15);
        guiNode.attachChild(boardGUI.target);
        
        hudText = new BitmapText(guiFont, false);    
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.White);                             // font color
        hudText.setLocalTranslation(0, this.settings.getHeight()-5, 0); // position
        guiNode.attachChild(hudText);

        //init materials
        mainBoard.mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        Texture tex = assetManager.loadTexture("Textures/Wood.jpg");
        tex.setWrap(Texture.WrapMode.Repeat); //This should set the texture to repeat.
        mainBoard.mat.setTexture("ColorMap", tex); // with Unshaded.j3md
        
        mainBoard.goalMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        
        //Setup the models
        mainBoard.setBoard();
        mainPlayer.setPlayer();
        mainPlayer.playerPhy.setPhysicsLocation(new Vector3f(mainBoard.block[0].geom.getLocalTranslation().x,.3f,mainBoard.block[0].geom.getLocalTranslation().z));
        
        //add models to root node
        rootNode.attachChild( mainBoard.board);
        rootNode.attachChild( mainPlayer.player);
        
        //setup model physics
        //PLAYER
        bulletAppState.getPhysicsSpace().add(mainPlayer.playerPhy);
        mainPlayer.playerPhy.setGravity(new Vector3f(0.0f, -1f, 0.0f)); 
        
        //BOARD
        bulletAppState.getPhysicsSpace().add(mainBoard.boardPhy);
        
        //set physics accuracy
        bulletAppState.getPhysicsSpace().setAccuracy(1f/80f);
        
        //add skybox
        rootNode.attachChild(SkyFactory.createSky(
            assetManager, "Textures/Skybox.dds", false));
        //initiate the camera position
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f( 0.0f, 3.0f, 0.0f ));
        Quaternion q = new Quaternion();
        cam.setRotation(q.fromAngles((float)Math.PI/2, 0f, 0f)); //radians, not degrees :(
        
    }
    private void initAudio() {
        if (doOnce == true) {
            background = new AudioNode(assetManager, "Sounds/Music Track.wav", true);
            background.setPositional(true);   
            background.setVolume(3);
            rootNode.attachChild(background);
            background.play();// play continuously!
        }
  }

    @Override
    public void simpleUpdate(float tpf) {
        Vector2f mousePos = inputManager.getCursorPosition();
        Vector2f screenSize = new Vector2f();
        screenSize.x = this.settings.getWidth();
        screenSize.y = this.settings.getHeight();
        
        if (mousePos.x > screenSize.x/2 - 10 && mousePos.x < screenSize.x/2 + 10) {
            if (mousePos.y > screenSize.y/2 - 10 && mousePos.y < screenSize.y/2 + 10) {
                isReady = true;
                guiNode.detachChild(boardGUI.target);              
                initAudio();
                doOnce = false;
                
            }
        }
        if (isReady) {
           mainBoard.updateRotation(mousePos, screenSize);  
        }
        
        if (mainPlayer.isDead()) { //check if we are dead
            mainPlayer.playerPhy.setPhysicsLocation(new Vector3f(mainBoard.block[0].geom.getLocalTranslation().x,.3f,mainBoard.block[0].geom.getLocalTranslation().z));
            mainPlayer.playerPhy.setLinearVelocity(Vector3f.ZERO);//stop any movement
            mainPlayer.playerPhy.setAngularVelocity(Vector3f.ZERO);
            isReady = false;
            guiNode.attachChild(boardGUI.target);
            Quaternion q = new Quaternion();
            mainBoard.board.setLocalRotation(q.fromAngles(0,0,0));
            deaths ++;
            background.stop();//stop the music
            doOnce = true;
        }
        hudText.setText("Deaths: " + deaths);             // the text
        //System.out.println(ballSpeed());
        if (background != null) {
            background.setPitch(ballSpeed());
        }
    }
    public float ballSpeed() {
        float speed1 = mainPlayer.playerPhy.getAngularVelocity().z;
        float speed2 = mainPlayer.playerPhy.getAngularVelocity().x;
        float finalSpeed;

        if (speed1 < 1) { speed1 *= -1;}
        if (speed2 < 1) { speed2 *= -1;}
        
        if (speed1 > speed2) {
            finalSpeed = speed1;
        }
        else {
            finalSpeed = speed2;
        }
        
        if (finalSpeed < 0.3f) {
            return 0.95f;
        }
        else if (finalSpeed > 15f) {
            return 1.25f;
        }
        else {
            //return (((speed - OldMin) * (NewMax - NewMin)) / (OldMax - OldMin)) + NewMin;
            return (((finalSpeed - 0.3f) * (1.25f - 0.95f)) / (15f - 0.3f)) + 0.95f;
        }
   }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }   
}
