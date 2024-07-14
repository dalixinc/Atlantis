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
    public int lives = 0;

    // DEBUG
    boolean showCollisionRect = false;

    public volatile boolean isDead = false;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        setDefaultValues();
        getPlayerImage();

        //width = gamePanel.tileSize;
        //height = gamePanel.tileSize;

        width = 48.0 * 2;
        height = 32.0 * 2;

        //width = 48.0 * gamePanel.scale;
        //height = 32.0 * gamePanel.scale;
        imageBasedOnSuppliedSize = true;

        solidArea = new Rectangle(0, 0, (int)width, (int)height);
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 5;
        direction = "down";
        lives = 5;
    }

    public void getPlayerImage() {
        // Load player images
        String path = "/sprites/diver/";
        try {
            right1 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos1c.png" ) );
            right2 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos2c.png") );
            right3 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos3c.png" ) );
            right4 = ImageIO.read( getClass().getResourceAsStream( path + "diver1_pos4c.png") );
            System.out.printf("Diver size: - Width %s  - Height %s\n", right1.getWidth(), right1.getHeight());
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

            // CHECK TILE COLLISION
            ///collisionOn = false; // Not yet used
            if (collisionOn) {
                for (int n = 0; n < gamePanel.sharks.length; n++) {

                    int tempMeX = (int)(solidArea.getX() + x);
                    int tempMeY = (int)(int)(solidArea.getY() + y);
                    Rectangle rMe = new Rectangle(tempMeX, tempMeY, (int)solidArea.getWidth(), (int)solidArea.getHeight());

                    int tempX = (int)(gamePanel.sharks[n].x + gamePanel.sharks[n].solidArea.getX());
                    int tempY = (int)(gamePanel.sharks[n].y + gamePanel.sharks[n].solidArea.getY());
                    int tempW = (int)(gamePanel.sharks[n].solidArea.getWidth());
                    int tempH = (int)(gamePanel.sharks[n].solidArea.getHeight());
                    Rectangle rThem = new Rectangle(tempX, tempY, tempW, tempH);

                    if (!isDead && gamePanel.collisionChecker.checkCollision(rMe, rThem)) {
                        isDead = true;
                        lives--;
                        gamePanel.stopMusic();
                        gamePanel.playSFX(8);

                    new Thread() {
                        @Override
                        public void run() {

                            try {
                               Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            if (lives == 0)
                                gamePanel.gameUI.gameOver = true;
                            x = (int)(800 * Math.random());
                            y = (int)(800 * Math.random());
                            isDead = false;
                            gamePanel.playMusic(0);
                        }
                    }.start();

                    //Usiing anonymous threads
                    /* new Thread(new Runnable() {
                            public  void run() {
                                System.out.println("Runner");
                            }
                        }).start();

                        new Thread() {
                            public  void run() {
                                System.out.println("Runner");
                            }
                        }.start(); */
                    }
                }
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

       // g2d.drawImage(img, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
        System.out.println("ARE YOU DEAD???? " + isDead);
        if (isDead) {
            g2d.setColor(Color.RED);
            g2d.fillRect(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);
            g2d.setColor(Color.WHITE);
            g2d.drawString("You are DEAD!!", x + 5, y + 32);
            System.out.println("YOU ARE DEAD!!!");

        } else {
            g2d.drawImage(img, x, y, (int) width, (int) height, null);
        }

        if (showCollisionRect) {
            g2d.setColor(Color.RED);
            g2d.drawRect(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
