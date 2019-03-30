package com.xxx.mail;

import com.xxx.pojo.IaEmailPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 使用POP3协议接收163邮件
 */
@Component
public class ReceiveMailCompment {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveMailCompment.class);

    @Value("${spring.mail.receive.protocol}")
    private String protocol;//pop3

    @Value("${spring.mail.receive.port}")
    private String port;//110

    @Value("${spring.mail.receive.servicepath}")
    private String servicePath;//pop3.163.com

    @Value("${spring.mail.receive.username}")
    private String receiveuser;

    @Value("${spring.mail.receive.password}")
    private String receivepass;

    /**
     * 接收邮件
     */
    public List<IaEmailPojo> resceive() {
        /**
         * 因为现在使用的是163邮箱 而163的 pop地址是　pop3.163.com　 端口是　110　　
         * 比如使用好未来企业邮箱 就需要换成 好未来邮箱的 pop服务器地址 pop.263.net  和   端口 110
         **/
        Folder folder = null;
        Store store = null;
        List<IaEmailPojo> list=null;
        try {
            // 准备连接服务器的会话信息
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", protocol);       // 使用pop3协议
            props.setProperty("mail.pop3.port", port);           // 端口
            props.setProperty("mail.pop3.host", servicePath);       // pop3服务器

            // 创建Session实例对象
            Session session = Session.getInstance(props);
            store = session.getStore(protocol);
            store.connect(receiveuser, receivepass); //163邮箱程序登录属于第三方登录所以这里的密码是163给的授权密码而并非普通的登录密码

            // 获得收件箱
            folder = store.getFolder("INBOX");
            /* Folder.READ_ONLY：只读权限
             * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
             */
            folder.open(Folder.READ_WRITE); //打开收件箱

            // 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
            logger.info("未读邮件数: " + folder.getUnreadMessageCount());

            // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
            logger.info("删除邮件数: " + folder.getDeletedMessageCount());
            logger.info("新邮件: " + folder.getNewMessageCount());

            // 获得收件箱中的邮件总数
            logger.info("邮件总数: " + folder.getMessageCount());

            // 得到收件箱中的所有邮件,并解析
            Message[] messages = folder.getMessages();
            list = parseMessage(messages);

            //得到收件箱中的所有邮件并且删除邮件
            //deleteMessage(messages);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            if (folder != null) {
                try {
                    folder.close(true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 解析邮件
     */
    public List<IaEmailPojo> parseMessage(Message... messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

        List<IaEmailPojo> list =new ArrayList<IaEmailPojo>();
        IaEmailPojo iaEmailPojo=null;
        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {
            iaEmailPojo=new IaEmailPojo();
            MimeMessage msg = (MimeMessage) messages[i];
            logger.info("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
             String subject=getSubject(msg);
            iaEmailPojo.setSubject(subject);
            logger.info("主题: " + subject);
            String sendmail=getFrom(msg);
            iaEmailPojo.setSendEmail(sendmail);
            logger.info("发件人: " + sendmail);
            String receiveMail=getReceiveAddress(msg, null);
            iaEmailPojo.setReceiveEmail(receiveMail);
            logger.info("收件人：" + receiveMail);
            Timestamp sendTime=new Timestamp(getSentDate(msg, null).getTime());
            iaEmailPojo.setSendTime(sendTime);
            logger.info("发送时间：" + sendTime);
            logger.info("是否已读：" + isSeen(msg));
            logger.info("邮件优先级：" + getPriority(msg));
            logger.info("是否需要回执：" + isReplySign(msg));
            logger.info("邮件大小：" + msg.getSize() * 1024 + "kb");
            boolean isContainerAttachment = isContainAttachment(msg);
            logger.info("是否包含附件：" + isContainerAttachment);
            if (isContainerAttachment) {
                //saveAttachment(msg, "f:\\mailTest\\" + msg.getSubject() + "_" + i + "_"); //保存附件
            }
            StringBuffer content = new StringBuffer(30);
            getMailTextContent(msg, content);
            iaEmailPojo.setContent(content.toString());
            list.add(iaEmailPojo);
            logger.info("邮件正文：" + (content.length() > 100 ? content.substring(0, 100) + "..." : content));
            logger.info("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
        }
        return list;
    }

    /**
     * 解析邮件
     */
    /*public void deleteMessage(Message ...messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {
            Message message = messages[i];
            String subject = message.getSubject();
            // set the DELETE flag to true
            message.setFlag(Flags.Flag.DELETED, true);
            logger.info("Marked DELETE for message: " + subject);
        }
    }*/

    /**
     * 获得邮件主题
     */
    public String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    /**
     * 获得邮件发件人
     */
    public String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from = person + "<" + address.getAddress() + ">";

        return from;
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     */
    public String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1)
            throw new MessagingException("没有收件人!");
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress) address;
            receiveAddress.append(internetAddress.toUnicodeString()).append(",");
        }
        receiveAddress.deleteCharAt(receiveAddress.length() - 1); //删除最后一个逗号
        return receiveAddress.toString();
    }

    /**
     * 获得邮件发送时间
     */
    public Date getSentDate(MimeMessage msg, String pattern) throws MessagingException {
        Date receivedDate = msg.getSentDate();
        return receivedDate;
        /*if (receivedDate == null)
            return "";

        if (pattern == null || "".equals(pattern))
            pattern = "yyyy-MM-dd HH:mm:ss";

        return new SimpleDateFormat(pattern).format(receivedDate);*/
    }

    /**
     * 判断邮件中是否包含附件
     */
    public boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }
                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }

    /**
     * 判断邮件是否已读
     */
    public boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 判断邮件是否需要阅读回执
     */
    public boolean isReplySign(MimeMessage msg) throws MessagingException {
        boolean replySign = false;
        String[] headers = msg.getHeader("Disposition-Notification-To");
        if (headers != null)
            replySign = true;
        return replySign;
    }

    /**
     * 获得邮件的优先级
     */
    public String getPriority(MimeMessage msg) throws MessagingException {
        String priority = "普通";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
                priority = "紧急";
            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
                priority = "低";
            else
                priority = "普通";
        }
        return priority;
    }

    /**
     * 获得邮件文本内容
     */
    public void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }

    /**
     * 保存附件
     */
    public void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir);
        }
    }

    /**
     * 读取输入流中的数据保存至指定目录
     */
    private void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /**
     * 文本解码
     */
    public String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

}