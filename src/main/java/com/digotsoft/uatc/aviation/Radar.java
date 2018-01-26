package com.digotsoft.uatc.aviation;

import com.digotsoft.uatc.*;
import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.scenes.Renderable;
import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.*;

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

    @Getter @Setter
    private boolean displayVors = true;
    @Getter @Setter
    private boolean displayNdbs = true;
    @Getter @Setter
    private boolean displayFixs = true;

    private Font radarFont;

    private GameContainer container;
    
    public Radar() {
        super( null );
        this.activeSector = Sector.getSector( "LOVV_CTR" );
        this.radarFont = new TrueTypeFont(new java.awt.Font("Arial", 0, 8), true);
        this.cameraX = this.activeSector.getInitCamX();
        this.cameraY = this.activeSector.getInitCamY();
    }
    
    @Override
    public void render( GameContainer container, Graphics g ) throws SlickException {
        for ( Fix fix : this.activeSector.getFixes() ) {
            switch(fix.getType()) {
                case VOR:
                    if(!this.displayVors) continue;
                    g.setColor(Color.cyan);
                    break;
                case NDB:
                    if(!this.displayNdbs) continue;
                    g.setColor(Color.yellow);
                    break;
                case FIX:
                    if(!this.displayFixs) continue;
                    g.setColor(Color.green);
                    break;
            }

            this.radarFont.drawString( (float ) (( fix.getX() - this.cameraX ) * this.zoom), ( float ) ((( fix.getY() - this.cameraY ) * this.zoom) + 5), fix.getName() );
            g.fillOval( (float)(( fix.getX() - this.cameraX ) * this.zoom), ( float ) (( fix.getY() - this.cameraY ) * this.zoom), 3,3 );
        }
    
        for ( Path path: this.activeSector.getPaths() ) {
            g.setColor( path.getColor() );
            g.drawLine( (float)(( path.getFromX() - this.cameraX ) * this.zoom), (float)(( path.getFromY() - this.cameraY ) * this.zoom),
                    (float)(( path.getToX() - this.cameraX ) * this.zoom), ( float ) (( path.getToY() - this.cameraY ) * this.zoom) );
        }
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
        this.zoom *= (change > 0 ? this.zoomSpeed : 1 / this.zoomSpeed);
        this.cameraX += (this.container.getWidth() / zoomBefore - this.container.getWidth() / this.zoom) / 2;
        this.cameraY += (this.container.getHeight() / zoomBefore - this.container.getHeight() / this.zoom) / 2;
        this.moveSpeed = 0.5 / this.zoom;
    }
}
