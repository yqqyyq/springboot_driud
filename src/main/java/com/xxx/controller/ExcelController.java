package com.xxx.controller;

import com.xxx.excel.poi.ExcelData;
import com.xxx.excel.poi.ExportExcelUtils;
import com.xxx.pojo.FileLogPojo;
import com.xxx.pojo.IaEmailPojo;
import com.xxx.pojo.OaEmailPojo;
import com.xxx.pojo.QuartzPojo;
import com.xxx.service.FileLogService;
import com.xxx.service.JobService;
import com.xxx.service.ReceiveMailService;
import com.xxx.service.SendMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private FileLogService fileLogService;

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private ReceiveMailService receiveMailService;

    @RequestMapping("/job/out")
    public void job(String jobName, HttpServletRequest rquest, HttpServletResponse response) throws IOException {
        List<QuartzPojo> list = jobService.SelectByJobName(jobName);
        ExcelData data = new ExcelData();
        data.setName("job");
        List<String> titles = new ArrayList();
        titles.add("jobName");
        titles.add("jobGroup");
        titles.add("description");
        titles.add("jobClassName");
        titles.add("cronExpression");
        titles.add("triggerName");
        titles.add("triggerState");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();
        for (int i = 0, length = list.size(); i < length; i++) {
            QuartzPojo quartzPojo = list.get(i);
            List<Object> row = new ArrayList();
            row.add(quartzPojo.getJobName());
            row.add(quartzPojo.getJobGroup());
            row.add(quartzPojo.getDescription());
            row.add(quartzPojo.getJobClassName());
            row.add(quartzPojo.getCronExpression());
            row.add(quartzPojo.getTriggerName());
            row.add(quartzPojo.getTriggerState());
            rows.add(row);
        }
        data.setRows(rows);
        /*HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "job");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);*/
        try {
            ExportExcelUtils.exportExcel(response, "任务列表", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return ResponseEntity<byte[]>(FileUtils.readFileToByteArray(response.getOutputStream()), headers, HttpStatus.OK);
    }

    @RequestMapping("/file/out")
    public void file(String filename, HttpServletRequest rquest, HttpServletResponse response) {
        List<FileLogPojo> list = fileLogService.selectByFileName(filename);
        ExcelData data = new ExcelData();
        data.setName("down");
        List<String> titles = new ArrayList();
        titles.add("文件");
        titles.add("用户");
        titles.add("大小");
        titles.add("时间");
        titles.add("路径");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();
        for (int i = 0, length = list.size(); i < length; i++) {
            FileLogPojo fileLogPojo = list.get(i);
            List<Object> row = new ArrayList();
            row.add(fileLogPojo.getFilename());
            row.add(fileLogPojo.getFileuser());
            row.add(fileLogPojo.getFilesize());
            row.add(fileLogPojo.getFiletime());
            row.add(fileLogPojo.getFilepath());
            rows.add(row);
        }
        data.setRows(rows);
        try {
            ExportExcelUtils.exportExcel(response, "下载文件列表", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/sendmail/out")
    public void sendmail(String receiveEmail, HttpServletRequest rquest, HttpServletResponse response) {
        List<OaEmailPojo> list = sendMailService.findByReceiveEmail(receiveEmail);
        ExcelData data = new ExcelData();
        data.setName("sendmail");
        List<String> titles = new ArrayList();
        titles.add("接收人邮箱");
        titles.add("主题");
        titles.add("模板");
        titles.add("发送时间");
        titles.add("内容");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();
        for (int i = 0, length = list.size(); i < length; i++) {
            OaEmailPojo oaEmailPojo = list.get(i);
            List<Object> row = new ArrayList();
            row.add(oaEmailPojo.getReceiveEmail());
            row.add(oaEmailPojo.getSubject());
            row.add(oaEmailPojo.getTemplate());
            row.add(oaEmailPojo.getSendTime());
            row.add(oaEmailPojo.getContent());
            rows.add(row);
        }
        data.setRows(rows);
        try {
            ExportExcelUtils.exportExcel(response, "发送邮件列表", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/receivemail/out")
    public void receivemail(String sendEmail, HttpServletRequest rquest, HttpServletResponse response) {
        List<IaEmailPojo> list = receiveMailService.findBySendEmail(sendEmail);
        ExcelData data = new ExcelData();
        data.setName("sendmail");
        List<String> titles = new ArrayList();
        titles.add("发件人邮箱");
        titles.add("收件人邮箱");
        titles.add("主题");
        titles.add("发送时间");
        //titles.add("内容");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();
        for (int i = 0, length = list.size(); i < length; i++) {
            IaEmailPojo iaEmailPojo = list.get(i);
            List<Object> row = new ArrayList();
            row.add(iaEmailPojo.getSendEmail());
            row.add(iaEmailPojo.getReceiveEmail());
            row.add(iaEmailPojo.getSubject());
            row.add(iaEmailPojo.getSendTime());
            //row.add(iaEmailPojo.getContent());
            rows.add(row);
        }
        data.setRows(rows);
        try {
            ExportExcelUtils.exportExcel(response, "接收邮件列表", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
