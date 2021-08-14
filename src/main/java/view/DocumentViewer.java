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
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DocumentViewer extends JFrame {

    protected AppController controller;
    protected JPanel documentPanel;
    protected JTextArea textArea;
    protected JLabel titleLabel;

    public DocumentViewer(AppController controller, String title) {
        this.controller = controller;

        setupDocumentViewerScreen();
        setupTextArea();
        setupTitleLabel(title);
        setupDocumentPanel();

        this.setVisible(true);
    }

    protected void setupDocumentViewerScreen() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(700, 900);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth())/2;
        int y = (screenSize.height - this.getHeight())/2;
        this.setLocation(x, y);
    }

    protected void setupDocumentPanel() {
        this.documentPanel = new JPanel(new BorderLayout());
        this.documentPanel.setBorder(BorderCreator.createAllSidesEmptyBorder(50));

        this.documentPanel.add(titleLabel, BorderLayout.PAGE_START);
        this.documentPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        this.add(documentPanel);
    }

    protected void setupTitleLabel(String title) {
        this.titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(new EmptyBorder(0, 0,20,20));
    }

    protected void setupTextArea() {
        this.textArea = new JTextArea();
        this.textArea.setBorder(BorderCreator.createAllSidesEmptyBorder(20));
        this.textArea.setEditable(false);
    }

    public void displayData(Map<String, List<String>> data, List<String> dataHeadings) {
        dataHeadings.forEach(heading -> {
            // Heading
            textArea.append(controller.getHeadingStyle() + heading + "\n");

            // Underline heading if required
            String underlineStyle = controller.getUnderlineStyle();
            if (!underlineStyle.isEmpty()){
                for (int i = 0; i < controller.getHeadingStyle().length() + heading.length(); i++) {
                    textArea.append(underlineStyle);
                }
            }
            textArea.append("\n");

            // Section data
            data.get(heading).forEach(line -> {
                textArea.append(controller.getLineMarker() + line);
                textArea.append("\n");
            });

            // End section spacing
            for (int i = 0; i < controller.getLineSpacing(); i++) {
                textArea.append("\n");
            }
            textArea.append("\n");
        });
    }
}
