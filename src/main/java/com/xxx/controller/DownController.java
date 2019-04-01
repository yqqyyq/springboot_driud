package com.xxx.controller;

import com.xxx.pojo.FileLogPojo;
import com.xxx.quartz.Result;
import com.xxx.service.FileLogService;
import com.xxx.upload.ConstantByProperties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description 用户登录和注销
 */
@Controller
@RequestMapping("/file")
public class DownController {

    private static Logger logger = Logger.getLogger(DownController.class);

    @Autowired
    private FileLogService fileLogService;

    @PostMapping("/listfile")
    @ResponseBody
    public Result list(String filename, Integer pageNo, Integer pageSize) {
        List<FileLogPojo> list =fileLogService.selectByFileName(filename);
        return Result.ok(list);
    }

    @RequestMapping(value = "/downfile")
    public ResponseEntity<byte[]> fileDownload(String filename, String filepath, HttpServletRequest request) throws IOException {
        String path = ConstantByProperties.basePath;//存放我们上传的文件路径
        logger.info("[down] fileName = " + filename + " , status = down");
        File file = new File(path + filepath);
        if (file.exists()) {
            // 设置响应头通知浏览器下载
            HttpHeaders headers = new HttpHeaders();
            // 将对文件做的特殊处理还原
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            filename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
            logger.info("[down] fileName = " + filename + " , status = succ");
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } else {
            logger.info("[down] fileName = " + filename + " , status = fail");
            return new ResponseEntity<byte[]>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@PostMapping(value = "/downfilepost")
    public ResponseEntity<byte[]> fileDownloadPost(@RequestParam(value = "fileuser") String fileuser, @RequestParam(value = "filename") String filename, @RequestParam(value = "filepath") String filepath, HttpServletRequest request) throws IOException {
        String path = ConstantByProperties.basePath + fileuser + "/";//存放我们上传的文件路径
        logger.info("[downpsot] fileName = " + filename + " , status = down");
        File file = new File(path + filepath);
        if (file.exists()) {
            // 设置响应头通知浏览器下载
            HttpHeaders headers = new HttpHeaders();
            // 将对文件做的特殊处理还原
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            filename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
            logger.info("[downpsot] fileName = " + filename + " , status = succ");
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } else {
            logger.info("[downpsot] fileName = " + filename + " , status = fail");
            return new ResponseEntity<byte[]>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    /*public String getFilename(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
        String[] IEBrowerKeyWords = {"MSIE", "Trident", "Edge"};
        String userAgent = request.getHeader("User-Agent");
        for (String keyword : IEBrowerKeyWords) {
            if (userAgent.contains(keyword)) {
                return URLEncoder.encode(filename, "UTF-8");
            }
        }
        return new String(filename.getBytes("UTF-8"), "ISO-8859-1");
    }*/
}
