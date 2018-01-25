package com.digotsoft.uatc.scenes;

import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.aviation.Radar;
import com.digotsoft.uatc.speech.SpeechReco;
import com.digotsoft.uatc.speech.SpeechRecoCallable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class GameScene extends Renderable {
    
    private Radar radar;
    private AtomicBoolean ppt;
    
    public GameScene( Game game ) {
        super( game );
    }
    
    @Override
    public void loaded() {
        this.radar = new Radar();
        this.ppt = new AtomicBoolean();
    }
    
    @Override
    public void render( GameContainer container, Graphics g ) throws SlickException {
        this.radar.render(container, g);
    }
    
    @Override
    public void update( GameContainer container, int delta ) throws SlickException {
        this.radar.update(container, delta);
        
        if(!this.ppt.get()) {
            if(container.getInput().isKeyDown( Input.KEY_RALT )) {
                this.ppt = SpeechReco.read( (result) -> {
                
                } );
            }
        }
        if(this.ppt.get()) {
            if(!container.getInput().isKeyDown( Input.KEY_RALT )) {
                this.ppt.set( false );
            }
        }
    }
    
    @Override
    public void mouseWheelMoved( int change ) {
        this.radar.mouseWheelMoved( change );
    }
}
