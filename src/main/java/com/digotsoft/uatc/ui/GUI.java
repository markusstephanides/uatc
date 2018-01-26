package com.digotsoft.uatc.ui;

import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.scenes.Renderable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class GUI extends Renderable {

    private List<UIElement> elements;

    public GUI(Game game) {
        super(game);

        this.elements = new ArrayList<>();

    }

    public void addElement(UIElement element) {
        this.elements.add(element);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        for (UIElement element : this.elements) {
            element.render(container, g);
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        for (UIElement element : this.elements) {
            element.update(container, delta);
        }
    }
}
