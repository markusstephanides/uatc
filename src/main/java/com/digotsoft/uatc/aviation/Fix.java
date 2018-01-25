package com.digotsoft.uatc.aviation;

import com.digotsoft.uatc.util.Converters;
import lombok.Getter;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
@Getter
public class Fix {
    
    private String name;
    private String fullName;
    private String freq;
    private FixType type;
    private double x;
    private double y;
    
    public Fix( FixType type, String name, String fullName, String freq, String x, String y ) {
        this.type = type;
        this.name = name;
        this.fullName = fullName;
        this.freq = freq;
        this.x = Converters.parseCoordinate( y );
        this.y = 360 - Converters.parseCoordinate( x );
    }
    
    public Fix( FixType type, String name, String x, String y ) {
        this.type = type;
        this.name = name;
        this.x = Converters.parseCoordinate( y );
        this.y = 360 - Converters.parseCoordinate( x );
    }
}
