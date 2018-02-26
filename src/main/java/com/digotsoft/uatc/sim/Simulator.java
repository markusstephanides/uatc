package com.digotsoft.uatc.sim;

import com.digotsoft.uatc.scenes.GameScene;
import com.digotsoft.uatc.speech.VoiceGenerator;
import com.digotsoft.uatc.util.Converters;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.Sys;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author MaSte
 * @created 28-Jan-18
 */
public class Simulator {
    
    private int maxFlights = 5;
    @Getter
    private Airport controllingAirport;
    @Getter
    private List<Flight> flights;
    private List<String> occupiedGates;
    private VoiceGenerator voiceGenerator;
    private Queue<WaitingTransmission> transmissionsQueue;
    @Setter private boolean planesMayTx = true;
    private GameScene gameScene;
    
    @Getter
    private long tick;
    private long startMs = 0;
    private final int ticksPerSecond = 20;
    private final long lastTxWaitTicks = 20 * 5;
    private long lastTxTick;
    
    private Flight maySpeak;
    
    public Simulator( GameScene gameScene, Airport airport ) {
        this.controllingAirport = airport;
        this.voiceGenerator = new VoiceGenerator();
        this.flights = new ArrayList<>();
        this.occupiedGates = new ArrayList<>();
        this.transmissionsQueue = new LinkedList<>();
        this.gameScene = gameScene;
    }
    
    public void transmit( Flight flight, String voice, String line, boolean waitForResponse ) {
        if ( ! this.planesMayTx && flight != this.maySpeak ) {
            System.out.println(flight.getCallsign() + " wait");
            this.transmissionsQueue.add( new WaitingTransmission( voice, line, null, waitForResponse, flight ) );
            return;
        }
        
        this.gameScene.setRxButton( true );
        this.lastTxTick = this.tick;
        this.planesMayTx = false;
        this.voiceGenerator.play( voice, line, () -> {
            // runnable.run();
            this.gameScene.setRxButton( false );
            if ( ! waitForResponse ) this.planesMayTx = true;
            else this.maySpeak = flight;
        } );
    }
    
    public void finishTx() {
        this.maySpeak = null;
        this.planesMayTx = true;
    }
    
    public void update() {
        if ( System.currentTimeMillis() - startMs >= 1000 / ticksPerSecond ) {
            this.tick++;
            this.startMs = System.currentTimeMillis();
        }
        
        if ( this.planesMayTx && ( this.tick - this.lastTxTick ) >= this.lastTxWaitTicks && this.transmissionsQueue.size() > 0 ) {
            System.out.println("ok ");
            WaitingTransmission waitingTransmission = this.transmissionsQueue.poll();
            this.transmit( waitingTransmission.getFlight(), waitingTransmission.getVoice(), waitingTransmission.getLine(), waitingTransmission.isWaitForResponse() );
        }
        
        for ( Flight flight : this.flights ) {
            try {
                flight.update(this.tick);
            }
            catch(Exception ex) {
                return;
            }
        }
    }
    
    public void generateFlights() {
        for ( int i = 0; i < this.maxFlights; i++ ) {
            // generate flight
            Airline airline = StaticData.chooseAirline();
            Aircraft aircraft = airline.getAircrafts().get( 0 );
            String callsign = StaticData.generateRandomCallsign( airline );
            // find dep and dest
            Airport dep = this.controllingAirport;
            Airport arr = StaticData.getRandomAirport( dep.getIcao() );
            String cruiseSpeed = "N250";
            Stand stand = getAvailableStand( aircraft, Simulator.this.controllingAirport );
            Simulator.this.occupiedGates.add( dep + ":" + stand.getName() );
            String route = DynamicData.findRoute( dep.getIcao(), arr.getIcao());
            // cruise fl and cruise speed
            String cruiseFL = "FL" + String.valueOf( 140 + ( route.split( " " ).length * 20 ) );
                
            Flight flight = new Flight( this, callsign, aircraft, new Flightplan( dep, arr, stand, Flightrule.IFR, route, cruiseFL, cruiseSpeed ), FlightStatus.STARTED_AT_STAND );
            this.flights.add( flight );
            System.out.println( "Added flight " + callsign + " from " + dep.getIcao() + " to " + arr.getIcao() + " via " + flight.getFlightplan().getRoute() );

        }
    }
    
    private Stand getAvailableStand( Aircraft aircraft, Airport airport ) {
        Random random = new Random();
        // TODO What happens if all gates are occupied?
        while ( true ) {
            Stand stand = airport.getStands().get( random.nextInt( airport.getStands().size() ) );
            if ( ! this.occupiedGates.contains( airport.getIcao() + ":" + stand.getName() ) && stand.getCat().equals( aircraft.getCategory() ) ) {
                this.occupiedGates.add( airport.getIcao() + ":" + stand.getName() );
                return stand;
            }
        }
    }
    
    public void processATCTX( String result ) {
        
        String[] splitted = result.split( " " );
        
        // find the flight
        String callsign = "";
        if ( splitted[ 0 ].equals( "austrian" ) ) callsign = "AUA";
        callsign += Converters.convertNumberToWord( splitted[ 1 ] );
        callsign += Converters.convertNumberToWord( splitted[ 2 ] );
        callsign += Converters.convertNumberToWord( splitted[ 3 ] );
        
        for ( Flight flight : this.flights ) {
            if ( flight.getCallsign().equals( callsign ) ) {
                String[] dest = new String[ splitted.length - 4 ];
                System.arraycopy( splitted, 4, dest, 0, splitted.length - 4 );
                flight.processATCResponse( String.join( " ", dest ) );
                break;
            }
        }
    }
}
