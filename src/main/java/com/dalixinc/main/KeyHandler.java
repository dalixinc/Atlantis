package com.dalixinc.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {

    // INSTANCE VARIABLES
    GamePanel gamePanel;

    // KEY PRESSED BOOLEANS
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, escapePressed;
    //DEBUG
    boolean debugToggle = false;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            // Done with a switch statement
            switch (keyCode) {
                case KeyEvent.VK_W:
                    upPressed = true;
                    System.out.println("W key pressed");
                    break;
                case KeyEvent.VK_S:
                    downPressed = true;
                    System.out.println("S key pressed");
                    break;
                case KeyEvent.VK_A:
                    leftPressed = true;
                    System.out.println("A key pressed");
                    break;
                case KeyEvent.VK_D:
                    rightPressed = true;
                    System.out.println("D key pressed");
                    break;
                case KeyEvent.VK_0:
                    System.out.println("0 key pressed - Collision detection on!!");
                    //game   player.collisionOn = true;
                    break;
                case KeyEvent.VK_SPACE:
                    spacePressed = true;
                    gamePanel.playSFX(9);
                    System.out.println("Space key pressed");
                    break;
                case KeyEvent.VK_ESCAPE:
                    escapePressed = true;
                    System.out.println("Escape key pressed");
                    break;
                case KeyEvent.VK_P:
                    System.out.println("P key pressed - PAUSE");
                    if (gamePanel.gameState == eGAME_STATE.PLAY_GAME) {
                            gamePanel.gameState = eGAME_STATE.PAUSE_GAME;
                            new Thread(() -> gamePanel.stopMusic()).start();

                    } else if (gamePanel.gameState == eGAME_STATE.PAUSE_GAME) {
                            gamePanel.gameState = eGAME_STATE.PLAY_GAME;
                            new Thread(() -> gamePanel.playMusic(0)).start();
                    }
                    break;
                // DEBUG TOGGLE
                case KeyEvent.VK_O:
                    System.out.println("O key pressed - DEBUG Toggled");
                    debugToggle = !debugToggle;
                    break;
                default:
                    System.out.println("Unregistered Key pressed");
            }
        }

    public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();

        // Alternative to switch statement - if ladder
        if (keyCode == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (keyCode == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (keyCode == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (keyCode == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }


    //ACCESSOR METHODS

    public boolean isUpPressed() {
        return upPressed;
    }
    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }
    public boolean isRightPressed() {
        return rightPressed;
    }

    //MUTATOR METHODS

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }
    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }


}
