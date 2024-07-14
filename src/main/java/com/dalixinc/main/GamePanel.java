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
    public final int  scale = 5;               // 3x scale
    public final int tileSize = originalTileSize * scale;  // 48 x 48 pixels

    // BASIC SETUP
    KeyHandler keyHandler = new KeyHandler();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    Thread gameThread;

    // GAMECHARS SETUP
    Player player = new Player(this, keyHandler);
    public Shark[] sharks = new Shark[3];
    //Shark shark = new Shark(this);
    //sharks[0] = shark;
    //Shark shark2 = new Shark(this);
    //Shark shark3 = new Shark(this);
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
        sharks[0] = new Shark(this);
        sharks[0].hSpeed= 2;
        sharks[0].width = sharks[0].left1.getWidth();
        sharks[0].height = sharks[0].left1.getHeight();
        sharks[0].solidArea = new Rectangle(0 + 8, 0, (int)sharks[0].width, (int)sharks[0].height - 8);

        sharks[1] = new Shark(this);
        sharks[1].setGraphics("shark1tgpt_left", "shark1tgpt_right");
        sharks[1].y = sharks[1].y - 400;
        sharks[1].hSpeed = 5;
        sharks[1].solidArea = new Rectangle(0, 0 + 8, (int)sharks[1].width, (int)sharks[1].height - 8);

        sharks[2] = new Shark(this);
        sharks[2].setGraphics("shark_transparentc1left", "shark_transparentc1right");
        sharks[2].y = sharks[2].y - 700;
        sharks[2].hSpeed = 3;
        sharks[2].vSpeed = 1;
        sharks[2].width = sharks[2].left1.getWidth() * 0.75;
        sharks[2].height = sharks[2].left1.getHeight() * 0.75;
        sharks[2].solidArea = new Rectangle(0, 0, (int)sharks[2].width - 8, (int)sharks[2].height);

        //System.out.println("Shark2: " + shark2.toString());

        System.out.println("Setting up game...");
        player.collisionOn = true;
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
        sharks[0].update();
        sharks[1].update();
        sharks[2].update();
        player.update();

    }

    public void paintComponent( Graphics g ) {
        super.paintComponent( g );

        Graphics2D g2d = (Graphics2D) g;

        land.draw(g2d);
        sharks[0].draw(g2d);
        sharks[1].draw(g2d);
        sharks[2].draw(g2d);
        player.draw(g2d);
        // For Test only
/*        g2d.setColor( Color.WHITE );
        g2d.fillRect( playerX, playerY, tileSize, tileSize);*/
        g2d.dispose();

    }
}

