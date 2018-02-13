package com.digotsoft.uatc.sim;

import com.digotsoft.uatc.util.Converters;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author MaSte
 * @created 28-Jan-18
 */
@Getter
public class Flight {
    
    private String callsign;
    private Flightplan flightplan;
    private String clearedWaypoint;
    private String clearedFL;
    private String squawk;
    private boolean squawkTx;
    private boolean onGround;
    private Aircraft aircraft;
    
    private double x;
    private double y;
    
    private float labelDistance = 40;
    private float labelXOffset = 2 + 40;
    private float labelYOffset = 6 + 40;
    private float labelWidth = 50;
    private float labelHeight = 20;

    @Setter @Getter private boolean assumed;
    
    private FlightStatus status;
    private long tickNextTx;
    private Simulator simulator;
    private Random random;
    private String voice;
    
    public Flight( Simulator simulator, String callsign, Aircraft aircraft, Flightplan flightplan, FlightStatus status ) {
        this.simulator = simulator;
        this.aircraft = aircraft;
        this.callsign = callsign;
        this.flightplan = flightplan;
        this.status = status;
        this.clearedWaypoint = "";
        this.clearedFL = "";
        this.squawk = "1200";
        this.squawkTx = false;
        this.onGround = false;
        this.x = flightplan.getDepStand().getX();
        this.y = flightplan.getDepStand().getY();
        this.random = new Random();
        this.setNextActionTick(30, 300);
        this.voice = "male_1";
    }

    private void setNextActionTick(int from, int to) {
        this.tickNextTx = this.simulator.getTick() + this.random.nextInt( to - from ) + from;
    }
    
    public void update( long tick ) {
        if ( this.tickNextTx == tick ) {
            this.nextAction();
        }
    }
    
    public void nextAction() {
        switch ( this.status ) {
            case STARTED_AT_STAND:
                this.status = FlightStatus.WAITING_FOR_FP_CLEARANCE;
                this.simulator.transmit( this, this.voice, this.flightplan.getDepAirport().getShortName().toLowerCase() + " tower " + this.callbackToVoiceString() + " req_ifr_to " + this.flightplan.getDestAirport().getShortName().toLowerCase(), true );
        }
    }
    
    public void processATCResponse( String resp ) {
        if ( this.status == FlightStatus.WAITING_FOR_FP_CLEARANCE ) {
            System.out.println( "FP clearance:" + resp );
            String[] data = resp.split( " " );
            String[] runwayDigits = this.extract( data, "runway", 2 );
            String[] squawkDigits = this.extract( data, "squawk", 4 );
            
            String runwayStr = "";
            String squawkStr = "";
            
            for ( String runwayDigit : runwayDigits ) {
                runwayStr += Converters.convSpeechWord(runwayDigit) + "_h ";
            }
            
            runwayStr = runwayStr.substring( 0, runwayStr.length() - 1 );
            
            for ( String squawkDigit : squawkDigits ) {
                squawkStr += Converters.convSpeechWord(squawkDigit) + "_h ";
            }
            
            squawkStr = squawkStr.substring( 0, squawkStr.length() - 1 );
            
            
            this.status = FlightStatus.FP_CLEARANCE_WAITING_RB_CORR;
            this.simulator.transmit( this, this.voice, this.callbackToVoiceString() + " clrd_to " + this.flightplan.getDestAirport().getShortName().toLowerCase() + " clrd_out_of_rwy " + runwayStr + " squawk " + squawkStr, true );
        }
        else if ( this.status == FlightStatus.FP_CLEARANCE_WAITING_RB_CORR ) {
            if(resp.contains("correct")) {
                System.out.println("Hi");
                this.simulator.finishTx();
                this.setNextActionTick(20 * 2, 20 * 5);
                this.status = FlightStatus.WAITING_FOR_STARTUP_AND_PUSHBACK;
            }
        }
    }
    
    private String[] extract( String[] data, String search, int length ) {
        List<String> infos = new ArrayList<>();
        boolean act = false;
        for ( int i = 0; i < data.length; i++ ) {
            String d = data[ i ];
            
            if ( act ) {
                infos.add( d );
                length--;
                
                if(length == 0) {
                    break;
                }
            }
            
            if ( d.equals( search ) ) {
                act = true;
            }
        }
        
        return infos.toArray( new String[ infos.size() ] );
    }
    
    private String callbackToVoiceString() {
        StringBuilder str = new StringBuilder();
        if ( this.callsign.startsWith( "AUA" ) ) {
            str.append( "austrian_h" );
        }
        
        str.append( " " );
        
        String[] numberToWord = new String[]{ "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
        for ( char c : this.callsign.toCharArray() ) {
            if ( Character.isDigit( c ) ) {
                int i = ( int ) c - 48;
                str.append( numberToWord[ i ] ).append( "_h " );
            }
        }
        
        return str.substring( 0, str.length() - 1 );
    }
    
    
}
