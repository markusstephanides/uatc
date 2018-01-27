package com.digotsoft.uatc;

import com.digotsoft.uatc.scenes.LoadingScene;
import com.digotsoft.uatc.scenes.Renderable;
import com.digotsoft.uatc.util.Converters;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;

import java.lang.reflect.Constructor;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A game using Slick2d
 */
public class Game extends BasicGame {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;


    public static final Queue<Runnable> queue = new LinkedBlockingQueue<>();
    
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
        if(Display.getWidth() != container.getWidth() || Display.getHeight() != container.getHeight()) {
            try {
                AppGameContainer appGameContainer = (AppGameContainer)container;
    
                appGameContainer.setDisplayMode(Display.getWidth(), Display.getHeight(), false);
                //appGameContainer.reinit();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        if( this.active != null ) {
            this.active.update( container, delta );
        }

        while( queue.size() > 0) {
            queue.poll().run();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if( this.active != null ) {
            this.active.keyPressed( key, c );
        }
    }

    @Override
    public void mouseWheelMoved( int change ) {
        if( this.active != null ) {
            this.active.mouseWheelMoved( change );
        }
    }
    
    @Override
    public void mouseDragged( int oldx, int oldy, int newx, int newy ) {
        if( this.active != null ) {
            this.active.mouseDragged( oldx, oldy, newx, newy );
        }
    }
    
    public static void run( Runnable runnable) {
        queue.add(runnable);
    }
    
    public static void main( String[] args) throws SlickException {
        Display.setResizable(true);
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(WIDTH, HEIGHT, false);
        app.setForceExit(false);
        app.setUpdateOnlyWhenVisible( false );
        app.setAlwaysRender( true );
        //app.setShowFPS( false );
        app.start();
    }

}
