package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class FeedbackScreen {

    private JFrame feedbackScreen;
    private JPanel feedbackScreenPanel;

    private JSplitPane documentsAndEditorSplitPane;
    private JEditorPane editorPane;
    private JList feedbackDocsList;

    private JSplitPane suggestionsAndControlsSplitPane;
    private JPanel suggestionsPanel;
    private JPanel controlPanel;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem saveOption;
    private JMenuItem loadOption;
    private JMenuItem exportOption;


    private void setupMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        saveOption = new JMenuItem("Save");
        loadOption = new JMenuItem("Load");
        exportOption = new JMenuItem("Export");

        fileMenu.add(saveOption);
        fileMenu.add(loadOption);
        fileMenu.add(exportOption);

        menuBar.add(fileMenu);

        feedbackScreenPanel.add(menuBar, BorderLayout.PAGE_START);
    }


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
        setupMenuBar();
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

        JScrollPane listScrollPane = new JScrollPane(feedbackDocsList);
        JScrollPane editorScrollPane = new JScrollPane(editorPane);

        documentsAndEditorSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, editorScrollPane);
        documentsAndEditorSplitPane.setOneTouchExpandable(true);
        documentsAndEditorSplitPane.setDividerLocation(0.33);

        listScrollPane.setMinimumSize(new Dimension(250, 700));
        editorScrollPane.setMinimumSize(new Dimension(600, 700));
        documentsAndEditorSplitPane.setPreferredSize(new Dimension(900, 800));


        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 1, 20, 20));
        JLabel phraseEntryLabel = new JLabel("Enter a phrase to store:");
        JTextArea phraseEntryArea = new JTextArea();
        JButton phraseSubmitButton = new JButton("Submit Phrase");
        phraseEntryArea.setSize(new Dimension(200, 100));
        controlPanel.add(phraseEntryLabel);
        controlPanel.add(phraseEntryArea);
        controlPanel.add(phraseSubmitButton);

        suggestionsPanel = new JPanel();
        suggestionsPanel.setLayout(new BoxLayout(suggestionsPanel, BoxLayout.PAGE_AXIS));

        phraseSubmitButton.addActionListener(e -> {
            suggestionsPanel.add(new JLabel(phraseEntryArea.getText()));
            suggestionsPanel.add(new JSeparator());
            feedbackScreen.revalidate();
            feedbackScreen.repaint();
        });





        JScrollPane suggestionsScrollPane = new JScrollPane(suggestionsPanel);
        suggestionsScrollPane.setViewportView(suggestionsPanel);
        suggestionsAndControlsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, controlPanel, suggestionsScrollPane);
        suggestionsAndControlsSplitPane.setOneTouchExpandable(true);
        suggestionsAndControlsSplitPane.setDividerLocation(0.33);
        controlPanel.setMinimumSize(new Dimension(250, 300));
        suggestionsScrollPane.setMinimumSize(new Dimension(250, 300));
        suggestionsAndControlsSplitPane.setPreferredSize(new Dimension(300, 800));

        feedbackScreenPanel.add(documentsAndEditorSplitPane, BorderLayout.CENTER);
        feedbackScreenPanel.add(suggestionsAndControlsSplitPane, BorderLayout.LINE_END);
        feedbackScreen.add(feedbackScreenPanel);
    }
}
