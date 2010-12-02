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
			String matchedUserEmail = distListRegistry.aliasExists(fromAddress
					.getAddress());
			if (matchedUserEmail != null) {
				logger.info("FROM address is an alias!");
				fromAddress = new InternetAddress(matchedUserEmail,
						fromAddress.getPersonal());
			}
			logger.info(new StringBuffer("From=[").append(
					fromAddress.getAddress()).append("]"));
			// get all emails
			ArrayList<DistListMember> members = distListRegistry
					.findDistListMembers(distListId, fromAddress.getAddress());
			logger.info(new StringBuffer("[").append(fromAddress.getAddress())
					.append("] with distListId=[").append(distListId)
					.append("] has ").append(members.size()).append(" members"));

			Address[] ccAddresses = message.getRecipients(RecipientType.CC);
			Address[] bccAddresses = message.getRecipients(RecipientType.BCC);
			String subject = message.getSubject();

			Object content = getContent(message);
			if (content instanceof String) {
				send(createMimeMessage(
						fromAddress,
						ccAddresses,
						bccAddresses,
						subject,
						new MessageBody((String) content, message
								.getContentType())), members);
			} else if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				Multipart mpBody = traversal(multipart, 0);
				send(createMimeMessage(fromAddress, ccAddresses, bccAddresses,
						subject, mpBody), members);
			}
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			// this is for if this method is called from TaskQueues (it's not
			// right now)
			// always send status OK or Appengine Task Queues will keep retrying
			resp.setStatus(HttpServletResponse.SC_OK);
		}
	}

	private Multipart traversal(Multipart multipart, int level)
			throws Exception {
		Multipart retMultipart = new MimeMultipart();
		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart p = multipart.getBodyPart(i);
			String disposition = p.getDisposition();
			String contentType = p.getContentType();
			if (disposition != null
					&& (disposition.equals(Part.ATTACHMENT) || (disposition
							.equals(Part.INLINE)))) {
				Attachment attachment = getAttachment(p);
				logger.info(space(level) + "attachment[" + contentType + "] fileName[" + attachment.fileName + "] disposition[" + attachment.disposition + "] contentType[" + attachment.contentType + "] size[" + attachment.getFileSize() + "]");
				retMultipart
						.addBodyPart(createMimeBodyPartAttachemnt(attachment));
			} else {
				Object content = getContent((MimeBodyPart) p);
				if (content instanceof String) {
					logger.info(space(level) + "String[" + contentType + "]");
					MessageBody body = new MessageBody((String) content, contentType);
					retMultipart.addBodyPart(createMimeBodyPartString(body));
				} else if (content instanceof Multipart) {
					logger.info(space(level) + "Multipart[" + contentType + "]");
					Multipart child = traversal((Multipart) content, level + 1);
					// flatten the child body part
					for (int j = 0; j < child.getCount(); j++) {
						retMultipart.addBodyPart(child.getBodyPart(j));
					}
//					BodyPart bp = new MimeBodyPart();
//					bp.setContent(child, contentType);
//					retMultipart.addBodyPart(bp);
				} else if (content instanceof InputStream) {
					logger.info(space(level) + content + "[" + contentType + "]");					
					retMultipart.addBodyPart(createMimeBodyPartInputStream((InputStream) content, contentType));
				} else {
					logger.error(space(level) + "Unknown type " + content + "[" + contentType + "]");
				}
			}
		}
		return retMultipart;
	}

	private BodyPart createMimeBodyPartInputStream(InputStream inputStream,
			String contentType) throws Exception {
		byte[] data = getBytesFromInputStream(inputStream);
		MimeBodyPart p = new MimeBodyPart();
		DataSource src = new ByteArrayDataSource(data, contentType);
		DataHandler handler = new DataHandler(src);
		p.setDataHandler(handler);
		return p;
	}

	private BodyPart createMimeBodyPartString(MessageBody msgBody)
			throws Exception {
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(msgBody.msgBody, msgBody.contentType);
		return htmlPart;
	}

	private BodyPart createMimeBodyPartAttachemnt(Attachment attachment)
			throws Exception {
		MimeBodyPart attachmentPart = new MimeBodyPart();
		attachmentPart.setFileName(attachment.fileName);
		// FIXME has to put as attachment, as inline doesn't work http://code.google.com/p/googleappengine/issues/detail?id=965
		attachmentPart.setDisposition(Part.ATTACHMENT); 
		DataSource src = new ByteArrayDataSource(attachment.data,
				attachment.contentType);
		DataHandler handler = new DataHandler(src);
		attachmentPart.setDataHandler(handler);
		return attachmentPart;
	}

	private String space(int level) {
		StringBuffer buf = new StringBuffer("level ").append(level).append(" ");
		for (int i = 0; i < level; i++) {
			buf.append("--");
		}
		return buf.toString();
	}

	private MimeMessage createMimeMessage(InternetAddress fromAddress,
			Address[] ccAddresses, Address[] bccAddresses, String subject,
			Multipart mpBody) throws Exception {
		MimeMessage defaultMessage = createDefaultMimeMessage(fromAddress,
				ccAddresses, bccAddresses, subject);
		defaultMessage.setContent(mpBody);
		return defaultMessage;
	}

	private MimeMessage createMimeMessage(InternetAddress fromAddress,
			Address[] ccAddresses, Address[] bccAddresses, String subject,
			MessageBody msgBody) throws Exception {
		MimeMessage defaultMessage = createDefaultMimeMessage(fromAddress,
				ccAddresses, bccAddresses, subject);
		defaultMessage.setContent(msgBody.msgBody, msgBody.contentType);
		return defaultMessage;
	}

	private MimeMessage createDefaultMimeMessage(InternetAddress fromAddress,
			Address[] ccAddresses, Address[] bccAddresses, String subject)
			throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage forwardedMessage = new MimeMessage(session);
		forwardedMessage.setSubject(subject);
		forwardedMessage.setFrom(fromAddress);
		if (ccAddresses != null)
			forwardedMessage.setRecipients(RecipientType.CC, ccAddresses);
		if (bccAddresses != null)
			forwardedMessage.setRecipients(RecipientType.BCC, bccAddresses);
		return forwardedMessage;
	}

	private void send(MimeMessage forwardedMessage,
			ArrayList<DistListMember> members) throws Exception {
		for (DistListMember member : members) {
			// do forwading
			forwardedMessage.setRecipient(RecipientType.TO,
					new InternetAddress(member.getEmail(), member.getName()));
			try {
				logger.debug("Sending to [" + member.getName() + "]");
				Transport.send(forwardedMessage);
			} catch (Exception e1) {
				logger.error(e1, e1);
			}
		}
	}

	private Attachment getAttachment(BodyPart bodyPart) throws Exception {
		InputStream attachmentInputStream = null;
		try {
			attachmentInputStream = bodyPart.getInputStream();
			byte[] imageData = getBytesFromInputStream(attachmentInputStream);
			return new Attachment(bodyPart.getFileName(), imageData,
					bodyPart.getContentType(), bodyPart.getDisposition());
		} finally {
			try {
				if (attachmentInputStream != null)
					attachmentInputStream.close();
			} catch (Exception e) {
			}
		}
	}

	public byte[] getBytesFromInputStream(InputStream inputStream)
			throws IOException {
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

	private static final byte ESCAPE_CHAR = '=';

	public String decodeQuotedPrintable(byte[] bytes, String charset)
			throws IOException {
		return new String(decodeQuotedPrintable(bytes), charset);
	}

	public byte[] decodeQuotedPrintable(byte[] bytes) throws IOException {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];
            if (b == ESCAPE_CHAR) {
                try {
                    if (bytes[i + 1] == 10) {
                        // FIX skip newline, lenient
                        ++i;
                    } else {
                        int u = digit16(bytes[++i]);
                        int l = digit16(bytes[++i]);
                        out.write((char) ((u << 4) + l));
                    }
                } catch (Exception e) {
                    throw new IOException("Invalid quoted-printable encoding", e);
                }
            } else {
                out.write(b);
            }
        }
        return out.toByteArray();
    }

	public int digit16(byte b) throws IOException {
        int i = Character.digit(b, 16);
        if (i == -1) {
            throw new IOException("Invalid encoding: not a valid digit (radix 16): " + b);
        }
        return i;
    }

	public Object getContent(MimeMessage message) throws Exception {
		String charset = contentType2Charset(message.getContentType(), null);
		Object content;
		try {
			content = message.getContent();
		} catch (Exception e) {
			try {
				byte[] out = getBytesFromInputStream(message.getRawInputStream());
				out = decodeQuotedPrintable(out);
				if (charset != null) {
					content = new String(out, charset);
				} else {
					content = new String(out);
				}
			} catch (Exception e1) {
				throw e;
			}
		}
		return content;
	}

	public Object getContent(MimeBodyPart part) throws Exception {
		String charset = contentType2Charset(part.getContentType(), null);
		Object content;
		try {
			content = part.getContent();
		} catch (Exception e) {
			try {
				byte[] out = getBytesFromInputStream(part.getRawInputStream());
				out = decodeQuotedPrintable(out);
				if (charset != null) {
					content = new String(out, charset);
				} else {
					content = new String(out);
				}
			} catch (Exception e1) {
				throw e;
			}
		}
		return content;
	}

	public String contentType2Charset(String contentType,
			String defaultCharset) {
		String charset = defaultCharset;
		if (contentType.indexOf("charset=") != -1) {
			String[] split = contentType.split("charset=");
			if (split.length > 1) {
				charset = split[1];
				if (charset.indexOf(';') >= 0) {
					charset = charset.substring(0, charset.indexOf(';'));
				}
				charset = charset.replaceAll("\"", "");
				charset = charset.trim();
			}
		}
		return charset;
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
		return new StringBuffer("MessageBody {contentType=[")
				.append(contentType).append("]}").toString();
	}
}

class Attachment {
	public byte[] data;
	public String fileName;
	public String contentType;
	public String disposition;

	public Attachment(String fileName, byte[] data, String contentType,
			String disposition) {
		this.data = data;
		this.fileName = fileName;
		int id = contentType.indexOf(";");
		this.contentType = id > -1 ? contentType.substring(0, id) : contentType;
		this.disposition = disposition;
	}

	public int getFileSize() {
		return data == null ? -1 : data.length;
	}
}