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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description 用户登录和注销
 */
@Controller
@RequestMapping("/")
public class DownController {

    private static Logger logger = Logger.getLogger(DownController.class);

    @Autowired
    private FileLogService fileLogService;

    /*@RequestMapping(value = "/uploadfile/{userid}")
    public String uploadfile(@RequestParam("file") MultipartFile file,@PathVariable("userid") String userid,
                             HttpServletRequest request) {
        String msg = "";
        if (!file.isEmpty()) {

            String storePath = ConstantByProperties.basePath+userid+"/";//存放我们上传的文件路径
            String fileName = file.getOriginalFilename();
            logger.info("[upload] fileName = " + fileName + ", status = upload");
            File filepath = new File(storePath, fileName);
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();//如果目录不存在，创建目录
            }
            try {
                file.transferTo(new File(storePath + File.separator + fileName));//把文件写入目标文件地址
                fileLogService.deleteByPrimaryKey(fileName);
                FileLogPojo fileLog = new FileLogPojo();
                fileLog.setFilename(fileName);
                fileLog.setFileuser(userid);
                fileLog.setFiletime(CommonDate.getTime24());

                float flsie=file.getSize();
                String fileSize="";
                DecimalFormat df = new DecimalFormat("###.00");
                if(flsie>1024*1024){
                    fileSize=df.format(flsie/(1024*1024))+" M";
                }else if(flsie>1024){
                    fileSize=df.format(flsie/(1024))+" K";
                }else{
                    fileSize=flsie+" B";
                }
                fileLog.setFilesize(fileSize);
                fileLogService.insert(fileLog);
                logger.info("[upload] fileName = " + fileName + ", status = succ");
                msg = "文件上传成功！";
            } catch (Exception e) {
                e.printStackTrace();
                msg = "文件上传失败！";
                logger.info("[upload] fileName = " + fileName + ", status = fail");
            }
        } else {
            msg = "上传的文件为空！";
            logger.info("[upload] fileName = null , status = null");
        }
        request.setAttribute("msg", msg);
        return "upload";
    }*/

    @RequestMapping(value = "/down")
    public String down() {
        return "down";
    }

    @RequestMapping(value = "/filelist")
    @ResponseBody
    public List<FileLogPojo> filelist() {
        List<FileLogPojo> fileLogs = fileLogService.selectAll();
        return fileLogs;
    }

    @PostMapping("/filelistpost")
    @ResponseBody
    public Result filelistpost() {
        return fileLogService.selectAll1();
    }

    @PostMapping("/filelistpostbyfilename")
    @ResponseBody
    public Result filelistpostbyfilename(String filename, Integer pageNo, Integer pageSize) {
        return fileLogService.selectByFileName(filename);
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

    @PostMapping(value = "/downfilepost")
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
    }

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
