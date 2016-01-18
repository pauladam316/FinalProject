package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.Timer;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements PhysicsCollisionListener {
    
    private int deaths = 0;
    private int levelTime = 30;
    private float pointsPerLevel = 1000f;
    
    private boolean doOnce = true;
    
    Nifty nifty;
    GUIHandle gui = new GUIHandle();
    Timer pointsTimer;
    Timer winTimer;
    BitmapText hudText; 
    BitmapText pointText;
    BitmapText timerText;  
    Player mainPlayer = new Player();
    public static GameBoard mainBoard = new GameBoard();
    GUI boardGUI = new GUI();
    AudioNode background;
    AudioNode bounceSoundEft;
    AudioNode gameOver;
    AudioNode winSound;
    AudioNode timeOverEft;
    private BulletAppState bulletAppState;
    boolean isWin = false;
    boolean winTimerInit = false;
    boolean playEffect = true;
    
     Spatial effect;
    
    static String playerPath = "Models/marble.j3o";
    
    double points = 1000;
    
    public boolean isReady = false;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {    /** Explosion effect. Uses Texture from jme3-test-data library! */ 
       
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
        
        hudText = new BitmapText(guiFont, false);    
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.White);                             // font color
        hudText.setLocalTranslation(0, this.settings.getHeight()-5, 0); // position
        guiNode.attachChild(hudText);
        
        pointsTimer = getTimer();
        
        pointText = new BitmapText(guiFont, false);    
        pointText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        pointText.setColor(ColorRGBA.White);                             // font color
        pointText.setLocalTranslation(0, this.settings.getHeight()-30, 0); // position
        guiNode.attachChild(pointText);
        
        timerText = new BitmapText(guiFont, false);    
        timerText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        timerText.setColor(ColorRGBA.White);                             // font color
        timerText.setLocalTranslation(0, this.settings.getHeight()-50, 0); // position
        guiNode.attachChild(timerText);

        //init materials
        mainBoard.mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        Texture tex = assetManager.loadTexture("Textures/Wood.jpg");
        tex.setWrap(Texture.WrapMode.Repeat); //This should set the texture to repeat.
        mainBoard.mat.setTexture("ColorMap", tex); // with Unshaded.j3md
        
        mainBoard.goalMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mainBoard.goalMat.setColor("Color", ColorRGBA.Blue);
        
        //Setup the models
        mainBoard.setBoard();
        mainPlayer.setPlayer();
        
        mainPlayer.playerPhy.setPhysicsLocation(new Vector3f(mainBoard.block[0].geom.getLocalTranslation().x,.6f,mainBoard.block[0].geom.getLocalTranslation().z));
        
        //trigger.geom.setLocalTranslation(trigger.triggerPhy.getPhysicsLocation());
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
        bulletAppState.getPhysicsSpace().setAccuracy(1f/100f);
        
        //add skybox
        rootNode.attachChild(SkyFactory.createSky(
            assetManager, "Textures/Skybox.dds", false));
        //initiate the camera position
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f( 0.0f, 3.0f, 0.0f ));
        Quaternion q = new Quaternion();
        cam.setRotation(q.fromAngles((float)Math.PI/2, 0f, 0f)); //radians, not degrees :(
        
        //CREATE MAIN MENU
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
        assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        //flyCam.setDragToRotate(true);

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        gui.getNifty(nifty);
        // <screen>
        nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen"){{
            controller(gui); // Screen properties       
            
             layer(new LayerBuilder("PIC_LAYER") {{
                childLayoutVertical(); // layer properties, add more...
                image(new ImageBuilder() {{
                    filename("Textures/Black.png");
                }});
                
             }});
             layer(new LayerBuilder("PIC_LAYER2") {{
                childLayoutVertical(); // layer properties, add more...
                image(new ImageBuilder() {{
                    filename("Textures/LOGO.png");
                    valignCenter();
                    alignCenter();
                    height("95%");
                    width("95%");
                }});
             }});
            // <layer>
            layer(new LayerBuilder("Layer_ID") {{
                childLayoutVertical(); // layer properties, add more...

                // <panel>
                panel(new PanelBuilder("Panel_ID") {{
                   childLayoutCenter(); // panel properties, add more...               
                   valignCenter();
                    // GUI elements
                                         
                    control(new ButtonBuilder("StartButton", "Play"){{
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("15%");
                        visibleToMouse(true);
                        interactOnClick("startGame()");
                    }});


                    //.. add more GUI elements here              
                }});

                 panel(new PanelBuilder("Panel2") {{
                   childLayoutCenter(); // panel properties, add more...               
                    valignCenter();
                    // GUI elements
                    control(new ButtonBuilder("LoadButton", "Load"){{
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("15%");
                        interactOnClick("load()");
                    }});

                    //.. add more GUI elements here              
                }});
                // </panel>

              }});
            // </layer>
          }}.build(nifty));
        // </screen>

       nifty.gotoScreen("Screen_ID"); // start the screen
        //StartScreenState startScreenState = new StartScreenState(this);
        //stateManager.attach(startScreenState); 
       
    }
    private void initAudio() {
        if (doOnce == true) {
            background = new AudioNode(assetManager, "Sounds/Music Track.wav", true);
            background.setPositional(true);   
            background.setVolume(3);
            rootNode.attachChild(background);
            background.play();// play continuously!
            bounceSoundEft = new AudioNode(assetManager, "Sounds/Bouncing Ball Sound.wav", true);
            bounceSoundEft.setPositional(true);   
            bounceSoundEft.setVolume(3);
            rootNode.attachChild(bounceSoundEft);
            gameOver = new AudioNode(assetManager, "Sounds/Game Over sound effect.wav", true);
            gameOver.setPositional(true);   
            gameOver.setVolume(3);
            rootNode.attachChild(gameOver);
            winSound = new AudioNode(assetManager, "Sounds/WinSound.wav", true);
            bounceSoundEft.setPositional(true);   
            bounceSoundEft.setVolume(3);
            rootNode.attachChild(bounceSoundEft);
            pointsTimer.reset();
            points = pointsPerLevel;
        }
  }

    @Override
    public void simpleUpdate(float tpf) {
        Vector2f mousePos = inputManager.getCursorPosition();
        Vector2f screenSize = new Vector2f();
        screenSize.x = this.settings.getWidth();
        screenSize.y = this.settings.getHeight();
        
        if (gui.playing && !isReady) {
            guiNode.attachChild(boardGUI.target);
        }
        
        if (Math.rint(levelTime-pointsTimer.getTimeInSeconds()) == 0){
            mainPlayer.playerPhy.setPhysicsLocation(new Vector3f(0f, -10f, 0f));
            pointsTimer.reset();
            timerText.setColor(ColorRGBA.White);
        }
        
        if (mousePos.x > screenSize.x/2 - 10 && mousePos.x < screenSize.x/2 + 10 && gui.playing == true) {
            if (mousePos.y > screenSize.y/2 - 10 && mousePos.y < screenSize.y/2 + 10) {    
                isReady = true;
                guiNode.detachChild(boardGUI.target);              
                initAudio();
                gameOver.stop();// play continuously!
                NiftyJmeDisplay saveDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
                nifty = saveDisplay.getNifty();
                guiViewPort.addProcessor(saveDisplay);
                //flyCam.setDragToRotate(true);

                nifty.loadStyleFile("nifty-default-styles.xml");
                nifty.loadControlFile("nifty-default-controls.xml");
                gui.getNifty(nifty);
                // <screen>
                nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen"){{
                    controller(gui); // Screen properties       
                    // <layer>
                    layer(new LayerBuilder("Layer_ID") {{
                        childLayoutVertical(); // layer properties, add more...

                        // <panel>
                        panel(new PanelBuilder("Panel_ID") {{
                           childLayoutCenter(); // panel properties, add more...               
                           valignCenter();
                            // GUI elements

                            control(new ButtonBuilder("SaveButton", "Save"){{
                                alignRight();
                                valignTop();
                                height("5%");
                                width("15%");
                                visibleToMouse(true);
                                interactOnClick("save()");
                            }});
                            //.. add more GUI elements here              
                        }});
                        // </panel>

                      }});
                    // </layer>
                  }}.build(nifty));
                // </screen>

               nifty.gotoScreen("Screen_ID"); // start the screen
                doOnce = false;
                timerText.setColor(ColorRGBA.White); 
            }
        }
        
        if (isReady && winTimerInit == false) {
           mainBoard.updateRotation(mousePos, screenSize); 
           points = points - 0.1;
           
        }
        else if (winTimerInit == false && isReady == false) {
            pointsTimer.reset();
            mainPlayer.playerPhy.setPhysicsLocation(new Vector3f(mainBoard.block[0].geom.getLocalTranslation().x,.6f,mainBoard.block[0].geom.getLocalTranslation().z));
        }
        
        if (mainPlayer.isDead()) { //check if we are dead
            points = pointsPerLevel;
            mainPlayer.playerPhy.setPhysicsLocation(new Vector3f(mainBoard.block[0].geom.getLocalTranslation().x,.3f,mainBoard.block[0].geom.getLocalTranslation().z));
            mainPlayer.playerPhy.setLinearVelocity(Vector3f.ZERO);//stop any movement
            mainPlayer.playerPhy.setAngularVelocity(Vector3f.ZERO);
            isReady = false;
            guiNode.attachChild(boardGUI.target);
            Quaternion q = new Quaternion();
            mainBoard.board.setLocalRotation(q.fromAngles(0,0,0));
            deaths ++;
            background.stop();//stop the music
            gameOver.play();// play continuously!
            doOnce = true;
        }
        
        if (gui.playing){
            hudText.setText("Deaths: " + deaths);             // the text
            pointText.setText("Points: " + Math.rint(points));             // display score
            if (Math.rint(levelTime-pointsTimer.getTimeInSeconds()) < 10) {
            timerText.setColor(ColorRGBA.Red);
            }

            if (isReady) {
                timerText.setText("Timer: " +Math.rint(levelTime-pointsTimer.getTimeInSeconds()) + " seconds");             // display score

            }
        }
        
        //System.out.println(ballSpeed());
        if (background != null) {
            background.setPitch(ballSpeed());
        }
        checkWin();
    }
    /**
     * Finds the speed of the ball for the sound check
     * @return float sound speed
     */
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
            return 1.15f;
        }
        else {
            //return (((speed - OldMin) * (NewMax - NewMin)) / (OldMax - OldMin)) + NewMin;
            return (((finalSpeed - 0.3f) * (1.15f - 0.95f)) / (15f - 0.3f)) + 0.95f;
        }
   }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }   

    public void checkWin() {
        if (mainPlayer.player.getLocalTranslation().x > mainBoard.block[5].geom.getLocalTranslation().x - 0.1f) {
           if (mainPlayer.player.getLocalTranslation().x < mainBoard.block[5].geom.getLocalTranslation().x + 0.1f) {
               if (mainPlayer.player.getLocalTranslation().z > mainBoard.block[5].geom.getLocalTranslation().z - 0.1f) {
                    if (mainPlayer.player.getLocalTranslation().z < mainBoard.block[5].geom.getLocalTranslation().z + 0.1f) {                
                        playWinAnim();
                    } 
                } 
            } 
        }
        
    }

    public void collision(PhysicsCollisionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void playWinAnim() {
        
        if (winTimerInit == false) {
            pointsTimer.reset();
            winTimerInit = true;
            winSound.play();
            background.stop();
            mainBoard.level ++;
            Quaternion q = new Quaternion();
            mainBoard.board.setLocalRotation(q.fromAngles(0,0,0));
           
        }
        if (isWin == false) {
            mainPlayer.playerPhy.setPhysicsLocation(mainBoard.block[5].geom.getLocalTranslation());
        }  
        if (Math.rint(1-pointsTimer.getTimeInSeconds()) == 0 && playEffect) {
            effect = assetManager.loadModel("Scenes/ParticleTest.j3o");
            effect.setLocalTranslation(new Vector3f(.3f,.3f,.3f));
            rootNode.attachChild(effect);
            playEffect = false;
        }
        if (Math.rint(2-pointsTimer.getTimeInSeconds()) == 0 ) {
            effect.setLocalTranslation(new Vector3f(-0.4f,.3f,-0.4f));
        }
        if (Math.rint(3-pointsTimer.getTimeInSeconds()) == 0 ) {
            effect.setLocalTranslation(new Vector3f(-0.2f,.3f,-0.5f));
        }
        if (Math.rint(4-pointsTimer.getTimeInSeconds()) == 0 ) {
            effect.setLocalTranslation(new Vector3f(.1f,.3f,-.5f));
        }
        if (Math.rint(5-pointsTimer.getTimeInSeconds()) == 0 ) {
            effect.setLocalTranslation(new Vector3f(-0.4f,.3f,-0.3f));
        }
        if (Math.rint(5-pointsTimer.getTimeInSeconds()) == 0) {
            isWin = true;
            mainPlayer.playerPhy.setLinearVelocity(new Vector3f(0f,3f,0f));
        }
        if (Math.rint(7-pointsTimer.getTimeInSeconds()) == 0) {
            resetBoard();
            rootNode.detachChild(effect);
        }
    }
    public void resetBoard() {
        mainBoard.setBoard();
        winTimerInit = false;
        mainPlayer.playerPhy.setPhysicsLocation(new Vector3f(mainBoard.block[0].geom.getLocalTranslation().x,.3f,mainBoard.block[0].geom.getLocalTranslation().z));
        mainPlayer.playerPhy.setLinearVelocity(Vector3f.ZERO);//stop any movement
        mainPlayer.playerPhy.setAngularVelocity(Vector3f.ZERO);
        isReady = false;
        guiNode.attachChild(boardGUI.target);
        bulletAppState.getPhysicsSpace().remove(mainBoard.boardPhy);
        bulletAppState.getPhysicsSpace().add(mainBoard.boardPhy);
    }
}
