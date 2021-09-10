package com.github.ScipioAM.scipio_utils_doc.pdf.pdf_box;

import com.github.ScipioAM.scipio_utils_doc.pdf.IPdfReader;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;

/**
 * 基于Apache PDFBox的PDF读取者
 * @author Alan Scipio
 * @since 1.0.2-p2
 * @date 2021/8/26
 */
public class PdfboxReader implements IPdfReader {

    private PdfboxPageHandler pageHandler;

    //加载PDF
    public PDDocument loadPdf(File file, String password) throws IOException {
        return Loader.loadPDF(file,password);
    }

    public void read(File file, String password) throws Exception {
        //加载PDF
        PDDocument document = loadPdf(file, password);
        //遍历页面
        if(pageHandler != null) {
            int totalPages = document.getNumberOfPages();
            for(int i=0; i < totalPages; i++) {
                PDPage page = document.getPage(i);
                pageHandler.onEveryPage(document,page,i,totalPages);
            }
        }
    }//end of read()

}
