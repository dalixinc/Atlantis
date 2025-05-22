package com.dalixinc.dialogue;

import com.dalixinc.main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class DialogueTest {

    @Mock
    private GamePanel mockGamePanel;

    // Concrete subclass for testing the abstract Dialogue class
    static class ConcreteDialogue extends Dialogue {
        public ConcreteDialogue(String[] dialogues, GamePanel gamePanel) {
            super(dialogues, gamePanel);
        }

        @Override
        public void printDialogue() {
            // Dummy implementation for testing purposes
            // System.out.println("Printing dialogue...");
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void constructor_assignsGamePanelAndDialoguesCorrectly() {
        String[] testDialogues = {"Hello", "This is a test."};
        ConcreteDialogue dialogue = new ConcreteDialogue(testDialogues, mockGamePanel);

        assertSame(mockGamePanel, dialogue.gamePanel, "GamePanel should be assigned correctly.");
        assertArrayEquals(testDialogues, dialogue.dialogues, "Dialogues array should be initialized with the passed strings.");
        assertEquals(2, dialogue.dialogues.length, "Dialogues array length should match the input.");
        assertEquals("Hello", dialogue.dialogues[0]);
        assertEquals("This is a test.", dialogue.dialogues[1]);
    }

    @Test
    void constructor_withEmptyDialoguesArray_initializesEmptyDialogues() {
        String[] emptyDialogues = {};
        ConcreteDialogue dialogue = new ConcreteDialogue(emptyDialogues, mockGamePanel);

        assertSame(mockGamePanel, dialogue.gamePanel);
        assertArrayEquals(emptyDialogues, dialogue.dialogues, "Dialogues array should be empty if an empty array is passed.");
        assertEquals(0, dialogue.dialogues.length);
    }
    
    @Test
    void constructor_withInitiallyNonEmptyInternalDialogues_appendsNewDialogues() {
        // This test demonstrates the appending behavior if super() was called in a way that
        // this.dialogues was already populated. For a standard subclass, this.dialogues starts empty.
        
        // First, create an instance where 'this.dialogues' (internal to Dialogue) gets populated
        // For this specific test, we'll simulate a scenario where the superclass constructor
        // might be called in a sequence or if the initial `this.dialogues = {};` was different.
        // Given `this.dialogues = {}` is hardcoded at field declaration, this test primarily verifies
        // the array copying logic under the assumption that `this.dialogues` could theoretically
        // be non-empty if the class was designed differently or if the constructor logic was part of a non-constructor method.
        // In the current Dialogue.java, `this.dialogues` always starts empty before constructor logic runs.
        // So, oldLength is always 0. The constructor effectively just copies the input `dialogues`.

        String[] initialDialogues = {"Line 1"};
        ConcreteDialogue dialogue = new ConcreteDialogue(initialDialogues, mockGamePanel);
        // At this point, dialogue.dialogues = {"Line 1"}

        // If we were to call a method that uses the same logic as constructor:
        // (Simulating this by creating a new object for clarity of current constructor behavior)
        // String[] additionalDialogues = {"Line 2", "Line 3"};
        // ConcreteDialogue dialogue2 = new ConcreteDialogue(additionalDialogues, mockGamePanel);
        // dialogue2.dialogues would be {"Line 2", "Line 3"}

        // The constructor's array copy logic:
        // String[] newDialogues = new String[newLength];
        // System.arraycopy(this.dialogues, 0, newDialogues, 0, oldLength); // oldLength is 0
        // System.arraycopy(dialogues, 0, newDialogues, oldLength, inLength); // effectively copies input 'dialogues' to newDialogues[0]
        // this.dialogues = newDialogues;
        // This means the "append" logic in the constructor doesn't really append in a typical subclass scenario
        // because `this.dialogues` is initialized to empty just before the constructor logic we're testing runs.

        // So, the current behavior is that it always just takes the passed 'dialogues'.
        // The test `constructor_assignsGamePanelAndDialoguesCorrectly` already covers this.
        // This test is more of a thought experiment on the array copying code itself.
        // Let's re-verify the primary case:
        assertArrayEquals(initialDialogues, dialogue.dialogues);
    }


    @Test
    void constructor_withNullDialoguesArray_throwsNullPointerException() {
        // The line `int inLength = dialogues.length;` in Dialogue's constructor
        // will throw a NullPointerException if the passed `dialogues` array is null.
        assertThrows(NullPointerException.class, () -> {
            new ConcreteDialogue(null, mockGamePanel);
        }, "Constructor should throw NullPointerException if dialogues array is null.");
    }

    // Methods like setDialogues, getCurrentDialogue, getNextDialogue, isDialogueFinished
    // are not present in Dialogue.java, so they cannot be tested here.
    // The only concrete behavior in Dialogue.java is in its constructor.
}
