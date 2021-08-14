package view;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Preview Panel Class.
 */
public class PreviewPanel extends JPanel {

    // Instance variables
    private List<PreviewBox> previewBoxes;
    private Map<String, PreviewBox> headingAndPreviewBoxMap;

    /**
     * Constructor.
     *
     * @param previewBoxes The list of preview boxes to display on the preview panel.
     */
    public PreviewPanel(List<PreviewBox> previewBoxes) {
        // Store the preview boxes and keep a map of them for easy access
        this.previewBoxes = previewBoxes;
        this.headingAndPreviewBoxMap = new HashMap<String, PreviewBox>();
        previewBoxes.forEach(previewBox -> {
            this.headingAndPreviewBoxMap.put(previewBox.getHeading(), previewBox);
        });

        // Set layout top to bottom
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Setup the preview boxes and make the panel visible
        setupPreviewBoxes();
        this.setVisible(true);
    }

    /**
     * Setup the preview boxes.
     */
    private void setupPreviewBoxes() {
        // Add all the boxes and highlight top preview box
        previewBoxes.forEach(this::add);
        previewBoxes.get(0).highlight();
    }

    /**
     * Highlight a given preview box.
     *
     * @param heading The heading of the preview box to highlight.
     */
    public void highlightPreviewBox(String heading) {
        headingAndPreviewBoxMap.get(heading).highlight();
    }

    /**
     * Unhighlight a given preview box.
     *
     * @param heading The heading of the preview box to unhighlight.
     */
    public void unhighlightPreviewBox(String heading) {
        headingAndPreviewBoxMap.get(heading).unhighlight();
    }

    /**
     * Update the contents of a given preview box.
     *
     * @param heading The heading of the preview box to update.
     * @param line    The new line text to display.
     * @param grade   The new grade to display.
     */
    public void updatePreviewBox(String heading, String line, double grade) {
        headingAndPreviewBoxMap.get(heading).setFirstLine(line);
        headingAndPreviewBoxMap.get(heading).setGrade(grade);
        revalidate();
        repaint();
    }

    /**
     * Update the contents of a given preview box.
     *
     * @param heading The heading of the preview box to update.
     * @param line    The new line text to display.
     */
    public void updatePreviewBoxLine(String heading, String line) {
        System.out.println("In update box line - new line to use is: " + line);
        headingAndPreviewBoxMap.get(heading).setFirstLine(line);
        revalidate();
        repaint();
    }

    /**
     * Update the contents of a given preview box.
     *
     * @param heading The heading of the preview box to update.
     * @param grade   The new grade to display.
     */
    public void updatePreviewBoxGrade(String heading, double grade) {
        headingAndPreviewBoxMap.get(heading).setGrade(grade);
        revalidate();
        repaint();
    }
}
