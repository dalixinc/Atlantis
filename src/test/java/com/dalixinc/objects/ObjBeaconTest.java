package com.dalixinc.objects;

import com.dalixinc.main.GamePanel;
import com.dalixinc.main.eGAME_STATE;
import com.dalixinc.utils.UtilFunctions; // Assuming UtilFunctions is mockable or its methods static/testable
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjBeaconTest {

    @Mock
    private GamePanel mockGamePanel;
    @Mock
    private Graphics2D mockG2D;
    // UtilFunctions is instantiated directly in ObjBeacon.
    // To control its behavior (like getSpriteImages), we would need to refactor ObjBeacon
    // or use PowerMockito for static methods or constructor mocking if UtilFunctions methods are static or it's final.
    // For now, we will directly mock the resulting beaconImages array after construction.

    private ObjBeacon objBeacon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Stub GamePanel properties
        mockGamePanel.tileSize = 48;
        mockGamePanel.FPS = 60;
        // gamePanel.player = mock(Player.class); // If needed by interact or other methods

        // Due to direct instantiation of UtilFunctions and image loading in constructor,
        // we construct objBeacon first, then override the images.
        objBeacon = new ObjBeacon(mockGamePanel);

        // Mock the beacon images after construction
        objBeacon.beaconImages[0] = mock(BufferedImage.class);
        objBeacon.beaconImages[1] = mock(BufferedImage.class);
        objBeacon.beaconImages[2] = mock(BufferedImage.class);
        objBeacon.beaconImages[3] = mock(BufferedImage.class);

        // Reset spriteNum to ensure consistent test starts, as constructor might call update or similar
        objBeacon.spriteNum = 0;
        objBeacon.spriteCounter = 0;
    }

    @Test
    void constructor_initializesPropertiesCorrectly() {
        assertSame(mockGamePanel, objBeacon.gamePanel, "GamePanel should be assigned.");
        assertEquals(680, objBeacon.screenX, "screenX should be initialized.");
        assertEquals(8, objBeacon.screenY, "screenY should be initialized.");
        assertEquals(0, objBeacon.speed, "speed should be 0.");
        assertEquals(mockGamePanel.tileSize, objBeacon.width, "width should be tileSize.");
        assertEquals(mockGamePanel.tileSize, objBeacon.height, "height should be tileSize.");
        assertTrue(objBeacon.playerCollision, "playerCollision should be true.");
        assertNotNull(objBeacon.solidArea, "solidArea should be initialized.");
        assertEquals(new Rectangle(4, 4, mockGamePanel.tileSize - 8, mockGamePanel.tileSize - 8), objBeacon.solidArea, "solidArea dimensions are incorrect.");
        assertFalse(objBeacon.imageBasedOnSuppliedSize, "imageBasedOnSuppliedSize should be false.");

        assertEquals(4, objBeacon.spriteframecount, "spriteframecount should be 4 for beaconImages.");
        assertEquals(mockGamePanel.FPS / 4, objBeacon.spriteInterval, "spriteInterval is incorrect.");

        for (int i = 0; i < 4; i++) {
            assertNotNull(objBeacon.beaconImages[i], "beaconImages[" + i + "] should be mocked.");
        }
    }

    @Test
    void interraction_playsSoundChangesStateAndReturnsTrue() {
        // Stub playSFX to avoid NullPointerException if SoundManager is not initialized in GamePanel mock
        doNothing().when(mockGamePanel).playSFX(anyInt());

        boolean result = objBeacon.interraction();

        assertTrue(result, "interraction should return true.");
        verify(mockGamePanel, times(3)).playSFX(2); // Sound effect 2 is played 3 times

        // Verify game state changes
        // This requires capturing the order or ensuring the final state.
        // The actual Dialogue part is hard to test without deeper mocking of Dialogue/OpeningDialogue.
        // We focus on the observable effects on GamePanel.
        assertEquals(eGAME_STATE.PLAY_GAME, mockGamePanel.gameState, "GameState should return to PLAY_GAME.");
        // To check intermediate state, one might need an ArgumentCaptor on a setter for gameState, if it existed.
        // For now, we check the final state and assume the sequence (PLAY -> DIALOGUE -> PLAY).
    }

    @Test
    void update_cyclesSpriteNumCorrectly() {
        assertEquals(0, objBeacon.spriteNum, "Initial spriteNum should be 0.");
        assertEquals(0, objBeacon.spriteCounter, "Initial spriteCounter should be 0.");

        int interval = objBeacon.spriteInterval; // e.g., 60/4 = 15

        // Loop up to interval, spriteNum should not change yet
        for (int i = 0; i < interval; i++) {
            objBeacon.update();
            assertEquals(0, objBeacon.spriteNum, "spriteNum should remain 0 until interval is reached.");
            assertEquals(i + 1, objBeacon.spriteCounter, "spriteCounter should increment.");
        }

        // One more update: spriteCounter resets, spriteNum increments
        objBeacon.update();
        assertEquals(1, objBeacon.spriteNum, "spriteNum should be 1 after reaching interval.");
        assertEquals(0, objBeacon.spriteCounter, "spriteCounter should reset after reaching interval.");

        // Cycle through all sprites
        for (int s = 1; s < objBeacon.spriteframecount; s++) {
            for (int i = 0; i < interval; i++) {
                objBeacon.update();
            }
            objBeacon.update(); // Trigger spriteNum change
            assertEquals((s + 1) % objBeacon.spriteframecount, objBeacon.spriteNum, "spriteNum should cycle.");
        }
    }

    @Test
    void draw_drawsCorrectSpriteBasedOnSpriteNum() {
        objBeacon.screenX = 100;
        objBeacon.screenY = 150;

        for (int i = 0; i < objBeacon.spriteframecount; i++) {
            objBeacon.spriteNum = i;
            objBeacon.draw(mockG2D);
            verify(mockG2D).drawImage(objBeacon.beaconImages[i], 100, 150, mockGamePanel.tileSize, mockGamePanel.tileSize, null);
        }
    }

    @Test
    void draw_showsCollisionRectangle_whenFlagIsTrue() {
        objBeacon.screenX = 100;
        objBeacon.screenY = 150;
        objBeacon.spriteNum = 0; // Any valid sprite number
        objBeacon.showCollisionRect = true;
        objBeacon.solidArea = new Rectangle(5, 5, 38, 38); // Example solid area

        objBeacon.draw(mockG2D);

        // Verify main image is drawn
        verify(mockG2D).drawImage(objBeacon.beaconImages[0], 100, 150, mockGamePanel.tileSize, mockGamePanel.tileSize, null);

        // Verify collision rectangle is drawn
        verify(mockG2D).setColor(Color.RED);
        verify(mockG2D).drawRect(100 + objBeacon.solidArea.x, 150 + objBeacon.solidArea.y, objBeacon.solidArea.width, objBeacon.solidArea.height);
    }
}
