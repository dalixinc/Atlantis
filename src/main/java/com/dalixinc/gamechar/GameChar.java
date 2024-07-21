package com.dalixinc.gamechar;

import com.dalixinc.main.GamePanel;
import com.dalixinc.utils.UtilFunctions;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameChar {


    // FUNDAMENTAL HELPER CLASSES
    GamePanel gamePanel;

    UtilFunctions utilFunctions = new UtilFunctions();
    // POSITION AND MOVEMENT
    public int x, y;
    public int speed;
    public int vSpeed, hSpeed;
    public double width, height;
    boolean imageBasedOnSuppliedSize = true;

    public BufferedImage right1, right2, right3, right4; // ToDo - not generic enough
    public String direction ;

    public int spriteCounter = 0;
    public  int spriteNum = 1;

    // COLLISION INFO
    public Rectangle solidArea;
    public boolean collisionOn = false;

}
