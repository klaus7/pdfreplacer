package com.allpiper.pdfreplacer;

import lombok.Setter;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public class PDFTextReplacer extends PDFTextStripper {

    /** Search locations. */
    private final PDFTextLocations locations;

    /** Found unicode entries since last BT. */
    private List<UnicodeEntry> unicodeEntries = new ArrayList<>();

    private int beginTextTokenIdx = 0;

    /** All tokens. */
    private List tokens = new ArrayList<>();

    /** Zero based page to token map. */
    Map<Integer, List> tokensPerPage = new HashMap<>();

    /** If found text elements should be completely removed. */
    private boolean removeFoundTextElements = false;
    private boolean removeUnprocessedPages = false;

    @Setter
    private float multilineMatrixTranslationY = -1f;

    public PDFTextReplacer(PDDocument document, PDFTextLocations locations) throws IOException {
        super.setSortByPosition(true);
        this.document = document;
        this.locations = locations;
        this.output = new NoopWriter();
    }

    public void searchAndReplace() throws IOException {
        removeFoundTextElements = true;
        search();
        writeTokensWithoutFoundTextElements();
        addChangedText(document);
        if (removeUnprocessedPages) {
            removeUnprocessedPages();
        }
    }

    /** Used when modifying text elements is not wanted or there are faulty results.
     * @param appendableDocument The document where the text is added (without the marker) */
    public void searchAndAddToDifferentDocument(PDDocument appendableDocument) throws IOException {
        search();
        addChangedText(appendableDocument);
    }

    public void search() throws IOException {
        processPages(document.getDocumentCatalog().getPages());
    }

    public void writeTokensWithoutFoundTextElements() throws IOException {
        int numberOfPages = Math.min(getEndPage(), document.getNumberOfPages());
        for (int numPage = 0; numPage < numberOfPages; numPage++) {
            PDStream updatedStream = new PDStream(document);
            OutputStream out = createOutputStream(updatedStream);
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(tokensPerPage.get(numPage));
            out.close();

            PDPage page = document.getPage(numPage);
            page.setContents(updatedStream);
        }
    }

    protected OutputStream createOutputStream(PDStream updatedStream) throws IOException {
        return updatedStream.createOutputStream(); // COSName.FLATE_DECODE
    }

    public void addChangedText(PDDocument document) throws IOException {
        int fontSize = 1;
        for (PDFTextSearchLocation location : locations) {
            if (!location.isFound()) {
                continue;
            }
            List<PDFTextSearchResult> results = location.getResults();
            for (PDFTextSearchResult result : results) {
                String showText;
                if (location.replacementTextTransformer != null) {
                    showText = location.replacementTextTransformer.apply(result.text);
                } else {
                    showText = location.replaceText;
                }
                if (showText == null || "".equals(showText)) {
                    continue;
                }

                PDPage page = document.getPage(result.getPage() - 1);
                PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

                cs.setFont(location.font, fontSize);
                location.contentStreamTransformer.transform(cs);
                Matrix textMatrix = result.textMatrix;
                if (showText.contains("\n")) {

                    // Multiline processing
                    textMatrix = textMatrix.clone();

                    String[] lines = showText.split("\n");
                    for (String line : lines) {
                        cs.beginText();
                        cs.setTextMatrix(textMatrix);
                        cs.showText(line);
                        cs.endText();
                        textMatrix.translate(0f, multilineMatrixTranslationY);
                    }

                } else {
                    cs.beginText();
                    cs.setTextMatrix(textMatrix);
                    cs.showText(showText);
                    cs.endText();
                }

                cs.close();
            }
        }
    }

    public void removeUnprocessedPages() {
        for (int i = document.getNumberOfPages() - 1; i >= getEndPage(); i--) {
            document.removePage(i);
        }
    }

    @Override
    public void processPage(PDPage page) throws IOException {
        super.processPage(page);
        tokensPerPage.put(getCurrentPageNo() - 1, tokens);
        tokens = new ArrayList();
    }

    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        tokens.addAll(operands);
        tokens.add(operator);
        super.processOperator(operator, operands);
    }

    @Override
    protected void writePage() throws IOException {
        super.writePage();
    }

    @Override
    protected void operatorException(Operator operator, List<COSBase> operands, IOException e) throws IOException {
        super.operatorException(operator, operands, e);
    }

    @Override
    protected void unsupportedOperator(Operator operator, List<COSBase> operands) throws IOException {
        super.unsupportedOperator(operator, operands);
    }

    @Override
    public void beginText() throws IOException {
        unicodeEntries = new ArrayList<>();
        beginTextTokenIdx = tokens.size();
    }

    @Override
    public void endText() throws IOException {
        if (unicodeEntries.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (UnicodeEntry unicodeEntry : unicodeEntries) {
            sb.append(unicodeEntry.getUnicode());
        }
        String text = sb.toString();
        boolean found = false;
        for (PDFTextSearchLocation location : locations) {
            found |= location.findInText(text, getCurrentPageNo(), unicodeEntries);
        }

        if (removeFoundTextElements && found) {
            int removeTokenCount = tokens.size() - beginTextTokenIdx + 1;
            for (int i = 0; i < removeTokenCount; i++) {
                tokens.remove(tokens.size() - 1);
            }
        }
    }

    @Override
    protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, String unicode, Vector displacement) throws IOException {
        super.showGlyph(textRenderingMatrix, font, code, unicode, displacement);
        unicodeEntries.add(new UnicodeEntry(unicode, textRenderingMatrix));
    }

    public void setRemoveFoundTextElements(boolean removeFoundTextElements) {
        this.removeFoundTextElements = removeFoundTextElements;
    }

    public void setRemoveUnprocessedPages(boolean removeUnprocessedPages) {
        this.removeUnprocessedPages = removeUnprocessedPages;
    }

    private static class NoopWriter extends Writer {
        @Override public void write(char[] cbuf, int off, int len) throws IOException {}
        @Override public void flush() throws IOException {}
        @Override public void close() throws IOException {}
    }
}
