package com.xxx.controller;

import com.xxx.pojo.FileLogPojo;
import com.xxx.service.FileLogService;
import com.xxx.upload.ConstantByProperties;
import com.xxx.upload.Range;
import com.xxx.upload.StreamException;
import com.xxx.utils.CommonDate;
import com.xxx.utils.IoUtil;
import com.xxx.utils.NetUtil;
import com.xxx.utils.WordDefined;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 前端多个文件上传时串行调用的，并非同时上传
 * 
 * @author 
 *
 */
@Controller
public class UploadController {

	static final int BUFFER_LENGTH = 10240;
	static final String START_FIELD = "start";
	public static final String CONTENT_RANGE_HEADER = "content-range";

	@Autowired
	private FileLogService fileLogService;

	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = 0;
		boolean success = true;
		String message = "";
		JSONObject json = new JSONObject();
		final String token = request.getParameter(WordDefined.TOKEN_FIELD); // 文件名和大小的hashcode编码
		final String size = request.getParameter(WordDefined.FILE_SIZE_FIELD); // 文件大小
		final String fileName = request.getParameter(WordDefined.FILE_NAME_FIELD); // 文件名
		final PrintWriter writer = response.getWriter();
		try {
            NetUtil.doOptions(request, response);
			// 创建空文件，以及上级文件夹名
			File file = IoUtil.getTokenedFile(token);
			start = file.length();
			if (token.endsWith("_0") && "0".equals(size) && 0 == start)
				file.renameTo(IoUtil.getFile(fileName));
		} catch (FileNotFoundException e) {
			message = "Error: " + e.getMessage();
			success = false;
		} finally {
			try {
				if (success)
					json.put(START_FIELD, start);
				json.put(WordDefined.SUCCESS, success);
				json.put(WordDefined.MESSAGE, message);
			} catch (JSONException e) {

			}
			writer.write(json.toString());
			IoUtil.close(writer);
		}
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFileListPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
        NetUtil.doOptions(request, response);
		// 文件名和大小的hashcode编码
		final String token = request.getParameter(WordDefined.TOKEN_FIELD);
		// 文件名
		final String fileName = request.getParameter(WordDefined.FILE_NAME_FIELD);
		Range range = IoUtil.parseRange(request);
		OutputStream out = null;
		InputStream content = null;
		final PrintWriter writer = response.getWriter();

		JSONObject json = new JSONObject();
		long start = 0;
		boolean success = true;
		String message = "";
		File f = IoUtil.getTokenedFile(token);
		try {
			if (f.length() != range.getFrom()) {
				/** drop this uploaded data */
				throw new StreamException(StreamException.ERROR_FILE_RANGE_START);
			}

			out = new FileOutputStream(f, true);
			content = request.getInputStream();
			int read = 0;
			final byte[] bytes = new byte[BUFFER_LENGTH];
			while ((read = content.read(bytes)) != -1) {
				out.write(bytes, 0, read);
				try {
					// 这里主要是控制读写速度，呈现给前端的是上传速度
					Thread.sleep(ConstantByProperties.uploadSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			start = f.length();
		} catch (StreamException se) {
			success = StreamException.ERROR_FILE_RANGE_START == se.getCode();
			message = "Code: " + se.getCode();
			throw new StreamException(se.getCode());
		} catch (FileNotFoundException fne) {
			message = "Code: " + StreamException.ERROR_FILE_NOT_EXIST;
			success = false;
			throw new FileNotFoundException();
		} catch (IOException io) {
			message = "IO Error: " + io.getMessage();
			success = false;
			throw new IOException(io);
		} finally {
			IoUtil.close(out);
			IoUtil.close(content);

			/** rename the file */
			Map<String, String> map = new HashMap<String, String>();

			if (range.getSize() == start) {
				/** fix the `renameTo` bug */
				try {
					String userid= (String) request.getSession().getAttribute("userid");

					String cgSubDir = ConstantByProperties.basePath+userid+"/";
					String uuid = UUID.randomUUID().toString();

					String fileSaveName = uuid + "." + fileName.split("\\.")[fileName.split("\\.").length - 1];

					String url = cgSubDir + fileSaveName;

					Path pathtrue = f.toPath().resolveSibling(url);
					File parentFile = pathtrue.toFile().getParentFile();
					if (!parentFile.exists()) {
						parentFile.mkdirs();
					}
					Files.move(f.toPath(), pathtrue);

					fileLogService.deleteByPrimaryKey(fileName);
					FileLogPojo fileLog = new FileLogPojo();
					fileLog.setFilename(fileName);
					fileLog.setFileuser(userid);
					fileLog.setFiletime(CommonDate.getTime24());
                    fileLog.setFilepath(fileSaveName);
					float flsie=range.getSize();
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

					map.put("name", fileName);
					map.put("uuid", uuid);
					map.put("url", url);
					// 获得上传后的文件对象,可以做一些后期的处理，比如生成图片缩略图，视频缩略图等等,最好用异步的方式去执行这些操作,开启异步配置自行百度
					File newFile = pathtrue.toFile();

					// 生成图片缩略图,生成缩略图存放的路径跟原图路径是相同的
					//imageAsync.createThumbnail(newFile);
					// 异步生成视频缩略图,第二个参数表示取(视频长度/xx)的那一帧作为缩略图
					//videoAsync.randomGrabberFFmpegImage(pathtrue.toString(), 2);
				} catch (IOException e) {
					e.printStackTrace();
					success = false;
					message = "Rename file error: " + e.getMessage();
				}
			}
			try {
				if (success) {
					json.put(START_FIELD, start);
					json.put("map", map);
				}
				json.put(WordDefined.SUCCESS, success);
				json.put(WordDefined.MESSAGE, message);
			} catch (JSONException e) {
				System.out.println(e.toString());
			}
			writer.write(json.toString());
			IoUtil.close(writer);
		}
	}
}
