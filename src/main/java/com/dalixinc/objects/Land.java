package com.dalixinc.objects;

import com.dalixinc.main.AtlantisMain;
import com.dalixinc.main.GamePanel;
import com.dalixinc.utils.UtilFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Land {

    int x, y, lowY, speed;
    String direction;
    BufferedImage lowland;
    BufferedImage[] landImages;
    String path = "sprites/land/";
    UtilFunctions utilFunctions = new UtilFunctions();
    GamePanel gamePanel;

    private final int[] landX = {1, 1, 2, 1, 3, 2, 1, 2, 2, 1};

    public Land(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setDefaultValues();
        getLandImages();
    }

    public void setDefaultValues() {
        x = (x == 0) ? x = 0: x;
        y = (y == 0) ? y = AtlantisMain.ACTUAL_HEIGHT- 96 : y;
        lowY = (lowY == 0) ? lowY = AtlantisMain.ACTUAL_HEIGHT- 64 : lowY;

        speed = (speed == 0) ? 2 : speed;

        direction = "left";
    }

    public void getLandImages() {
        landImages = new BufferedImage[10];
        for (int i = 0; i < landX.length; i++) {
            //landImages[i] = utilFunctions.getSpriteImages  (path, "land" + landX[i] + ".png");
            String imgName = "land" + landX[i];
            landImages[i] = utilFunctions.getSpriteImages  ("/sprites/land/",  imgName);
        }
        lowland = utilFunctions.getSpriteImages("/sprites/land/", "lowland1");
        System.out.println("Land images loaded");
    }

    public void update() {
        if (direction.equals("left")) {
            x -= speed;
            //JOptionPane.showConfirmDialog(null, "x = " + x + ", AtlantisMain.ACTUAL_WIDTH = " + AtlantisMain.ACTUAL_WIDTH);
            if (x <= (-2560 + AtlantisMain.ACTUAL_WIDTH - 1)) {
                x = 0;
            }
        }
    }

    public void draw(Graphics g2d) {
        // g2d.setColor( Color.WHITE );
        // g2d.fillRect( x, y, gamePanel.tileSize, gamePanel.tileSize);
        BufferedImage img = null;
        int tempX = x;
        for (int i = 0; i <  landImages.length; i++) {
            if (direction == "left") {
                img = landImages[i];
                g2d.drawImage(img, tempX, y, 256, 32, null);
                g2d.drawImage(lowland, tempX, lowY, 256, 32, null);
                //g2d.drawImage(img, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
                System.out.println("Drawing land image: x = " + tempX + ", y = " + y + ", i = " + i);
                tempX += 256;
            } else if (direction == "right") {
                // Not yet implemented
                System.out.println("Right direction not yet implemented");
            }
        }
    }
}
