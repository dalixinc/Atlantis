package com.dalixinc.main;

import java.awt.*;

public class CollisionChecker {

    private final GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean checkCollision(Rectangle me, Rectangle them){
        if (me.intersects(them )) {
            gamePanel.player.isDead = true;
            /*System.out.println("Collision detected! - Player dead: : " + gamePanel.player.isDead);
            System.out.println("Collision detected! - x: " + me.x + " y: " + me.y + " - w: " + me.width + " h: " + me.height);
            System.out.println("Collision detected! - x: " + them.x + " y: " + them.y + " - w: " + them.width + " h: " + them.height);*/
            return true;
        }
        else return false;
    }

    public boolean checkObjectCollision(Rectangle me, Rectangle them){
        if (me.intersects(them )) {
            return true;
        }
        else return false;
    }


}
