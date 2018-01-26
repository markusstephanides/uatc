package com.digotsoft.uatc.scenes;

import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.aviation.Radar;
import com.digotsoft.uatc.speech.SpeechReco;
import com.digotsoft.uatc.ui.Button;
import com.digotsoft.uatc.ui.GUI;
import org.newdawn.slick.*;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class GameScene extends Renderable {
    
    private Radar radar;
    private GUI gui;
    private AtomicBoolean ppt;
    
    public GameScene( Game game ) {
        super( game );
    }
    
    @Override
    public void loaded() {
        this.radar = new Radar();
        this.ppt = new AtomicBoolean();
        this.gui = new GUI(null);
        this.gui.addElement(new Button(null, "VOR", 20,0, 40, 20));
    }
    
    @Override
    public void render( GameContainer container, Graphics g ) throws SlickException {
        this.radar.render(container, g);
        this.gui.render(container, g);
    }
    
    @Override
    public void update( GameContainer container, int delta ) throws SlickException {
        this.radar.update(container, delta);
        if(!this.ppt.get()) {
            if(container.getInput().isKeyDown( Input.KEY_RALT )) {
                this.ppt = SpeechReco.read( (result) -> {
                    System.out.println(result);
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
    public void keyPressed(int key, char c) {
        if(key == Input.KEY_Z)
            this.radar.setDisplayVors(!this.radar.isDisplayVors());
        if(key == Input.KEY_X)
            this.radar.setDisplayNdbs(!this.radar.isDisplayNdbs());
        if(key == Input.KEY_C)
            this.radar.setDisplayFixs(!this.radar.isDisplayFixs());
    }

    @Override
    public void mouseWheelMoved( int change ) {
        this.radar.mouseWheelMoved( change );
    }
}
