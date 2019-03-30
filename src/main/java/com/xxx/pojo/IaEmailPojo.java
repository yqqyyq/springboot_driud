package com.xxx.pojo;

import com.xxx.mail.Email;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

@Entity
@Table(name = "ia_email")
public class IaEmailPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String sendEmail;

    private String receiveEmail;

    private String subject;

    private String content;

    private Timestamp sendTime;

    public IaEmailPojo() {
        super();
    }

    public IaEmailPojo(Email mail) {
        this.sendEmail = mail.getSendMail();
        this.receiveEmail = Arrays.toString(mail.getReceiveMail());
        this.subject = mail.getSubject();
        this.content = mail.getContent();
        this.sendTime = mail.getSendTime();
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

    public void setSendEmail(String sendEmail) { this.sendEmail = sendEmail; }

    @Column(name = "send_email", nullable = false, length = 500)
    public String getSendEmail() { return sendEmail; }

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

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    @Column(name = "send_time", nullable = false, length = 19)
    public Timestamp getSendTime() {
        return sendTime;
    }

}
