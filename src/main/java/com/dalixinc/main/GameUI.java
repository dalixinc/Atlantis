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
    public String currentDialogue = "";
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

        //this.g2d = graphics2d;  //TODO: Not certain this is required

        //DEFAULT FONT AND COLOUR
        graphics2d.setFont(arial_40);
        graphics2d.setColor(Color.WHITE);

        if (gamePanel.gameState == eGAME_STATE.PLAY_GAME) {
            drawPlayScreen(graphics2d);
        } else if (gamePanel.gameState == eGAME_STATE.PAUSE_GAME) {
            drawPauseScreen(graphics2d);
        } else if (gamePanel.gameState == eGAME_STATE.DIALOGUE) {
            drawDialogueScreen(graphics2d);
        }

    }
    public void drawPlayScreen(Graphics2D graphics2d) {

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
            // DEFAULT FONT AND COLOUR
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

    private void drawPauseScreen(Graphics2D graphics2d) {
        graphics2d.setColor(Color.WHITE);
        graphics2d.setFont(ariel_80B);
        String text = "PAUSED";
        int x = getXforCenteredText(text, graphics2d);
        int y = gamePanel.screenHeight / 2;
        graphics2d.drawString(text, x, y);
    }

    private void drawDialogueScreen(Graphics2D g2d) {

        // WINDOW
        int x = gamePanel.tileSize * 2;
        int y = gamePanel.tileSize / 2;
        int width = gamePanel.screenWidth - gamePanel.tileSize * 4;
        int height = gamePanel.tileSize * 4;
        drawSubWindow(g2d, x, y, width, height);

        // DIALOGUE
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 28F));
        x += gamePanel.tileSize; // /2;
        y += gamePanel.tileSize; // /2;

        for (String line : currentDialogue.split("\n")) {
            g2d.drawString(line, x, y);
            y += g2d.getFontMetrics().getHeight();
        }

/*        graphics2d.setColor(Color.WHITE);
        graphics2d.setFont(ariel_80B);
        String text = "DIALOGUE";
        int x = getXforCenteredText(text, graphics2d);
        int y = gamePanel.screenHeight / 2;
        graphics2d.drawString(text, x, y);*/
    }

    public void drawSubWindow(Graphics2D graphics2d, int x, int y, int width, int height) {

        Color windowBG = new Color(0, 0, 0, 220);
        graphics2d.setColor(windowBG);
        graphics2d.fillRoundRect(x, y, width, height, 35, 35);
        System.out.println("Drawing SubWindow: Color is: " + windowBG.toString());

        Color windowBorder = new Color(255, 255, 255, 255);
        graphics2d.setColor(windowBorder);
        graphics2d.setStroke(new BasicStroke(5));
        graphics2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
        /*graphics2d.setColor(Color.BLACK);
        graphics2d.fillRect(x, y, width, height);
        graphics2d.setColor(Color.WHITE);
        graphics2d.drawRect(x, y, width, height);*/
    }

    // ToDO: Move this to a utility class
    private int getXforCenteredText(String text, Graphics2D graphics2d) {
        int textLength = (int) graphics2d.getFontMetrics().getStringBounds(text, graphics2d).getWidth();
        return gamePanel.screenWidth / 2 - textLength / 2;
    }
}
