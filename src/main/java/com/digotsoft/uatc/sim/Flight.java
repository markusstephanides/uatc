package com.digotsoft.uatc.sim;

import lombok.Getter;

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
    
    public Flight( String callsign, Aircraft aircraft, Flightplan flightplan ) {
        this.aircraft = aircraft;
        this.callsign = callsign;
        this.flightplan = flightplan;
        this.clearedWaypoint = "";
        this.clearedFL = "";
        this.squawk = "1200";
        this.squawkTx = false;
        this.onGround = false;
        this.x = flightplan.getDepStand().getX();
        this.y = flightplan.getDepStand().getY();
    }
}
