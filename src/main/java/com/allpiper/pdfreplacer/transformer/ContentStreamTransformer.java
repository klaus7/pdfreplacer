package com.allpiper.pdfreplacer.transformer;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public interface ContentStreamTransformer {

    void transform(PDPageContentStream cs) throws IOException;

}
