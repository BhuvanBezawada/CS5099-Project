package view;

import javax.swing.*;
import java.util.List;

public class PreviewPanel extends JPanel {

    private List<PreviewBox> previewBoxes;

    public PreviewPanel(List<PreviewBox> previewBoxes) {
        this.previewBoxes = previewBoxes;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setupPreviewBoxes();
        this.setVisible(true);
    }

    private void setupPreviewBoxes() {
        previewBoxes.forEach(this::add);
    }
}
