/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Adam
 */
public class GameBoard{
    Node board = new Node(); //declare board as a node (ie an empty object that we can connect shit to)
    RigidBodyControl boardPhy; //the boards physics
    public Material mat; //material for the board
    public Material goalMat; //material for the board
    Block [] block = new Block [10000]; //max # of paths that can be created
    BoardNode [] [] nodeGrid = new BoardNode [10] [10]; //# of nodes for the board
    int numBlocks = 0; //keep track of how many blocks exist
    float boardWidth = 0.4f; //change this for whatever board width you want
    Vector3f blockLocation;
    float tileOffset = 0f;
    int level = 1;  
    /**sets the location of the board.
    */
    public void setBoard() {
        makeBoard(); //make the grid
        /*///////////////////////////////////
        HOW TO MAKE BLOCKS
        * USE THE MAKEBLOCK METHOD
        * MAKEBLOCK TAKES 2 ARGUMENTS
        * 1. THE START NODE 
        * 2. THE END NODE
        * START AND END NODES ARE WHERE THE GAME WILL DRAW THE BLOCK BETWEEN. THEY ARE BOARDNODES AND REFRENECE A POSITION IN THE NODEGRID
        * 0,0 IS THE BOTTOM RIGHT CORNER OF THE GRID AND 9,9 IS THE TOP LEFT
        *//////////////////////////////////////     
        numBlocks = 0;
        board.detachAllChildren();
        if (level == 1) {
            makePlatform(nodeGrid[0][0]);
            makeBlock(nodeGrid[0][0], nodeGrid[0][3]);
            makeBlock(nodeGrid[0][3], nodeGrid[3][3]);
            makeBlock(nodeGrid[3][3], nodeGrid[3][9]);
            makeBlock(nodeGrid[3][9], nodeGrid[9][9]);
            makePlatform(nodeGrid[9][9]);
        }
        if (level == 2) {
            makePlatform(nodeGrid[0][0]);
            makeBlock(nodeGrid[0][0], nodeGrid[0][9]);
            makeBlock(nodeGrid[0][9], nodeGrid[4][9]);
            makeBlock(nodeGrid[4][9], nodeGrid[4][5]);
            makeBlock(nodeGrid[4][5], nodeGrid[9][5]);
            makePlatform(nodeGrid[9][5]);
        }
        CompoundCollisionShape boardShape = new CompoundCollisionShape();
        boardShape.addChildShape(block[0].collider, block[0].geom.getLocalTranslation());
        boardShape.addChildShape(block[1].collider, block[1].geom.getLocalTranslation());
        boardShape.addChildShape(block[2].collider, block[2].geom.getLocalTranslation());
        boardShape.addChildShape(block[3].collider, block[3].geom.getLocalTranslation());
        boardShape.addChildShape(block[4].collider, block[4].geom.getLocalTranslation());
        boardShape.addChildShape(block[5].collider, block[5].geom.getLocalTranslation());
        
        boardPhy = new RigidBodyControl(boardShape, 0);
        boardPhy.setKinematic(true);
        board.addControl(boardPhy);

    }
    /**creates a 10x10 grid of nodes for the board
    */
    public void makeBoard() {
        float a = -1.0f; //the board goes from -1 metre to 1 metre
        float b = -1.0f; 
        for (int i = 0; i < 10; i ++ ){ //10 x 10 grid
            for (int k = 0; k < 10; k ++ ){
                nodeGrid[i][k] = new BoardNode(); //create a node
                nodeGrid[i][k].position = new Vector3f(b,0,a); //set its x and z position
                b += 0.2f; //place nodes 0.2 metres apart
            }
            b = -1f;
            a += 0.2f;
        }
    }
     /**make a block on the board
      * 
      * @param start //node to draw block from
      * @param end  //node to draw block to
      */
    public void makeBlock(BoardNode start, BoardNode end) {
        block[numBlocks] = new Block(); //declare a new block
        Vector3f scale = new Vector3f(findDistance(start.position,end.position)); //find distance between start and end node and scale block up accordingly
        block[numBlocks].createBlock(scale, mat); //create the block
        
        block[numBlocks].geom.setLocalTranslation(findAvg(start.position, end.position)); //move it to the central point between the 2 nodes
        if (scale.z == boardWidth) {
            blockLocation = new Vector3f(block[numBlocks].geom.getLocalTranslation().x - boardWidth/2f,block[numBlocks].geom.getLocalTranslation().y,block[numBlocks].geom.getLocalTranslation().z);
            block[numBlocks].geom.setLocalTranslation(blockLocation); //move it to the central point between the 2 nodes
        }
        else if (scale.x == boardWidth) {
            blockLocation = new Vector3f(block[numBlocks].geom.getLocalTranslation().x,block[numBlocks].geom.getLocalTranslation().y,block[numBlocks].geom.getLocalTranslation().z - boardWidth/2f);
            block[numBlocks].geom.setLocalTranslation(blockLocation); //move it to the central point between the 2 nodes
        }
        
        
        
        board.attachChild(block[numBlocks].geom);  //add geometry to the block
        numBlocks ++;
    }
    
      /**make a block on the board
      * 
      * @param start //node to draw block from
      * @param end  //node to draw block to
      */
    public void makePlatform(BoardNode node) {
        block[numBlocks] = new Block(); //declare a new block
        Vector3f scale = new Vector3f(0.1f, 0.01f, 0.1f); //make a square platform
        block[numBlocks].createBlock(scale, goalMat); //create the block
        if (numBlocks == 0) {
            block[numBlocks].geom.setLocalTranslation(new Vector3f(node.position.x, tileOffset + 0.01f, node.position.z)); //place it on the selected node
        }
        else {
            block[numBlocks].geom.setLocalTranslation(new Vector3f(node.position.x, tileOffset + 0.02f, node.position.z)); //place it on the selected node
        }
        board.attachChild(block[numBlocks].geom);  //add geometry to the block
        numBlocks ++;
    }
     /**finds the average position of 2 nodes
      * 
      * @param start the start node
      * @param end the end node
      * @return the average position
      */
    public Vector3f findAvg(Vector3f start, Vector3f end) {
        Vector3f avg = new Vector3f();
        avg.x = (start.x + end.x) / 2;
        avg.y = tileOffset;
        tileOffset -= 0.01f;
        avg.z = (start.z + end.z) / 2;
        return avg;
    }
    /**finds the distance between 2 objects
     * 
     * @param start the start node
     * @param end the end node
     * @return the distance between them, altered so it applys to the scale of a block
     */
    public Vector3f findDistance(Vector3f start, Vector3f end) {
        Vector3f dist = new Vector3f(); 
        dist.x = (end.x - start.x) / 2 + boardWidth/2f; //x scale of the block. the distance between 2 nodes divided by 2 + the thickness of a block
        if (dist.x == 0) {
            dist.x = boardWidth;} //if there is no difference between the blocks then set it to the thickness of a block
        dist.z = (end.z - start.z) / 2 + boardWidth/2f; //same for z
        if (dist.z == 0) {dist.z = boardWidth;}
        dist.y = 0.01f; //y will always be the same
        return dist;
    }
    /**rotate the board according to the mouse position
     * 
     * @param mousePos //current position of the mouse in pixels
     * @param screenSize //the size of the screen in pixels
     */
    public void updateRotation(Vector2f mousePos, Vector2f screenSize) {
        Vector2f rotAngle = new Vector2f();   
        
        float [] [] oldRange = new float [2] [2]; //range of numbers in screenPos
        oldRange[0][0] = 1; //range of numbers from 1 to middle of screen in pixels
        oldRange[0][1] = screenSize.x/2;

        oldRange[1][0] = 1; //range of numbers from 1 to middle of screen in pixels
        oldRange[1][1] = screenSize.y/2;
        
        float [] [] newRange = new float [2] [2]; //range of numbers in angles
        newRange[0][0] = 0;
        newRange[0][1] = 30; //max rotation angle of the board.
        
        newRange[1][0] = 0;
        newRange[1][1] = 30; //max rotation angle of the board.
        
        //converts mouseposition from pixels to degrees
        rotAngle.x = (((mousePos.x - oldRange[0][0]) * (newRange[0][0] - newRange[0][1])) / (oldRange[0][1] - oldRange[0][0])) + newRange[0][1];   
        rotAngle.x = (float) Math.toRadians(rotAngle.x); //converts to radians
        
        rotAngle.y = (((mousePos.y - oldRange[1][0]) * (newRange[1][0] - newRange[1][1])) / (oldRange[1][1] - oldRange[1][0])) + newRange[1][1];   
        rotAngle.y = (float) Math.toRadians(rotAngle.y);
        
        Quaternion q = new Quaternion();
        board.setLocalRotation( q.fromAngles( -rotAngle.y , 0f, -rotAngle.x)); //apply the rotation
        
    }
}
