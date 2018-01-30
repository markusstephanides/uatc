package com.digotsoft.uatc.ui;

import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.scenes.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUI extends Renderable {
    
    private Map<String, UIElement> elements;
    
    public GUI( Game game ) {
        super( game );
        
        this.elements = new HashMap<>();
    }
    
    public void addElement( String name, UIElement element ) {
        this.elements.put( name, element );
    }
    
    public UIElement getElement( String name ) {
        return this.elements.get( name );
    }
    
    public Button getButtonElement( String name ) {
        return ( Button ) this.elements.get( name );
    }
    
    @Override
    public void render( GameContainer container, Graphics g ) throws SlickException {
        for ( UIElement element : this.elements.values() ) {
            element.render( container, g );
        }
    }
    
    @Override
    public void mouseClicked( int button, int x, int y, int clickCount ) {
        for ( UIElement element : this.elements.values() ) {
            if ( x >= element.getX() && x <= element.getX() + element.getWidth() &&
                    y >= element.getY() && y <= element.getY() + element.getHeight() ) {
                element.mouseClicked( button, x, y, clickCount );
            }
        }
    }
    
    @Override
    public void update( GameContainer container, int delta ) throws SlickException {
        for ( UIElement element : this.elements.values() ) {
            element.update( container, delta );
        }
    }
}
