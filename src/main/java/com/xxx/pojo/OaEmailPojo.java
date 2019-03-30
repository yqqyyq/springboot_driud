package com.xxx.pojo;

import com.xxx.mail.Email;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "oa_email")
public class OaEmailPojo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String receiveEmail;

	private String subject;
	
	private String content;

	private String template;

	private Timestamp sendTime;

	public OaEmailPojo() {
		super();
	}

	public OaEmailPojo(Email mail) {
		this.receiveEmail = Arrays.toString(mail.getReceiveMail());
		this.subject = mail.getSubject();
		this.content = mail.getContent();
		this.template = mail.getTemplate();
		this.sendTime = new Timestamp(new Date().getTime());
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}
	@Column(name = "receive_email", nullable = false, length = 500)
	public String getReceiveEmail() {
		return receiveEmail;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Column(name = "subject", nullable = false, length = 100)
	public String getSubject() {
		return subject;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "content", nullable = false, length = 65535)
	public String getContent() {
		return content;
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}
	@Column(name = "template", nullable = false, length = 100)
	public String getTemplate() {
		return template;
	}
	
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	@Column(name = "send_time", nullable = false, length = 19)
	public Timestamp getSendTime() {
		return sendTime;
	}
	
}
