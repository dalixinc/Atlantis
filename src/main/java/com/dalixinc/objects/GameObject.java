package com.dalixinc.objects;

import com.dalixinc.main.GamePanel;
import com.dalixinc.utils.UtilFunctions;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameObject {

    GamePanel gamePanel;
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public int screenX, screenY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); //   TODO 48 is the default size of the object - shouldn't be hardcoded
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public int speed;
    public int vSpeed, hSpeed;
    public double width, height;
    boolean imageBasedOnSuppliedSize = true;
    public int spriteframecount = 0;

    public int spriteInterval = 0;

    public int spriteNum = 1;
    public int spriteCounter = 0;

    UtilFunctions utilFunctions = new UtilFunctions();

    public void draw(Graphics2D g2d, GamePanel gamePanel) {
        g2d.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
/*        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        if(     worldX + gamePanel.tileSize > gamePanel.player.worldX -gamePanel.player.screenX  &&
                worldX - gamePanel.tileSize  < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY ) {
            g2d.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }*/
    }

    public void update() {
        // This method should be overridden by the subclass
    }

    public void  draw(Graphics2D g2d) {
        // This method should be overridden by the subclass
    }
}
