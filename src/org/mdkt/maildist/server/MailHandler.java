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

import javax.mail.Address;
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
			logger.info(new StringBuffer("From=[").append(fromAddress.getAddress())
					.append("]"));

			// get all emails
			ArrayList<DistListMember> members = distListRegistry
					.findDistListMembers(distListId, fromAddress.getAddress());
			logger.info(new StringBuffer("[").append(fromAddress.getAddress())
					.append("] with distListId=[").append(distListId)
					.append("] has ").append(members.size()).append(" members"));

			Object content = message.getContent();
			String msgBody = "";
			String contentType = "text/html";
			Attachment attachment = null;
			if (content instanceof String) {
				msgBody = (String) content;
			} else if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				Part part = multipart.getBodyPart(0);
				Object partContent = part.getContent();
				logger.info("Content type: " + part.getContentType());
				contentType = part.getContentType();
				if (partContent instanceof String) {
					msgBody = (String) partContent;
				}
				// extract attachment if any
				attachment = getMailAttachmentBytes(multipart);
				if (attachment != null) {
					logger.info("Attachment fileName [" + attachment.fileName + "] with data size [" + attachment.getFileSize() + "] contentType [" + attachment.contentType + "]");
				}
			}
			String subject = message.getSubject();
			send(fromAddress, subject, msgBody, contentType, attachment, members);			

		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			// this is for if this method is called from TaskQueues (it's not
			// right now)
			// always send status OK or Appengine Task Queues will keep retrying
			resp.setStatus(HttpServletResponse.SC_OK);
		}
	}

	private void send(InternetAddress fromAddress,
			String subject, String msgBody, String contentType, Attachment attachment,
			ArrayList<DistListMember> members) throws Exception {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
		MimeMessage forwardedMessage = new MimeMessage(session);
		forwardedMessage.setSubject(subject);
		forwardedMessage.setFrom(fromAddress);
		if (attachment != null) {
			// using multipart
			Multipart mp = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(msgBody, contentType);
			mp.addBodyPart(htmlPart);
			
			MimeBodyPart attachmentPart = new MimeBodyPart();
			attachmentPart.setFileName(attachment.fileName);
			attachmentPart.setContent(attachment.data, attachment.contentType);
			mp.addBodyPart(attachmentPart);
			
			forwardedMessage.setContent(mp);
		} else {
			forwardedMessage.setContent(msgBody, contentType);
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
	private Attachment getMailAttachmentBytes(Multipart mimeMultipart)
			throws MessagingException, IOException {
		InputStream attachmentInputStream = null;
		try {
			for (int i = 0, n = mimeMultipart.getCount(); i < n; i++) {
				String disposition = mimeMultipart.getBodyPart(i)
						.getDisposition();
				if (disposition == null) {
					continue;
				}
				if ((disposition.equals(Part.ATTACHMENT) || (disposition
						.equals(Part.INLINE)))) {
					attachmentInputStream = mimeMultipart.getBodyPart(i)
							.getInputStream();
					byte[] imageData = getImageDataFromInputStream(attachmentInputStream);
					return new Attachment(mimeMultipart.getBodyPart(i).getFileName(), imageData, mimeMultipart.getBodyPart(i).getContentType());
				}
			}
		} finally {
			try {
				if (attachmentInputStream != null)
					attachmentInputStream.close();
			} catch (Exception e) {
			}
		}
		return null;
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

class Attachment {
	public byte[] data;
	public String fileName;
	public String contentType;
	
	public Attachment(String fileName, byte[] data, String contentType) {
		this.data = data;
		this.fileName = fileName;
		this.contentType = contentType;
	}

	public int getFileSize() {
		return data == null ? -1 : data.length;
	}
}