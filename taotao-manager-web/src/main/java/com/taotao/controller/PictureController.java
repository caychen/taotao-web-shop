package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;

@Controller
@RequestMapping("/pic")
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String imageServerUrl;

	@RequestMapping("/upload")
	@ResponseBody
	public String picUpload(MultipartFile uploadFile) {
		try {
			// System.out.println(imageServerUrl);
			// 1、接收上传的文件
			// 2、取扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String ext = originalFilename.substring(originalFilename.indexOf(".") + 1);

			// 3、上传到图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");

			// 4、响应上传图片的url
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), ext);
			url = imageServerUrl + url;

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("error", 1);
			result.put("url", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
	}

}
