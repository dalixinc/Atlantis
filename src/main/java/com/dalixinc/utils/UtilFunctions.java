package com.dalixinc.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UtilFunctions {

    // BUILD A REGULAR SCALABLE IMAGE
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

    // SLEEP FOR SOME ms
    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // BUBBLE ALL NON NULL ARRAY ITEMS IN AN ARRAY TO THE BEGINNING OF THE ARRAY
    public  synchronized void bubbleArray(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                for (int j = i; j < array.length; j++) {
                    if (array[j] != null) {
                        array[i] = array[j];
                        array[j] = null;
                        break;
                    }
                }
            }
        }
    }
}
