package com.digotsoft.uatc.scenes;

import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.radar.Sector;
import com.digotsoft.uatc.sim.StaticData;
import com.digotsoft.uatc.speech.SpeechReco;
import org.newdawn.slick.*;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class LoadingScene extends Renderable {
    
    private Font font;
    private int progress;
    private int maxProgress = 4;
    
    public LoadingScene( com.digotsoft.uatc.Game game ) {
        super( game );
    }
    
    public void loaded() {
        this.font = new TrueTypeFont( new java.awt.Font( "Arial", java.awt.Font.BOLD, 20 ), true );
        this.loadGameStuff();
    }
    
    private void loadGameStuff() {
        new Thread( () -> {
            // init the speech reco
            SpeechReco.init();
            LoadingScene.this.progress++;
            // load sectors
            Sector.loadSectors();
            LoadingScene.this.progress++;
            // load airports
            StaticData.load();
            LoadingScene.this.progress++;
            // done - switch to main menu //TODO
            Game.run( () -> this.game.switchScene( GameScene.class ) );
            LoadingScene.this.progress++;
        } ).start();
    }
    
    public void render( GameContainer container, Graphics g ) throws SlickException {
        String text = "Loading...";
        int x = ( container.getWidth() - this.font.getWidth( text ) ) / 2;
        int y = ( container.getHeight() - this.font.getHeight( text ) ) / 2;
        
        this.font.drawString( x, y, text );
        this.font.drawString( x + 5, y + 25, Math.round( (LoadingScene.this.progress / LoadingScene.this.maxProgress) * 100f) + "%");
    }
    
    public void update( GameContainer container, int delta ) throws SlickException {
    
    }
}
