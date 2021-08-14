package view;

import controller.AppController;
import edu.stanford.nlp.pipeline.CoreDocument;
import model.FeedbackDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SentimentViewer extends DocumentViewer {

    private Map<String, Color> sentimentColourMap = Collections.unmodifiableMap(new HashMap<String, Color>() {{
        put("Very positive", Color.GREEN);
        put("Positive", Color.GREEN);
        put("Neutral", Color.WHITE);
        put("Negative", Color.RED);
        put("Very negative", Color.RED);
    }});

    public SentimentViewer(AppController controller, String title) {
        super(controller, title);
    }

    public void displayData(FeedbackDocument feedbackDocument) {
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
    }
}
