package com.digotsoft.uatc.radar;

import com.digotsoft.uatc.util.Converters;
import lombok.Getter;
import org.newdawn.slick.Color;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
@Getter
public class Path {
    
    private double fromX;
    private double fromY;
    private double toX;
    private double toY;
    private Color color;
    
    public Path( String fromX, String fromY, String toX, String toY, Color color ) {
        this.fromX = Converters.parseCoordinate( fromY );
        this.fromY = 360 - Converters.parseCoordinate( fromX );
        this.toX = Converters.parseCoordinate( toY );
        this.toY = 360 - Converters.parseCoordinate( toX );
        this.color = color;
    }
}
