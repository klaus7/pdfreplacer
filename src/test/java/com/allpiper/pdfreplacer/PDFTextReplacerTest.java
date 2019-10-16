package com.allpiper.pdfreplacer;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
class PDFTextReplacerTest {

    static PDFTextLocations locations = new PDFTextLocations();

    public static void main(String args[]) throws Exception {

//        locations.add(new PDFTextSearchLocation("###NAME###", "Màx Müstermánn").setContentStreamTransformer(cs -> cs.setNonStrokingColor(Color.red)));
//        locations.add(new PDFTextSearchLocation("###ADRESSE###"));

        addTest("FIELD1");
        addTest("FIELD2");

        int endPage = 6;

        String doc = "src/test/resources/" + "test";
        File file = new File(doc + ".pdf");
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();

        try (COSDocument cosDoc = parser.getDocument()) {
            PDDocument document = new PDDocument(cosDoc);

            PDFTextReplacer locator = new PDFTextReplacer(document, locations);
            locator.setEndPage(endPage);

            locator.searchAndReplace();

            // Write out result
            for (PDFTextSearchLocation location : locations) {
                System.out.println(location);
            }

            document.setAllSecurityToBeRemoved(true);
            document.save(doc + "_processed.pdf");
            document.close();
        }

        toJPG(doc + "_processed.pdf", doc + "_processed.jpg");
    }

    private static void addTest(String s) {
        locations.add(new PDFTextSearchLocation("###" + s + "###", "Test " + s));
    }

    private static void addTestX(String s) {
        locations.add(new PDFTextSearchLocation("###" + s + "###", "X"));
    }


    public static void toJPG(String in, String out) throws Exception {
        PDDocument pd = PDDocument.load(new File(in));
        PDFRenderer pr = new PDFRenderer(pd);
        BufferedImage bi = pr.renderImageWithDPI(0, 300);
        ImageIO.write(bi, "JPEG", new File(out));
    }

    public static void toPNG(String in, String out) throws Exception {
        ImageIO.write(new PDFRenderer(PDDocument.load(new File(in))).renderImageWithDPI(0, 300), "PNG", new File(out));
    }


}
