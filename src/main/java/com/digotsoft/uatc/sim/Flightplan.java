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
    
    private Airport depAirport;
    private Airport destAirport;
    private Stand depStand;
    private Flightrule flightRule;
    private String route;
    private String cruiseFL;
    private String cruiseSpeed;
    
}
