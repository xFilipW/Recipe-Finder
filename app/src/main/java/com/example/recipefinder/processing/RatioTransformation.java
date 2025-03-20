package com.example.recipefinder.processing;

import android.graphics.Bitmap;

public class RatioTransformation implements com.squareup.picasso.Transformation {
    private final int targetWidth;
    private final int targetHeight;

    // targetWidth is the screen width (or desired width)
    // targetHeight is computed to maintain a 3:2 ratio.
    public RatioTransformation(int targetWidth) {
        this.targetWidth = targetWidth;
        this.targetHeight = (targetWidth * 2) / 3;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap workingBitmap = source;
        // If the source image is smaller than desired, upscale it.
        if (source.getWidth() < targetWidth || source.getHeight() < targetHeight) {
            float scaleFactor = Math.max((float) targetWidth / source.getWidth(),
                    (float) targetHeight / source.getHeight());
            int scaledWidth = Math.round(source.getWidth() * scaleFactor);
            int scaledHeight = Math.round(source.getHeight() * scaleFactor);
            workingBitmap = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true);
            if (workingBitmap != source) {
                source.recycle();
            }
        }

        int width = workingBitmap.getWidth();
        int height = workingBitmap.getHeight();
        float currentRatio = (float) width / height;
        float desiredRatio = 3f / 2f;
        int cropWidth, cropHeight;

        // Determine whether to crop width or height to achieve 3:2 ratio.
        if (currentRatio > desiredRatio) {
            // Image is too wide: crop width.
            cropHeight = height;
            cropWidth = Math.round(height * desiredRatio);
        } else {
            // Image is too tall: crop height.
            cropWidth = width;
            cropHeight = Math.round(width / desiredRatio);
        }

        // Calculate offsets to crop the image centered.
        int xOffset = (width - cropWidth) / 2;
        int yOffset = (height - cropHeight) / 2;
        Bitmap cropped = Bitmap.createBitmap(workingBitmap, xOffset, yOffset, cropWidth, cropHeight);
        if (cropped != workingBitmap) {
            workingBitmap.recycle();
        }

        // Finally, scale the cropped image exactly to the target dimensions.
        Bitmap finalBitmap = Bitmap.createScaledBitmap(cropped, targetWidth, targetHeight, true);
        if (finalBitmap != cropped) {
            cropped.recycle();
        }
        return finalBitmap;
    }

    @Override
    public String key() {
        return "ratioTransformation_" + targetWidth;
    }
}

