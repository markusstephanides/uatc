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
    
    public void mouseDragged( int oldx, int oldy, int newx, int newy ) {
    
    }
    
    public void mouseClicked( int button, int x, int y, int clickCount ) {
    
    }
    
    public boolean isInRenderArea( GameContainer container, float x, float y) {
        return x > 0 && y > 0 && x <= container.getWidth() && y <= container.getHeight();
    }

}
