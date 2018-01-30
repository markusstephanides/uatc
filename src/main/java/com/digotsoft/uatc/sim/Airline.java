package com.digotsoft.uatc.sim;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author MaSte
 * @created 30-Jan-18
 */
@AllArgsConstructor
@Getter
public class Airline {
    
    private String name;
    private String callsign;
    private String callsignShort;
    private List<Aircraft> aircrafts;
    
}
