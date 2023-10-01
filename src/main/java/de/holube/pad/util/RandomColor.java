package de.holube.pad.util;

import java.awt.*;
import java.util.Random;

public class RandomColor {

    private static final Random random = new Random();

    public static Color getBright() {
        Color color = get();
        int sum = color.getRed() + color.getGreen() + color.getBlue();
        if (sum < 300) {
            return color.brighter();
        }
        return color;
    }

    public static Color get() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }

}
