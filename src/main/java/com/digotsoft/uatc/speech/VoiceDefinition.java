package com.digotsoft.uatc.speech;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author MaSte
 * @created 04-Feb-18
 */
@Getter
public class VoiceDefinition {
    
    private String name;
    private Music voiceFile;
    private Sound backgroundFile;
    private Map<String, PositionDefinition> positions;
    
    public VoiceDefinition( String name ) {
        this.name = name;
        this.positions = new HashMap<>();
        this.load();
    }
    
    private void load() {
        Scanner scanner = new Scanner( Thread.currentThread().getContextClassLoader().getResourceAsStream( "data/voices/" + this.name + "/" + this.name + ".vdf" ) );
        while ( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            String word = line.split( ":" )[ 0 ];
            float start = Float.parseFloat( line.split( ":" )[ 1 ] );
            float end = Float.parseFloat( line.split( ":" )[ 2 ] );
            this.positions.put( word, new PositionDefinition( start, end ) );
        }
        
        try {
            this.voiceFile = new Music( Thread.currentThread().getContextClassLoader().getResourceAsStream( "data/voices/" + this.name + "/" + this.name + ".wav" ), ".wav" );
            this.backgroundFile = new Sound( Thread.currentThread().getContextClassLoader().getResourceAsStream( "data/voices/" + this.name + "/" + this.name + "_background.wav" ), ".wav" );
        } catch ( SlickException e ) {
            e.printStackTrace();
        }
    }
    
    public void play( String word ) {
        PositionDefinition positionDefinition = this.positions.get( word );
        if(positionDefinition == null) return;
        this.voiceFile.play();
        this.voiceFile.setPosition( positionDefinition.getStart() );
        
        while ( this.voiceFile.getPosition() < positionDefinition.getEnd() ) {
        
        }
    
        this.voiceFile.stop();
    }
    
    public void startBackground() {
        if ( this.backgroundFile != null ) this.backgroundFile.play();
    }
    
    public void stopBackground() {
        if ( this.backgroundFile != null ) this.backgroundFile.stop();
    }
}

