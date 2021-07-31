package view;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public abstract class BorderCreator {

    public static final int PADDING_10_PIXELS = 10;
    public static final int PADDING_20_PIXELS = 20;

    public static Border createEmptyBorder(int padding) {
        return new EmptyBorder(padding, padding, 0, padding);
    }

    public static Border createAllSidesEmptyBorder(int padding) {
        return new EmptyBorder(padding, padding, padding, padding);
    }


}
