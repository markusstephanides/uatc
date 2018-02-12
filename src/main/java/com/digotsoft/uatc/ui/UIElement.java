package com.digotsoft.uatc.ui;

import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.scenes.Renderable;
import lombok.Getter;

@Getter
public abstract class UIElement extends Renderable {

    protected float x;
    protected float y;
    private float width;
    private float height;

    public UIElement(Game game, float x, float y, float width, float height) {
        super(game);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


}
