package view;

import controller.IAppController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;
import java.util.Map;

/**
 * Document Viewer Class.
 */
public class DocumentViewer extends JFrame {

    // Instance variables
    protected IAppController controller;
    protected JPanel documentPanel;
    protected JTextArea textArea;
    protected JLabel titleLabel;

    /**
     * Constructor.
     *
     * @param controller The controller.
     * @param title      The document title.
     */
    public DocumentViewer(IAppController controller, String title) {
        this.controller = controller;

        // Setup components
        setupDocumentViewerScreen();
        setupTextArea();
        setupTitleLabel(title);
        setupDocumentPanel();

        // Set visibility
        this.setVisible(true);
    }

    /**
     * Setup the document viewer screen.
     */
    protected void setupDocumentViewerScreen() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(700, 900);

        // Centre the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
    }

    /**
     * Setup the document panel.
     */
    protected void setupDocumentPanel() {
        this.documentPanel = new JPanel(new BorderLayout());
        this.documentPanel.setBorder(BorderCreator.createAllSidesEmptyBorder(50));
        this.documentPanel.add(this.titleLabel, BorderLayout.PAGE_START);
        this.documentPanel.add(new JScrollPane(this.textArea), BorderLayout.CENTER);
        this.add(this.documentPanel);
    }

    /**
     * Setup the title label.
     *
     * @param title The title of the document.
     */
    protected void setupTitleLabel(String title) {
        this.titleLabel = new JLabel(title);
        this.titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
        this.titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.titleLabel.setBorder(new EmptyBorder(0, 0, 20, 20));
    }

    /**
     * Setup the text area.
     */
    protected void setupTextArea() {
        this.textArea = new JTextArea();
        this.textArea.setBorder(BorderCreator.createAllSidesEmptyBorder(20));
        this.textArea.setEditable(false);
    }

    public void displayData(Map<String, List<String>> data, List<String> dataHeadings) {
        dataHeadings.forEach(heading -> {
            // Heading
            this.textArea.append(this.controller.getHeadingStyle() + heading + "\n");

            // Underline heading if required
            String underlineStyle = this.controller.getUnderlineStyle();
            if (!underlineStyle.isEmpty()) {
                for (int i = 0; i < this.controller.getHeadingStyle().length() + heading.length(); i++) {
                    this.textArea.append(underlineStyle);
                }
            }
            this.textArea.append("\n");

            // Section data
            data.get(heading).forEach(line -> {
                this.textArea.append(this.controller.getLineMarker() + line);
                this.textArea.append("\n");
            });

            // End section spacing
            for (int i = 0; i < this.controller.getLineSpacing(); i++) {
                this.textArea.append("\n");
            }
            this.textArea.append("\n");
        });
    }

}
