package com.github.ScipioAM.scipio_utils_pdf.reader;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

/**
 * 基于Apache PDFBox的PDF读取者
 * @author Alan Scipio
 * @since 1.0.2-p2
 * @date 2021/8/26
 */
public class PdfboxReader implements PdfReader{

    public void read(File file, String password) throws Exception {
        PDDocument document = Loader.loadPDF(file,password);
        PDFTextStripper textStripper = new PDFTextStripper();
        String text = textStripper.getText(document);
        System.out.println(text);
    }

}
