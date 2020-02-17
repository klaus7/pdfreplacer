package com.allpiper.pdfreplacer.transformer;

import com.allpiper.pdfreplacer.PDFTextSearchLocation;
import com.allpiper.pdfreplacer.PDFTextSearchResult;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public interface PDFTextSearchLocationTransformer {

    /** Transform the location object.
     *
     * @param location the original {@link PDFTextSearchLocation}
     * @param result the result as {@link PDFTextSearchResult}
     * @return the transformed {@link PDFTextSearchLocation}
     */
    PDFTextSearchLocation transform(PDFTextSearchLocation location, PDFTextSearchResult result);
}
