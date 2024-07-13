package com.dalixinc.main;

import com.dalixinc.gamechar.Player;
import com.dalixinc.gamechar.Shark;
import com.dalixinc.objects.Land;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    private static final int FPS = 120;  // Frames per second
    final int originalTileSize = 16;   // 16 x 16 tiles
    final int  scale = 5;               // 3x scale
    public final int tileSize = originalTileSize * scale;  // 48 x 48 pixels

    // BASIC SETUP
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    // GAMECHARS SETUP
    Player player = new Player(this, keyHandler);
    Shark shark = new Shark(this);
    Shark shark2 = new Shark(this);
    Shark shark3 = new Shark(this);
    Land land = new Land(this);


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

        //SHARK PROPERTIES
        shark.hSpeed= 2;
        shark.width = shark.left1.getWidth();
        shark.height = shark.left1.getHeight();

        shark2.setGraphics("shark1tgpt_left", "shark1tgpt_right");
        shark2.y = shark2.y - 400;
        shark2.hSpeed = 5;

        shark3.setGraphics("shark_transparentc1left", "shark_transparentc1right");
        shark3.y = shark.y - 700;
        shark3.hSpeed = 3;
        shark3.vSpeed = 1;
        shark3.width = shark3.left1.getWidth() * 0.5;
        shark3.height = shark3.left1.getHeight() * 0.5;
        //System.out.println("Shark2: " + shark2.toString());

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
        land.update();
        shark.update();
        shark2.update();
        shark3.update();
        player.update();
    }

    public void paintComponent( Graphics g ) {
        super.paintComponent( g );

        Graphics2D g2d = (Graphics2D) g;

        land.draw(g2d);
        shark.draw(g2d);
        shark2.draw(g2d);
        shark3.draw(g2d);
        player.draw(g2d);
        // For Test only
/*        g2d.setColor( Color.WHITE );
        g2d.fillRect( playerX, playerY, tileSize, tileSize);*/
        g2d.dispose();

    }
}

