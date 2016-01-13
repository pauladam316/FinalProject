/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
 
public class GUIHandle extends AbstractAppState implements ScreenController {
    Nifty nifty;
    boolean playing = false;
        @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
 
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
 
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
 
    public void bind(Nifty nifty, Screen screen) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
 
    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
 
    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    public void getNifty (Nifty gui) {
        nifty = gui;
    }
    /**
     * removes GUI and allows you to start your shit
     */
    public void startGame() {
      nifty.removeScreen("Screen_ID");
      playing = true;
      // start the game and do some more stuff...
    }
    public void save() {
        //ERIC PUT SAVING SHIT HERE
    }
 
}