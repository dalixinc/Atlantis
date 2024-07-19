package com.dalixinc.main;


public enum eGAME_STATE {
        MAIN_MENU("Main Menu"),
        PLAY_GAME("Playing"),
        PAUSE_GAME("Pause"),
        DIALOGUE("Dialogue"),
        GAME_OVER("Hero is dead");

    private String state;

    eGAME_STATE(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
