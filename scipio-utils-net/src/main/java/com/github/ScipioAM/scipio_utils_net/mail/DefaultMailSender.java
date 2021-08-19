package com.github.ScipioAM.scipio_utils_net.mail;

import com.github.ScipioAM.scipio_utils_common.StringUtil;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 默认邮件发送者(javax-mail)
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/19
 */
public class DefaultMailSender implements MailSender {

    /**
     * 获取邮件服务器连接的会话对象
     * @param account 发送账号信息
     * @return 邮件服务器连接的会话对象
     */
    private Session getSession(MailAccount account) throws IllegalArgumentException {
        checkMailAccount(account);
        Properties properties  =  new Properties();
        //邮件发送时输出debug信息
        properties.setProperty("mail.debug", account.isDebug() + "");
        //发送邮件协议名称 这里使用的是smtp协议
        properties.setProperty("mail.transport.protocol", "smtp");
        //SMTP服务器地址
        properties.setProperty("mail.smtp.host", account.getSmtpHost());
        //SMTP服务器端口
        properties.setProperty("mail.smtp.port", account.getSmtpPort().toString());
        //SMTP服务器是否身份验证
        properties.setProperty("mail.smtp.auth", account.isAuth() + "");
        //默认开启ttls
        properties.setProperty("mail.smtp.starttls.enable", "true");
        //超时设置
        if(account.getTimeOut() != null && account.getTimeOut() > 0) {
            properties.setProperty("mail.smtp.connectiontimeout",account.getTimeOut().toString());//与邮件服务器建立连接的超时
            properties.setProperty("mail.smtp.writetimeout",account.getTimeOut().toString());//邮件发送时间限制
        }
        //开启ssl
        if(account.getSslFlag()){
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.port", account.getSmtpPort().toString());
            properties.setProperty("mail.smtp.ssl.enable", "true");
        }
        return Session.getDefaultInstance(properties);
    }

    /**
     * 创建邮件内容
     * @param session 与SMTP服务器连接的会话
     * @param mailInfo 邮件内容
     * @param accountUsername 发件人默认值（SMTP服务器的认证用户名）
     * @return 实际邮件内容
     * @throws MessagingException 设置发件人、收件人等时异常
     * @throws IOException 添加附件异常
     * @throws IllegalArgumentException 邮件内容非法
     */
    private MimeMessage createMimeMessage(Session session, MailInfo mailInfo, String accountUsername) throws MessagingException, IOException, IllegalArgumentException {
        //检查参数合法性，非法就抛异常
        checkMainInfo(mailInfo);
        MimeMessage message = new MimeMessage(session);
        //发件人
        message.setFrom(StringUtil.isNull(mailInfo.getFrom()) ? accountUsername : mailInfo.getFrom());
        //收件人
        for(String to : mailInfo.getTo()) {
            message.addRecipients(Message.RecipientType.TO,to);
        }
        //抄送
        if(mailInfo.getCc() != null && mailInfo.getCc().size() > 0) {
            for(String cc : mailInfo.getCc()) {
                message.addRecipients(Message.RecipientType.CC,cc);
            }
        }
        //密送
        if(mailInfo.getBcc() != null && mailInfo.getBcc().size() > 0) {
            for(String bcc : mailInfo.getBcc()) {
                message.addRecipients(Message.RecipientType.BCC,bcc);
            }
        }
        //邮件主题
        message.setSubject(mailInfo.getSubject(),mailInfo.getCharset());
        //发送日期
        if(mailInfo.getSentDate() != null) {
            message.setSentDate(mailInfo.getSentDate());
        }
        //自定义头信息
        if(mailInfo.getHeaders() != null && mailInfo.getHeaders().size() > 0) {
            for(Map.Entry<String,String> header : mailInfo.getHeaders().entrySet()) {
                message.addHeader(header.getKey(),header.getValue());
            }
        }
        // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
        String content = mailInfo.getContent();
        List<File> attachments = mailInfo.getAttachments();
        if(StringUtil.isNotNull(content) || (attachments != null && attachments.size() > 0)) {
            MimeMultipart multipart = new MimeMultipart();
            //正文
            if(StringUtil.isNotNull(content)) {
                MimeBodyPart contentPart = new MimeBodyPart();
                contentPart.setContent(content,mailInfo.getMimeType().value + ";charset=" + mailInfo.getCharset());
                multipart.addBodyPart(contentPart);
            }
            //附件
            if(attachments != null && attachments.size() > 0) {
                MimeBodyPart attachmentsPart = new MimeBodyPart();
                for(File attachment : attachments) {
                    attachmentsPart.attachFile(attachment);
                }
                multipart.addBodyPart(attachmentsPart);
            }
            message.setContent(multipart);
        }
        message.saveChanges();
        return message;
    }

    //==================================================================================================================

    @Override
    public MailSendResult send(MailAccount account, MailInfo mailInfo) throws Exception {
        Session session = getSession(account);
        MimeMessage message = createMimeMessage(session,mailInfo,account.getUsername());
        Transport transport = session.getTransport();
        //连接发件服务器
        try {
            if(account.isAuth()) {
                transport.connect(account.getUsername(),account.getPassword());
            }
            else {
                transport.connect();
            }
        } catch (Exception e) {
            return MailSendResult.FAIL_CONNECT;
        }
        //发送邮件
        MailSendResult result = MailSendResult.SUCCESS;
        try {
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            result = MailSendResult.FAIL_SEND;
        } finally {
            try {
                transport.close();
            } catch (Exception e) {
                result.subResult = MailSendResult.FAIL_CLOSE;
            }
        }
        return result;
    }

    //==================================================================================================================

    /**
     * 检查发送账号信息的合法性
     * @param account 设定的发送账号
     * @throws IllegalArgumentException 信息非法
     */
    private void checkMailAccount(MailAccount account) throws IllegalArgumentException {
        if(account == null) {
            throw new IllegalArgumentException("argument \"account\" is null");
        }
        if(StringUtil.isNull(account.getSmtpHost())) {
            throw new IllegalArgumentException("argument \"smtpHost\" is blank");
        }
        if(account.getSmtpPort() == null || account.getSmtpPort() <= 0) {
            throw new IllegalArgumentException("argument \"smtpPort\" is blank");
        }
        if(account.isAuth()) {
            if(StringUtil.isNull(account.getUsername())) {
                throw new IllegalArgumentException("argument \"username\" is blank");
            }
            if(StringUtil.isNull(account.getPassword())) {
                throw new IllegalArgumentException("argument \"password\" is blank");
            }
        }
    }

    /**
     * 检查邮件内容的合法性
     * @param mailInfo 设定的邮件内容
     * @throws IllegalArgumentException 内容非法
     */
    private void checkMainInfo(MailInfo mailInfo) throws IllegalArgumentException {
        if(mailInfo == null) {
            throw new IllegalArgumentException("argument \"mailInfo\" is null");
        }
        if(mailInfo.getTo() == null || mailInfo.getTo().size() <= 0) {
            throw new IllegalArgumentException("argument \"to\" is empty");
        }
        if(StringUtil.isNull(mailInfo.getSubject())) {
            throw new IllegalArgumentException("argument \"subject\" is blank");
        }
        if(StringUtil.isNull(mailInfo.getCharset())) {
            throw new IllegalArgumentException("argument \"charset\" is blank");
        }
        if(mailInfo.getMimeType() == null) {
            throw new IllegalArgumentException("argument \"mimeType\" is null");
        }
    }

}
