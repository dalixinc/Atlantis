package com.dalixinc.objects;

import com.dalixinc.main.GamePanel;
import com.dalixinc.main.eGAME_STATE;
import com.dalixinc.utils.UtilFunctions;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    GamePanel gamePanel;
    UtilFunctions utilFunctions = new UtilFunctions();
    public BufferedImage image;
    public String name;
    public boolean collision = false;  // For tiles
    public boolean objectCollision = false; // For objects
    public boolean playerCollision = false; // For player
    public boolean creatureCollision = false; // For creatures
    public boolean triganCollision = false; // For trigan
    public int worldX, worldY;
    public int screenX, screenY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); //   TODO 48 is the default size of the object - shouldn't be hardcoded
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public int speed;
    public int vSpeed, hSpeed;
    public double width, height;
    boolean imageBasedOnSuppliedSize = true;

    // SPRITE INFO
    public int spriteframecount = 0;

    public int spriteInterval = 0;

    public int spriteNum = 1;
    public int spriteCounter = 0;

    // DEBUG
    boolean showCollisionRect = false;

    protected GameObject() {
        // This constructor should not be used
    }

    public abstract boolean interraction() ;  // This method should be overridden by the subclass

    public void update() {
        // This method should be overridden by the subclass
    }

    public void  draw(Graphics2D g2d) {
        // This method should be overridden by the subclass
    }
}
