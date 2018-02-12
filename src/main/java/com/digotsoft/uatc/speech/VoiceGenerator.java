package com.digotsoft.uatc.speech;

import java.util.*;

/**
 * @author MaSte
 * @created 04-Feb-18
 */
public class VoiceGenerator {
    
    private Map<String, VoiceDefinition> definitions;
    
    public VoiceGenerator() {
        this.definitions = new HashMap<>();
        this.definitions.put( "male_1", new VoiceDefinition( "male_1" ) );
    }
    
    public void play( String voiceName, String str, Runnable runnable ) {
        System.out.println(str);
        Random random = new Random();
        new Thread(() -> {
            String string = str.toLowerCase();
            VoiceDefinition voiceDefinition = this.definitions.get( voiceName );
            voiceDefinition.startBackground();
            try {
                Thread.sleep( random.nextInt(100) + 100 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
            for ( String word : string.split( " " ) ) {
                voiceDefinition.play( word );
            }
            try {
                Thread.sleep( random.nextInt(300) + 200 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
            voiceDefinition.stopBackground();
            runnable.run();
        }).start();
    }
}
