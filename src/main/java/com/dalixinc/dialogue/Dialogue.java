package com.dalixinc.dialogue;

import com.dalixinc.main.GamePanel;

public abstract class Dialogue {

    public GamePanel gamePanel;
    String[] dialogues = {};

    public Dialogue(String[] dialogues, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        // ADD PASSED IN DIALOGUES TO ARRAY - CLUNKY - ToDo: REFACTOR
        int inLength = dialogues.length;
        int oldLength = this.dialogues.length;
        int newLength = inLength + oldLength;

        String[] newDialogues = new String[newLength];
        System.arraycopy(this.dialogues, 0, newDialogues, 0, oldLength);
        System.arraycopy(dialogues, 0, newDialogues, oldLength, inLength);
        this.dialogues = newDialogues;
    }

    public abstract void printDialogue();

}
