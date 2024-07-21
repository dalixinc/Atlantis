package com.dalixinc.objects;

import com.dalixinc.dialogue.Dialogue;
import com.dalixinc.dialogue.OpeningDialogue;
import com.dalixinc.main.GamePanel;
import com.dalixinc.main.eGAME_STATE;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ObjBeacon extends GameObject{
    private final String[] dialogueLines;

    // ToDo - pull out all common vars to the superclass

    // RELEVANT VARIABLES TO ALL GAME OBJECTS

    public int speed;
    public int vSpeed, hSpeed;
    public double width, height;


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
        this.playerCollision = true;
        this.solidArea = new Rectangle(4, 4, gamePanel.tileSize - 8, gamePanel.tileSize - 8);
        imageBasedOnSuppliedSize = false;

        // DEBUG
        this.showCollisionRect = false;

        //GET THE BEACON IMAGES
        beaconImages[0] = utilFunctions.getSpriteImages("/sprites/objects/", "beacon1a");
        beaconImages[1] = utilFunctions.getSpriteImages("/sprites/objects/", "beacon1b");
        beaconImages[2] = utilFunctions.getSpriteImages("/sprites/objects/", "beacon1c");
        beaconImages[3] = utilFunctions.getSpriteImages("/sprites/objects/", "beacon1d");

        // SETUP SPRITE COUNTER
        spriteframecount = beaconImages.length;
        spriteInterval = gamePanel.FPS / 4; // / 8;
        spriteCounter = 0;
        spriteNum = 0;

        // PROPER ATLANTIS OPENING DIALOGUE
        dialogueLines = new String[]{
            "Hey there, brave challenger!!",
            "We, the peace-loving citizens of the city of\nAtlantis,beg that you help us break the \nshackles of....",
            "The Evil Trigan Empire\n\nWhose grip grows ever tighter!",
            "Your task will not be easy, the Trigans\nhave put many powerful deterrents\nin your path.",
            "And the deep is populated by the most\nfearsome of creatures!",
            "Your first task is to collect the 4 pieces of\nthe Atlantan Amulet - this will offer some\nprotection as you proceed in this task.",
            "Artemis III,\n\nAdjudicant, Assembly of Atlantis"
        };

    }
    @Override public boolean interraction() {

        for (int n = 0; n < 3; n++) {
            gamePanel.playSFX(2);
            utilFunctions.sleep(200);
        }
        utilFunctions.sleep(50);
        gamePanel.gameState = eGAME_STATE.DIALOGUE;
        Dialogue dialog = new OpeningDialogue(dialogueLines, gamePanel);
        dialog.printDialogue();
        gamePanel.gameState = eGAME_STATE.PLAY_GAME;
        boolean removeMe = true; //ToDo: Change to a more general solution
        return removeMe;
    }

    @Override public void update() {

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

    @Override public void draw(Graphics2D g2d) {
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

        if (showCollisionRect) {
            g2d.setColor(Color.RED);
            g2d.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
