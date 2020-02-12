package com.allpiper.pdfreplacer.transformer;

import com.allpiper.pdfreplacer.PDFTextSearchLocation;
import com.allpiper.pdfreplacer.PDFTextSearchResult;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public interface PDFTextSearchLocationTransformer {
    PDFTextSearchLocation transform(PDFTextSearchLocation location, PDFTextSearchResult result);
}
