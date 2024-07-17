package com.dalixinc.objects;

import com.dalixinc.main.GamePanel;

import java.awt.image.BufferedImage;

public class ObjBeacon extends GameObject{

    // ToDo - pull out all common vars to the superclass

    // RELEVANT VARIABLES TO ALL GAME OBJECTS

    public int screenX, screenY;
    public int worldX, worldY;
    public int speed;
    public int vSpeed, hSpeed;
    public double width, height;
    boolean imageBasedOnSuppliedSize = false;

    BufferedImage[] beaconImages = new BufferedImage[4];




    public ObjBeacon(GamePanel gamePanel) {

        this .gamePanel = gamePanel;
        this.screenX = 680;
        this.screenY = 8;
        this.speed = 0;
        this.vSpeed = 0;
        this.hSpeed = 0;
        this.width = gamePanel.tileSize;
        this.height = gamePanel.tileSize;

        //GET THE BEACON IMAGES
        beaconImages[0] = utilFunctions.getSpriteImages("/sprites/objects/", "beacon1a");
        beaconImages[1] = utilFunctions.getSpriteImages("/sprites/objects/", "beacon1b");
        beaconImages[2] = utilFunctions.getSpriteImages("/sprites/objects/", "beacon1c");
        beaconImages[3] = utilFunctions.getSpriteImages("/sprites/objects/", "beacon1d");

        // SETUP SPRITE COUNTER
        spriteframecount = beaconImages.length;
        spriteInterval = gamePanel.FPS / 4;
        spriteCounter = 0;
        spriteNum = 0;

    }

    public void update() {

        // UPDATE SPRITE
        spriteCounter++;
        if (spriteCounter > spriteInterval) {
            spriteCounter = 0;
            spriteNum++;
            if (spriteNum >= spriteframecount) {
                spriteNum = 0;
            }
        }
    }

    public void draw(java.awt.Graphics2D g2d) {
        BufferedImage img = null;

        switch (spriteNum) {
            case 0:
                img = beaconImages[0];
                break;
            case 1:
                img = beaconImages[1];
                break;
            case 2:
                img = beaconImages[2];
                break;
            case 3:
                img = beaconImages[3];
                break;
        }
        g2d.drawImage(img, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
