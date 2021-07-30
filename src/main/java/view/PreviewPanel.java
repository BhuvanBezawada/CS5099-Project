package view;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreviewPanel extends JPanel {

    private List<PreviewBox> previewBoxes;
    private Map<String, PreviewBox> headingAndPreviewBoxMap;

    public PreviewPanel(List<PreviewBox> previewBoxes) {
        this.previewBoxes = previewBoxes;
        this.headingAndPreviewBoxMap = new HashMap<String, PreviewBox>();
        previewBoxes.forEach(previewBox -> {
            this.headingAndPreviewBoxMap.put(previewBox.getHeading(), previewBox);
        });

        // Highlight top preview box
        previewBoxes.get(0).highlight();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setupPreviewBoxes();
        this.setVisible(true);
    }

    private void setupPreviewBoxes() {
        previewBoxes.forEach(this::add);
    }

    public void highlightPreviewBox(String heading) {
        System.out.println("Highlighting preview box for: " + headingAndPreviewBoxMap.get(heading).getHeading());
        headingAndPreviewBoxMap.get(heading).highlight();
    }

    public void unhighlightPreviewBox(String heading) {
        System.out.println("Unhighlighting " + heading);
        headingAndPreviewBoxMap.get(heading).unhighlight();
    }

    public void updatePreviewBox(String heading, String line, double grade) {
        System.out.println("Updating preview box for: " + headingAndPreviewBoxMap.get(heading).getHeading());
        headingAndPreviewBoxMap.get(heading).setUniqueLine(line);
        headingAndPreviewBoxMap.get(heading).setGrade(grade);
    }
}
