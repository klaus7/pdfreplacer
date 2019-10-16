package com.allpiper.pdfreplacer;

import java.util.ArrayList;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public class PDFTextLocations extends ArrayList<PDFTextSearchLocation> {

    public boolean textFoundIn(String text) {
        for (PDFTextSearchLocation location : this) {
            if (text.contains(location.getSearchText())) {
                return true;
            }
        }
        return false;
    }
}
