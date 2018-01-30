package com.digotsoft.uatc.sim;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MaSte
 * @created 28-Jan-18
 */
public class Simulator {
    
    private int maxFlights = 1;
    @Getter
    private Airport controllingAirport;
    @Getter private List<Flight> flights;
    
    public Simulator(Airport airport) {
        this.controllingAirport = airport;
        this.flights = new ArrayList<>();
    }
    
    public void generateFlights() {
        for(int i = 0; i < maxFlights; i++) {
            // generate flight
            String callsign = StaticData.generateRandomCallsign();
            // find dep and dest
            String dep = this.controllingAirport.getIcao();
            String arr = StaticData.getRandomAirport( dep );
            
            Flight flight = new Flight(callsign, new Flightplan( dep, arr, getAvailableStand( this.controllingAirport ), Flightrule.IFR, "DCT"));
            this.flights.add( flight );
            System.out.println("Added flight " + callsign);
        }
    }
    
    private Stand getAvailableStand(Airport airport) {
        return airport.getStands().get( 0 );
    }
    
}