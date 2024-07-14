package com.dalixinc.main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class eGAME_STATETest {

    @Test
    public void testStates() {

        for (eGAME_STATE state : eGAME_STATE.values()) {
            System.out.println(state + ": " + state.getState());
        }

        assertEquals("Main Menu", eGAME_STATE.MAIN_MENU.getState());
        assertEquals("Playing", eGAME_STATE.PLAY_GAME.getState());
        assertEquals("Pause", eGAME_STATE.PAUSE_GAME.getState());
        assertEquals("Hero is dead", eGAME_STATE.GAME_OVER.getState());
    }
}