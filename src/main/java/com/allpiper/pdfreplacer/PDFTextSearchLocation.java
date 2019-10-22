package com.allpiper.pdfreplacer;

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

    /** Custom replacement transformations. */
    Function<String, String> replacementTextTransformer = null;

    /** Custom content stream transformations. (e.g. font color) */
    ContentStreamTransformer contentStreamTransformer = cs -> {};

    boolean found;
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
