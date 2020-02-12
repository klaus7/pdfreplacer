package com.allpiper.pdfreplacer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.pdfbox.util.Matrix;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
@Data
@AllArgsConstructor
public class PDFTextSearchResult {
    /** A 1 based number representing the current page. */
    int page;
    Matrix textMatrix;
    String text;
}
