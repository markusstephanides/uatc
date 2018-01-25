package com.digotsoft.uatc;

import com.digotsoft.uatc.scenes.LoadingScene;
import com.digotsoft.uatc.scenes.Renderable;
import com.digotsoft.uatc.util.Converters;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;

import java.lang.reflect.Constructor;

/**
 * A game using Slick2d
 */
public class Game extends BasicGame {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    
    private Renderable active;

    public Game() {
        super("Ultimate ATC Simulator");
    }
    
    public void switchScene(Class<? extends Renderable> sceneClass) {
        try {
            Constructor<? extends Renderable> constructor = sceneClass.getConstructor( Game.class );
            this.active = constructor.newInstance(this);
            this.active.loaded();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        if( this.active != null ) {
            this.active.render( container, g );
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.switchScene( LoadingScene.class );
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if( this.active != null ) {
            this.active.update( container, delta );
        }
    }
    
    @Override
    public void mouseWheelMoved( int change ) {
        if( this.active != null ) {
            this.active.mouseWheelMoved( change );
        }
    }
    
    
    
    public static void main( String[] args) throws SlickException {
        Display.setResizable(true);
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(WIDTH, HEIGHT, false);
        app.setForceExit(false);
        app.setUpdateOnlyWhenVisible( false );
        app.start();
        
     
       
    }

}
