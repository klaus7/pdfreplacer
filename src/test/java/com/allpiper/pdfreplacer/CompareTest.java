package com.allpiper.pdfreplacer;

import com.allpiper.pdfreplacer.util.ImageComparator;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public class CompareTest {

    @Test
    public void checkCorrectEmptyReplacement() throws Exception {
        PDFTextLocations locations = new PDFTextLocations();
        locations.add(new PDFTextSearchLocation("###FIELD1###"));
        locations.add(new PDFTextSearchLocation("###FIELD2###"));

        PDFParser parser = new PDFParser(new RandomAccessBuffer(getClass().getResourceAsStream("/test.pdf")));
        parser.parse();

        try (COSDocument cosDoc = parser.getDocument()) {
            PDDocument document = new PDDocument(cosDoc);

            PDFTextReplacer locator = new PDFTextReplacer(document, locations);

            locator.searchAndReplace();

            document.setAllSecurityToBeRemoved(true);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            document.save(output);
            byte[] outputBytes = output.toByteArray();
            document.close();

            // compare
            BufferedImage empty = new PDFRenderer(PDDocument.load(getClass().getResourceAsStream("/test_empty.pdf"))).renderImageWithDPI(0, 300);
            BufferedImage processed = new PDFRenderer(PDDocument.load(outputBytes)).renderImageWithDPI(0, 300);

            ImageComparator imageComparator = new ImageComparator(empty, processed);
            ImageComparator.Result difference = imageComparator.getDifference(false);
            System.out.println(difference);

            assertThat(difference.getPixelDifferenceCount()).isEqualTo(0);
        }
    }

    @Test
    public void checkRegexEmptyReplacement() throws Exception {
        PDFTextLocations locations = new PDFTextLocations();
        locations.add(new PDFTextSearchLocation(MatchMode.REGEX, "###.*", ""));

        PDFParser parser = new PDFParser(new RandomAccessBuffer(getClass().getResourceAsStream("/test.pdf")));
        parser.parse();

        try (COSDocument cosDoc = parser.getDocument()) {
            PDDocument document = new PDDocument(cosDoc);

            PDFTextReplacer locator = new PDFTextReplacer(document, locations);

            locator.searchAndReplace();

            document.setAllSecurityToBeRemoved(true);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            document.save(output);
            byte[] outputBytes = output.toByteArray();
            document.close();

            // compare
            BufferedImage empty = new PDFRenderer(PDDocument.load(getClass().getResourceAsStream("/test_empty.pdf"))).renderImageWithDPI(0, 300);
            BufferedImage processed = new PDFRenderer(PDDocument.load(outputBytes)).renderImageWithDPI(0, 300);

            ImageComparator imageComparator = new ImageComparator(empty, processed);
            ImageComparator.Result difference = imageComparator.getDifference(false);
            System.out.println(difference);

            assertThat(difference.getPixelDifferenceCount()).isEqualTo(0);
        }
    }

    @Test
    public void checkTransformerEmptyReplacement() throws Exception {
        PDFTextLocations locations = new PDFTextLocations();
        locations.add(new PDFTextSearchLocation(MatchMode.REGEX, "###.*", s -> ""));

        PDFParser parser = new PDFParser(new RandomAccessBuffer(getClass().getResourceAsStream("/test.pdf")));
        parser.parse();

        try (COSDocument cosDoc = parser.getDocument()) {
            PDDocument document = new PDDocument(cosDoc);

            PDFTextReplacer locator = new PDFTextReplacer(document, locations);

            locator.searchAndReplace();

            document.setAllSecurityToBeRemoved(true);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            document.save(output);
            byte[] outputBytes = output.toByteArray();
            document.close();

            // compare
            BufferedImage empty = new PDFRenderer(PDDocument.load(getClass().getResourceAsStream("/test_empty.pdf"))).renderImageWithDPI(0, 300);
            BufferedImage processed = new PDFRenderer(PDDocument.load(outputBytes)).renderImageWithDPI(0, 300);

            ImageComparator imageComparator = new ImageComparator(empty, processed);
            ImageComparator.Result difference = imageComparator.getDifference(false);
            System.out.println(difference);

            assertThat(difference.getPixelDifferenceCount()).isEqualTo(0);
        }
    }


}
