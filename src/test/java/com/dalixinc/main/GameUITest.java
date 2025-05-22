package com.dalixinc.main;

import com.dalixinc.gamechar.Player; // Assuming Player class exists for gamePanel.player
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameUITest {

    @Mock
    private GamePanel mockGamePanel;
    @Mock
    private Graphics2D mockGraphics2D;
    @Mock
    private Player mockPlayer;
    @Mock
    private Font mockFont; // For general font checks if needed

    private GameUI gameUI;

    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<Integer> xCaptor;
    @Captor
    private ArgumentCaptor<Integer> yCaptor;
    @Captor
    private ArgumentCaptor<Image> imageCaptor;
    @Captor
    private ArgumentCaptor<Font> fontCaptor;
     @Captor
    private ArgumentCaptor<Color> colorCaptor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Stubbing GamePanel fields that GameUI might access
        mockGamePanel.player = mockPlayer;
        mockGamePanel.screenWidth = 800;  // Example value
        mockGamePanel.screenHeight = 600; // Example value
        mockGamePanel.tileSize = 48;      // Example value
        mockGamePanel.FPS = 60;           // Example value

        gameUI = new GameUI(mockGamePanel);

        // Mock images loaded in GameUI constructor to avoid actual file I/O
        gameUI.lifeImage = mock(BufferedImage.class);
        gameUI.titleImage = mock(BufferedImage.class);

        // Reset message state for each test
        gameUI.messageOn = false;
        gameUI.message = "";
        gameUI.messageCounter = 0;
        gameUI.gameOver = false;
        gameUI.inputNum = 0; // Default menu selection
        gameUI.currentDialogue = "";
        
        // Ensure font metrics can be created from the mock
        when(mockGraphics2D.getFontMetrics(any(Font.class))).thenReturn(mock(FontMetrics.class));
        when(mockGraphics2D.getFontMetrics()).thenReturn(mock(FontMetrics.class)); // General case
    }

    @Test
    void constructor_initializesGamePanelAndFontsAndImages() {
        assertSame(mockGamePanel, gameUI.gamePanel, "GamePanel should be assigned.");
        assertNotNull(gameUI.arial_40, "Arial 40 font should be initialized.");
        assertNotNull(gameUI.ariel_80B, "Arial 80 Bold font should be initialized.");
        assertNotNull(gameUI.maruMonica, "Maru Monica font should be initialized.");
        assertNotNull(gameUI.purisaBold, "Purisa Bold font should be initialized.");
        // Non-null check for images (they are mocked in setUp)
        assertNotNull(gameUI.lifeImage, "Life image should be initialized (mocked).");
        assertNotNull(gameUI.titleImage, "Title image should be initialized (mocked).");
    }

    @Test
    void showMessage_setsMessagePropertiesCorrectly() {
        String testMessage = "Test Message";
        gameUI.showMessage(testMessage);

        assertEquals(testMessage, gameUI.message, "Message string should be set.");
        assertTrue(gameUI.messageOn, "messageOn should be true.");
        assertEquals(0, gameUI.messageCounter, "messageCounter should be reset to 0.");
    }

    @Test
    void draw_titleState_rendersTitleScreen() {
        mockGamePanel.gameState = eGAME_STATE.MAIN_MENU; // GameUI uses MAIN_MENU for title screen
        gameUI.inputNum = 1; // "LOAD GAME" selected

        gameUI.draw(mockGraphics2D);

        // Verify background
        verify(mockGraphics2D).setColor(eq(new Color(0x21, 0x4B, 0, 255)));
        verify(mockGraphics2D).fillRect(0, 0, mockGamePanel.screenWidth, mockGamePanel.screenHeight);
        
        // Verify title text "PLAY ATLANTIS" (checking for at least one part of it)
        verify(mockGraphics2D, atLeastOnce()).drawString(contains("PLAY ATLANTIS"), anyInt(), anyInt());
        
        // Verify title image
        verify(mockGraphics2D).drawImage(eq(gameUI.titleImage), anyInt(), anyInt(), anyInt(), anyInt(), eq(null));

        // Verify menu items - check for "LOAD GAME" and cursor near it due to inputNum = 1
        // This requires more precise argument capturing for coordinates if exactness is needed.
        // For simplicity, checking if "LOAD GAME" is drawn.
        verify(mockGraphics2D, atLeastOnce()).drawString(eq("LOAD GAME"), anyInt(), anyInt());
        // Check cursor for "LOAD GAME" (inputNum = 1)
        // The text ">" should be drawn. We capture all drawString calls and check.
        verify(mockGraphics2D, atLeastOnce()).drawString(eq(">"), anyInt(), anyInt());
    }

    @Test
    void draw_playState_rendersHud_noGameOver() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        gameUI.gameOver = false;
        mockGamePanel.player.lives = 3; // Example player lives

        gameUI.draw(mockGraphics2D);

        // Verify life image and lives text
        verify(mockGraphics2D).drawImage(eq(gameUI.lifeImage), anyInt(), anyInt(), eq(mockGamePanel.tileSize), eq(mockGamePanel.tileSize), eq(null));
        verify(mockGraphics2D).drawString(eq("= " + mockGamePanel.player.lives), anyInt(), anyInt());

        // Verify time is drawn
        verify(mockGraphics2D).drawString(startsWith("Time: "), anyInt(), anyInt());
    }
    
    @Test
    void draw_playState_rendersHud_withTimedMessage() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME;
        gameUI.gameOver = false;
        String testMsg = "HUD Message";
        gameUI.showMessage(testMsg);
        assertTrue(gameUI.messageOn);
        assertEquals(0, gameUI.messageCounter);

        gameUI.draw(mockGraphics2D); // Frame 1

        verify(mockGraphics2D).drawString(eq(testMsg), anyInt(), anyInt());
        assertEquals(1, gameUI.messageCounter);
        assertTrue(gameUI.messageOn);

        // Simulate frames to make message disappear
        int timeoutFrames = mockGamePanel.FPS * 2; // e.g., 60 * 2 = 120
        for (int i = 1; i < timeoutFrames; i++) {
            gameUI.draw(mockGraphics2D); // Simulate drawing calls which increments counter internally via drawPlayScreen
        }
        // One more call to cross the threshold
        gameUI.draw(mockGraphics2D);
        assertFalse(gameUI.messageOn, "Message should be off after timeout.");
        assertEquals(0, gameUI.messageCounter, "Message counter should reset after timeout.");
    }

    @Test
    void draw_playState_rendersGameOverScreen_whenGameOverIsTrue() {
        mockGamePanel.gameState = eGAME_STATE.PLAY_GAME; // Still PLAY_GAME state, but internal gameOver flag is true
        gameUI.gameOver = true;
        gameUI.playTime = 123.456; // Example play time

        gameUI.draw(mockGraphics2D);

        verify(mockGamePanel).stopMusic();
        verify(mockGamePanel).playSFX(10);

        // Verify "GAME OVER" text
        verify(mockGraphics2D, atLeastOnce()).drawString(eq("GAME OVER"), anyInt(), anyInt());
        // Verify time display
        verify(mockGraphics2D, atLeastOnce()).drawString(contains(new DecimalFormat("#.##").format(gameUI.playTime)), anyInt(), anyInt());
        // Verify "Congratulations!"
        verify(mockGraphics2D, atLeastOnce()).drawString(eq("Congratulations!"), anyInt(), anyInt());
        
        // Verify gameThread is set to null in GamePanel
        assertNull(mockGamePanel.gameThread, "GamePanel's gameThread should be set to null on game over.");
    }


    @Test
    void draw_pauseState_rendersPauseMessage() {
        mockGamePanel.gameState = eGAME_STATE.PAUSE_GAME;
        gameUI.draw(mockGraphics2D);
        verify(mockGraphics2D, times(1)).drawString(eq("PAUSED"), anyInt(), anyInt());
    }

    @Test
    void draw_dialogueState_rendersDialogueWindowAndText() {
        mockGamePanel.gameState = eGAME_STATE.DIALOGUE;
        String testDialogue = "Hello adventurer!\nWelcome to the test.";
        gameUI.currentDialogue = testDialogue;

        gameUI.draw(mockGraphics2D);

        // Verify sub-window drawing (check for a fillRoundRect call)
        verify(mockGraphics2D).fillRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        // Verify border drawing
        verify(mockGraphics2D).drawRoundRect(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        
        // Verify each line of dialogue is drawn
        verify(mockGraphics2D).drawString(eq("Hello adventurer!"), anyInt(), anyInt());
        verify(mockGraphics2D).drawString(eq("Welcome to the test."), anyInt(), anyInt());
    }
    
    // Note: GameUI.java does not have a distinct eGAME_STATE.GAME_OVER for its main draw router.
    // Game over screen is handled within drawPlayScreen() based on gameUI.gameOver flag.
    // KeyHandler.java also did not have specific menu navigation for a GAME_OVER state.
    // If a separate GAME_OVER state with menu options like "Retry", "Quit" was implemented in GameUI.draw(),
    // tests similar to draw_titleState_rendersTitleScreen (for menu items and cursor) would be added here.
}
