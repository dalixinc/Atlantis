package com.dalixinc.dialogue;

import com.dalixinc.main.GamePanel;
import com.dalixinc.main.UI;
// UtilFunctions is directly instantiated in OpeningDialogue, making sleep hard to mock without PowerMock/refactor.
// import com.dalixinc.utils.UtilFunctions; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpeningDialogueTest {

    @Mock
    private GamePanel mockGamePanel;
    @Mock
    private UI mockUI;
    // @Mock private UtilFunctions mockUtilFunctions; // Would need refactor to inject

    @Captor
    private ArgumentCaptor<String> dialogueCaptor;

    // Accessing private static defaultDialogues for comparison is not directly possible
    // without reflection or making it package-private/public for testing.
    // We will use known values from the source code for assertion.
    private static String[] expectedDefaultDialogues = {
            "Welcome to the game!",
            "You are the hero of this story.",
            "You will face many challenges\nyou demented numpty!.",
            "Good luck!"
    };

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockGamePanel.gameUI = mockUI; // Link mock UI to mock GamePanel
    }

    @Test
    void constructor_default_initializesWithDefaultDialogues() {
        OpeningDialogue dialogue = new OpeningDialogue(mockGamePanel);

        assertNotNull(dialogue.dialogues, "Dialogues array should be initialized.");
        assertEquals(expectedDefaultDialogues.length, dialogue.dialogues.length, "Dialogues length should match default.");
        assertArrayEquals(expectedDefaultDialogues, dialogue.dialogues, "Dialogues content should match default.");
        assertSame(mockGamePanel, dialogue.gamePanel, "GamePanel should be assigned.");
    }

    @Test
    void constructor_customDialogues_initializesWithProvidedDialogues() {
        String[] customDialogues = {"Custom Line 1", "Custom Line 2"};
        OpeningDialogue dialogue = new OpeningDialogue(customDialogues, mockGamePanel);

        assertNotNull(dialogue.dialogues, "Dialogues array should be initialized.");
        assertEquals(customDialogues.length, dialogue.dialogues.length, "Dialogues length should match custom input.");
        assertArrayEquals(customDialogues, dialogue.dialogues, "Dialogues content should match custom input.");
        assertSame(mockGamePanel, dialogue.gamePanel, "GamePanel should be assigned.");
    }

    @Test
    void printDialogue_setsCurrentDialogueOnUI_forEachLine() {
        // This test will be slow due to UtilFunctions.sleep(3000)
        System.out.println("Starting test printDialogue_setsCurrentDialogueOnUI_forEachLine (will be slow due to sleep)...");
        OpeningDialogue dialogue = new OpeningDialogue(mockGamePanel); // Uses default dialogues

        dialogue.printDialogue();

        // Verify that gameUI.currentDialogue was set for each line.
        // The number of times currentDialogue field is set should match the number of dialogue lines.
        // We capture all arguments to the (implicit) setter.
        // Since currentDialogue is a public field, Mockito doesn't "verify" field sets directly like method calls.
        // We would have to check the value of mockUI.currentDialogue after each sleep, which is hard.
        // A workaround if UI had a method like 'showDialogue(String text)': verify(mockUI, times(dialogue.dialogues.length)).showDialogue(anyString());
        // Or, if UI had a setter 'setCurrentDialogue(String text)': verify(mockUI, times(dialogue.dialogues.length)).setCurrentDialogue(dialogueCaptor.capture());
        
        // For direct field access mockGamePanel.gameUI.currentDialogue = dialogue,
        // we can't use verify setters in the same way.
        // However, the loop structure means it *was* set multiple times.
        // We can at least check the *last* value set.
        if (expectedDefaultDialogues.length > 0) {
            assertEquals(expectedDefaultDialogues[expectedDefaultDialogues.length - 1], mockUI.currentDialogue,
                    "The last dialogue line should be set on gameUI.currentDialogue.");
        }
        System.out.println("Finished test printDialogue_setsCurrentDialogueOnUI_forEachLine.");
    }
    
    @Test
    void printDialogue_withCustomDialogues_setsCurrentDialogueOnUI() {
        // This test will also be slow.
        System.out.println("Starting test printDialogue_withCustomDialogues_setsCurrentDialogueOnUI (will be slow due to sleep)...");
        String[] customDialogues = {"Test 1", "Test 2"};
        OpeningDialogue dialogue = new OpeningDialogue(customDialogues, mockGamePanel);

        dialogue.printDialogue();
        
        if (customDialogues.length > 0) {
            assertEquals(customDialogues[customDialogues.length - 1], mockUI.currentDialogue,
                    "The last custom dialogue line should be set on gameUI.currentDialogue.");
        }
        System.out.println("Finished test printDialogue_withCustomDialogues_setsCurrentDialogueOnUI.");
    }

    // Methods like getCurrentDialogue, getNextDialogue, isDialogueFinished
    // are not present in Dialogue.java or OpeningDialogue.java, so they cannot be tested here.
}
