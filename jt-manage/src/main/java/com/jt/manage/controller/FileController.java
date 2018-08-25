package com.jt.manage.controller;

import java.io.File;
import java.io.IOException;

import org.apache.http.entity.mime.MultipartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileService;

@Controller
public class FileController {
	/**
	 * 要求文件上传完成后,再次跳转到文件上传页面
	 * @return
	 * @throws Exception 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/file")
	private String file(MultipartFile file) throws IllegalStateException, Exception{
		//准备文件上传的页面
		String path="D:/jt-upload";
		//判断文件夹是佛存在
		File filePath=new File(path);
		if(!filePath.exists()){
			//如果文件夹不存在，需要创建一个文件夹
			filePath.mkdirs();
		}
		
		//获取文件名称
		String fileName=file.getOriginalFilename();
		//实现文件上传
		file.transferTo(new File(path+"/"+fileName));
	
		
		return "redirect:/file.jsp";
	}
	
	
	
	@Autowired
	private FileService fileService;
	//实现商品的文件上传
	@RequestMapping("/pic/upload")                  //名字保持一致动态
	@ResponseBody  //返回对象调用本身的get方法，json串。
	public PicUploadResult uploadFile(MultipartFile uploadFile){
		return fileService.upload(uploadFile);
	}
	
	
	

}
