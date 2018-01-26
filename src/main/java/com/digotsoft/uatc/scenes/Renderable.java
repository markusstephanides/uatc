package com.digotsoft.uatc.scenes;

import com.digotsoft.uatc.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public abstract class Renderable {
    
    protected Game game;
    
    public Renderable( Game game ) {
        this.game = game;
    }
    
    public void loaded() {
    
    }
    
    public void mouseWheelMoved(int change) {
    
    }
    
    public abstract void render( GameContainer container, Graphics g ) throws SlickException;
    
    public abstract void update( GameContainer container, int delta ) throws SlickException;

    public void keyPressed(int key, char c) {

    }

}
