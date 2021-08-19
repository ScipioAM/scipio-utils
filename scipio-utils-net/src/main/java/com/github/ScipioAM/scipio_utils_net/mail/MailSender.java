package com.github.ScipioAM.scipio_utils_net.mail;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送者
 * @author Alan Scipio
 * @since 2021/6/25
 */
public interface MailSender {

    MailSendResult send(MailAccount account, MailInfo mailInfo) throws Exception;

}
