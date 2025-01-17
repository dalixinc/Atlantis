package com.dalixinc.objects;

import java.awt.*;

public class ObjLifeIndicator extends GameObject {

    public ObjLifeIndicator() {
        name = "LifeIndicator";
        //try {
            //image = ImageIO.read(getClass().getResourceAsStream("/sprites/objects/lifeIndicator.png"));
            image = utilFunctions.getSpriteImages("/sprites/objects/", "lifeIndicator");
            image = utilFunctions.scaledImage(image, 48, 48);
       // } catch (IOException e) {
        //    e.printStackTrace();
       // }
        collision = false;
    }

    @Override
    public boolean interraction() {
        return false;
    }

    @Override
    public void update() {
        // Do nothing
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Do nothing
    }
}
