package com.dalixinc.gamechar;

import com.dalixinc.main.GamePanel;
import com.dalixinc.main.KeyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Todo - I need to come back to this shit after I understand Mockito
class PlayerTest {

    private Player player;
    private GamePanel gamePanel;
    private KeyHandler keyHandler;

    @BeforeEach
    void setUp() {
        gamePanel = Mockito.mock(GamePanel.class);
        keyHandler = Mockito.mock(KeyHandler.class);
        player = new Player(gamePanel, keyHandler);
    }

    @Test
    void playerMovesUpWhenUpPressed() {
        Mockito.when(keyHandler.upPressed).thenReturn(true);
        player.update();
        assertEquals("up", player.direction);
    }

    @Test
    void playerMovesDownWhenDownPressed() {
        Mockito.when(keyHandler.downPressed).thenReturn(true);
        player.update();
        assertEquals("down", player.direction);
    }

    @Test
    void playerMovesLeftWhenLeftPressed() {
        Mockito.when(keyHandler.leftPressed).thenReturn(true);
        player.update();
        assertEquals("left", player.direction);
    }

    @Test
    void playerMovesRightWhenRightPressed() {
        //keyHandler.setRightPressed(true);
        Mockito.when(keyHandler.isRightPressed()).thenReturn(true);
        player.update();
        assertEquals("right", player.direction);
    }

    @Test
    void playerStopsWhenNoKeyIsPressed() {
        Mockito.when(keyHandler.upPressed).thenReturn(false);
        Mockito.when(keyHandler.downPressed).thenReturn(false);
        Mockito.when(keyHandler.leftPressed).thenReturn(false);
        Mockito.when(keyHandler.rightPressed).thenReturn(false);
        player.update();
        assertEquals(100, player.x);
        assertEquals(100, player.y);
    }

    @Test
    void playerCollisionAreaIsCorrect() {
        Rectangle expected = new Rectangle(0, 0, (int)player.width, (int)player.height);
        assertEquals(expected, player.solidArea);
    }
}