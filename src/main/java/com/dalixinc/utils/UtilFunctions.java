package com.dalixinc.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UtilFunctions {

    public  BufferedImage getSpriteImages(String path, String imgName) {
        // Load player images
        BufferedImage img = null;
        System.out.println(path + imgName + ".png");
        try {
            img = ImageIO.read( getClass().getResourceAsStream( path + imgName + ".png" ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
