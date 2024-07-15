package com.dalixinc.gamechar;

import com.dalixinc.main.AtlantisMain;
import com.dalixinc.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Shark extends GameChar {

    public BufferedImage left1, right1;

    int moveCounter = 0;

    GamePanel gamePanel;

    boolean showCollisionRect = false;

    public Shark(GamePanel gamePanel) {
        this (gamePanel, 0, 0, 0, 0, 0, "left");
    }

    public Shark(GamePanel gamePanel, int x, int y) {
        this(gamePanel, x, y, 0, 0, 0, "left");
    }

    public Shark(GamePanel gamePanel, int x, int y, int speed) {
        this(gamePanel, x, y, speed, 0, 0, "left");
    }

    public Shark(GamePanel gamePanel, int x, int y, int speed, int hSpeed, int vSpeed) {
       this(gamePanel, x, y, speed, hSpeed, vSpeed, "left");

    }

    public Shark(GamePanel gamePanel, int x, int y, int speed, int hSpeed, int vSpeed, String direction) {
        this(gamePanel, x, y, speed, hSpeed, vSpeed, direction, "dork_shark2", "dork_shark1");
    }

    public Shark(GamePanel gamePanel, int x, int y, int speed, int hSpeed, int vSpeed, String direction, String leftImg1, String rightImg1) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.hSpeed = hSpeed;
        this.vSpeed = vSpeed;
        this.direction = direction;
        this.left1 = left1;
        this.right1 = right1;
        setDefaultValues();
        if (leftImg1 != null || rightImg1 != null) {
            getSharkImages(leftImg1, rightImg1);
        } else {
            getSharkImage();
        }
    }

    public void setGraphics(String left1, String right1) {
        if (left1 != null || right1 != null) {
            getSharkImages(left1, right1);
        } else {
            getSharkImage();
        }
    }
    public void setLeft1(BufferedImage left1) {
        this.left1 = left1;
    }

    public void setRight1(BufferedImage right1) {
        this.right1 = right1;
    }

    public void setDefaultValues() {
        x = (x == 0) ? x = AtlantisMain.ACTUAL_WIDTH - 100 : x;
        y = (y == 0) ? y = AtlantisMain.ACTUAL_HEIGHT- 100 : y;
        speed = (speed == 0) ? 2 : speed;
        hSpeed = (hSpeed == 0) ? 4 : hSpeed;
        vSpeed =  (vSpeed == 0) ? 2 : vSpeed;
        //width = imageBasedOnSuppliedSize ? left1.getWidth() : gamePanel.tileSize;
        //height = imageBasedOnSuppliedSize ? left1.getHeight() : gamePanel.tileSize;
        width =gamePanel.tileSize;  // The default
        height = gamePanel.tileSize; // The default
        direction = "left";
    }

    private void getSharkImage() {
        // Load shark images
        String path = "/sprites/shark/";
        try {
            left1 = ImageIO.read( getClass().getResourceAsStream( path + "dork_shark2.png" ) );
            right1 = ImageIO.read( getClass().getResourceAsStream( path + "dork_shark1.png") );
            /*right3 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos3c.png" ) );
            right4 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos4c.png") );
*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSharkImages(String leftImg1, String rightImg1) {
        // Load shark images
        String path = "/sprites/shark/";
        try {
            left1 = ImageIO.read( getClass().getResourceAsStream( path + leftImg1 + ".png" ) );
            right1 = ImageIO.read( getClass().getResourceAsStream( path + rightImg1 + ".png") );
            /*right3 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos3c.png" ) );
            right4 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos4c.png") );
*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        moveCounter++;


        if (moveCounter == 2) {
            moveCounter = 0;
            if (x <= 0) {
                direction = "right";
                hSpeed *= -1;
            } else if (x >= AtlantisMain.ACTUAL_WIDTH - gamePanel.tileSize) {
                direction = "left";
                hSpeed *= -1;
            }

            if (y <= 0) {
                vSpeed *= -1;
            } else if (y >= AtlantisMain.ACTUAL_HEIGHT - gamePanel.tileSize) {
                vSpeed *= -1;
            }

            x -= hSpeed;
            y -= vSpeed;
        }
    }

    public void draw(Graphics g2d) {
        // g2d.setColor( Color.WHITE );
        // g2d.fillRect( x, y, gamePanel.tileSize, gamePanel.tileSize);
        BufferedImage img = null;
        if (direction == "left") {
            img = left1;
        } else if (direction == "right") {
            img = right1;
        }

        g2d.drawImage(img, x, y, (int)width, (int)height, null);
        //g2d.drawImage(img, x, y, img.getWidth(), img.getHeight(), null);
        //g2d.drawImage(img, x, y, gamePanel.tileSize, gamePanel.tileSize, null);

        if (showCollisionRect) {
            g2d.setColor(Color.RED);
            g2d.drawRect(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
