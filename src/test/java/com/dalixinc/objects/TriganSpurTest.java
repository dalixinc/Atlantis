package com.dalixinc.objects;

import com.dalixinc.main.AtlantisMain; // For screen dimension constants
import com.dalixinc.main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TriganSpurTest {

    @Mock
    private GamePanel mockGamePanel;
    @Mock
    private Graphics2D mockG2D;

    private TriganSpur triganSpur;

    // Constants for testing, assuming AtlantisMain values
    private static final int TEST_SCREEN_WIDTH = 800;
    private static final int TEST_SCREEN_HEIGHT = 600;
    private static final int TEST_TILE_SIZE = 48;
    private static final int FPS = 60;
    private static final int SCALUS = 2; // From TriganSpur.java

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockGamePanel.tileSize = TEST_TILE_SIZE;
        mockGamePanel.FPS = FPS;

        // Using constructor that sets coordinates, to avoid AtlantisMain dependency for defaults in basic setup
        triganSpur = new TriganSpur(mockGamePanel, 100, 100, 2, 2, 2, TriganSpur.eTravelDir.LEFT);

        // Mock all 8 directional images
        triganSpur.north = mock(BufferedImage.class);
        triganSpur.northeast = mock(BufferedImage.class);
        triganSpur.east = mock(BufferedImage.class);
        triganSpur.southeast = mock(BufferedImage.class);
        triganSpur.south = mock(BufferedImage.class);
        triganSpur.southwest = mock(BufferedImage.class);
        triganSpur.west = mock(BufferedImage.class);
        triganSpur.northwest = mock(BufferedImage.class);

        // Stub getWidth/getHeight for these mocked images, as draw method uses them for scaling
        when(triganSpur.north.getWidth()).thenReturn(32); when(triganSpur.north.getHeight()).thenReturn(32);
        when(triganSpur.northeast.getWidth()).thenReturn(32); when(triganSpur.northeast.getHeight()).thenReturn(32);
        when(triganSpur.east.getWidth()).thenReturn(32); when(triganSpur.east.getHeight()).thenReturn(32);
        when(triganSpur.southeast.getWidth()).thenReturn(32); when(triganSpur.southeast.getHeight()).thenReturn(32);
        when(triganSpur.south.getWidth()).thenReturn(32); when(triganSpur.south.getHeight()).thenReturn(32);
        when(triganSpur.southwest.getWidth()).thenReturn(32); when(triganSpur.southwest.getHeight()).thenReturn(32);
        when(triganSpur.west.getWidth()).thenReturn(32); when(triganSpur.west.getHeight()).thenReturn(32);
        when(triganSpur.northwest.getWidth()).thenReturn(32); when(triganSpur.northwest.getHeight()).thenReturn(32);


        triganSpur.spriteNum = 0;
        triganSpur.spriteCounter = 0;
    }

    @Test
    void constructor_initializesPropertiesCorrectly() {
        // Values set in setUp's constructor call
        assertSame(mockGamePanel, triganSpur.gamePanel);
        assertEquals(100, triganSpur.screenX);
        assertEquals(100, triganSpur.screenY);
        assertEquals(2, triganSpur.speed);
        assertEquals(2, triganSpur.hSpeed);
        assertEquals(2, triganSpur.vSpeed);
        assertEquals(TriganSpur.eTravelDir.LEFT, triganSpur.direction);

        assertTrue(triganSpur.showCollisionRect); // Set to true in constructor
        assertTrue(triganSpur.playerCollision);   // Set in setDefaultValues
        assertTrue(triganSpur.imageBasedOnSuppliedSize); // Set in setDefaultValues

        // setDefaultValues is called by constructor, let's check some of its effects
        assertEquals(TEST_TILE_SIZE, triganSpur.width); // default from setDefaultValues
        assertEquals(TEST_TILE_SIZE, triganSpur.height); // default from setDefaultValues
        // SolidArea calculation: new Rectangle(4, 4, 32 * SCALUS - 8, 32 * SCALUS - 8) -> (4,4, 64-8, 64-8) -> (4,4,56,56)
        assertEquals(new Rectangle(4, 4, 32 * SCALUS - 8, 32 * SCALUS - 8), triganSpur.solidArea);

        assertEquals(8, triganSpur.spriteframecount); // eImage.values().length
        assertEquals(FPS / 3, triganSpur.spriteInterval);

        assertNotNull(triganSpur.north); // Check one, assuming others are also mocked
    }

    @Test
    void setDefaultValues_usesDefaultsForZeroInputs() {
        TriganSpur defaultSpur = new TriganSpur(mockGamePanel, 0, 0, 0, 0, 0, TriganSpur.eTravelDir.RIGHT);
        // Values should be replaced by defaults from setDefaultValues
        // AtlantisMain.ACTUAL_WIDTH/HEIGHT are used here. We use TEST_SCREEN_WIDTH/HEIGHT as stand-ins.
        assertEquals(TEST_SCREEN_WIDTH - 100, defaultSpur.screenX);
        assertEquals(TEST_SCREEN_HEIGHT - 100, defaultSpur.screenY);
        assertEquals(2, defaultSpur.speed);
        assertEquals(2, defaultSpur.hSpeed); // Default hSpeed
        assertEquals(2, defaultSpur.vSpeed); // Default vSpeed
        assertEquals(TriganSpur.eTravelDir.LEFT, defaultSpur.direction); // direction is reset to LEFT
    }

    @Test
    void interraction_playsSoundAndReturnsFalse() {
        doNothing().when(mockGamePanel).playSFX(anyInt());

        boolean result = triganSpur.interraction();

        assertFalse(result, "interraction should return false.");
        verify(mockGamePanel).playSFX(6);
    }

    @Test
    void update_cyclesSpriteNumCorrectly() {
        assertEquals(0, triganSpur.spriteNum);
        int interval = triganSpur.spriteInterval; // FPS / 3

        for (int i = 0; i < interval; i++) {
            triganSpur.update();
            assertEquals(0, triganSpur.spriteNum);
        }
        triganSpur.update(); // Trigger sprite change
        assertEquals(1, triganSpur.spriteNum);

        // Cycle through all 8 sprites
        for (int s = 1; s < 8; s++) {
            for (int i = 0; i < interval; i++) {
                triganSpur.update();
            }
            triganSpur.update();
            assertEquals((s + 1) % 8, triganSpur.spriteNum);
        }
    }

    @Test
    void update_movesAndHandlesBoundaryCollisions() {
        triganSpur.screenX = 10;
        triganSpur.hSpeed = 2; // Moving left
        triganSpur.direction = TriganSpur.eTravelDir.LEFT;

        // Move left into boundary
        // Loop until screenX would be <= 0. screenX -= hSpeed.
        // 10/2 = 5 updates to reach 0.
        for(int i=0; i<5; i++) triganSpur.update();
        assertEquals(0, triganSpur.screenX);
        triganSpur.update(); // Collision happens, properties change
        assertEquals(TriganSpur.eTravelDir.RIGHT, triganSpur.direction);
        assertEquals(-2, triganSpur.hSpeed); // hSpeed flips
        assertEquals(2, triganSpur.screenX); // Moved away from boundary: 0 - (-2) = 2

        // Move right into boundary (TEST_SCREEN_WIDTH - TEST_TILE_SIZE)
        triganSpur.screenX = TEST_SCREEN_WIDTH - TEST_TILE_SIZE - 2; // e.g. 800 - 48 - 2 = 750
        triganSpur.hSpeed = -2; // Moving right
        triganSpur.direction = TriganSpur.eTravelDir.RIGHT;
        triganSpur.update();
        assertEquals(752, triganSpur.screenX); // 750 - (-2) = 752
        triganSpur.update(); // Hit boundary
        assertEquals(TriganSpur.eTravelDir.LEFT, triganSpur.direction);
        assertEquals(2, triganSpur.hSpeed);
        assertEquals(TEST_SCREEN_WIDTH - TEST_TILE_SIZE - 2, triganSpur.screenX); // 752 - 2 = 750

        // Vertical collision (simplified, only checking vSpeed flip)
        triganSpur.screenY = 0;
        triganSpur.vSpeed = 2; // Moving up
        triganSpur.update(); // Hits boundary
        assertEquals(-2, triganSpur.vSpeed);
        assertEquals(2, triganSpur.screenY); // 0 - (-2) = 2

        triganSpur.screenY = TEST_SCREEN_HEIGHT - TEST_TILE_SIZE;
        triganSpur.vSpeed = -2; // Moving down
        triganSpur.update(); // Hits boundary
        assertEquals(2, triganSpur.vSpeed);
         assertEquals(TEST_SCREEN_HEIGHT - TEST_TILE_SIZE - 2, triganSpur.screenY); // (H-T) - 2
    }


    @Test
    void draw_drawsCorrectSpriteAndScalesIt() {
        triganSpur.screenX = 200;
        triganSpur.screenY = 250;
        triganSpur.imageBasedOnSuppliedSize = true; // Default, but explicit for test clarity

        BufferedImage[] images = {
            triganSpur.north, triganSpur.northeast, triganSpur.east, triganSpur.southeast,
            triganSpur.south, triganSpur.southwest, triganSpur.west, triganSpur.northwest
        };

        for (int i = 0; i < 8; i++) {
            triganSpur.spriteNum = i;
            BufferedImage currentImage = images[i];
            when(currentImage.getWidth()).thenReturn(32); // Ensure mocks return value
            when(currentImage.getHeight()).thenReturn(32);

            triganSpur.draw(mockG2D);

            verify(mockG2D).drawImage(currentImage, 200, 250, 32 * SCALUS, 32 * SCALUS, null);
        }
    }
    
    @Test
    void draw_usesTileSizeWhenNotImageBased() {
        triganSpur.screenX = 200;
        triganSpur.screenY = 250;
        triganSpur.imageBasedOnSuppliedSize = false;
        triganSpur.spriteNum = 0; // north image

        triganSpur.draw(mockG2D);
        verify(mockG2D).drawImage(triganSpur.north, 200, 250, TEST_TILE_SIZE, TEST_TILE_SIZE, null);
    }

    @Test
    void draw_showsCollisionRectangle_whenFlagIsTrue() {
        triganSpur.screenX = 200;
        triganSpur.screenY = 250;
        triganSpur.spriteNum = 0;
        triganSpur.showCollisionRect = true;
        triganSpur.solidArea = new Rectangle(5, 5, 50, 50); // Example

        // Ensure image mock is ready for the main draw call
        when(triganSpur.north.getWidth()).thenReturn(32);
        when(triganSpur.north.getHeight()).thenReturn(32);

        triganSpur.draw(mockG2D);

        verify(mockG2D).drawImage(triganSpur.north, 200, 250, 32 * SCALUS, 32 * SCALUS, null);
        verify(mockG2D).setColor(Color.RED);
        verify(mockG2D).drawRect(200 + triganSpur.solidArea.x, 250 + triganSpur.solidArea.y, triganSpur.solidArea.width, triganSpur.solidArea.height);
    }
}
