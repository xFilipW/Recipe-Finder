package com.example.recipefinder.shared.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

public class PixelUtils {

    private static final int TOLERANCE = 10; // adjust if needed

    private static boolean isWhite(int pixel) {
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        return r >= 255 - TOLERANCE && g >= 255 - TOLERANCE && b >= 255 - TOLERANCE;
    }

    private static boolean isAllWhite(int[] pixels) {
        for (int pixel : pixels) {
            if (!isWhite(pixel)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the bitmap has a "white frame" by verifying that either
     * both the top and bottom rows are completely white,
     * OR the left and right columns are completely white.
     */
    public static boolean hasWhiteFrame(Bitmap bitmap) {
        if (bitmap == null) return false;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Get entire top row.
        int[] topPixels = new int[width];
        bitmap.getPixels(topPixels, 0, width, 0, 0, width, 1);
        boolean topWhite = isAllWhite(topPixels);

        // Get entire bottom row.
        int[] bottomPixels = new int[width];
        bitmap.getPixels(bottomPixels, 0, width, 0, height - 1, width, 1);
        boolean bottomWhite = isAllWhite(bottomPixels);

        // Get entire left column.
        int[] leftPixels = new int[height];
        bitmap.getPixels(leftPixels, 0, 1, 0, 0, 1, height);
        boolean leftWhite = isAllWhite(leftPixels);

        // Get entire right column.
        int[] rightPixels = new int[height];
        bitmap.getPixels(rightPixels, 0, 1, width - 1, 0, 1, height);
        boolean rightWhite = isAllWhite(rightPixels);

        // Treat the image as having a white frame if either the horizontal (top and bottom)
        // OR the vertical (left and right) lines are completely white.
        return (topWhite && bottomWhite) || (leftWhite && rightWhite);
    }
}

