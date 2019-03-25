package com.xxx.controller;

import com.xxx.pojo.FileLog;
import com.xxx.service.FileLogService;
import com.xxx.utils.CommonDate;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @Description 用户登录和注销
 */
@Controller
@RequestMapping("/")
public class FileController {

    private static Logger logger = Logger.getLogger(FileController.class);

    private static final String FILE_PATH = "/Users/yuqi/ideaspace/datasource/springboot_driud/src/main/resources/static/upload/file/";

    @Autowired
    private FileLogService fileLogService;

    @RequestMapping(value = "/about")
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/system")
    public String system() {
        return "system-setting";
    }

    @RequestMapping(value = "/upload")
    public String upload() {
        return "upload";
    }

    @RequestMapping("/chat")
    public ModelAndView getIndex(){
        ModelAndView mv = new ModelAndView("chat");
        return mv;
    }

    @RequestMapping(value = "/uploadfile/{userid}")
    public String uploadfile(@RequestParam("file") MultipartFile file,@PathVariable("userid") String userid,
                             HttpServletRequest request) {
        String msg = "";
        if (!file.isEmpty()) {

            String storePath = FILE_PATH+userid+"/";//存放我们上传的文件路径
            String fileName = file.getOriginalFilename();
            logger.info("[upload] fileName = " + fileName + ", status = upload");
            File filepath = new File(storePath, fileName);
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();//如果目录不存在，创建目录
            }
            try {
                file.transferTo(new File(storePath + File.separator + fileName));//把文件写入目标文件地址
                fileLogService.deleteByPrimaryKey(fileName);
                FileLog fileLog = new FileLog();
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
    }

    @RequestMapping(value = "/down")
    public String down() {
        return "down";
    }

    @RequestMapping(value = "/filelist")
    @ResponseBody
    public List<FileLog> filelist() {
        List<FileLog> fileLogs = fileLogService.selectAll();
        return fileLogs;
    }

    @RequestMapping(value = "/downfile")
    public ResponseEntity<byte[]> fileDownload(String filename, HttpServletRequest request) throws IOException {
        String path = FILE_PATH;//存放我们上传的文件路径
        filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
        logger.info("[down] fileName = " + filename + " , status = down");
        File file = new File(path + filename);
        if(file.exists()){
            // 设置响应头通知浏览器下载
            HttpHeaders headers = new HttpHeaders();
            // 将对文件做的特殊处理还原
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            logger.info("[down] fileName = "+filename+" , status = succ");
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        }else{
            logger.info("[down] fileName = "+filename+" , status = fail");
            return new ResponseEntity<byte[]>(null,null,HttpStatus.INTERNAL_SERVER_ERROR);
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