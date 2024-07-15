package com.dalixinc.objects;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjLifeIndicator extends SuperObject {

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
}
