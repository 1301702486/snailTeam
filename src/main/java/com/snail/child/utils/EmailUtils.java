package com.snail.child.utils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Author: 陈一
 * Date: 2019/6/28
 * Description: 发送邮件的工具类
 */

public class EmailUtils {

    private String from = "1725145436@qq.com";

    private String password = "tpsaycxcxhhobjgb";

    private String host = "smtp.qq.com";

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
            message.setContent(content, "text/html;charset=gb2312");
            Transport.send(message);
            System.out.println("邮件发送成功！");
            System.out.println("详情: To " + to + message.getSubject());
        } catch (MessagingException e) {
            System.out.println("邮件发送失败！");
            e.printStackTrace();
        }
    }

}