package com.dalixinc.gamechar;

import com.dalixinc.main.CollisionChecker;
import com.dalixinc.main.GamePanel;
import com.dalixinc.main.KeyHandler;
import com.dalixinc.main.UI;
import com.dalixinc.object.SuperObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Todo - I need to come back to this shit after I understand Mockito
class PlayerTest {

    private Player player;
    @Mock
    private GamePanel gamePanel;
    @Mock
    private KeyHandler keyHandler;
    @Mock
    private CollisionChecker mockCollisionChecker;
    @Mock
    private UI mockUI;
    @Mock
    private Graphics2D mockGraphics2D;

    private Player player;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock annotated fields

        // Setup GamePanel mock behaviors needed for Player constructor or general use
        gamePanel.sharks = new GameChar[5]; // Initialize with a new array
        gamePanel.gameObjects = new SuperObject[10]; // Initialize with a new array
        gamePanel.collisionChecker = mockCollisionChecker; // Assign the mock CollisionChecker
        gamePanel.gameUI = mockUI; // Assign the mock UI

        // Mock methods that might be called indirectly or directly by Player
        // For example, if Player constructor or its methods use gamePanel.tileSize
        // Mockito.when(gamePanel.getTileSize()).thenReturn(48); // Example

        player = new Player(gamePanel, keyHandler);
        // Default values: x=100, y=100, speed=5, direction="down", lives=5
        // Player images are also loaded in constructor.
        // Ensure necessary images are "loaded" for drawing tests later by assigning mock BufferedImages
        player.right1 = Mockito.mock(BufferedImage.class);
        player.right2 = Mockito.mock(BufferedImage.class);
        player.right3 = Mockito.mock(BufferedImage.class);
        player.right4 = Mockito.mock(BufferedImage.class);
    }

    @Test
    void setDefaultValues_setsPlayerToDefaultState() {
        player.x = 50; // Change from default
        player.y = 50;
        player.speed = 10;
        player.direction = "left";
        player.lives = 3;

        player.setDefaultValues();

        assertEquals(100, player.x, "Player X should be reset to default.");
        assertEquals(100, player.y, "Player Y should be reset to default.");
        assertEquals(5, player.speed, "Player speed should be reset to default.");
        assertEquals("down", player.direction, "Player direction should be reset to default.");
        assertEquals(5, player.lives, "Player lives should be reset to default.");
    }
    
    @Test
    void getPlayerImage_loadsImages() {
        // This test is basic and just checks if images are not null after loading.
        // It doesn't verify the content of the images.
        // A more robust test would mock ImageIO.read.
        player.getPlayerImage(); // This method is called in constructor, but call again to be sure
        // For now, assuming images are in classpath and loadable.
        // If ImageIO.read throws an exception, this test will fail.
        // Based on Player.java, it loads right1, right2, right3, right4.
        // These are used for all directions.
        assertNotNull(player.right1, "right1 sprite should be loaded by constructor.");
        assertNotNull(player.right2, "right2 sprite should be loaded by constructor.");
        assertNotNull(player.right3, "right3 sprite should be loaded by constructor.");
        assertNotNull(player.right4, "right4 sprite should be loaded by constructor.");
    }


    @Test
    void playerMovesUpWhenUpPressed() {
        int initialY = player.y;
        Mockito.when(keyHandler.upPressed).thenReturn(true);
        player.update();
        assertEquals("up", player.direction, "Direction should be up.");
        assertEquals(initialY - player.speed, player.y, "Player Y should decrease by speed.");
        assertEquals(100, player.x, "Player X should not change.");
    }

    @Test
    void playerMovesDownWhenDownPressed() {
        int initialY = player.y;
        Mockito.when(keyHandler.downPressed).thenReturn(true);
        player.update();
        assertEquals("down", player.direction, "Direction should be down.");
        assertEquals(initialY + player.speed, player.y, "Player Y should increase by speed.");
        assertEquals(100, player.x, "Player X should not change.");
    }

    @Test
    void playerMovesLeftWhenLeftPressed() {
        int initialX = player.x;
        Mockito.when(keyHandler.leftPressed).thenReturn(true);
        player.update();
        assertEquals("left", player.direction, "Direction should be left.");
        assertEquals(initialX - player.speed, player.x, "Player X should decrease by speed.");
        assertEquals(100, player.y, "Player Y should not change.");
    }

    @Test
    void playerMovesRightWhenRightPressed() {
        int initialX = player.x;
        // Corrected from keyHandler.isRightPressed() to keyHandler.rightPressed
        Mockito.when(keyHandler.rightPressed).thenReturn(true);
        player.update();
        assertEquals("right", player.direction, "Direction should be right.");
        assertEquals(initialX + player.speed, player.x, "Player X should increase by speed.");
        assertEquals(100, player.y, "Player Y should not change.");
    }

    @Test
    void playerStopsWhenNoKeyIsPressed() {
        int initialX = player.x;
        int initialY = player.y;
        // Ensure all key presses are false (default mock behavior, but explicit is clearer)
        Mockito.when(keyHandler.upPressed).thenReturn(false);
        Mockito.when(keyHandler.downPressed).thenReturn(false);
        Mockito.when(keyHandler.leftPressed).thenReturn(false);
        Mockito.when(keyHandler.rightPressed).thenReturn(false);
        
        player.update(); // animateWhenStill is true by default in Player.java
        
        // Since animateWhenStill is true, the spriteCounter will increment,
        // but x and y should not change if no keys are pressed.
        assertEquals(initialX, player.x, "Player X should not change when no keys are pressed.");
        assertEquals(initialY, player.y, "Player Y should not change when no keys are pressed.");
    }

    @Test
    void playerCollisionAreaIsCorrect() {
        // player.width and player.height are set in the constructor.
        // solidArea is new Rectangle(0, 0, (int)width, (int)height);
        // Values from Player constructor: width = 48.0 * 2 = 96; height = 32.0 * 2 = 64;
        Rectangle expected = new Rectangle(0, 0, (int)(48.0 * 2), (int)(32.0 * 2));
        assertEquals(expected, player.solidArea, "Player solidArea should match initialized dimensions.");
    }

    @Test
    void update_spriteAnimationCyclesCorrectly() {
        // Player.java: animateWhenStill = true; by default
        // spriteCounter increments each update.
        // spriteNum changes based on spriteCounter / 10
        // 0-9 ticks: spriteNum = 1
        // 10-19 ticks: spriteNum = 2
        // 20-29 ticks: spriteNum = 3
        // 30-39 ticks: spriteNum = 4
        // 40+ ticks: spriteCounter resets, spriteNum = 1

        // Initial state (after constructor, spriteCounter could be 0, spriteNum likely 1 due to initial animation call or default)
        // Let's assume player.spriteNum is 1 initially or set it.
        // Player constructor doesn't explicitly set spriteNum, but update() does.
        // Let's call update once to initialize spriteNum based on initial spriteCounter = 0.
        player.spriteCounter = 0;
        player.update(); // spriteCounter becomes 1, spriteNum = 1
        assertEquals(1, player.spriteNum, "SpriteNum should be 1 after 1 update.");

        // Call update 9 more times (total 10 updates)
        for (int i = 0; i < 9; i++) { // spriteCounter will go from 2 to 10
            player.update();
        }
        assertEquals(2, player.spriteNum, "SpriteNum should be 2 after 10 updates."); // spriteCounter = 10

        // Call update 10 more times (total 20 updates)
        for (int i = 0; i < 10; i++) { // spriteCounter will go from 11 to 20
            player.update();
        }
        assertEquals(3, player.spriteNum, "SpriteNum should be 3 after 20 updates."); // spriteCounter = 20

        // Call update 10 more times (total 30 updates)
        for (int i = 0; i < 10; i++) { // spriteCounter will go from 21 to 30
            player.update();
        }
        assertEquals(4, player.spriteNum, "SpriteNum should be 4 after 30 updates."); // spriteCounter = 30

        // Call update 10 more times (total 40 updates)
        // spriteCounter will go from 31 to 40. When spriteCounter/10 > 3 (i.e. 40/10=4), it resets.
        for (int i = 0; i < 10; i++) {
            player.update();
        }
        // After 40 calls to update, spriteCounter becomes 40.
        // Inside update: spriteCounter/10 > 3 (40/10=4 > 3) is true.
        // So, spriteCounter is reset to 0. Then spriteNum is set based on new spriteCounter (0).
        // spriteNum = 1 (because spriteCounter/10 == 0).
        assertEquals(1, player.spriteNum, "SpriteNum should reset to 1 after 40 updates.");
        assertEquals(0, player.spriteCounter, "SpriteCounter should reset to 0 after 40 updates.");
    }

    @Test
    void update_collisionWithShark_handlesDeathAndRespawn() throws InterruptedException {
        // Setup: Player is alive, one shark exists
        player.isDead = false;
        player.lives = 3;
        player.collisionOn = true; // Enable collision checking in player
        int initialLives = player.lives;
        int initialX = player.x;
        int initialY = player.y;

        GameChar mockShark = Mockito.mock(GameChar.class);
        mockShark.x = player.x; // Shark at player's x
        mockShark.y = player.y; // Shark at player's y
        mockShark.solidArea = new Rectangle(0, 0, 10, 10); // Shark's solid area
        gamePanel.sharks[0] = mockShark;

        // Mock collision checker to return true for player and shark
        when(mockCollisionChecker.checkCollision(any(Rectangle.class), any(Rectangle.class))).thenReturn(true);
        // Mock sound methods
        doNothing().when(gamePanel).stopMusic();
        doNothing().when(gamePanel).playSFX(anyInt());
        doNothing().when(gamePanel).playMusic(anyInt());


        // Action: Update player, causing collision
        player.update();

        // Assertions: Post-collision, pre-respawn-thread
        assertTrue(player.isDead, "Player should be marked as dead after collision.");
        assertEquals(initialLives - 1, player.lives, "Player lives should decrease by 1.");
        verify(gamePanel, times(2)).stopMusic(); // Called twice in Player.java
        verify(gamePanel).playSFX(8);

        // Wait for the respawn thread to complete
        // The thread in Player.java has a Thread.sleep(1000)
        Thread.sleep(1500); // Wait a bit longer than the sleep in the thread

        // Assertions: Post-respawn
        assertFalse(player.isDead, "Player should no longer be dead after respawn.");
        assertFalse(mockUI.gameOver, "gameOver flag in UI should not be set if lives > 0.");
        assertTrue(player.x != initialX || player.y != initialY, "Player position should change after respawn.");
        verify(gamePanel, times(1)).playMusic(0); // Check if background music restarts

        // Test game over scenario
        player.lives = 1; // Next collision will be game over
        player.isDead = false; // Reset for another collision
        gamePanel.sharks[0].x = player.x; // Move shark to new player position
        gamePanel.sharks[0].y = player.y;
        
        player.update(); // Another collision

        Thread.sleep(1500); // Wait for respawn logic again

        assertTrue(mockUI.gameOver, "gameOver flag in UI should be set if lives reach 0.");
        assertEquals(0, player.lives, "Player lives should be 0.");
    }

     @Test
    void update_collisionWithObject_interactsAndRemovesObject() throws InterruptedException {
        player.collisionOn = false; // Assuming object collision is separate or always on
        player.x = 100; player.y = 100; // Reset player pos for predictability
        SuperObject mockObject = Mockito.mock(SuperObject.class);
        mockObject.screenX = 100;
        mockObject.screenY = 100;
        mockObject.solidArea = new Rectangle(0,0,10,10);
        mockObject.playerCollision = true;
        gamePanel.gameObjects[0] = mockObject;

        when(mockCollisionChecker.checkObjectCollision(any(Rectangle.class), any(Rectangle.class))).thenReturn(true);
        when(mockObject.interraction()).thenReturn(true); // Object should be removed

        doNothing().when(gamePanel).stopMusic();
        doNothing().when(gamePanel).playMusic(anyInt());

        int initialPlayerX = player.x;
        player.update(); // Trigger collision

        // Interaction happens in a new thread in Player.java
        Thread.sleep(500); // Wait for thread to execute

        verify(gamePanel).stopMusic();
        verify(mockObject).interraction();
        assertNull(gamePanel.gameObjects[0], "Object should be removed (set to null).");
        assertEquals(initialPlayerX - 100, player.x, "Player X should be moved back.");
        verify(gamePanel).playMusic(0);
    }

    @Test
    void update_collisionWithObject_interactsAndRepositionsObject() throws InterruptedException {
        player.collisionOn = false;
        player.x = 100; player.y = 100;
        SuperObject mockObject = Mockito.mock(SuperObject.class);
        mockObject.screenX = 100;
        mockObject.screenY = 100;
        mockObject.solidArea = new Rectangle(0,0,10,10);
        mockObject.playerCollision = true;
        gamePanel.gameObjects[0] = mockObject;

        when(mockCollisionChecker.checkObjectCollision(any(Rectangle.class), any(Rectangle.class))).thenReturn(true);
        when(mockObject.interraction()).thenReturn(false); // Object should be repositioned

        doNothing().when(gamePanel).stopMusic();
        doNothing().when(gamePanel).playMusic(anyInt());
        
        int initialPlayerX = player.x;
        int expectedPlayerYAfterCollision = 32; // Player.java sets this.y = 32

        player.update(); // Trigger collision

        Thread.sleep(500); // Wait for thread

        verify(gamePanel).stopMusic();
        verify(mockObject).interraction();
        assertNotNull(gamePanel.gameObjects[0], "Object should still exist.");
        assertEquals(initialPlayerX - 100, player.x, "Player X should be moved back.");
        assertEquals(expectedPlayerYAfterCollision, player.y, "Player Y should be repositioned.");
        // In Player.java, this case also sets gamePanel.gameObjects[dex].screenX/Y to gamePanel.screenWidth/Height
        // but these fields are not directly mockable on SuperObject unless it has setters or direct access.
        // For now, we confirm player's Y is set.
        verify(gamePanel).playMusic(0);
    }

    @Test
    void draw_defaultState_drawsCorrectSprite() {
        player.direction = "right";
        player.spriteNum = 1; // Should use player.right1 (which is a mock BufferedImage)
        player.isDead = false;
        player.showCollisionRect = false;

        player.draw(mockGraphics2D);

        // Verify drawImage is called with the expected image, coordinates, and size
        // Width and height are from Player constructor: 48*2=96, 32*2=64
        verify(mockGraphics2D).drawImage(player.right1, player.x, player.y, (int)(48.0*2), (int)(32.0*2), null);
    }

    @Test
    void draw_allSpriteCases_drawsCorrectSprite() {
        // This test will check each direction and spriteNum combination
        // For simplicity, we'll just check one sprite per direction, assuming the others follow the same pattern.
        // Player images (right1, right2, right3, right4) are used for all directions.

        player.isDead = false;
        player.showCollisionRect = false;
        int expectedWidth = (int)(48.0*2);
        int expectedHeight = (int)(32.0*2);

        // Direction: up, spriteNum: 1 (uses right1)
        player.direction = "up";
        player.spriteNum = 1;
        player.draw(mockGraphics2D);
        verify(mockGraphics2D).drawImage(player.right1, player.x, player.y, expectedWidth, expectedHeight, null);

        // Direction: down, spriteNum: 2 (uses right2)
        player.direction = "down";
        player.spriteNum = 2;
        player.draw(mockGraphics2D);
        verify(mockGraphics2D).drawImage(player.right2, player.x, player.y, expectedWidth, expectedHeight, null);

        // Direction: left, spriteNum: 3 (uses right3)
        player.direction = "left";
        player.spriteNum = 3;
        player.draw(mockGraphics2D);
        verify(mockGraphics2D).drawImage(player.right3, player.x, player.y, expectedWidth, expectedHeight, null);
        
        // Direction: right, spriteNum: 4 (uses right4)
        player.direction = "right";
        player.spriteNum = 4;
        player.draw(mockGraphics2D);
        verify(mockGraphics2D).drawImage(player.right4, player.x, player.y, expectedWidth, expectedHeight, null);
    }


    @Test
    void draw_whenDead_showsDeathMessage() {
        player.isDead = true;
        player.showCollisionRect = false; // Ensure this doesn't interfere

        player.draw(mockGraphics2D);

        // Verify fillRect for red background
        verify(mockGraphics2D).setColor(Color.RED);
        verify(mockGraphics2D).fillRect(player.x + player.solidArea.x, player.y + player.solidArea.y, player.solidArea.width, player.solidArea.height);
        
        // Verify setColor for white text and drawString for "You are DEAD!!"
        verify(mockGraphics2D).setColor(Color.WHITE);
        verify(mockGraphics2D).drawString("You are DEAD!!", player.x + 5, player.y + 32);
        
        // Ensure drawImage is NOT called
        verify(mockGraphics2D, Mockito.never()).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    @Test
    void draw_whenShowCollisionRect_drawsCollisionRectangle() {
        player.isDead = false; // Ensure normal drawing path
        player.showCollisionRect = true;
        player.direction = "right"; // Needs a direction for normal drawing part
        player.spriteNum = 1;

        player.draw(mockGraphics2D);

        // Verify main image is drawn
        verify(mockGraphics2D).drawImage(player.right1, player.x, player.y, (int)(48.0*2), (int)(32.0*2), null);

        // Verify collision rectangle is drawn
        verify(mockGraphics2D).setColor(Color.RED); // Color set for collision rect
        verify(mockGraphics2D).drawRect(player.x + player.solidArea.x, player.y + player.solidArea.y, player.solidArea.width, player.solidArea.height);
    }

}