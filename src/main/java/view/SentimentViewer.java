package view;

import controller.IAppController;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import model.FeedbackDocument;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Sentiment Viewer Class.
 */
public class SentimentViewer extends DocumentViewer {

    // Sentiment and colour map
    private static final Map<String, Color> SENTIMENT_COLOUR_MAP = Collections.unmodifiableMap(new HashMap<String, Color>() {{
        put("Very positive", Color.GREEN);
        put("Positive", Color.GREEN);
        put("Neutral", Color.WHITE);
        put("Negative", Color.RED);
        put("Very negative", Color.RED);
    }});

    /**
     * Constructor.
     *
     * @param controller The controller.
     * @param title      The title of the document.
     */
    public SentimentViewer(IAppController controller, String title) {
        super(controller, title);
    }

    /**
     * Display the data.
     *
     * @param feedbackDocument The feedback document to display.
     */
    public void displayData(FeedbackDocument feedbackDocument) {
        int lineNum = 0;
        Highlighter highlighter = this.textArea.getHighlighter();

        // Loop through each heading
        for (String heading : feedbackDocument.getHeadings()) {
            // Heading
            this.textArea.append(this.controller.getHeadingStyle() + heading + "\n");
            lineNum++;

            // Underline heading if required
            String underlineStyle = this.controller.getUnderlineStyle();
            if (!underlineStyle.isEmpty()) {
                for (int i = 0; i < this.controller.getHeadingStyle().length() + heading.length(); i++) {
                    this.textArea.append(underlineStyle);
                }
            }
            this.textArea.append("\n");
            lineNum++;

            // Section data
            String data = feedbackDocument.getHeadingData(heading).replace("\n", ".");
            CoreDocument sentimentForText = this.controller.getSentimentForText(data);

            // Loop through each sentence in the section
            for (CoreSentence sentence : sentimentForText.sentences()) {
                // Don't process empty sentences
                if (!sentence.text().startsWith(".") && !sentence.text().trim().equals("-")) {
                    // Write the sentence
                    this.textArea.append(sentence.text() + "\n");

                    // Highlighting code adapted from:
                    // https://stackoverflow.com/questions/20341719/how-to-highlight-a-single-word-in-a-jtextarea
                    Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(SENTIMENT_COLOUR_MAP.get(sentence.sentiment()));
                    try {
                        int p0 = this.textArea.getLineStartOffset(lineNum);
                        int p1 = p0 + sentence.text().length();
                        highlighter.addHighlight(p0, p1, painter);
                        lineNum++;
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            }

            // End section spacing
            for (int i = 0; i < this.controller.getLineSpacing(); i++) {
                this.textArea.append("\n");
                lineNum++;
            }
            this.textArea.append("\n");
            lineNum++;
        }
    }

}
