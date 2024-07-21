package com.dalixinc.dialogue;

import com.dalixinc.main.GamePanel;
import com.dalixinc.utils.UtilFunctions;

public class OpeningDialogue extends Dialogue{

    UtilFunctions util = new UtilFunctions();
    private static String[] defaultDialogues = {
            "Welcome to the game!",
            "You are the hero of this story.",
            "You will face many challenges\nyou demented numpty!.",
            "Good luck!"
    };
    public OpeningDialogue(GamePanel gamePanel) {
         super(defaultDialogues, gamePanel);
    }

    public OpeningDialogue(String[] dialogues, GamePanel gamePanel)  {
        super(dialogues, gamePanel);
    }

        @Override
        public void printDialogue() {

            for (String dialogue : dialogues) {
                gamePanel.gameUI.currentDialogue = dialogue;
                System.out.println(dialogue);
                util.sleep(3000);
            }
        }
}
