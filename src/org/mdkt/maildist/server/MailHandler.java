/**
 * 
 */
package org.mdkt.maildist.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mdkt.maildist.client.dto.DistListMember;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Receiving email and forward
 * 
 * @author trung
 * 
 */
public class MailHandler extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger logger = Logger.getLogger(MailHandler.class);

	private static DistListRegistry distListRegistry = null;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// /_ah/mail/<address>
		String requestUri = req.getRequestURI();
		String address = requestUri.substring(requestUri.lastIndexOf("/") + 1);
		String distListId = address.substring(0, address.indexOf("@"));
		logger.info(new StringBuffer("RequestUri=[").append(requestUri)
				.append("], address=[").append(address)
				.append("], distListId=[").append(distListId).append("]"));
		if (distListRegistry == null) {
			distListRegistry = WebApplicationContextUtils
					.getWebApplicationContext(
							req.getSession().getServletContext()).getBean(
							DistListRegistry.class);
		}
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {

			MimeMessage message = new MimeMessage(session, req.getInputStream());
			Address[] froms = message.getFrom();
			InternetAddress fromAddress = (InternetAddress) froms[0];
			String matchedUserEmail = distListRegistry.aliasExists(fromAddress.getAddress());
			if (matchedUserEmail != null) {
				logger.info("FROM address is an alias!");
				fromAddress = new InternetAddress(matchedUserEmail, fromAddress.getPersonal());
			}
			logger.info(new StringBuffer("From=[").append(fromAddress.getAddress())
					.append("]"));
			// get all emails
			ArrayList<DistListMember> members = distListRegistry
					.findDistListMembers(distListId, fromAddress.getAddress());
			logger.info(new StringBuffer("[").append(fromAddress.getAddress())
					.append("] with distListId=[").append(distListId)
					.append("] has ").append(members.size()).append(" members"));
			
			Address[] ccAddresses = message.getRecipients(RecipientType.CC);
			Address[] bccAddresses = message.getRecipients(RecipientType.BCC);

			Object content = message.getContent();
			ArrayList<MessageBody> msgBodies = new ArrayList<MessageBody>();
			String contentType = "text/html";
			ArrayList<Attachment> attachments = null;
			if (content instanceof String) {
				msgBodies.add(new MessageBody((String) content, contentType));
			} else if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				logHiararchy(multipart, 0);
				Part part = multipart.getBodyPart(0);
				Object partContent = part.getContent();
				contentType = part.getContentType();
				if (partContent instanceof String) {
					msgBodies.add(new MessageBody((String) partContent, contentType));
				} else if (partContent instanceof Multipart) {
					msgBodies = getMessageBody((Multipart) partContent);
				}
				// extract attachment if any
				attachments = getAttachments(multipart);
			}
			String subject = message.getSubject();
			send(fromAddress, ccAddresses, bccAddresses, subject, msgBodies, attachments, members);			

		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			// this is for if this method is called from TaskQueues (it's not
			// right now)
			// always send status OK or Appengine Task Queues will keep retrying
			resp.setStatus(HttpServletResponse.SC_OK);
		}
	}

	private void logHiararchy(Multipart multipart, int level) throws Exception {
		for (int i = 0; i < multipart.getCount(); i++) {
			Part p = multipart.getBodyPart(i);
			String disposition = p.getDisposition();
			String contentType = p.getContentType();
			if (disposition != null && (disposition.equals(Part.ATTACHMENT) || (disposition
					.equals(Part.INLINE)))) {
				logger.info(space(level) + "attachment[" + contentType + "]");
			} else {
				Object content = p.getContent();
				if (content instanceof String) {
					logger.info(space(level) + "String[" + contentType + "]");
				} else if (content instanceof Multipart) {
					logHiararchy((Multipart) content, level + 1);
				} else {
					logger.info("Unknown type " + content + "[" + contentType + "]");
				}
			}
		}
	}

	private String space(int level) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < level; i++) {
			buf.append("   ");
		}
		return buf.toString();
	}

	private ArrayList<MessageBody> getMessageBody(Multipart partContent) throws Exception {
		ArrayList<MessageBody> bodies = new ArrayList<MessageBody>();
		for (int i = 0; i < partContent.getCount(); i++) {
			Part p = partContent.getBodyPart(i);
			Object content = p.getContent();
			if (content instanceof String) {
				MessageBody msgBody = new MessageBody((String) content, p.getContentType());
				logger.info("Message body string[" + msgBody.contentType + "]");
				bodies.add(msgBody);
			}
		}
		return bodies.size() == 0 ? null : bodies;
	}

	private void send(InternetAddress fromAddress,
			Address[] ccAddresses, Address[] bccAddresses, String subject, ArrayList<MessageBody> msgBodies, ArrayList<Attachment> attachments,
			ArrayList<DistListMember> members) throws Exception {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
		MimeMessage forwardedMessage = new MimeMessage(session);
		forwardedMessage.setSubject(subject);
		forwardedMessage.setFrom(fromAddress);
		if (ccAddresses != null)
			forwardedMessage.setRecipients(RecipientType.CC, ccAddresses);
		if (bccAddresses != null)
			forwardedMessage.setRecipients(RecipientType.BCC, bccAddresses);
		
		if (attachments != null) {
			// using multipart
			MimeMultipart mp = new MimeMultipart();
			if (msgBodies != null) {
				for (MessageBody msgBody : msgBodies) {					
					MimeBodyPart htmlPart = new MimeBodyPart();
					htmlPart.setContent(msgBody.msgBody, msgBody.contentType);
					mp.addBodyPart(htmlPart);
				}					
			}
			
			for (Attachment attachment : attachments) {
				MimeBodyPart attachmentPart = new MimeBodyPart();
				attachmentPart.setFileName(attachment.fileName);
				attachmentPart.setDisposition(Part.ATTACHMENT);
				DataSource src = new ByteArrayDataSource(attachment.data, attachment.contentType);
				DataHandler handler = new DataHandler(src);
				attachmentPart.setDataHandler(handler);
				
				mp.addBodyPart(attachmentPart);
			}
			
			forwardedMessage.setContent(mp);
		} else {
			if (msgBodies.size() > 1) {
				MimeMultipart mp = new MimeMultipart();
				for (MessageBody msgBody : msgBodies) {					
					MimeBodyPart htmlPart = new MimeBodyPart();
					htmlPart.setContent(msgBody.msgBody, msgBody.contentType);
					mp.addBodyPart(htmlPart);
				}
				forwardedMessage.setContent(mp);
			} else {
				MessageBody msgBody = msgBodies.get(0);
				forwardedMessage.setContent(msgBody.msgBody, msgBody.contentType);
			}
		}
		
		for (DistListMember member : members) {
			// do forwading
			forwardedMessage.setRecipient(RecipientType.TO, new InternetAddress(member.getEmail(), member.getName()));
			try {
				logger.debug("Sending to [" + member.getName() + "]");
				Transport.send(forwardedMessage);
			} catch (Exception e1) {
				logger.error(e1, e1);
			}
		}		
	}

	/**
	 * From http://java.sun.com/developer/onlineTraining/JavaMail/contents.html#
	 * JavaMailMessage
	 * 
	 * @param attachmentInputStream
	 * @param mimeMultipart
	 * @return image data from attachment or null if there is none
	 * @throws MessagingException
	 * @throws IOException
	 */
	private ArrayList<Attachment> getAttachments(Multipart mimeMultipart)
			throws Exception {
		ArrayList<Attachment> attachments = new ArrayList<Attachment>();
		for (int i = 0, n = mimeMultipart.getCount(); i < n; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			
			String disposition = bodyPart.getDisposition();
			if (disposition == null) {
				continue;
			}
			if ((disposition.equals(Part.ATTACHMENT) || (disposition
					.equals(Part.INLINE)))) {
				Attachment attachment = getAttachment(bodyPart);
				logger.info("Attachment fileName [" + attachment.fileName + "] with data size [" + attachment.getFileSize() + "] contentType [" + attachment.contentType + "]");
				attachments.add(attachment);
			}
		}
		return attachments.size() == 0 ? null : attachments;
	}

	private Attachment getAttachment(BodyPart bodyPart) throws Exception {
		InputStream attachmentInputStream = null;
		try {
			attachmentInputStream = bodyPart.getInputStream();
			byte[] imageData = getImageDataFromInputStream(attachmentInputStream);
			return new Attachment(bodyPart.getFileName(), imageData, bodyPart.getContentType());
		} finally {
			try {
				if (attachmentInputStream != null)
					attachmentInputStream.close();
			} catch (Exception e) {
			}
		}
	}

	public byte[] getImageDataFromInputStream(InputStream inputStream) throws IOException {
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
		try {
			bis = new BufferedInputStream(inputStream);
			// write it to a byte[] using a buffer since we don't know the exact
			// image size
			byte[] buffer = new byte[1024];
			bos = new ByteArrayOutputStream();
			int i = 0;
			while (-1 != (i = bis.read(buffer))) {
				bos.write(buffer, 0, i);
			}
			byte[] imageData = bos.toByteArray();
			return imageData;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
}

class MessageBody {
	public String msgBody;
	public String contentType;
	
	public MessageBody(String body, String contentType) {
		this.msgBody = body;
		this.contentType = contentType;
	}
	
	@Override
	public String toString() {
		return new StringBuffer("MessageBody {contentType=[").append(contentType).append("]}").toString();
	}
}

class Attachment {
	public byte[] data;
	public String fileName;
	public String contentType;
	
	public Attachment(String fileName, byte[] data, String contentType) {
		this.data = data;
		this.fileName = fileName;
		int id = contentType.indexOf(";");
		this.contentType = id > -1 ? contentType.substring(0, id) : contentType;;
	}

	public int getFileSize() {
		return data == null ? -1 : data.length;
	}
}