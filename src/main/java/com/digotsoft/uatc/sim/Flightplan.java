package com.digotsoft.uatc.sim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author MaSte
 * @created 28-Jan-18
 */
@AllArgsConstructor
@Getter @Setter
public class Flightplan {
    
    private String depAirport;
    private String destAirport;
    private Stand depStand;
    private Flightrule flightRule;
    private String route;
    
}