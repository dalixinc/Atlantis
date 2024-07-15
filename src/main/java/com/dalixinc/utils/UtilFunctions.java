package com.dalixinc.utils;

import javax.imageio.ImageIO;
import java.awt.*;
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

    //TRANSFORM AN IMAGE TO A SCALED IMAGE
    public BufferedImage scaledImage(BufferedImage rawImage, int newWidth, int newHeight) {

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, rawImage.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(rawImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return scaledImage;
    }
}
