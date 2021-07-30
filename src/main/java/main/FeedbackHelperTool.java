package main;

import controller.AppController;
import model.AppModel;
import model.IAppModel;
import view.AppView;
import view.IAppView;

import javax.swing.*;
import java.awt.*;

public class FeedbackHelperTool {

    public static void main(String[] args) {

        FeedbackHelperTool fht = new FeedbackHelperTool();
        fht.start();



    }

    public void start() {

        new Thread(this::showSplashScreen).start();

        AppModel model = new AppModel();
        AppController controller = new AppController(model);
        IAppView view = new AppView(controller);

        ((AppView) view).start();
    }

    private void showSplashScreen() {
        // Splash screen adapted from:
        // https://www.tutorialspoint.com/how-can-we-implement-a-splash-screen-using-jwindow-in-java

        JWindow splash = new JWindow();
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/splashscreen.png"));

        splash.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - imageIcon.getIconWidth())/2;
        int y = (screenSize.height - imageIcon.getIconHeight())/2;
        splash.setLocation(x, y);
        splash.setVisible(true);

        Graphics graphics = splash.getGraphics();
        splash.paint(graphics);
        graphics.drawImage(imageIcon.getImage(), 0, 0, splash);

        try {
            Thread.sleep(10000);
            splash.dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
