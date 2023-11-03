package polyu.learn.bbssoadfs.util;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtils {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmailUtils.class);

	public static void send(EmailContext emailContext) {

		
		try {
			LOGGER.info("sending email");
			Properties properties = System.getProperties();
			String host = emailContext.getHost();
			LOGGER.info("Email HOST: {}", host);
			properties.setProperty("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(properties);
			MimeMessage message = new MimeMessage(session);
			message.setHeader("X-Mailer", "LOTONtechEmail");
			message.setSentDate(new Date());

			String from = emailContext.getFromAddress();
			message.setFrom(new InternetAddress(from));
			LOGGER.info("Email FROM: {}", from);

			Set<String> toAddress = emailContext.getToAddress();

			for (String to : toAddress) {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(to));
				LOGGER.info("Email TO: {}", to);
			}
			
			Set<String> ccAddress = emailContext.getCcAddress();
			for (String cc : ccAddress) {
				message.addRecipient(Message.RecipientType.CC,
						new InternetAddress(cc));
				LOGGER.info("Email CC: {}", cc);
			}

			Set<String> bccAddress = emailContext.getBccAddress();

			for (String bcc : bccAddress) {
				message.addRecipient(Message.RecipientType.BCC,
						new InternetAddress(bcc));
				LOGGER.info("Email BCC: {}", bcc);
			}

			String subject = emailContext.getSubject();
			message.setSubject(subject);
			LOGGER.info("Email SUBJECT: {}", subject);

			Multipart multipart = new MimeMultipart();

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(emailContext.getMessageBody(),
					"text/html; charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			List<File> fileAttachment = emailContext.getFileAttachment();
			for (File file : fileAttachment) {
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.attachFile(file);
				multipart.addBodyPart(messageBodyPart);
				LOGGER.info("Email ATTACH: {}", file.getName());
			}
			message.setContent(multipart);
			Transport.send(message);
			LOGGER.info("email sent");

		} catch (Exception ex) {
			LOGGER.error("Error occured when sending email.", ex);
		}
	}

}
