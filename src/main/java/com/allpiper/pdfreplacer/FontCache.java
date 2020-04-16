package com.allpiper.pdfreplacer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public class FontCache {

    public static final String CLASSPATH = "classpath:";
    public static final String FILE = "file:";
    public static final String INTERNAL = "internal:";

    public static final PDType1Font DEFAULT_FONT = PDType1Font.HELVETICA_BOLD;

    private final Map<String, PDFont> cacheMap = new HashMap<>();

    public PDFont get(String key, PDDocument doc) throws IOException {
        if (key == null) {
            return DEFAULT_FONT;
        }
        PDFont pdFont = cacheMap.get(key);
        if (pdFont == null) {
            if (key.startsWith(CLASSPATH)) {
                pdFont = PDType0Font.load(doc, FontCache.class.getResourceAsStream(key.substring(CLASSPATH.length())));
            } else if (key.startsWith(FILE)) {
                pdFont = PDType0Font.load(doc, new File(key.substring(FILE.length())));
            } else if (key.startsWith(INTERNAL)) {
                pdFont = DEFAULT_FONT;
                // TODO to be implemented..
            } else {
                try {
                    // Try classpath load
                    pdFont = PDType0Font.load(doc, FontCache.class.getResourceAsStream(key));
                } catch (IOException e) {
                    try {
                        // Try external load
                        pdFont = PDType0Font.load(doc, new File(key));
                    } catch (IOException ex) {
                        System.err.println("Font " + key + " couldn't be loaded! " + ex.getMessage());
                    }
                }
            }
            cacheMap.put(key, pdFont);
        }
        if (pdFont == null) {
            return PDType1Font.HELVETICA_BOLD;
        }
        return pdFont;
    }

    public void clear() {
        cacheMap.clear();
    }
}
