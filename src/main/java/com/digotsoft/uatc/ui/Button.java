package com.digotsoft.uatc.ui;

import com.digotsoft.uatc.Game;
import org.newdawn.slick.*;

public class Button extends UIElement {

    private Color color;
    private String text;
    private TrueTypeFont font;

    public Button(Game game, String text, float x, float y, float width, float height) {
        super(game, x, y, width, height);
        this.text = text;

        this.font = new TrueTypeFont(new java.awt.Font("Arial", 0, 15), true);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.drawRect( this.getX(), this.getY(), this.getWidth(), this.getHeight());
        //g.setColor();
        this.font.drawString(this.getX(), this.getY(), this.text);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {

    }


}
