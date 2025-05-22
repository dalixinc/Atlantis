package com.dalixinc.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.KeyEvent;
import java.awt.Component; // Required for KeyEvent source

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KeyHandlerTest {

    @Mock
    private GamePanel mockGamePanel;
    @Mock
    private UI mockUI; // GamePanel.gameUI is of type UI
    @Mock
    private Component mockComponent; // Dummy component for KeyEvent source

    private KeyHandler keyHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Associate mockUI with mockGamePanel.gameUI
        // KeyHandler accesses gamePanel.gameUI directly.
        mockGamePanel.gameUI = mockUI;

        keyHandler = new KeyHandler(mockGamePanel);
    }

    private KeyEvent createKeyEvent(int eventType, int keyCode, char keyChar) {
        return new KeyEvent(mockComponent, eventType, System.currentTimeMillis(), 0, keyCode, keyChar);
    }
    private KeyEvent createKeyEvent(int eventType, int keyCode) {
        // For keys without a specific char, like Enter, P, arrows etc.
        return new KeyEvent(mockComponent, eventType, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }


    @Test
    void keyTyped_doesNothing() {
        KeyEvent event = createKeyEvent(KeyEvent.KEY_TYPED, 0, 'a');
        keyHandler.keyTyped(event);
        // Assert no state changes relevant to KeyHandler's direct responsibilities
        assertFalse(keyHandler.upPressed);
        assertFalse(keyHandler.spacePressed);
        // No specific game state change expected from keyTyped
    }

    // Test keyPressed for Movement Keys (W, A, S, D)
    @Test
    void keyPressed_W_setsUpPressedTrue_inPlayOrPauseState() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(event);
        assertTrue(keyHandler.upPressed);
    }

    @Test
    void keyPressed_S_setsDownPressedTrue_inPlayOrPauseState() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_S, 'S');
        keyHandler.keyPressed(event);
        assertTrue(keyHandler.downPressed);
    }

    @Test
    void keyPressed_A_setsLeftPressedTrue_inPlayOrPauseState() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_A, 'A');
        keyHandler.keyPressed(event);
        assertTrue(keyHandler.leftPressed);
    }

    @Test
    void keyPressed_D_setsRightPressedTrue_inPlayOrPauseState() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_D, 'D');
        keyHandler.keyPressed(event);
        assertTrue(keyHandler.rightPressed);
    }

    @Test
    void keyPressed_Space_setsSpacePressedTrue_inPlayOrPauseState() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_SPACE);
        keyHandler.keyPressed(event);
        assertTrue(keyHandler.spacePressed);
        verify(mockGamePanel).playSFX(9);
    }

    @Test
    void keyPressed_Escape_setsEscapePressedTrue_inPlayOrPauseState() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ESCAPE);
        keyHandler.keyPressed(event);
        assertTrue(keyHandler.escapePressed);
    }
    
    @Test
    void keyPressed_O_togglesDebug_inPlayOrPauseState() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_O);
        
        assertFalse(keyHandler.debugToggle, "Debug toggle should be initially false.");
        keyHandler.keyPressed(event);
        assertTrue(keyHandler.debugToggle, "Debug toggle should be true after first O press.");
        keyHandler.keyPressed(event);
        assertFalse(keyHandler.debugToggle, "Debug toggle should be false after second O press.");
    }


    // Test keyPressed for Pause Key (P)
    @Test
    void keyPressed_P_togglesPauseState_andMusic() {
        KeyEvent eventP = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_P);

        // From PLAY to PAUSE
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        keyHandler.keyPressed(eventP);
        assertEquals(eGAME_STATE.PAUSE_GAME, mockGamePanel.gameState);
        verify(mockGamePanel, times(1)).stopMusic(); // stopMusic is called in a new thread

        // From PAUSE to PLAY
        keyHandler.keyPressed(eventP);
        assertEquals(eGAME_STATE.PLAY_GAME, mockGamePanel.gameState);
        verify(mockGamePanel, times(1)).playMusic(0); // playMusic is called in a new thread
    }

    // Test keyPressed for MAIN_MENU State
    @Test
    void keyPressed_MainMenu_UpDownNavigation() {
        mockGamePanel.gameState = eGAME_STATE.MAIN_MENU;
        KeyEvent eventUp = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_UP);
        KeyEvent eventDown = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_DOWN);

        // Initial commandNum (e.g., 0)
        mockGamePanel.gameUI.inputNum = 0;

        // Press UP from 0 -> should go to 2 (assuming 3 menu items 0,1,2)
        keyHandler.keyPressed(eventUp);
        assertEquals(2, mockGamePanel.gameUI.inputNum);

        // Press UP from 2 -> should go to 1
        keyHandler.keyPressed(eventUp);
        assertEquals(1, mockGamePanel.gameUI.inputNum);
        
        // Press UP from 1 -> should go to 0
        keyHandler.keyPressed(eventUp);
        assertEquals(0, mockGamePanel.gameUI.inputNum);

        // Press DOWN from 0 -> should go to 1
        keyHandler.keyPressed(eventDown);
        assertEquals(1, mockGamePanel.gameUI.inputNum);

        // Press DOWN from 1 -> should go to 2
        keyHandler.keyPressed(eventDown);
        assertEquals(2, mockGamePanel.gameUI.inputNum);

        // Press DOWN from 2 -> should go to 0
        keyHandler.keyPressed(eventDown);
        assertEquals(0, mockGamePanel.gameUI.inputNum);
    }

    @Test
    void keyPressed_MainMenu_Enter_SelectNewGame() {
        mockGamePanel.gameState = eGAME_STATE.MAIN_MENU;
        mockGamePanel.gameUI.inputNum = 0; // New Game selected
        KeyEvent eventEnter = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ENTER);

        keyHandler.keyPressed(eventEnter);

        assertEquals(eGAME_STATE.PLAY_GAME, mockGamePanel.gameState);
        verify(mockGamePanel).playMusic(0);
    }
    
    @Test
    void keyPressed_MainMenu_Enter_SelectLoadGame_doesNothingCurrently() {
        // This option is commented out in KeyHandler.java
        mockGamePanel.gameState = eGAME_STATE.MAIN_MENU;
        mockGamePanel.gameUI.inputNum = 1; // "Load Game" (currently does nothing)
        KeyEvent eventEnter = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ENTER);

        keyHandler.keyPressed(eventEnter);
        // Assert that game state does NOT change from MAIN_MENU
        assertEquals(eGAME_STATE.MAIN_MENU, mockGamePanel.gameState);
    }


    // System.exit(0) test is problematic with standard Mockito.
    // We can verify the conditions leading to it but not the call itself easily.
    // For this test, we'll confirm that if inputNum is 2, it attempts this path.
    // A full test would require PowerMockito or a refactor of how exit is handled.
    @Test
    void keyPressed_MainMenu_Enter_SelectExit_attemptsSystemExit() {
        mockGamePanel.gameState = eGAME_STATE.MAIN_MENU;
        mockGamePanel.gameUI.inputNum = 2; // Exit selected
        KeyEvent eventEnter = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ENTER);

        // We cannot easily assert System.exit(0) was called.
        // This test mainly documents that this path is taken.
        // If there were other state changes before System.exit, we could test those.
        // For now, just calling it to ensure no exceptions from KeyHandler itself.
        try {
            keyHandler.keyPressed(eventEnter);
            // If System.exit was not mocked/prevented, the test runner might terminate here.
            // This is why testing System.exit is hard.
            // In a real scenario with a testable design, System.exit might be wrapped in a mockable service.
        } catch (SecurityException se) {
            // Some test environments might throw a SecurityException when System.exit is called.
            // This is an acceptable outcome for this test if it happens.
            System.out.println("System.exit(0) was called and caught by SecurityManager (expected in some test environments).");
        }
        // No assertion here as the behavior is System.exit.
    }


    // Test keyReleased for Movement Keys
    @Test
    void keyReleased_W_setsUpPressedFalse() {
        keyHandler.upPressed = true; // Simulate it was pressed
        KeyEvent event = createKeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.VK_W, 'W');
        keyHandler.keyReleased(event);
        assertFalse(keyHandler.upPressed);
    }

    @Test
    void keyReleased_S_setsDownPressedFalse() {
        keyHandler.downPressed = true;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.VK_S, 'S');
        keyHandler.keyReleased(event);
        assertFalse(keyHandler.downPressed);
    }

    @Test
    void keyReleased_A_setsLeftPressedFalse() {
        keyHandler.leftPressed = true;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.VK_A, 'A');
        keyHandler.keyReleased(event);
        assertFalse(keyHandler.leftPressed);
    }

    @Test
    void keyReleased_D_setsRightPressedFalse() {
        keyHandler.rightPressed = true;
        KeyEvent event = createKeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.VK_D, 'D');
        keyHandler.keyReleased(event);
        assertFalse(keyHandler.rightPressed);
    }
    
    // Note: KeyHandler.java does not handle release for SPACE, ESCAPE, ENTER.
    // So, no tests for their release setting flags to false, as the flags don't exist (for enter)
    // or are not reset on release by KeyHandler (for space, escape).
}
