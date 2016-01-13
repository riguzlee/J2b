package com.riguz.jb.util.mail;

import java.io.File;

import javax.mail.MessagingException;

public class MailKit {
	public static void sendMail(String to, String subject, String content) {
		Email smtpMail = null;
		try {
			smtpMail = new Email();
			smtpMail.addTo(to);
			smtpMail.setSubject(subject);
			smtpMail.setBody(content);
			// 初始化一个Topic泛型（String类型）发送器，构造函数参数为，MQ名，Topic名
			// Sender<Email> topicSender = new TopicSender<Email>("MailTopicMq",
			// "MailTopic");
			// topicSender.sendAsync(smtpMail);
			smtpMail.send();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param to
	 *            邮件接收者的E-mail地址.
	 * @param subject
	 *            邮件的标题字符串. 必须是纯文本的字符串,建议不要超过64个字符.
	 * @param content
	 *            邮件的正文内容. 标准的html格式.
	 * @param file
	 *            File文件对象.或者绝对路径
	 */
	public static boolean sendMail(String to, String subject, String content, File file) {
		Email smtpMail = null;
		try {
			smtpMail = new Email();
			smtpMail.addTo(to);
			smtpMail.setSubject(subject);
			smtpMail.setBody(content);
			smtpMail.addFile(file);
			// 初始化一个Topic泛型（String类型）发送器，构造函数参数为，MQ名，Topic名
			// Sender<Email> topicSender = new TopicSender<Email>("MailTopicMq",
			// "MailTopic");
			// topicSender.sendAsync(smtpMail);
			smtpMail.send();
		} catch (MessagingException e) {
			e.printStackTrace();
			System.err.println("邮件发送异常----to->" + to);
			return false;
		}
		return true;
	}

}
