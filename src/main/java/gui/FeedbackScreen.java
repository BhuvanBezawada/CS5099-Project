package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class FeedbackScreen {

    private JFrame feedbackScreen;

    private JPanel feedbackScreenPanel;
    private JEditorPane editorPane;

    private JSplitPane documentsAndEditorSplitPane;
    private JPanel rightPanel;

    private JList feedbackDocsList;

    public FeedbackScreen() {
        feedbackScreen = new JFrame("Feedback Composition");
        feedbackScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        feedbackScreen.setSize(1200, 800);

        createEmptyFeedbackDocs();
        setupFeedbackScreenComponents();
        displayFeedbackScreen();
    }

    public void createEmptyFeedbackDocs() {

        String[] docNames = new String[20];
        for (int i = 0; i < 20; i++) {
            docNames[i] = "Number-" + i;
        }

        feedbackDocsList = new JList(docNames);
        feedbackDocsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feedbackDocsList.setSelectedIndex(0);
        feedbackDocsList.addListSelectionListener(e -> {
            System.out.println("New doc selected!");
            editorPane.setText("File: " + feedbackDocsList.getSelectedValue().toString());

        });
    }

    public void setupFeedbackScreenComponents() {
        setupPanels();
        //setupLeftPanel();
    }

    public void displayFeedbackScreen() {
        feedbackScreen.setVisible(true);
    }

//    public void setupLeftPanel() {
//        JButton testButton = new JButton("Test");
//
//        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
//        leftPanel.add(testButton);
//
//        testButton.addActionListener(e -> {
//            JTextPane textPane = new JTextPane();
//            textPane.setText("A new doc!");
//            textPane.setPreferredSize(new Dimension(200, 80));
//            textPane.setBackground(Color.WHITE);
//            leftPanel.add(textPane);
//            feedbackScreen.repaint();
//        });
//
//    }

    public void setupPanels() {
        feedbackScreenPanel = new JPanel();
        feedbackScreenPanel.setLayout(new BorderLayout());

        editorPane = new JEditorPane();
//        leftPanel = new JPanel();
//        leftPanel.setPreferredSize(new Dimension(280, 740));
//        leftPanel.setBackground(Color.CYAN);
//
//        centerPanel = new JPanel();
//        centerPanel.setPreferredSize(new Dimension(600, 740));
//        centerPanel.setBackground(Color.WHITE);


        JScrollPane listScrollPane = new JScrollPane(feedbackDocsList);
        JScrollPane editorScrollPane = new JScrollPane(editorPane);

        documentsAndEditorSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, editorScrollPane);
        documentsAndEditorSplitPane.setOneTouchExpandable(true);
        documentsAndEditorSplitPane.setDividerLocation(0.33);

        listScrollPane.setMinimumSize(new Dimension(250, 700));
        editorScrollPane.setMinimumSize(new Dimension(600, 700));
        documentsAndEditorSplitPane.setPreferredSize(new Dimension(900, 800));


        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(280, 740));
        rightPanel.setBackground(Color.CYAN);

//        feedbackScreenPanel.add(leftPanel, BorderLayout.LINE_START);
//        feedbackScreenPanel.add(centerPanel, BorderLayout.CENTER);
        feedbackScreenPanel.add(documentsAndEditorSplitPane, BorderLayout.LINE_START);
        feedbackScreenPanel.add(rightPanel, BorderLayout.LINE_END);
        feedbackScreen.add(feedbackScreenPanel);
    }
}
