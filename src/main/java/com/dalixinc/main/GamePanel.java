package com.dalixinc.main;

import com.dalixinc.gamechar.Player;
import com.dalixinc.gamechar.Shark;
import com.dalixinc.objects.Land;
import com.dalixinc.utils.Sound;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    public static final int FPS = 120;  // Frames per second
    final int originalTileSize = 16;   // 16 x 16 tiles
    public final int  scale = 5;               // 3x scale
    public final int tileSize = originalTileSize * scale;  // 48 x 48 pixels
    final int maxScreenCol = 16;                    // 16 columns - 16 x 48 = 768 pixels   - Needs to be updated for Atlantis
    final int maxScreenRow = 12;                    // 12 rows - 12 x 48 = 576 pixels - Needs to be updated for Atlantis
    public  int screenWidth = tileSize * maxScreenCol; // 768 pixels - Needs to be updated for Atlantis
    public  int screenHeight = tileSize * maxScreenRow; // 576 pixels - - Needs to be updated for Atlantis

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final  int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // SOUND SETTINGS
    Sound music = new Sound();
    Sound sfx = new Sound();

    // BASIC SETUP
    KeyHandler keyHandler = new KeyHandler();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public GameUI gameUI = new GameUI(this);
    Thread gameThread;

    // GAMECHARS SETUP
    Player player = new Player(this, keyHandler);
    public Shark[] sharks = new Shark[10];
    Land land = new Land(this);


    // SET PLAYER'S INITIAL POSITION
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

   public GamePanel() {

        // APPEARS TO IGNORE THIS
        int windowX = AtlantisMain.spectrumWidthPixels * AtlantisMain.SPECTRUM_SCALE_FACTOR;
        int windowY= AtlantisMain.spectrumHeightPixels * AtlantisMain.SPECTRUM_SCALE_FACTOR;
        setPreferredSize(new Dimension(windowX, windowY));

        this.setBackground(Color.BLUE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        // SET SCREEN DETAILS FOR ATLANTIS
       screenWidth = AtlantisMain.spectrumWidthPixels * AtlantisMain.SPECTRUM_SCALE_FACTOR;
       screenHeight = AtlantisMain.spectrumHeightPixels * AtlantisMain.SPECTRUM_SCALE_FACTOR;
    }
    public void setUpGame() {

        //SHARK PROPERTIES
        sharks[0] = new Shark(this);
        sharks[0].hSpeed= 2;
        sharks[0].width = sharks[0].left1.getWidth();
        sharks[0].height = sharks[0].left1.getHeight();
        sharks[0].solidArea = new Rectangle(0 + 24, 0 + 8, (int)sharks[0].width - 32, (int)sharks[0].height - 16);

        sharks[1] = new Shark(this);
        sharks[1].setGraphics("shark1tgpt_left", "shark1tgpt_right");
        sharks[1].y = sharks[1].y - 400;
        sharks[1].hSpeed = 5;
        sharks[1].solidArea = new Rectangle(0 ,  0 + 16, (int)sharks[1].width - 8, (int)sharks[1].height - 32);

        sharks[2] = new Shark(this);
        sharks[2].setGraphics("shark_transparentc1left", "shark_transparentc1right");
        sharks[2].y = sharks[2].y - 700;
        sharks[2].hSpeed = 3;
        sharks[2].vSpeed = 1;
        sharks[2].width = sharks[2].left1.getWidth() * 0.75;
        sharks[2].height = sharks[2].left1.getHeight() * 0.75;
        sharks[2].solidArea = new Rectangle(0 + 32 , 0 + 16, (int)sharks[2].width - 48, (int)sharks[2].height - 32);

        sharks[3] = new Shark(this);
        sharks[3].setGraphics("shark_transparentc1left", "shark_transparentc1right");
        sharks[3].y = sharks[2].y + 700;
        sharks[3].hSpeed = 1;
        sharks[3].vSpeed = 1;
        sharks[3].width = sharks[3].left1.getWidth();
        sharks[3].height = sharks[3].left1.getHeight();
        sharks[3].solidArea = new Rectangle(0 + 32 , 0 + 16, (int)sharks[3].width - 48, (int)sharks[3].height - 32);

        sharks[4] = new Shark(this);
        sharks[4].setGraphics("shark_transparentc1left", "shark_transparentc1right");
        sharks[4].y = sharks[2].y + 250;
        sharks[4].hSpeed = 6;
        sharks[4].vSpeed = 3;
        sharks[4].width = sharks[4].left1.getWidth() * 0.5;
        sharks[4].height = sharks[4].left1.getHeight() * 0.5;
        sharks[4].solidArea = new Rectangle(0 + 32 , 0 + 16, (int)sharks[4].width - 60, (int)sharks[4].height - 24);

        player.collisionOn = true;

        // START MUSIC
        playMusic(0);

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

        // UUPDATE LAND
        land.update();

        // UPDATE SHARKS
        for (int i = 0; i < sharks.length; i++) {
            if (sharks[i] != null) {
                sharks[i].update();
            }
        }

       // UPDATE PLAYER
        player.update();

    }

    public void paintComponent( Graphics g ) {
        super.paintComponent( g );

        //DEBUG INFO - BEFORE DRAWING
        long drawStartTime = 0;
        if (keyHandler.debugToggle == true) {
            drawStartTime = System.nanoTime();
        }

        Graphics2D g2d = (Graphics2D) g;

        // DRAW LAND
        land.draw(g2d);

        // DRAW SHARKS
        for( int i = 0; i < sharks.length; i++ ) {
            if (sharks[i]  != null) {
                sharks[i].draw(g2d);
            }
        }

        // DRAW PLAYER
        player.draw(g2d);

        // DRAW UI
        gameUI.draw(g2d);

        // DEBUG INFO - AFTER DRAWING
        if (keyHandler.debugToggle == true) {

            //ToDo: Explicitly set font
            long drawEndTime = System.nanoTime();
            long drawTime = drawEndTime - drawStartTime;
            g2d.setColor( Color.yellow);
            g2d.drawString( "Draw Time: " + drawTime / 1_000 + " μs", 10, 440 ); // 1 ms = 1,000 μs
            System.out.println( "Draw Time: " + drawTime / 1_000 + " μs"); // Divide by 1_000_000  to convert to ms
            System.out.println( "Draw Time: " + drawTime  + " ns");

            g2d.setColor( Color.WHITE );
            g2d.drawString( "Player X: " + player.x , 10, 260 );
            g2d.drawString( "Player Y: " + player.y , 10, 320 );
            //.drawString( "Player Row: " + player.worldX, 10, 340 );
            //g2d.drawString( "Player Col: " + player.screenX, 10, 360 );
            g2d.drawString( "Player Direction: " + player.direction, 10, 380 );
        }

        g2d.dispose();
    }

    // SOUND CONTROL
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSFX(int i) {
        sfx.setFile(i);
        sfx.play();
    }

}

