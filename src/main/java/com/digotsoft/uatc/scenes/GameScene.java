package com.digotsoft.uatc.scenes;

import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.radar.Radar;
import com.digotsoft.uatc.sim.Simulator;
import com.digotsoft.uatc.sim.StaticData;
import com.digotsoft.uatc.speech.SpeechReco;
import com.digotsoft.uatc.ui.ArrDepBox;
import com.digotsoft.uatc.ui.Button;
import com.digotsoft.uatc.ui.GUI;
import lombok.Getter;
import org.newdawn.slick.*;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class GameScene extends Renderable {
    
    @Getter
    private Simulator simulator;
    private Radar radar;
    private GUI gui;
    private AtomicBoolean ppt;
    
    private int guiBarHeight = 30;
    private GameContainer container;
    
    public GameScene( Game game ) {
        super( game );
    }
    
    @Override
    public void loaded() {
        this.simulator = new Simulator( this, StaticData.getAirportByICAO( "LOWW" ) );
        this.simulator.generateFlights();
        this.radar = new Radar( this );
        this.ppt = new AtomicBoolean();
        
        this.gui = new GUI( null );
        this.gui.addElement( "vorButton", new Button( null, "VOR", 0, 0, 40, 30, true ) );
        this.gui.addElement( "ndbButton", new Button( null, "NDB", 40, 0, 40, 30, true ) );
        this.gui.addElement( "fixButton", new Button( null, "FIX", 80, 0, 40, 30, true ) );
        this.gui.addElement( "z1Button", new Button( null, "Z1", 120, 0, 30, 30, true ) );
        this.gui.addElement( "z2Button", new Button( null, "Z2", 150, 0, 30, 30, true ) );
        this.gui.addElement( "z3Button", new Button( null, "Z3", 180, 0, 30, 30, true ) );
        this.gui.addElement( "z4Button", new Button( null, "Z4", 210, 0, 30, 30, true ) );
        this.gui.addElement( "zPButton", new Button( null, "Z+", 240, 0, 30, 30, true ) );
        this.gui.addElement( "zMButton", new Button( null, "Z-", 270, 0, 30, 30, true ) );
        this.gui.addElement( "txButton", new Button( null, "TX", 0, Integer.MAX_VALUE, 70, 50, false ) );
        this.gui.addElement( "rxButton", new Button( null, "RX", 70, Integer.MAX_VALUE, 70, 50, false ) );
        this.gui.addElement("arrDepBox", new ArrDepBox(null, Integer.MAX_VALUE, Integer.MAX_VALUE, 250, 300, this.simulator));
        
        this.gui.getButtonElement( "vorButton" ).addClickListener( () -> {
            GameScene.this.radar.setDisplayVors( ! GameScene.this.radar.isDisplayVors() );
            ( ( Button ) GameScene.this.gui.getElement( "vorButton" ) ).setState( GameScene.this.radar.isDisplayVors() );
        } );
        
        this.gui.getButtonElement( "ndbButton" ).addClickListener( () -> {
            GameScene.this.radar.setDisplayNdbs( ! GameScene.this.radar.isDisplayNdbs() );
            ( ( Button ) GameScene.this.gui.getElement( "ndbButton" ) ).setState( GameScene.this.radar.isDisplayNdbs() );
        } );
        
        this.gui.getButtonElement( "fixButton" ).addClickListener( () -> {
            GameScene.this.radar.setDisplayFixs( ! GameScene.this.radar.isDisplayFixs() );
            ( ( Button ) GameScene.this.gui.getElement( "fixButton" ) ).setState( GameScene.this.radar.isDisplayFixs() );
        } );
        
        this.gui.getButtonElement( "z1Button" ).addClickListener( () -> {
            if ( GameScene.this.container.getInput().isKeyDown( Input.KEY_LSHIFT ) )
                GameScene.this.radar.saveCurrPosZoom( 0 );
            else GameScene.this.radar.loadPosZoom( 0 );
        } );
        
        this.gui.getButtonElement( "z2Button" ).addClickListener( () -> {
            if ( GameScene.this.container.getInput().isKeyDown( Input.KEY_LSHIFT ) )
                GameScene.this.radar.saveCurrPosZoom( 1 );
            else GameScene.this.radar.loadPosZoom( 1 );
        } );
        
        this.gui.getButtonElement( "z3Button" ).addClickListener( () -> {
            if ( GameScene.this.container.getInput().isKeyDown( Input.KEY_LSHIFT ) )
                GameScene.this.radar.saveCurrPosZoom( 2 );
            else GameScene.this.radar.loadPosZoom( 2 );
        } );
        
        this.gui.getButtonElement( "z4Button" ).addClickListener( () -> {
            if ( GameScene.this.container.getInput().isKeyDown( Input.KEY_LSHIFT ) )
                GameScene.this.radar.saveCurrPosZoom( 3 );
            else GameScene.this.radar.loadPosZoom( 3 );
        } );

        this.gui.getButtonElement( "zPButton" ).addClickListener( () -> {
            this.radar.zoom(1);
        } );

        this.gui.getButtonElement( "zMButton" ).addClickListener( () -> {
            this.radar.zoom(-1);
        } );
    }
    
    public void setRxButton( boolean active ) {
        this.gui.getButtonElement( "rxButton" ).setState( active );
    }
    
    @Override
    public void render( GameContainer container, Graphics g ) throws SlickException {
        this.radar.render( container, g );
        g.setColor( new Color( 30, 30, 30 ) );
        g.fillRect( 0, 0, container.getWidth(), this.guiBarHeight );
        this.gui.render( container, g );
    }
    
    @Override
    public void update( GameContainer container, int delta ) throws SlickException {
        if ( this.container == null ) this.container = container;
        this.simulator.update();
        
        this.radar.update( container, delta );
        if ( ! this.ppt.get() ) {
            if ( container.getInput().isKeyDown( Input.KEY_RALT ) ) {
                this.gui.getButtonElement( "txButton" ).setState( true );
                this.ppt = SpeechReco.read( ( result ) -> {
                    this.simulator.processATCTX(result);
                } );
            }
        }
        if ( this.ppt.get() ) {
            if ( ! container.getInput().isKeyDown( Input.KEY_RALT ) ) {
                this.gui.getButtonElement( "txButton" ).setState( false );
                this.ppt.set( false );
            }
        }
    }
    
    @Override
    public void mouseClicked( int button, int x, int y, int clickCount ) {
        this.gui.mouseClicked( button, x, y, clickCount );
        this.radar.mouseClicked( button, x, y, clickCount );
    }
    
    @Override
    public void keyPressed( int key, char c ) {
        this.radar.keyPressed( key, c );
    }
    
    @Override
    public void mouseWheelMoved( int change ) {
        this.radar.mouseWheelMoved( change );
    }
    
    @Override
    public void mouseDragged( int oldx, int oldy, int newx, int newy ) {
        if ( oldy > this.guiBarHeight && newy > this.guiBarHeight ) {
            this.gui.mouseDragged(oldx, oldy, newx, newy);
            this.radar.mouseDragged( oldx, oldy, newx, newy );
        }
    }
}
