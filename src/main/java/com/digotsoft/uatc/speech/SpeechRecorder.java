package com.digotsoft.uatc.speech;

import javax.sound.sampled.*;
import java.io.File;

/**
 * @author MaSte
 * @created 04-Feb-18
 */
public class SpeechRecorder {
    File wavFile = new File( "speech.wav" );
    
    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    
    // the line from which audio data is captured
    TargetDataLine line;
    
    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat( sampleRate, sampleSizeInBits,
                channels, signed, bigEndian );
        return format;
    }
    
    /**
     * Captures the sound and record into a WAV file
     */
    public void start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info( TargetDataLine.class, format );
            
            // checks if system supports the data line
            if ( ! AudioSystem.isLineSupported( info ) ) {
                System.out.println( "Line not supported" );
                System.exit( 0 );
            }
            line = ( TargetDataLine ) AudioSystem.getLine( info );
            line.open( format );
            line.start();   // start capturing
            
            System.out.println( "Start capturing..." );
            
            AudioInputStream ais = new AudioInputStream( line );
            
            System.out.println( "Start recording..." );
            
            // start recording
            AudioSystem.write( ais, fileType, wavFile );
            
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Closes the target data line to finish capturing and recording
     */
    public void finish() {
        line.stop();
        line.close();
        System.out.println( "Finished" );
    }
    
}
