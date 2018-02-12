package com.digotsoft.uatc.util;

import org.newdawn.slick.Color;

import java.util.HashMap;


/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class Converters {
    
    public static Color intToColor( String strVal ) {
        int val = Integer.parseInt( strVal );
        int r = ( val ) & 0xFF;
        int g = ( val >> 8 ) & 0xFF;
        int b = ( val >> 16 ) & 0xFF;
        
        return new Color( r, g, b );
    }
    
    public static String convertNumberToWord(String nw) {
        nw = convSpeechWord( nw );
        HashMap<String, String> map = new HashMap<>();
        map.put("zero", "0");
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        map.put("four", "4");
        map.put("five", "5");
        map.put("six", "6");
        map.put("seven", "7");
        map.put("eight", "8");
        map.put("nine", "9");
        
        return map.get(nw);
    }
    
    public static String convSpeechWord(String str){
        return str.replace( "won", "one").replace("wan", "one" ).replace( "too", "two" ).replace( "to", "two" )
                .replace( "tree", "three" ).replace( "fore", "four" ).replace("ate", "eight").replace( "aid", "eight" ).replace( "niner", "nine" );
    }
    
    public static double parseCoordinate( String strVal ) {
        try{
            boolean longitude = strVal.substring( 0, 1 ).equals( "N" );
            strVal = strVal.substring( 1 ); // Cut off the N or E
            String[] valSplit = strVal.split( "\\." );
    
            int degrees = 0;
            int minutes = 0;
            double seconds = 0;
            
            if(valSplit.length >= 2){
                degrees = Integer.parseInt( valSplit[ 0 ] );
                minutes = Integer.parseInt( valSplit[ 1 ] );
            }
    
            if(valSplit.length >= 3){
                seconds = Integer.parseInt( valSplit[ 2 ] );
            }
    
            if(valSplit.length >= 4){
                seconds = Double.parseDouble( valSplit[ 2 ] + "." + valSplit[ 3 ]  );
            }
    
            double decimal = ( ( minutes * 60.0 ) + seconds ) / ( 60.0 * 60.0 );
            decimal += degrees;
           
            if(longitude) {
                decimal = decimal * Math.PI / 180;
                decimal = Math.log(Math.tan(0.7853981633974483D + 0.5 * decimal));
                decimal = decimal / Math.PI * 180;
            }
            return decimal;
        }
        catch ( Exception e ){
            return 0;
        }
      
    }
    
}
