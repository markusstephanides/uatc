package com.digotsoft.uatc.ui;

import com.digotsoft.uatc.Game;
import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.*;
import java.awt.Font;

public class Button extends UIElement {
    
    @Getter
    @Setter
    private Color foreColor;
    @Getter
    @Setter
    private Color backColor;
    @Getter
    @Setter
    private Color borderColor;
    
    private String text;
    private TrueTypeFont font;
    private int textWidth;
    private int textHeight;
    private Runnable clickRunnable;
    
    public Button( Game game, String text, float x, float y, float width, float height, boolean active ) {
        super( game, x, y, width, height );
        this.foreColor = new Color( Color.white );
        this.backColor = new Color( Color.blue );
        this.borderColor = new Color( Color.white );
        this.font = new TrueTypeFont( new java.awt.Font( "Arial", Font.BOLD, 15 ), true );
        this.setText( text );
        this.setState( active );
    }
    
    private void setText( String text ) {
        this.text = text;
        this.textWidth = this.font.getWidth( this.text );
        this.textHeight = this.font.getHeight( this.text );
    }
    
    @Override
    public void render( GameContainer container, Graphics g ) throws SlickException {
        if(this.x == Integer.MAX_VALUE) this.x = container.getWidth() - this.getWidth();
        if(this.y == Integer.MAX_VALUE) this.y = container.getHeight() - this.getHeight();
        
        g.setColor( this.backColor );
        g.fillRect( this.getX(), this.getY(), this.getWidth(), this.getHeight() );
        g.setColor( this.borderColor );
        g.drawRect( this.getX(), this.getY(), this.getWidth(), this.getHeight() );
        g.setColor( this.foreColor );
        this.font.drawString( this.getX() + ( this.getWidth() - this.textWidth ) / 2, this.getY() + ( this.getHeight() - this.textHeight ) / 2, this.text );
    }
    
    public void addClickListener(Runnable runnable) {
        this.clickRunnable = runnable;
    }
    
    public void setState( boolean on ) {
        this.backColor = on ? new Color( Color.blue ) : new Color( Color.transparent );
    }
    
    @Override
    public void mouseClicked( int button, int x, int y, int clickCount ) {
        if(this.clickRunnable != null) {
            this.clickRunnable.run();
        }
    }
    
    @Override
    public void update( GameContainer container, int delta ) throws SlickException {
    
    }
    
    
}
