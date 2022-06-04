package com.github.ScipioAM.scipio_utils_net.mail;

import com.github.ScipioAM.scipio_utils_net.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 邮件信息
 *
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/19
 */
public class MailInfo {

    /** [必填]编码字符集，默认UTF-8 */
    private String charset = "utf-8";

    /** [必填]邮件内容类型，默认为text/plain类型 */
    private MimeType mimeType = MimeType.TEXT_PLAIN;

    /** 发件人 */
    private String from;

    /** [必填]收件人 */
    private List<String> to;

    /** 抄送 */
    private List<String> cc;

    /** 密送 */
    private List<String> bcc;

    /** [必填]邮件主题 */
    private String subject;

    /** 邮件正文 */
    private String content;

    /** 附件 */
    private List<File> attachments;

    /** 发送日期 */
    private Date sentDate;

    /** smtp协议的自定义头信息 */
    private Map<String,String> headers;

    public String getCharset() {
        return charset;
    }

    public MailInfo setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public MailInfo setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public MailInfo setFrom(String from) {
        this.from = from;
        return this;
    }

    public List<String> getTo() {
        return to;
    }

    public MailInfo setTo(List<String> to) {
        this.to = to;
        return this;
    }

    public MailInfo addTo(String toEmailAddress) {
        if(to == null) {
            to = new ArrayList<>();
        }
        to.add(toEmailAddress);
        return this;
    }

    public List<String> getCc() {
        return cc;
    }

    public MailInfo setCc(List<String> cc) {
        this.cc = cc;
        return this;
    }

    public MailInfo addCd(String ccEmailAddress) {
        if(cc == null) {
            cc = new ArrayList<>();
        }
        cc.add(ccEmailAddress);
        return this;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public MailInfo setBcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public MailInfo addBcc(String bccEmailAddress) {
        if(bcc == null) {
            bcc = new ArrayList<>();
        }
        bcc.add(bccEmailAddress);
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public MailInfo setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MailInfo setContent(String content) {
        this.content = content;
        return this;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public MailInfo setAttachments(List<File> attachments) {
        this.attachments = attachments;
        return this;
    }

    public MailInfo addAttachment(File attachment) {
        if(attachments == null) {
            attachments = new ArrayList<>();
        }
        attachments.add(attachment);
        return this;
    }

    public MailInfo addAttachment(String attachmentFile) {
        if(attachments == null) {
            attachments = new ArrayList<>();
        }
        attachments.add(new File(attachmentFile));
        return this;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public MailInfo setSentDate(Date sentDate) {
        this.sentDate = sentDate;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public MailInfo setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
}
