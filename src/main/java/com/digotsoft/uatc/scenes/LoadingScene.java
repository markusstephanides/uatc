package com.digotsoft.uatc.scenes;

import com.digotsoft.uatc.*;
import com.digotsoft.uatc.aviation.Sector;
import com.digotsoft.uatc.speech.SpeechReco;
import org.newdawn.slick.*;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class LoadingScene extends Renderable {
    
    private Font font;
    
    public LoadingScene( com.digotsoft.uatc.Game game ) {
        super( game );
    }
    
    public void loaded() {
        System.out.println( "hi" );
        this.font = new TrueTypeFont( new java.awt.Font( "Arial", java.awt.Font.BOLD, 20 ), true );
        this.loadGameStuff();
    }
    
    private void loadGameStuff() {
     
            // init the speech reco
            SpeechReco.init();
            // load sectors
            Sector.loadSectors();
            // done - switch to main menu //TODO
            this.game.switchScene( GameScene.class );
       // }).start();
    }
    
    public void render( GameContainer container, Graphics g ) throws SlickException {
        String text = "Loading...";
        int x = ( container.getWidth() - this.font.getWidth( text ) ) / 2;
        int y = ( container.getHeight() - this.font.getHeight( text ) ) / 2;
        
        this.font.drawString( x, y, text );
    }
    
    public void update( GameContainer container, int delta ) throws SlickException {
    
    }
}
