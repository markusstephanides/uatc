package com.digotsoft.uatc.speech;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;
import org.omg.CORBA.portable.InputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class SpeechReco {
    
    private static LiveSpeechRecognizer recognizer;
    
    public static void init() {
        Configuration configuration = new Configuration();
        
        configuration.setAcousticModelPath( "resource:/edu/cmu/sphinx/models/en-us/en-us" );
        configuration.setDictionaryPath( "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict" );
        configuration.setLanguageModelPath( "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin" );
        configuration.setGrammarPath( "resource:/data/grammars" );
        configuration.setGrammarName( "grammar" );
        configuration.setUseGrammar( true );
        
        try {
            recognizer = new LiveSpeechRecognizer( configuration );
            recognizer.startRecognition( false );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    
    public static AtomicBoolean read( SpeechRecoCallable callable ) {
        AtomicBoolean atomicBoolean = new AtomicBoolean( true );
        new Thread( () -> {
            StringBuilder resultStr = new StringBuilder();
            while ( atomicBoolean.get() ) {
                SpeechResult result = recognizer.getResult();
                for ( WordResult wordResult : result.getWords() ) {
                
                }
                if ( result != null )
                    resultStr.append( result.getHypothesis() );
            }
            
            System.out.println( "Stop. Result: " + resultStr.toString() );
            callable.call( resultStr.toString() );
            
        } ).start();
        
        return atomicBoolean;
    }
    
}
