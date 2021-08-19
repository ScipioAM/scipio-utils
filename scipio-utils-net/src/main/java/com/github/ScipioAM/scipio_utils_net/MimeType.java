package com.github.ScipioAM.scipio_utils_net;

/**
 * MIME类型
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/19
 */
public enum MimeType {

    APPLICATION_ATOM_XML("application/atom+xml"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_SVG_XML("application/svg+xml"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_XML("application/xml"),
    APPLICATION_PDF("application/pdf"),

    MULTIPART_FORM_DATA("multipart/form-data"),

    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    TEXT_XML("text/xml"),
    TEXT_RICH_TEXT("text/richtext"),
    TEXT_MARKDOWN("text/markdown"),

    IMAGE_BMP("image/bmp"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_TIFF("image/tiff")
    ;

    public final String value;

    MimeType(String value) {
        this.value = value;
    }

}
