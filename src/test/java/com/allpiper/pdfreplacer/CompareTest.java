package com.allpiper.pdfreplacer;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public class CompareTest {

    public static void main(String[] args) throws Exception {
        PDFTextLocations locations = new PDFTextLocations();
        locations.add(new PDFTextSearchLocation("###FIELD1###"));
        locations.add(new PDFTextSearchLocation("###FIELD2###"));

        String doc = "src/test/resources/" + "test";
        File file = new File(doc + ".pdf");
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();

        try (COSDocument cosDoc = parser.getDocument()) {
            PDDocument document = new PDDocument(cosDoc);

            PDFTextReplacer locator = new PDFTextReplacer(document, locations);

            locator.searchAndReplace();

            document.setAllSecurityToBeRemoved(true);
            document.save(doc + "_processed.pdf");
            document.close();
        }

        BufferedImage empty = new PDFRenderer(PDDocument.load(new File(doc + "_empty.pdf"))).renderImageWithDPI(0, 300);
        BufferedImage processed = new PDFRenderer(PDDocument.load(new File(doc + "_processed.pdf"))).renderImageWithDPI(0, 300);
        ImageComparator imageComparator = new ImageComparator(empty, processed);
        ImageComparator.Result difference = imageComparator.getDifference(false);
        System.out.println(difference);

        assert difference.pixelDifferenceCount == 0;
    }


}
