package com.riguz.j2b.util;

import javax.mail.MessagingException;

import com.riguz.j2b.model.bean.Email;

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
}
