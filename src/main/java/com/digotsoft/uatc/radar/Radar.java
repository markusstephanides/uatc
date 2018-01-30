package com.digotsoft.uatc.radar;

import com.digotsoft.uatc.scenes.GameScene;
import com.digotsoft.uatc.scenes.Renderable;
import com.digotsoft.uatc.sim.Flight;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class Radar extends Renderable {
    
    private Sector activeSector;
    
    private double cameraX;
    private double cameraY;
    private double zoom = 100;
    private double zoomSpeed = 1.1;
    private double moveSpeed = 0.5 / this.zoom;
    
    @Getter
    @Setter
    private boolean displayVors = true;
    @Getter
    @Setter
    private boolean displayNdbs = true;
    @Getter
    @Setter
    private boolean displayFixs = true;
    @Getter
    @Setter
    private boolean displayLabels = true;
    
    @Getter
    @Setter
    private PosZoomSetting[] posZoomSettings;
    
    private List<Renderable> objectsToRender;
    
    private Font radarFont;
    private Font labelFont;
    private Color background;
    
    private GameContainer container;
    private GameScene gameScene;
    
    public Radar( GameScene gameScene ) {
        super( null );
        this.gameScene = gameScene;
        this.objectsToRender = new ArrayList<>();
        this.posZoomSettings = new PosZoomSetting[ 4 ];
        this.background = new Color( 0, 0, 10 );
        this.activeSector = Sector.getSector( "LOVV_CTR" );
        this.radarFont = new TrueTypeFont( new java.awt.Font( "Arial Narrow", java.awt.Font.PLAIN, 8 ), true );
        this.labelFont = new TrueTypeFont( new java.awt.Font( "Arial", java.awt.Font.PLAIN, 12 ), true );
        this.cameraX = this.gameScene.getSimulator().getControllingAirport().getX();
        this.cameraY = this.gameScene.getSimulator().getControllingAirport().getY();
        this.zoom = this.gameScene.getSimulator().getControllingAirport().getZoom();
        GL11.glClearColor( 0, 0, 0.06f, 1 );
    }
    
    @Override
    public void render( GameContainer container, Graphics g ) throws SlickException {
        g.setColor( this.background );
        
        for ( Fix fix : this.activeSector.getFixes() ) {
            switch ( fix.getType() ) {
                case VOR:
                    if ( ! this.displayVors ) continue;
                    g.setColor( Color.cyan );
                    break;
                case NDB:
                    if ( ! this.displayNdbs ) continue;
                    g.setColor( Color.yellow );
                    break;
                case FIX:
                    if ( ! this.displayFixs ) continue;
                    g.setColor( Color.green );
                case LABEL:
                    if ( ! this.displayLabels ) continue;
                    g.setColor( Color.white );
                    break;
            }
            
            int x = Math.toIntExact( Math.round( ( ( fix.getX() - this.cameraX ) * this.zoom ) ) );
            int y = Math.toIntExact( Math.round( ( ( fix.getY() - this.cameraY ) * this.zoom ) ) );
            
            if ( ! this.isInRenderArea( container, x, y ) ) {
                continue;
            }
            
            this.radarFont.drawString( x + 5, y - 10, fix.getName() );
            g.fillOval( x, y, 3, 3 );
        }
        
        for ( Path path : this.activeSector.getPaths() ) {
            g.setColor( path.getColor() );
            g.drawLine( ( float ) ( ( path.getFromX() - this.cameraX ) * this.zoom ), ( float ) ( ( path.getFromY() - this.cameraY ) * this.zoom ),
                    ( float ) ( ( path.getToX() - this.cameraX ) * this.zoom ), ( float ) ( ( path.getToY() - this.cameraY ) * this.zoom ) );
        }
        
        // draw flights
        for ( Flight flight : this.gameScene.getSimulator().getFlights() ) {
            float x = Math.round( ( ( flight.getX() - this.cameraX ) * this.zoom ) );
            float y = Math.round( ( ( flight.getY() - this.cameraY ) * this.zoom ) );
            
            // square
            g.setColor( Color.green );
            g.drawRect( x - 3, y - 3, 6, 6 );
            // line
            g.drawLine( x, y, x + flight.getLabelDistance(), y - flight.getLabelDistance() );
            
            x += flight.getLabelDistance() + 2;
            y -= flight.getLabelDistance() - 2;
            
            Color color = Color.green;
            this.labelFont.drawString( 1 + x, 1 + y, flight.getCallsign(), Color.black );
            this.labelFont.drawString( x, y, flight.getCallsign(), color );
        }
    }
    
    public void saveCurrPosZoom( int index ) {
        this.posZoomSettings[ index ] = new PosZoomSetting( this.cameraX, this.cameraY, this.zoom );
    }
    
    public void loadPosZoom( int index ) {
        this.loadPosZoom( this.posZoomSettings[ index ].getX(), this.posZoomSettings[ index ].getY(), this.posZoomSettings[ index ].getZoom() );
    }
    
    public void loadPosZoom( double x, double y, double zoom ) {
        this.cameraX = x;
        this.cameraY = y;
        this.zoom = zoom;
    }
    
    @Override
    public void update( GameContainer container, int delta ) throws SlickException {
        if ( container.getInput().isKeyDown( Input.KEY_LEFT ) ) this.cameraX -= this.moveSpeed * delta;
        if ( container.getInput().isKeyDown( Input.KEY_RIGHT ) ) this.cameraX += this.moveSpeed * delta;
        if ( container.getInput().isKeyDown( Input.KEY_UP ) ) this.cameraY -= this.moveSpeed * delta;
        if ( container.getInput().isKeyDown( Input.KEY_DOWN ) ) this.cameraY += this.moveSpeed * delta;
        
        this.container = container;
    }
    
    @Override
    public void mouseWheelMoved( int change ) {
        double zoomBefore = this.zoom;
        this.zoom *= ( change > 0 ? this.zoomSpeed : 1 / this.zoomSpeed );
        this.cameraX += ( this.container.getWidth() / zoomBefore - this.container.getWidth() / this.zoom ) / 2;
        this.cameraY += ( this.container.getHeight() / zoomBefore - this.container.getHeight() / this.zoom ) / 2;
        this.moveSpeed = 0.5 / this.zoom;
    }
    
    @Override
    public void keyPressed( int key, char c ) {
        if ( key == Input.KEY_F12 ) {
            System.out.println( this.cameraX + "," + this.cameraY + "," + this.zoom );
        }
    }
    
    @Override
    public void mouseDragged( int oldx, int oldy, int newx, int newy ) {
        this.cameraX += ( oldx - newx ) / this.zoom;
        this.cameraY += ( oldy - newy ) / this.zoom;
    }
}
