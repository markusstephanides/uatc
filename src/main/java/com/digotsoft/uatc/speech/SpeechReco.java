package com.digotsoft.uatc.speech;

import com.digotsoft.uatc.util.StringCallable;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class SpeechReco {
    
    private static StreamSpeechRecognizer recognizer;
    private static SpeechRecorder speechRecorder;
    
    public static void init() {
        Configuration configuration = new Configuration();
        
        configuration.setAcousticModelPath( "resource:/edu/cmu/sphinx/models/en-us/en-us" );
        configuration.setDictionaryPath( "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict" );
        configuration.setLanguageModelPath( "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin" );
        configuration.setGrammarPath( "resource:/data/grammars" );
        configuration.setGrammarName( "grammar" );
        configuration.setUseGrammar( true );
        
        try {
            recognizer = new StreamSpeechRecognizer( configuration );
            speechRecorder = new SpeechRecorder();
            //recognizer.startRecognition( false );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    
    public static AtomicBoolean read( StringCallable callable ) {
        AtomicBoolean atomicBoolean = new AtomicBoolean( true );
        new Thread( () -> {
            new Thread( speechRecorder::start ).start();
            
            while ( atomicBoolean.get() ) {
            
            }
            speechRecorder.finish();
            try {
                recognizer.startRecognition( new FileInputStream( "speech.wav" ) );
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            }
            
            SpeechResult result = null;
            StringBuilder resultStr = new StringBuilder();
            while ( ( result = recognizer.getResult() ) != null ) {
                resultStr.append( result.getHypothesis() ).append( " " );
            }
    
            recognizer.stopRecognition();
            String finalStr = resultStr.toString();
            finalStr = finalStr.substring( 0, finalStr.length() - 1 );
            
            //recognizer.startRecognition( false );
            //StringBuilder resultStr = new StringBuilder();
//            while ( atomicBoolean.get() ) {
//                SpeechResult result = recognizer.getResult();
//
//                if ( result != null )
//                    resultStr.append( result.getHypothesis() );
//            }
//            recognizer.stopRecognition();
            System.out.println( "Stop. Result: " + finalStr );
            callable.call( finalStr );
        } ).start();
        
        return atomicBoolean;
    }
    
}
