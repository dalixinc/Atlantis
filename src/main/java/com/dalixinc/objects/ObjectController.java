package com.dalixinc.objects;

import com.dalixinc.main.GamePanel;

public class ObjectController {

    GamePanel gamePanel;

    public ObjectController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setBeacon() {
        gamePanel.gameObjects[0] = new ObjBeacon(gamePanel);
        //gamePanel.gameObjects[0].screenX = 680;
        gamePanel.gameObjects[0].screenY = 96;
    }

    public void setSpur() {
        // Todo - insert collision effects
        gamePanel.gameObjects[1] = new TriganSpur(gamePanel);
        gamePanel.gameObjects[1].screenX = 680;
        gamePanel.gameObjects[1].screenY = 96;
    }
}

