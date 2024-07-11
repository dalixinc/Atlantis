package com.dalixinc.gamechar;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.dalixinc.main.GamePanel;
import com.dalixinc.main.KeyHandler;


public class Player extends GameChar {

    GamePanel gamePanel;
    KeyHandler keyHandler;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 5;
        direction = "down";
    }

    public void getPlayerImage() {
        // Load player images
        String path = "/sprites/diver/";
        try {
            right1 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos1c.png" ) );
            right2 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos2c.png") );
            right3 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos3c.png" ) );
            right4 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos4c.png") );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        boolean animateWhenStill = true;
        /// animateWhenStill= keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed;

        if (animateWhenStill) {
            if (keyHandler.upPressed) {
                direction = "up";
                y -= speed;
            }
            if (keyHandler.downPressed) {
                direction = "down";
                y += speed;
            }
            if (keyHandler.leftPressed) {
                direction = "left";
                x -= speed;
            }
            if (keyHandler.rightPressed) {
                direction = "right";
                x += speed;
            }

            spriteCounter++;
            if (spriteCounter /10 == 0) {
                spriteNum = 1;
            } else if (spriteCounter /10 == 1) {
                spriteNum = 2;
            } else if (spriteCounter /10 == 2) {
                spriteNum = 3;
            } else if (spriteCounter /10 == 3) {
                spriteNum = 4;
            } else if (spriteCounter /10 > 3) {
                spriteCounter = 0;
            }



/*            if (spriteCounter > 10) {
                spriteCounter = 0;
                spriteNum = (spriteNum == 1) ? 2 : 1;
            }*/
        }
    }

    public void draw(Graphics g2d) {
        // g2d.setColor( Color.WHITE );
        // g2d.fillRect( x, y, gamePanel.tileSize, gamePanel.tileSize);
        BufferedImage img = null;

        boolean animateWhenStill = false;

        switch(direction) {
            case "up":
                if (spriteNum == 1) {
                    img = right1;
                } else if (spriteNum == 2) {
                    img = right2;
                } else if (spriteNum == 3) {
                    img = right3;
                } else if (spriteNum == 4) {
                    img = right4;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    img = right1;
                } else if (spriteNum == 2) {
                    img = right2;
                } else if (spriteNum == 3) {
                    img = right3;
                } else if (spriteNum == 4) {
                    img = right4;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    img = right1;
                } else if (spriteNum == 2) {
                    img = right2;
                } else if (spriteNum == 3) {
                    img = right3;
                } else if (spriteNum == 4) {
                    img = right4;
                }
                break;
            case "right":
            if (spriteNum == 1) {
                img = right1;
            } else if (spriteNum == 2) {
                img = right2;
            } else if (spriteNum == 3) {
                img = right3;
            } else if (spriteNum == 4) {
                img = right4;
            }
            break;
        }

        g2d.drawImage(img, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
