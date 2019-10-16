package com.allpiper.pdfreplacer;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public interface ContentStreamTransformer {

    void transform(PDPageContentStream cs) throws IOException;

}
