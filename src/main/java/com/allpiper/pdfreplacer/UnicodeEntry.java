package com.allpiper.pdfreplacer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.pdfbox.util.Matrix;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
@Data
@AllArgsConstructor
class UnicodeEntry {
    String unicode;
    Matrix textRenderingMatrix;
}
