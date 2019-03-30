package com.xxx.mail;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Email封装类 
 * 创建者 科帮网 
 * 创建时间 2017年7月20日
 *
 */
public class Email implements Serializable {
	private static final long serialVersionUID = 1L;
	//必填参数
	private String[] receiveMail;//接收方邮件
	private String subject;//主题
	private String content;//邮件内容
	//选填
	private String sendMail;//发送人
	private String template;//模板
	private String[] file;//附件
	private HashMap<String, String> kvMap;// 自定义参数
	private Timestamp sendTime; //发送时间

	public Email() {
		super();
	}

	public Email(String[] receiveMail, String subject, String content, String template,
                 HashMap<String, String> kvMap,String[] file,String sendMail,Timestamp sendTime) {
		super();
		this.receiveMail = receiveMail;
		this.subject = subject;
		this.content = content;
		this.template = template;
		this.kvMap = kvMap;
		this.file=file;
		this.sendMail=sendMail;
		this.sendTime=sendTime;
	}

	public String[] getReceiveMail() { return receiveMail; }
	public void setReceiveMail(String[] receiveMail) {
		this.receiveMail = receiveMail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public HashMap<String, String> getKvMap() {
		return kvMap;
	}
	public void setKvMap(HashMap<String, String> kvMap) {
		this.kvMap = kvMap;
	}
	public String[] getFile() { return file; }
	public void setFile(String[] file) { this.file = file; }
	public String getSendMail() { return sendMail; }
	public void setSendMail(String sendMail) { this.sendMail = sendMail; }
	public Timestamp getSendTime() { return sendTime; }
	public void setSendTime(Timestamp sendTime) { this.sendTime = sendTime; }

}
