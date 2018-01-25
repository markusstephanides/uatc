package com.digotsoft.uatc.aviation;

import com.digotsoft.uatc.util.Converters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.newdawn.slick.Color;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
@Getter
@AllArgsConstructor
public class ColorDefintion {
    
    private String defName;
    private Color color;
    
    public ColorDefintion( String defName, String defValue ) {
        this.defName = defName;
        this.color = Converters.intToColor( defValue );
    }
    
  
}
