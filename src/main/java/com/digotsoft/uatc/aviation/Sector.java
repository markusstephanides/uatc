package com.digotsoft.uatc.aviation;

import com.digotsoft.uatc.util.Converters;
import lombok.Getter;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class Sector {
    
    private static List<Sector> loadedSectors;
    
    @Getter
    private String name;
    @Getter
    private String icao;
    @Getter
    private double initCamX;
    @Getter
    private double initCamY;
    @Getter
    private List<ColorDefintion> colorDefintions;
    @Getter
    private List<Fix> fixes;
    @Getter
    private List<Path> paths;
    
    public Sector( String[] parts ) {
        this.colorDefintions = new ArrayList<>();
        this.colorDefintions.add( new ColorDefintion( "LIME", new Color( 191, 255, 0 ) ) );
        this.colorDefintions.add( new ColorDefintion( "Coast", new Color( 205, 179, 139 ) ) );
        this.colorDefintions.add( new ColorDefintion( "taxilane", new Color( 1139300 ) ) );
        this.colorDefintions.add( new ColorDefintion( "DANGER", new Color( 6170562 ) ) );
        this.colorDefintions.add( new ColorDefintion( "rescrict", new Color( 12678579 ) ) );
        this.colorDefintions.add( new ColorDefintion( "building", new Color( 192 ) ) );
        this.colorDefintions.add( new ColorDefintion( "rescrict", new Color( 12678579 ) ) );
        this.colorDefintions.add( new ColorDefintion( "rescrict", new Color( 12678579 ) ) );
        this.fixes = new ArrayList<>();
        this.paths = new ArrayList<>();
        this.parseIVAOSector( parts );
    }
    
    private void parseIVAOSector( String[] parts ) {
        // INFO
        String[] infoBlock = parts[ 0 ].split( "\n" );
        this.name = infoBlock[ 0 ];
        this.icao = infoBlock[ 1 ];
        this.initCamX = Converters.parseCoordinate( infoBlock[ 4 ] );
        this.initCamY = 360 - Converters.parseCoordinate( infoBlock[ 3 ] );
        // DEFINES
        String[] definesBlock = parts[ 1 ].split( "\n" );
        for ( String s : definesBlock ) {
            String[] rawDefinition = s.split( " " );
            this.colorDefintions.add( new ColorDefintion( rawDefinition[ 1 ], rawDefinition[ 2 ] ) );
        }
        
        // VORs
        String[] vorsBlock = parts[ 2 ].split( "\n" );
        parseFixes( vorsBlock, "VOR" );
        // NDBs
        String[] ndbsBlock = parts[ 3 ].split( "\n" );
        parseFixes( ndbsBlock, "NDB" );
        // FIXs
        String[] fixesBlock = parts[ 4 ].split( "\n" );
        parseFixes( fixesBlock, "FIX" );
        
        // GEO
        String[] geoBlock = parts[ 14 ].split( "\n" );
        parsePaths( geoBlock );
    }
    
    private void parsePaths( String[] block ) {
        for ( String s : block ) {
            String[] rawGeoPath = s.split( " " );
            
            try {
                Color color = null;
                for ( ColorDefintion colorDefintion : this.colorDefintions ) {
                    if ( colorDefintion.getDefName().equalsIgnoreCase( rawGeoPath[ 4 ] ) ) {
                        color = colorDefintion.getColor();
                        break;
                    }
                }
                
                if ( color == null ) {
                    color = new Color( Integer.parseInt( rawGeoPath[ 4 ] ) );
                }
                
                this.paths.add( new Path( rawGeoPath[ 0 ], rawGeoPath[ 1 ], rawGeoPath[ 2 ], rawGeoPath[ 3 ], color ) );
            } catch ( Exception e ) {
                throw e;
            }
        }
    }
    
    private void parseFixes( String[] block, String fixType ) {
        for ( String s : block ) {
            s = s.replace( "   ", " " ).replace( "  ", " " ).replace( ";", "" );
            if ( s.equals( " " ) ||  s.equals( "  "  )) continue;
            
            String[] rawFix = s.split( " " );
            try {
                if ( fixType.equals( "FIX" ) ) {
                    this.fixes.add( new Fix( FixType.valueOf( fixType ), rawFix[ 0 ], rawFix[ 1 ], rawFix[ 2 ] ) );
                } else {
                    this.fixes.add( new Fix( FixType.valueOf( fixType ), rawFix[ 0 ], rawFix.length == 5 ? rawFix[ 4 ] : rawFix[ 0 ], rawFix[ 1 ], rawFix[ 2 ], rawFix[ 3 ] ) );
                }
            } catch ( Exception e ) {
                throw e;
            }
        }
    }
    
    public static void loadSectors() {
        loadedSectors = new ArrayList<>();
        loadSector( "LOVV" );
    }
    
    private static void loadSector( String sectorName ) {
        Scanner scanner = new Scanner( Thread.currentThread().getContextClassLoader().getResourceAsStream( "data/sectors/" + sectorName + ".sct" ) );
        StringBuilder data = new StringBuilder();
        List<String> parts = new ArrayList<>();
        boolean defineCleared = false;
        while ( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            if ( ! line.equals( " " ) && ! line.startsWith( ";" ) && ! line.equals( "" ) && ! line
                    .equals( "  " ) && ! line.equals( "[INFO]" ) ) {
                if ( line.startsWith( "[" ) || ( ! defineCleared && line.startsWith( "#define" ) ) ) {
                    parts.add( data.toString() );
                    data.setLength( 0 );
                    
                    if ( ! defineCleared ) {
                        data.append( line ).append( "\n" );
                        defineCleared = true;
                    }
                } else data.append( line ).append( "\n" );
            }
            
        }
        parts.add( data.toString() );
        loadedSectors.add( new Sector( parts.toArray( new String[ 0 ] ) ) );
        scanner.close();
        System.out.println( "Loaded sector " + sectorName );
    }
    
    public static Sector getSector( String name ) {
        for ( Sector loadedSector : loadedSectors ) {
            if ( loadedSector.getIcao().equals( name ) )
                return loadedSector;
        }
        
        return null;
    }
}
