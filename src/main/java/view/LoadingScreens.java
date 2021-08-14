package view;

import javax.swing.*;
import java.awt.*;

/**
 * Loading Screens Class.
 */
public class LoadingScreens {

    // Image file paths
    private static final String SPLASH_SCREEN = "/splashscreen.png";
    private static final String LOADING_SCREEN = "/loadingscreen.png";

    /**
     * Show the splash screen for 10 seconds.
     */
    public static void showSplashScreen() {
        showScreen(SPLASH_SCREEN, 10000);
    }

    /**
     * Show the loading screen for 3 seconds.
     */
    public static void showLoadingScreen() {
        showScreen(LOADING_SCREEN, 3000);
    }

    /**
     * Show the a given image on a window in the middle of the screen.
     * Code adapted from:
     * https://www.tutorialspoint.com/how-can-we-implement-a-splash-screen-using-jwindow-in-java
     */
    private static void showScreen(String imagePath, int time) {
        // Create the loading window
        JWindow loadingScreen = new JWindow();
        ImageIcon imageIcon = new ImageIcon(SetupOptionsScreen.class.getResource(imagePath));
        loadingScreen.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());

        // Centre the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - imageIcon.getIconWidth()) / 2;
        int y = (screenSize.height - imageIcon.getIconHeight()) / 2;
        loadingScreen.setLocation(x, y);
        loadingScreen.setVisible(true);

        // Draw the loading screen graphics
        Graphics graphics = loadingScreen.getGraphics();
        loadingScreen.paint(graphics);
        graphics.drawImage(imageIcon.getImage(), 0, 0, loadingScreen);

        // Display the loading screen
        try {
            Thread.sleep(time);
            loadingScreen.dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
