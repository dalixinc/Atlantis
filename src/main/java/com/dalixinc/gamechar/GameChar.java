package com.dalixinc.gamechar;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameChar {

    public int x, y;
    public int speed;
    public int vSpeed, hSpeed;
    public double width, height;
    boolean imageBasedOnSuppliedSize = true;

    public BufferedImage right1, right2, right3, right4;
    public String direction ;

    public int spriteCounter = 0;
    public  int spriteNum = 1;

    // COLLISION INFO
    public Rectangle solidArea;
    public boolean collisionOn = false;

}
