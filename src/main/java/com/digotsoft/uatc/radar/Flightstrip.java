package com.digotsoft.uatc.radar;

import com.digotsoft.uatc.sim.Flight;
import com.digotsoft.uatc.sim.StaticData;
import org.newdawn.slick.*;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.io.InputStream;

/**
 * @author MaSte
 * @created 31-Jan-18
 */
public class Flightstrip {
    
    private org.newdawn.slick.Image background;
    private Font callsignFontShort;
    private Font callsignFont;
    
    private float x = - 1;
    private float y;
    
    public Flightstrip() {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "data/imgs/fs.png" );
            this.background = new Image( inputStream, "fs_bg", false );
            this.callsignFontShort = new TrueTypeFont( new java.awt.Font( "Arial", java.awt.Font.PLAIN, 20 ), true );
            this.callsignFont = new TrueTypeFont( new java.awt.Font( "Arial", java.awt.Font.PLAIN, 10 ), true );
        } catch ( SlickException e ) {
            e.printStackTrace();
        }
    }
    
    public void render( GameContainer container, Graphics g, Flight flight ) {
        if ( this.x == - 1 ) {
            this.x = ( container.getWidth() - this.background.getWidth() ) / 2;
            this.y = ( container.getHeight() - this.background.getHeight() );
        }
        
        this.background.draw( this.x, container.getHeight() - this.background.getHeight() );
        
        if ( flight == null ) return;
        
        this.callsignFontShort.drawString( this.x + 85, this.y + 25, flight.getCallsign() );
        this.callsignFont.drawString( this.x + 85, this.y, StaticData.getCallsignByShortCS( flight.getCallsign() ).toUpperCase() );
        this.callsignFont.drawString( this.x + 192, this.y, flight.getFlightplan().getDepAirport().getIcao() );
        this.callsignFont.drawString( this.x + 5, this.y, flight.getFlightplan().getDestAirport().getIcao() );
        this.callsignFont.drawString( this.x + 7, this.y + 36, flight.getSquawk() );
        this.callsignFont.drawString( this.x + 17, this.y + 13, flight.getFlightplan().getFlightRule().toString().substring( 0, 1 ) );
        this.callsignFont.drawString( this.x + 43, this.y + 12, flight.getAircraft().getName() + " " + flight.getAircraft().getCategory() );
        this.callsignFont.drawString( this.x + 43, this.y, flight.getFlightplan().getCruiseFL() );
        this.callsignFont.drawString( this.x + 43, this.y + 24, flight.getFlightplan().getCruiseSpeed() );
        
        //route
        this.callsignFont.drawString( this.x + 247  , this.y, flight.getFlightplan().getRoute() );
    }
    
}
