package view;

import controller.AppController;
import edu.stanford.nlp.pipeline.CoreDocument;
import model.FeedbackDocument;
import nlp.BasicPipelineExample;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SentimentViewer extends JFrame {

    private AppController controller;
    private JPanel documentPanel;
    private JTextArea textArea;
    private JLabel titleLabel;

    private Map<String, Color> sentimentColourMap = Collections.unmodifiableMap(new HashMap<String, Color>() {{
        put("Very positive", Color.GREEN);
        put("Positive", Color.GREEN);
        put("Neutral", Color.WHITE);
        put("Negative", Color.RED);
        put("Very negative", Color.RED);
    }});

    public SentimentViewer(AppController controller, FeedbackDocument feedbackDocument) {
        this.controller = controller;
        this.documentPanel = new JPanel(new BorderLayout());
        this.documentPanel.setBorder(BorderCreator.createAllSidesEmptyBorder(50));

        this.textArea = new JTextArea();
        this.textArea.setBorder(BorderCreator.createAllSidesEmptyBorder(20));
        this.textArea.setEditable(false);

        this.titleLabel = new JLabel("Sentiment Overview of Document for: " + controller.getCurrentDocInView());
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(new EmptyBorder(0, 0,20,20));

        this.documentPanel.add(titleLabel, BorderLayout.PAGE_START);
        this.documentPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        this.add(documentPanel);


        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(700, 900);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (screenSize.width - this.getWidth())/2;
        int y = (screenSize.height - this.getHeight())/2;
        this.setLocation(x, y);

        displaySentimentOfDocument(feedbackDocument);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        //SentimentViewer sentimentViewer = new SentimentViewer("That is great work! \n Hello. \n That is bad work.");
//        sentimentViewer.displaySentimentOfDocument();
    }

    public void displaySentimentOfDocument(FeedbackDocument feedbackDocument) {
        AtomicInteger lineNum = new AtomicInteger();
        Highlighter highlighter = textArea.getHighlighter();

        feedbackDocument.getHeadings().forEach(heading -> {
            // Heading
            textArea.append(controller.getHeadingStyle() + heading + "\n");
            lineNum.getAndIncrement();

            // Underline heading if required
            String underlineStyle = controller.getUnderlineStyle();
            if (!underlineStyle.isEmpty()){
                for (int i = 0; i < controller.getHeadingStyle().length() + heading.length(); i++) {
                    textArea.append(underlineStyle);
                }
            }
            textArea.append("\n");
            lineNum.getAndIncrement();

            // Section data
            String data = feedbackDocument.getHeadingData(heading).replace("\n", ".");
            CoreDocument sentimentForText = controller.getSentimentForText(data);
            sentimentForText.sentences().forEach(sentence -> {

                if (!sentence.text().startsWith(".") && !sentence.text().trim().equals("-")) {
                    System.out.println("Line: " + sentence.text() + " - " + sentence.sentiment() + "\n");
                    textArea.append(sentence.text() + "\n");

                    // Highlighting code adapted from:
                    // https://stackoverflow.com/questions/20341719/how-to-highlight-a-single-word-in-a-jtextarea

                    Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(sentimentColourMap.get(sentence.sentiment()));

                    try {
                        int p0 = textArea.getLineStartOffset(lineNum.get());
                        int p1 = p0 + sentence.text().length();
                        highlighter.addHighlight(p0, p1, painter );
                        lineNum.getAndIncrement();
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            });

            // End section spacing
            for (int i = 0; i < controller.getLineSpacing(); i++) {
                textArea.append("\n");
                lineNum.getAndIncrement();
            }
            textArea.append("\n");
            lineNum.getAndIncrement();
        });
//        feedbackDocument.getHeadings().forEach(heading -> {
//            try {
//                // Heading
//                writer.write(assignment.getHeadingStyle() + heading);
//                writer.newLine();
//
//                // Underline heading if required
//                String underlineStyle = assignment.getUnderlineStyle();
//                if (!underlineStyle.isEmpty()){
//                    for (int i = 0; i < assignment.getHeadingStyle().length() + heading.length(); i++) {
//                        writer.write(underlineStyle);
//                    }
//                }
//
//                // Data
//                writer.newLine();
//                writer.write(feedbackDocument.getHeadingData(heading));
//
//                // End section spacing
//                for (int i = 0; i < assignment.getLineSpacing(); i++) {
//                    writer.newLine();
//                }
//                writer.newLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }
}