package com.digotsoft.uatc.sim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author MaSte
 * @created 28-Jan-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Airport {
    
    private String icao;
    private String name;
    private String shortName;
    private List<Runway> runways;
    private List<Stand> stands;
    private List<Taxiway> taxiways;
    private double x;
    private double y;
    private double zoom;
    
}
