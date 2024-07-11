package com.dalixinc.main;

import com.dalixinc.gamechar.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    private static final int FPS = 90;  // Frames per second
    final int originalTileSize = 16;   // 16 x 16 tiles
    final int  scale = 5;               // 3x scale
    public final int tileSize = originalTileSize * scale;  // 48 x 48 pixels

    // BASIC SETUP
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);

    // SET PLAYER'S INITIAL POSITION
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    public void setUpGame() {

        // APPEARS TO IGNORE THIS
        int windowX = AtlantisMain.spectrumWidthPixels * AtlantisMain.SPECTRUM_SCALE_FACTOR;
        int windowY= AtlantisMain.spectrumHeightPixels * AtlantisMain.SPECTRUM_SCALE_FACTOR;
        setPreferredSize(new Dimension(windowX, windowY));

        setBackground(Color.BLUE);
        setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable( true );
        System.out.println("Setting up game...");
    }

    public void startGameThread() {
        gameThread = new Thread( this );
        gameThread.setName("Game Thread");
        gameThread.start();
        System.out.println("Starting game thread...");
    }


    @Override
    public void run() {

        // Timer Vars
        double drawInterval = 1_000_000_000 / FPS;   // 0.01666 seconds (16.7 ms)
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        // "Proof" Vars
        //// long count = 0;
        long timer = 0;
        int drawCount = 0;

        // Game Loop
        while( gameThread != null ) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            //// System.out.println( "Count: " + count++ + "  Delta: " + delta);

            if ( delta >= 1 ) {
                update();
                repaint();
                delta--;
                drawCount++;
                //// System.out.println( "Delta Final: " + delta);
                //// System.exit(0);
            }

            if ( timer >= 1_000_000_000 ) {
                System.out.println( "FPS: " + drawCount );
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {

        // UPDATE GAME STATE
        player.update();
    }

    public void paintComponent( Graphics g ) {
        super.paintComponent( g );

        Graphics2D g2d = (Graphics2D) g;

        player.draw(g2d);
        // For Test only
/*        g2d.setColor( Color.WHITE );
        g2d.fillRect( playerX, playerY, tileSize, tileSize);*/
        g2d.dispose();

    }
}

