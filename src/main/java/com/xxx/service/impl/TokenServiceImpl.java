package com.xxx.service.impl;

import com.xxx.service.TokenService;
import com.xxx.utils.TokenUtil;
import com.xxx.utils.NetUtil;
import com.xxx.utils.WordDefined;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public void firstdoget(HttpServletRequest req, HttpServletResponse resp) {
        try {
            NetUtil.doOptions(req, resp);

            String name = req.getParameter(WordDefined.FILE_NAME_FIELD); // 文件名
            String size = req.getParameter(WordDefined.FILE_SIZE_FIELD); // 文件大小
            String token = TokenUtil.generateToken(name, size); // 利用文件名和文件大小重新生成编码作为临时文件的名字

            PrintWriter writer = resp.getWriter();
            JSONObject json = new JSONObject();
            json.put(WordDefined.TOKEN_FIELD, token);
            json.put(WordDefined.SUCCESS, true);
            json.put(WordDefined.MESSAGE, "");
            writer.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
