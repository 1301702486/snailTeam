package com.snail.child.utils;


import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Properties;

/**
 * Author: 郭瑞景
 * Date: 2019/7/6
 * Description: 发送邮件
 */

public class EmailUtils {

    private String from = "liulangbaobei@yeah.net";

    private String password = "llbbguanyou2019";

    private String host = "smtp.yeah.net";

    private Properties properties = System.getProperties();

    /**
     * 发送邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendMessage(String to, String subject, String content) {
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.transport.protocol", "SMTP");
        properties.put("mail.debug", "true");
        properties.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=UTF-8");
            Transport.send(message);
            System.out.println("邮件发送成功！");
            System.out.println("详情: To " + to + message.getSubject());
        } catch (MessagingException e) {
            System.out.println("邮件发送失败！");
            e.printStackTrace();
        }

    }

}
