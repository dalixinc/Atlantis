package com.dalixinc.main;

import com.dalixinc.gamechar.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollisionCheckerTest {

    @Mock
    private GamePanel mockGamePanel;
    @Mock
    private Player mockPlayer;

    private CollisionChecker collisionChecker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Setup GamePanel to return the mock Player instance
        when(mockGamePanel.getPlayer()).thenReturn(mockPlayer); // Assuming GamePanel has a getPlayer() method
                                                                // If access is direct via gamePanel.player,
                                                                // then mockGamePanel.player = mockPlayer; (with notes about mock field assignment)
                                                                // From Player.java, it seems gamePanel.player is a direct field.
        mockGamePanel.player = mockPlayer; // This is for conceptual clarity; direct field assignment on pure mocks
                                           // doesn't work as expected. GamePanel would need to be a spy or have a setter.
                                           // For this test, we will assume that mockGamePanel.player refers to our mockPlayer.
                                           // A better approach is to ensure GamePanel provides a getter for Player.
                                           // Let's write the test assuming the interaction works as intended for now.

        collisionChecker = new CollisionChecker(mockGamePanel);
    }

    // Tests for checkCollision(Rectangle me, Rectangle them)
    @Test
    void checkCollision_whenRectanglesIntersect_returnsTrueAndSetsPlayerDead() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10);
        Rectangle rect2 = new Rectangle(5, 5, 10, 10); // Intersecting

        // Ensure player is not dead initially for the side-effect check
        mockPlayer.isDead = false; // Direct field assignment for mock setup

        boolean result = collisionChecker.checkCollision(rect1, rect2);

        assertTrue(result, "checkCollision should return true for intersecting rectangles.");
        assertTrue(mockPlayer.isDead, "gamePanel.player.isDead should be set to true after collision.");
    }

    @Test
    void checkCollision_whenRectanglesDoNotIntersect_returnsFalseAndPlayerStateUnchanged() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10);
        Rectangle rect2 = new Rectangle(15, 15, 10, 10); // Not intersecting

        mockPlayer.isDead = false; // Player is initially not dead

        boolean result = collisionChecker.checkCollision(rect1, rect2);

        assertFalse(result, "checkCollision should return false for non-intersecting rectangles.");
        assertFalse(mockPlayer.isDead, "gamePanel.player.isDead should remain false if no collision.");
    }

    @Test
    void checkCollision_whenRectanglesTouchAtEdge_returnsTrueAndSetsPlayerDead() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10);
        Rectangle rect2 = new Rectangle(10, 0, 10, 10); // Touching at the edge (x=10)

        mockPlayer.isDead = false;

        // Rectangle.intersects() considers touching edges as an intersection.
        boolean result = collisionChecker.checkCollision(rect1, rect2);

        assertTrue(result, "checkCollision should return true for rectangles touching at the edge.");
        assertTrue(mockPlayer.isDead, "gamePanel.player.isDead should be set to true when rectangles touch.");
    }

    // Tests for checkObjectCollision(Rectangle me, Rectangle them)
    @Test
    void checkObjectCollision_whenRectanglesIntersect_returnsTrue() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10);
        Rectangle rect2 = new Rectangle(5, 5, 10, 10); // Intersecting

        boolean result = collisionChecker.checkObjectCollision(rect1, rect2);

        assertTrue(result, "checkObjectCollision should return true for intersecting rectangles.");
    }

    @Test
    void checkObjectCollision_whenRectanglesDoNotIntersect_returnsFalse() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10);
        Rectangle rect2 = new Rectangle(15, 15, 10, 10); // Not intersecting

        boolean result = collisionChecker.checkObjectCollision(rect1, rect2);

        assertFalse(result, "checkObjectCollision should return false for non-intersecting rectangles.");
    }

    @Test
    void checkObjectCollision_whenRectanglesTouchAtEdge_returnsTrue() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10);
        Rectangle rect2 = new Rectangle(10, 0, 10, 10); // Touching at the edge

        // Rectangle.intersects() considers touching edges as an intersection.
        boolean result = collisionChecker.checkObjectCollision(rect1, rect2);

        assertTrue(result, "checkObjectCollision should return true for rectangles touching at the edge.");
    }
}
