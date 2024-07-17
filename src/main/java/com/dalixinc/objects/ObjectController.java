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
}

