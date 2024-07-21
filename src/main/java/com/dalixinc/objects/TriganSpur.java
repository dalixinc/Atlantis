package com.dalixinc.objects;

import com.dalixinc.main.AtlantisMain;
import com.dalixinc.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TriganSpur extends GameObject {

    GamePanel gamePanel;
    eTravelDir direction; //ToDo: This must be pulled to Superclass

    private int SCALUS = 2;

    public BufferedImage north, northeast, east, southeast, south, southwest, west, northwest;
    public TriganSpur(GamePanel gamePanel) {
        this(gamePanel, 0, 0, 0, 0, 0, eTravelDir.LEFT);
    }

    public TriganSpur(GamePanel gamePanel, int x, int y) {
        this(gamePanel, x, y, 0, 0, 0, eTravelDir.LEFT);
    }

    public TriganSpur(GamePanel gamePanel, int x, int y, int speed) {
        this(gamePanel, x, y, speed, 0, 0, eTravelDir.LEFT);
    }

    public TriganSpur(GamePanel gamePanel, int x, int y, int speed, int hSpeed, int vSpeed) {
        this(gamePanel, x, y, speed, hSpeed, vSpeed, eTravelDir.LEFT);
    }


    public TriganSpur(GamePanel gamePanel, int x, int y, int speed, int hSpeed, int vSpeed, eTravelDir direction) {

        // VALUES FROM CONSTRUCTOR
        this.gamePanel = gamePanel;
        this.screenX = x;
        this.screenY = y;
        this.speed = speed;
        this.hSpeed = hSpeed;
        this.vSpeed = vSpeed;
        this.direction = direction;

        // DEBUG
        this.showCollisionRect = true;

        // INITIALISE
        setDefaultValues();

        // GET THE SPUR IMAGES
        setGraphics();

        // SETUP SPRITE COUNTER
        spriteframecount = eImage.values().length;  //spurImages.length;
        spriteInterval = gamePanel.FPS / 3; // / 8;
        spriteCounter = 0;
        spriteNum = 0;
    }

    public void setDefaultValues() {
        screenX = (screenX == 0) ? screenX = AtlantisMain.ACTUAL_WIDTH - 100 : screenX;
        screenY = (screenY == 0) ? screenY = AtlantisMain.ACTUAL_HEIGHT- 100 : screenY;
        speed = (speed == 0) ? 2 : speed;
        hSpeed = (hSpeed == 0) ? 2 : hSpeed;
        vSpeed =  (vSpeed == 0) ? 2 : vSpeed;
        //width = imageBasedOnSuppliedSize ? left1.getWidth() : gamePanel.tileSize;
        //height = imageBasedOnSuppliedSize ? left1.getHeight() : gamePanel.tileSize;
        width =gamePanel.tileSize;  // The default
        height = gamePanel.tileSize; // The default
        direction = eTravelDir.LEFT;
        this.playerCollision = true;
        imageBasedOnSuppliedSize = true;
        if (imageBasedOnSuppliedSize) {
            solidArea = new Rectangle(4, 4, 32 * SCALUS - 8, 32 * SCALUS - 8);
        } else {
            solidArea = new Rectangle(8, 8, gamePanel.tileSize - 12, gamePanel.tileSize - 12);
        }
        //this.solidArea = new Rectangle(4, 4, gamePanel.tileSize - 8, gamePanel.tileSize - 8);
        ////imageBasedOnSuppliedSize = true;
    }
    public void setGraphics() {
        String path = "/sprites/trigans/spur/";

        //this.north = ImageIO.read(AtlantisMain.class.getResourceAsStream("/sprites/trigans/spur/" + spur_n + ".png"));
        this.north = utilFunctions.getSpriteImages(path, "spur_n");  //ToDo: This could be a loop
        this.northeast = utilFunctions.getSpriteImages(path, "spur_ne");
        this.east = utilFunctions.getSpriteImages(path, "spur_e");
        this.southeast = utilFunctions.getSpriteImages(path, "spur_se");
        this.south = utilFunctions.getSpriteImages(path, "spur_s");
        this.southwest = utilFunctions.getSpriteImages(path, "spur_sw");
        this.west = utilFunctions.getSpriteImages(path, "spur_w");
        this.northwest = utilFunctions.getSpriteImages(path, "spur_nw");

    }

    @Override
    public boolean interraction() {
        System.out.println("HIT TRIGAN SPUR!!!");
        // System.exit(1);
        gamePanel.playSFX(6);
        return false;
    }

    @Override
    public void update() {

        // UPDATE SPRITE IMAGE
        spriteCounter++;
        if (spriteCounter > spriteInterval) {
            spriteCounter = 0;
            spriteNum++;
            if (spriteNum >= spriteframecount) {
                spriteNum = 0;
            }
        }

        // MOVE SPUR
        if (screenX <= 0) {
            direction = eTravelDir.RIGHT;
            hSpeed *= -1;
        } else if (screenX >= AtlantisMain.ACTUAL_WIDTH - gamePanel.tileSize) {
            direction = eTravelDir.LEFT;
            hSpeed *= -1;
        }

        if (screenY <= 0) {
            vSpeed *= -1;
        } else if (screenY >= AtlantisMain.ACTUAL_HEIGHT - gamePanel.tileSize) {
            vSpeed *= -1;
        }

        screenX -= hSpeed;
        screenY -= vSpeed;
    }

    @Override
    public void draw(Graphics2D g2d) {
        // ToDo - The switch case solution must go!!
        BufferedImage img = null;

        switch (spriteNum) {
            case 0:
                img = north;
                break;
            case 1:
                img = northeast;
                break;
            case 2:
                img = east;
                break;
            case 3:
                img = southeast;
                break;
            case 4:
                img = south;
                break;
            case 5:
                img = southwest;
                break;
            case 6:
                img = west;
                break;
            case 7:
                img = northwest;
                break;
        }

        if (imageBasedOnSuppliedSize) {
            g2d.drawImage(img, screenX, screenY, img.getWidth() * SCALUS, img.getHeight() * SCALUS,null);
        } else {
            g2d.drawImage(img, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }

        if (showCollisionRect) {
            g2d.setColor(Color.RED);
            g2d.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    private enum eImage {
        NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    private enum eTravelDir {
        LEFT, RIGHT
    }
}
