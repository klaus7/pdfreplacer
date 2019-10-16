package com.allpiper.pdfreplacer.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
@RequiredArgsConstructor
public class ImageComparator {

    final BufferedImage img1;
    final BufferedImage img2;

    @Setter int highlight = Color.MAGENTA.getRGB();

    public Result getDifference(boolean createImage) {
        final Result result = new Result();
        // convert images to pixel arrays
        final int w = img1.getWidth();
        final int h = img1.getHeight();
        result.totalPixelCount = w * h;
        final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
        final int[] p2 = img2.getRGB(0, 0, w, h, null, 0, w);
        // compare pixel by pixel. If different, highlight img1's pixel.
        for (int i = 0; i < p1.length; i++) {
            if (p1[i] != p2[i]) {
                p1[i] = highlight;
                result.pixelDifferenceCount++;
            }
        }
        result.pixelDifferenceRelation = (float) result.pixelDifferenceCount / result.totalPixelCount;
        if (createImage) {
            // save img1's pixels to a new BufferedImage, and return it
            // (May require TYPE_INT_ARGB)
            result.out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            result.out.setRGB(0, 0, w, h, p1, 0, w);
        }
        return result;
    }

    @ToString
    @Getter
    public static class Result {
        long totalPixelCount = 0;
        long pixelDifferenceCount = 0;
        float pixelDifferenceRelation = 0;

        BufferedImage out;
    }
}
