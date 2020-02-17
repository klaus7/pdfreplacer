package com.allpiper.pdfreplacer;

import com.allpiper.pdfreplacer.transformer.ContentStreamTransformer;
import com.allpiper.pdfreplacer.transformer.PDFTextSearchLocationTransformer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
@Data
@Accessors(chain = true)
public class PDFTextSearchLocation {

    /** The match mode to use for this location. */
    MatchMode matchMode = MatchMode.TRIM_EQUALS;

    /** Search text. */
    String searchText;

    /** Replacement text. */
    String replaceText = "";

    /** Replacement font. */
    PDFont font = PDType1Font.HELVETICA_BOLD;

    /** Replacement font size relative to found text matrix in the marker. */
    float fontSize = 1f;

    /** Custom replacement transformations. */
    Function<String, String> replacementTextTransformer = null;

    /** Custom replacement transformations. E.g. change maxWidth according to search result. */
    PDFTextSearchLocationTransformer locationTransformer = null;

    /** Custom content stream transformations. (e.g. font color) */
    ContentStreamTransformer contentStreamTransformer = cs -> {};

    /** Wrap at width. 0f for no wrapping. Width is determined by
     * {@link org.apache.pdfbox.pdmodel.font.PDFont#getStringWidth(java.lang.String)}
     * <p>Quote: The width of the string in 1/1000 units of text space.</p>
     * <p>Also according to the documentation the average font with: "The width is in 1000 unit of text space, ie 333 or 777"</p>
     * <p>E.g. ~50000 could be a good value to break lines at whole page width.</p>*/
    float maxWidth = 0f;

    /** True, if this location was found. */
    boolean found;

    /** Count of found markers for this location. */
    int foundCount;

    /** Search results. */
    List<PDFTextSearchResult> results = new ArrayList<>();

    public PDFTextSearchLocation(String searchText) {
        this.searchText = searchText;
    }

    public PDFTextSearchLocation(String searchText, String replaceText) {
        this.searchText = searchText;
        this.replaceText = replaceText;
    }

    public PDFTextSearchLocation(String searchText, String replaceText, float maxWidth) {
        this.searchText = searchText;
        this.replaceText = replaceText;
        this.maxWidth = maxWidth;
    }

    public PDFTextSearchLocation(MatchMode matchMode, String searchText, String replaceText) {
        this.matchMode = matchMode;
        this.searchText = searchText;
        this.replaceText = replaceText;
    }

    public PDFTextSearchLocation(MatchMode matchMode, String searchText, Function<String, String> replacementTextTransformer) {
        this.matchMode = matchMode;
        this.searchText = searchText;
        this.replacementTextTransformer = replacementTextTransformer;
    }

    public boolean findInText(String text, int currentPageNo, List<UnicodeEntry> unicodeEntries) {
        if (matchMode.matches(getSearchText(), text)) {
            setFound(true);
            foundCount++;
            getResults().add(new PDFTextSearchResult(
                    currentPageNo,
                    unicodeEntries.get(0).textRenderingMatrix,
                    text
            ));
            return true;
        }
        return false;

    }
}
