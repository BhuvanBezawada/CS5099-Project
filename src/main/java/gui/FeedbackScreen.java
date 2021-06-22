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

    private JTabbedPane suggestionsPanel;

    private JScrollPane customPanelScrollPane;
    private JPanel customPanel;
    private JPanel frequentlyUsedPanel;
    private JPanel insightsPanel;

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

        suggestionsPanel = new JTabbedPane();


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


        customPanel = new JPanel();
        customPanelScrollPane = new JScrollPane(customPanel);
        customPanelScrollPane.setPreferredSize(new Dimension(280, 300));
        frequentlyUsedPanel = new JPanel();
        insightsPanel = new JPanel();

        suggestionsPanel.addTab("Custom", customPanelScrollPane);
        suggestionsPanel.addTab("Frequently used", frequentlyUsedPanel);
        suggestionsPanel.addTab("Insights", insightsPanel);

        customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.PAGE_AXIS));

        phraseSubmitButton.addActionListener(e -> {
            JTextArea addedPhrase = new JTextArea(phraseEntryArea.getText());
            addedPhrase.setEditable(false);
            //addedPhrase.setRows(3);
            addedPhrase.setMaximumSize(new Dimension(250, 100));
            addedPhrase.setMinimumSize(new Dimension(250, 100));
            addedPhrase.setPreferredSize(new Dimension(250, 100));

            customPanel.add(addedPhrase);

            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(250, 10));
            customPanel.add(separator);

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
        suggestionsAndControlsSplitPane.setMaximumSize(new Dimension(600, 800));

        suggestionsPanel.setForeground(Color.BLACK);
        suggestionsPanel.setBackgroundAt(0, Color.BLUE);
        suggestionsPanel.addChangeListener(e -> {
            for (int i = 0; i< suggestionsPanel.getTabCount(); i++) {
                suggestionsPanel.setBackgroundAt(i, Color.LIGHT_GRAY);
            }
            suggestionsPanel.setBackgroundAt(suggestionsPanel.getSelectedIndex(), Color.BLUE);
        });

        feedbackScreenPanel.add(documentsAndEditorSplitPane, BorderLayout.CENTER);
        feedbackScreenPanel.add(suggestionsAndControlsSplitPane, BorderLayout.LINE_END);
        feedbackScreen.add(feedbackScreenPanel);
    }
}
