package com.xxx.controller;

import com.xxx.pojo.UserPojo;
import com.xxx.service.UserService;
import com.xxx.utils.WordDefined;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.UUID;

@Controller
public class UserController {

    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 获取个人头像路径
     */
    @RequestMapping("/head/{userid}")
    @ResponseBody
    public UserPojo head(@PathVariable("userid") String userid, HttpServletRequest request, HttpServletResponse response){
        UserPojo user = userService.getUserById(userid);
        return user;
    }

    /**
     * 跳转到个人信息页面
     */
    @RequestMapping(value = "/info/{userid}", method = RequestMethod.GET)
    public ModelAndView toInformation(@PathVariable("userid") String userid){
        ModelAndView view = new ModelAndView("info-show");
        UserPojo user = userService.getUserById(userid);
        view.addObject("user",user);
        return view;
    }

    /**
     * 显示个人信息编辑页面
     */
    @RequestMapping(value = "/config/{userid}")
    public ModelAndView setting(@PathVariable("userid") String userid){
        ModelAndView view = new ModelAndView("user/infoset");
        UserPojo user = userService.getUserById(userid);
        view.addObject("user", user);
        return view;
    }

    /**
     * 更新用户个人信息
     */
    @RequestMapping(value = "/update/{userid}", method = RequestMethod.POST)
    public String updateUser(@PathVariable("userid") String userid, HttpSession session, UserPojo user, RedirectAttributes attributes, HttpServletRequest request){
        int flag = userService.updateUser(user);
        if(flag > 0){
            user = userService.getUserById(userid);
            session.setAttribute("user", user);
            //Log log = LogUtil.setLog(userid, CommonDate.getTime24(), WordDefined.LOG_TYPE_UPDATE,WordDefined.LOG_DETAIL_UPDATE_PROFILE, NetUtil.getIpAddress(request));
            //logService.insertLog(log);
            session.setAttribute("userid", userid);
            session.setAttribute("user", user);
            session.setAttribute("login_status",true);
            attributes.addFlashAttribute("message", "["+userid+"]资料更新成功!");
        }else{
            attributes.addFlashAttribute("error", "["+userid+"]资料更新失败!");
        }
        return "redirect:/config/{userid}";
    }

    /**
     * 修改密码s
     */
    @RequestMapping(value = "/pass/{userid}", method = RequestMethod.POST)
    public String updateUserPassword(@PathVariable("userid") String userid,String oldpass, String newpass,HttpSession session, RedirectAttributes attributes,HttpServletRequest request){
        UserPojo user = userService.getUserById(userid);
        if(oldpass.equals(user.getPassword())){
            user.setPassword(newpass);
            int flag = userService.updateUser(user);
            if(flag > 0){
                //Log log = LogUtil.setLog(userid, CommonDate.getTime24(), WordDefined.LOG_TYPE_UPDATE,WordDefined.LOG_DETAIL_UPDATE_PASSWORD, NetUtil.getIpAddress(request));
                //logService.insertLog(log);
                session.setAttribute("userid", userid);
                session.setAttribute("user", user);
                session.setAttribute("login_status",true);
                attributes.addFlashAttribute("message", "["+userid+"]密码更新成功!");
            }else{
                attributes.addFlashAttribute("error", "["+userid+"]密码更新失败!");
            }
        }else{
            attributes.addFlashAttribute("error", "原密码错误!");
        }
        return "redirect:/config/{userid}";
    }

    @RequestMapping(value = "/uppic/{userid}", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String updateUserPic(@PathVariable("userid") String userid,String image,HttpServletRequest request){

        JSONObject responseJson = new JSONObject();
        String filePath = WordDefined.IMG_PATH+userid+"/";
        String PicName= UUID.randomUUID().toString()+".png";

        String header ="data:image";
        String[] imageArr=image.split(",");
        if(imageArr[0].contains(header)) {//是img的

            // 去掉头部
            image=imageArr[1];
            // 修改图片
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] decodedBytes = decoder.decodeBuffer(image); // 将字符串格式的image转为二进制流（biye[])的decodedBytes
                String imgFilePath = filePath + PicName;           //指定图片要存放的位
                File targetFile = new File(filePath);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                FileOutputStream out = new FileOutputStream(imgFilePath);        //新建一个文件输出器，并为它指定输出位置imgFilePath
                out.write(decodedBytes); //利用文件输出器将二进制格式decodedBytes输出
                out.close();
                // 修改图片
                UserPojo user = userService.getUserById(userid);
                user.setProfilehead(PicName);
                int flag = userService.updateUser(user);
                if(flag > 0){
                    //Log log = LogUtil.setLog(userid, CommonDate.getTime24(), WordDefined.LOG_TYPE_UPDATE,WordDefined.LOG_DETAIL_UPDATE_PROFILEHEAD, NetUtil.getIpAddress(request));
                    //logService.insertLog(log);

                    request.getSession().setAttribute("user", user);
                    responseJson.put("result","ok");
                    responseJson.put("msg","上传成功！");
                    responseJson.put("fileUrl","/static/upload/img/"+userid+"/" + PicName);
                }else{
                    responseJson.put("result","error");
                    responseJson.put("msg","上传失败！");
                }
            } catch (IOException e) {
                //e.printStackTrace();
                responseJson.put("result","error");
                responseJson.put("msg","上传失败！");
            }
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "/pic/{userid}/{profilehead}", method = RequestMethod.GET)
    public void showUserPic(@PathVariable("userid") String userid,@PathVariable("profilehead") String profilehead,
                    HttpServletRequest request, HttpServletResponse response) {

        String fileName = WordDefined.IMG_PATH +userid+"/"+profilehead;
        logger.info("[pic] fileName = " + profilehead + ", status = show");
        File filepath = new File(fileName);
        FileInputStream inputStream;
        if (!filepath.exists()) {
            fileName = WordDefined.IMG_PATH + "default_head.jpg";
        }
        try {
            inputStream = new FileInputStream(fileName);
            int available = inputStream.available();
            byte[] data = new byte[available];
            inputStream.read(data);
            inputStream.close();

            response.setCharacterEncoding("UTF-8");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            os.write(data);
            logger.info("[pic] data = " + profilehead + ", status = show");
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
