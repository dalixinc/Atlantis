package com.dalixinc.main;

import java.awt.*;
import javax.swing.*;

/** This is the main class for the Atlantis game engine.
 * <p>
 * © 2024 Dalix, Inc. All rights reserved.
 * Unauthorized copying or reproduction of this file is prohibited.
*/
public class AtlantisMain {

        public static final int spectrumWidthPixels = 256;
        public static final int spectrumHeightPixels = 192;

        public static final int SPECTRUM_SCALE_FACTOR = 5;

        public static void main(String[] args) {
           EventQueue.invokeLater(() -> {

               JFrame window = new JFrame();
               window.setSize(spectrumWidthPixels * SPECTRUM_SCALE_FACTOR,
                       spectrumHeightPixels * SPECTRUM_SCALE_FACTOR);

               window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               window.setResizable(false);
               window.setTitle("Atlantis");

               GamePanel gamePanel = new GamePanel();
               window.getContentPane().add(gamePanel);
               //window.pack();

               window.setLocationRelativeTo(null);
               window.setVisible(true);

               gamePanel.setUpGame();
               gamePanel.startGameThread();
           });
        }
}
