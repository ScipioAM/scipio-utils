package com.test;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author Alan Scipio
 * @date 2021/8/26
 */
public class PdfReadTest {

    @Test
    public void read_pdfbox0() {
        File file = new File("D:\\temp\\pdf-test.pdf");
        try {
            PDDocument document = Loader.loadPDF(file);
            PDFTextStripper textStripper = new PDFTextStripper();
            String docTxt = textStripper.getText(document);
            System.out.println(docTxt);
            document.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
