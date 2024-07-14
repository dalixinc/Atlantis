package com.dalixinc.main;

import com.dalixinc.objects.ObjLifeIndicator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class GameUI {

    // AWARENESS FIELDS
    GamePanel gamePanel;
    Font arial_40, ariel_80B;
    BufferedImage lifeImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameOver = false;
    double playTime = 0;

    //UTILITY FIELDS
    DecimalFormat df = new DecimalFormat("#.##");  // 2 decimal places ("#0.00")

    //CONSTRUCTOR
    public GameUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        ariel_80B = new Font("Arial", Font.BOLD, 80);
        ObjLifeIndicator life = new ObjLifeIndicator();
        lifeImage = life.image;
    }

    public void showMessage(String message) {
        this.message = message;
        messageOn = true;
    }

    // MAIN FUNCTIONAL METHOD OF THE GameUI CLASS

    public void draw(Graphics2D graphics2d) {

        if (gameOver) {

            gamePanel.stopMusic();
            gamePanel.playSFX(10);
            graphics2d.setFont(arial_40);
            graphics2d.setColor(Color.WHITE);

            String text = "GAME OVER";
            int textLength = (int) graphics2d.getFontMetrics().getStringBounds(text, graphics2d).getWidth();
            int x = gamePanel.screenWidth / 2 - textLength / 2;
            int y = gamePanel.screenHeight / 2 - gamePanel.tileSize * 3;
            graphics2d.drawString(text, x, y);

            text = "Your time is: " + df.format(playTime) + " !!";
            textLength = (int) graphics2d.getFontMetrics().getStringBounds(text, graphics2d).getWidth();
            x = gamePanel.screenWidth / 2 - textLength / 2;
            y = gamePanel.screenHeight / 2 + gamePanel.tileSize * 4;
            graphics2d.drawString(text, x, y);

            graphics2d.setFont(ariel_80B);
            graphics2d.setColor(Color.YELLOW);
            text = "Congratulations!";
            textLength = (int) graphics2d.getFontMetrics().getStringBounds(text, graphics2d).getWidth();
            x = gamePanel.screenWidth / 2 - textLength / 2;
            y = gamePanel.screenHeight / 2 + gamePanel.tileSize * 2;
            graphics2d.drawString(text, x, y);

            gamePanel.gameThread = null;

        } else {
            graphics2d.setColor(Color.WHITE);
            graphics2d.setFont(arial_40);
            graphics2d.drawImage(lifeImage, gamePanel.tileSize / 2 - 32, gamePanel.tileSize / 2 - 32, gamePanel.tileSize , gamePanel.tileSize , null);
            graphics2d.drawString("= " + gamePanel.player.lives, 74 + 8, 65 + 8);
            //graphics2d.drawString("Keys: " + gamePanel.player.hasKey, 20, 50);

            //TIME
            playTime += 1.0 / gamePanel.FPS;
            ///graphics2d.drawString("Time: " + (int) playTime, gamePanel.tileSize * 11, 65);
            graphics2d.drawString("Time: " + df.format(playTime) , gamePanel.tileSize * 11, 65);

            //MESSAGE
            if (messageOn) {
                // graphics2d.setColor(Color.BLACK);
                graphics2d.setFont(graphics2d.getFont().deriveFont(20F));
                graphics2d.drawString(message, gamePanel.tileSize * 7 - 12, gamePanel.tileSize  * 7);

                messageCounter++;
                if (messageCounter > gamePanel.FPS * 2) {
                    messageOn = false;
                    messageCounter = 0;
                }
            }
        }

    }
}
