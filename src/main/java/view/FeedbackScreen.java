package view;

import database.DocumentDatabaseInterface;
import database.DocumentDatabaseManager;
import model.Assignment;
import org.dizitart.no2.Document;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

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

    private JPopupMenu editorPopupMenu;
    private String selectedText;



    // Should be moved to model?
    Assignment assignment;
    DocumentDatabaseInterface database;
    List<Document> documents;
    List<String> docNames;

    Map<String, Position> headingAndPosition;


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

    public FeedbackScreen(String assignmentFilePath) {
        feedbackScreen = new JFrame("Feedback Composition");
        feedbackScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        feedbackScreen.setSize(1200, 800);

        assignment = Assignment.loadAssignment(assignmentFilePath);
        System.out.println("Assignment object: " + assignment);
        System.out.println(assignment.getAssignmentName());

        database = new DocumentDatabaseManager();

        loadFeedbackDocs();
        setupFeedbackScreenComponents();
        displayFeedbackScreen();
//        createEmptyFeedbackDocs();
//        setupFeedbackScreenComponents();
//        displayFeedbackScreen();
    }


    public void loadFeedbackDocs() {
        headingAndPosition = new HashMap<String, Position>();

        database.openDocumentDatabaseConnection("./assignments.db");

        documents = database.loadFeedbackDocumentsForAssignment(assignment);
        docNames = new ArrayList<>();
        documents.forEach(document -> {
            docNames.add((String) document.get("studentId"));
        });

        feedbackDocsList = new JList(docNames.toArray());
        feedbackDocsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feedbackDocsList.setSelectedIndex(0);


        feedbackDocsList.addListSelectionListener(e -> {
            String selectedDoc = feedbackDocsList.getSelectedValue().toString();
            System.out.println("New doc selected! " + selectedDoc);

            StringBuilder studentFeedbackData = new StringBuilder();

            Document selectedDocument = documents.get(docNames.indexOf(selectedDoc));
            assignment.getAssignmentConfig().getAssignmentHeadings().forEach( heading -> {
                studentFeedbackData.append(heading);
                studentFeedbackData.append("\n");

                ((ArrayList<String>) selectedDocument.get(heading)).forEach(line -> {
                    studentFeedbackData.append(line);
                    studentFeedbackData.append("\n");
                });

                studentFeedbackData.append("\n\n");
            });

            editorPane.setText(studentFeedbackData.toString());




//            editorPane.setContentType("text/html");
//            editorPane.setText(
//                    "<html> " +
//                        "<head> </head>" +
//                        "<body id='body'>" +
//                            "<h1 id='title'> Title </h1>" +
//                            "<h2 id='heading-1'> Heading </h2>" +
//                            "<p> Some text here... </p>" +
//                            "<h2 id='heading-1'> Heading </h2>" +
//                            "<h2 id='heading-1'> Heading </h2>" +
//                        "</body>" +
//                    " </html>");
//            HTMLDocument document = (HTMLDocument) editorPane.getDocument();
//            document.addDocumentListener(new DocumentListener() {
//                @Override
//                public void insertUpdate(DocumentEvent e) {
//                    DocumentEvent.ElementChange elementChange = e.getChange(document.getElement("body"));
//                    if (elementChange != null) {
//                        Element[] childrenAdded = elementChange.getChildrenAdded();
//                        if (childrenAdded != null) {
//                            System.out.println("Changes: " + Arrays.toString(childrenAdded));
//                        } else {
//                            System.out.println("No changes");
//                        }
//                    }
//                }
//
//                @Override
//                public void removeUpdate(DocumentEvent e) {
//
//                }
//
//                @Override
//                public void changedUpdate(DocumentEvent e) {
//
//                }
//            });
//            System.out.println("Text from editor: " + document.getElement("body").getElementCount());

//            editorPane.setText(studentFeedbackData.toString());
//            Element defaultRootElement = editorPane.getDocument().getDefaultRootElement();
//            System.out.println("Root element: " + defaultRootElement.toString());
//            System.out.println("Root has " + defaultRootElement.getElementCount() + " elements");

        });
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




        editorPopupMenu = new JPopupMenu();
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        JMenuItem addPhrase = new JMenuItem("Add phrase to bank");
        JMenuItem excludePhrase = new JMenuItem("Exclude phrase from bank");

        editorPopupMenu.add(cut);
        editorPopupMenu.add(copy);
        editorPopupMenu.add(paste);
        editorPopupMenu.add(new JSeparator());
        editorPopupMenu.add(addPhrase);
        editorPopupMenu.add(excludePhrase);

        editorPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    editorPopupMenu.show(editorPane, e.getX(), e.getY());
                }
            }
        });

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedText = editorPane.getSelectedText();
                System.out.println("Copied text: " + selectedText);
            }
        });

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editorPane.getDocument().insertString(editorPane.getCaretPosition(), selectedText, null);
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
            }
        });

        addPhrase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedText = editorPane.getSelectedText();
                JTextArea addedPhrase = new JTextArea(selectedText);
                addedPhrase.setEditable(false);
                addedPhrase.setLineWrap(true);
                addedPhrase.setWrapStyleWord(true);
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
            }
        });

    }
}
