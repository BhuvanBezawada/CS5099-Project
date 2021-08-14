package view;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public abstract class BorderCreator {

    public static final int PADDING_10_PIXELS = 10;
    public static final int PADDING_20_PIXELS = 20;
    public static final int PADDING_50_PIXELS = 50;

    public static Border createEmptyBorderLeavingBottom(int padding) {
        return new EmptyBorder(padding, padding, 0, padding);
    }

    public static Border createEmptyBorderBottomOnly(int padding) {
        return new EmptyBorder(0, 0, padding, 0);
    }

    public static Border createEmptyBorderLeavingTop(int padding) {
        return new EmptyBorder(0, padding, padding, padding);
    }

    public static Border createAllSidesEmptyBorder(int padding) {
        return new EmptyBorder(padding, padding, padding, padding);
    }

    public static Border unselectedBorder() {
        return new CompoundBorder(
                new MatteBorder(1, 5, 1, 1, Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        );
    }

    public static Border selectedBorder() {
        return new CompoundBorder(
                new MatteBorder(1, 5, 1, 1, Color.GREEN),
                new EmptyBorder(10, 10, 10, 10)
        );
    }


}
