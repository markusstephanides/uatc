package com.digotsoft.uatc.sim;

import com.digotsoft.uatc.util.Converters;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author MaSte
 * @created 28-Jan-18
 */
@Getter
public class StaticData {
    
    private static List<Airport> airports = new ArrayList<>();
    private static List<Airline> airlines = new ArrayList<>();
    
    
    public static void load() {
        loadAirports();
    }
    
    public static String generateRandomCallsign(Airline airline) {
        Random random = new Random();
        return airline.getCallsignShort() + ( random.nextInt( 899 ) + 100 );
    }
    
    public static Airline chooseAirline() {
        Random random = new Random();
        int index = random.nextInt( airlines.size() );
        return airlines.get( index );
    }
    
    public static Airport getRandomAirport( String except ) {
        Random random = new Random();
        
        if ( except == null ) {
            int index = random.nextInt( airports.size() );
            return airports.get( index );
        } else {
            List<Airport> airportPool = new ArrayList<>();
            for ( Airport airport : airports ) {
                if ( ! airport.getIcao().equals( except ) ) {
                    airportPool.add( airport );
                }
            }
            
            int index = random.nextInt( airportPool.size() );
            return airportPool.get( index );
        }
    }
    
    public static Airport getAirportByICAO( String icao ) {
        for ( Airport airport : airports ) {
            if ( airport.getIcao().equals( icao ) ) {
                return airport;
            }
        }
        
        return null;
    }
    
    public static String getCallsignByShortCS(String cs) {
        for ( Airline airline : airlines ) {
            if(cs.contains( airline.getCallsignShort() )) {
                return airline.getCallsign();
            }
        }
        
        return "";
    }
    
    private static void loadAirports() {
        String sector = "austria";
        
        loadSectorAirports( sector );
        loadSectorAirlines( sector );
    }
    
    private static void loadSectorAirlines( String name ) {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "data/sectors/" + name + "/airlines.xml" );
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse( inputStream );
            doc.getDocumentElement().normalize();
            NodeList airlines = doc.getElementsByTagName( "airline" );
            for ( int a = 0; a < airlines.getLength(); a++ ) {
                Node airlineNode = airlines.item( a );
                
                if ( airlineNode.getNodeType() == Node.ELEMENT_NODE ) {
                    Element airlineElement = ( Element ) airlineNode;
                    String airlineName = airlineElement.getAttribute( "name" );
                    String airlineCallsign = airlineElement.getAttribute( "callsign" );
                    String airlineCallsignShort = airlineElement.getAttribute( "callsignShort" );
                    
                    Airline airline = new Airline( airlineName, airlineCallsign, airlineCallsignShort, new ArrayList<>() );
                    
                    NodeList aircrafts = doc.getElementsByTagName( "aircraft" );
                    for ( int r = 0; r < aircrafts.getLength(); r++ ) {
                        Node runwayNode = aircrafts.item( r );
                        
                        if ( runwayNode.getNodeType() == Node.ELEMENT_NODE ) {
                            Element aircraftElement = ( Element ) runwayNode;
                            String aircraftName = aircraftElement.getAttribute( "name" );
                            String aircraftCategory = aircraftElement.getAttribute( "category" );
                            
                            airline.getAircrafts().add( new Aircraft( aircraftName, aircraftCategory ) );
                        }
                    }
                    
                    StaticData.airlines.add( airline );
                    System.out.println( "Added airline " + airlineName + " to sector " + name );
                }
                
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    
    
    private static void loadSectorAirports( String name ) {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "data/sectors/" + name + "/airports.xml" );
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse( inputStream );
            doc.getDocumentElement().normalize();
            NodeList airports = doc.getElementsByTagName( "airport" );
            for ( int a = 0; a < airports.getLength(); a++ ) {
                Node airportNode = airports.item( a );
                
                if ( airportNode.getNodeType() == Node.ELEMENT_NODE ) {
                    Element airportElement = ( Element ) airportNode;
                    String airportICAO = airportElement.getAttribute( "icao" );
                    String airportName = airportElement.getAttribute( "name" );
                    String airportShortName = airportElement.getAttribute( "shortName" );
                    double camX = Double.parseDouble( airportElement.getAttribute( "intlCamX" ) );
                    double camY = Double.parseDouble( airportElement.getAttribute( "intlCamY" ) );
                    double zoom = Double.parseDouble( airportElement.getAttribute( "intlCamZoom" ) );
    
    
    
                    Airport airport = new Airport( airportICAO, airportName, airportShortName, new ArrayList<>(), new ArrayList<>(), camX, camY, zoom );
                    
                    NodeList runways = doc.getElementsByTagName( "runway" );
                    for ( int r = 0; r < runways.getLength(); r++ ) {
                        Node runwayNode = runways.item( r );
                        
                        if ( runwayNode.getNodeType() == Node.ELEMENT_NODE ) {
                            Element runwayElement = ( Element ) runwayNode;
                            String runwayName = runwayElement.getAttribute( "name" );
                            String runwayCourse = runwayElement.getAttribute( "course" );
                            
                            airport.getRunways().add( new Runway( runwayName, Integer.parseInt( runwayCourse ) ) );
                        }
                    }
                    
                    NodeList stands = doc.getElementsByTagName( "stand" );
                    for ( int s = 0; s < stands.getLength(); s++ ) {
                        Node standNode = stands.item( s );
                        
                        if ( standNode.getNodeType() == Node.ELEMENT_NODE ) {
                            Element standElement = ( Element ) standNode;
                            String standName = standElement.getAttribute( "name" );
                            String standType = standElement.getAttribute( "type" );
                            String standCat = standElement.getAttribute( "cat" );
                            String standN = standElement.getAttribute( "n" );
                            String standE = standElement.getAttribute( "e" );
                            
                            double x = Converters.parseCoordinate( standE );
                            double y = 360 - Converters.parseCoordinate( standN );
                            
                            airport.getStands().add( new Stand( standName, standType, x, y, standCat ) );
                        }
                    }
                    
                    StaticData.airports.add( airport );
                    System.out.println( "Added airport " + airportName + " to sector " + name );
                }
                
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        
        
    }
    
}
