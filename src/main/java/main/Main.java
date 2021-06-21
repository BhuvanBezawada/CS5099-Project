package main;

import view.StartScreen;

import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        System.out.println("CS 5099 Project - Feedback Helper Tool");
        Main mainClass = new Main();
        mainClass.setupStartScreen();
    }

    public void setupStartScreen() {
        StartScreen startScreen = new StartScreen(this);
        JFrame mainFrame = new JFrame("Feedback Helper Tool");

        mainFrame.setSize(800, 600);
        mainFrame.setContentPane(startScreen.getStartScreenJPanel());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }
}
