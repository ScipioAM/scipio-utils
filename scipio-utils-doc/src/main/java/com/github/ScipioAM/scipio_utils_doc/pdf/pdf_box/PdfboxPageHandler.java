package com.github.ScipioAM.scipio_utils_doc.pdf.pdf_box;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 * 针对每页的处理回调
 * @author Alan Scipio
 * @since 1.0.2-p2
 * @date 2021/8/26
 */
@FunctionalInterface
public interface PdfboxPageHandler {

    void onEveryPage(PDDocument document, PDPage page, int pageIndex, int totalPages) throws Exception;

}
