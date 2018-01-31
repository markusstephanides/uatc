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
            Airline airline = StaticData.chooseAirline();
            Aircraft aircraft = airline.getAircrafts().get( 0 );
            String callsign = StaticData.generateRandomCallsign(airline);
            // find dep and dest
            String dep = this.controllingAirport.getIcao();
            String arr = StaticData.getRandomAirport( dep );
            
            Flight flight = new Flight(callsign, aircraft, new Flightplan( dep, arr, getAvailableStand( this.controllingAirport ), Flightrule.IFR, DynamicData.findRoute(dep, arr)));
            this.flights.add( flight );
            System.out.println("Added flight " + callsign + " from " + dep + " to " + arr + " via " + flight.getFlightplan().getRoute());
        }
    }
    
    private Stand getAvailableStand(Airport airport) {
        return airport.getStands().get( 0 );
    }
    
}
