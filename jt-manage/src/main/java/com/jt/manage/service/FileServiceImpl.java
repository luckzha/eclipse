package com.jt.manage.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
@Service
public class FileServiceImpl implements FileService{
/**
 * 1.是否为正确的图片
 * 2.是否为恶意程序
 * 3.不能讲图片保存到同一个目录
 * 4.图片的重名问题
 * 
 * 解决策略；
 * 1.正则表达式判断图片
 * 2.使用imageBuffer转化图片宽高
 * 3.使用分文件夹存储
 * 4.UUID+三位随机数区分图片
 */
	
	//定义文件存储的根目录
	@Value("${image.localPath}")
	private String localPath;//="D:/jt-upload/";
	
	
	//定义虚拟路径的根目录
	@Value("${image.urlPath}")
	private String urlPath;//="http://image.jt.com/";
	
	
	@Override
	public PicUploadResult upload(MultipartFile uploadFile) {
		PicUploadResult result=new PicUploadResult();
		//1.获取图片的名称
		String fileName=uploadFile.getOriginalFilename();
		fileName=fileName.toLowerCase();
		//2.判断是否为图片的类型,表示开始结束，点表示任意字符，星号表示0或多个字符
		if(!fileName.matches("^.*(jpg|png|gif)$")){
			result.setError(1);//表示不是 图片
			return result;
		}
		
		
		//3.判断是否为恶意程序,工具类
		try {
			BufferedImage bufferedImage=
					ImageIO.read(uploadFile.getInputStream());
			int height=bufferedImage.getHeight();
			int width=bufferedImage.getWidth();
			if(height == 0 || width ==0){
				result.setError(1);
				return result;
				
			}
				//4.将图片分文件存储yyyy/MM-dd
				String DatePath=
						new SimpleDateFormat("yyyy/MM/dd").format(new Date());
				
				//判断是否有改文件夹
				String picDir=localPath+DatePath;
				File picFile=new File(picDir);
				
				if(!picFile.exists()){
					picFile.mkdirs();
				}
				
				
				//防止文件重名
				String uuid=UUID.randomUUID().toString().replace("-", "");
				
				int randomNum=new Random().nextInt(1000);
				//.jpg
			   String fileType=fileName.substring(fileName.lastIndexOf("."));
					   
			   String fileNowName=uuid+randomNum+fileType;	   
				
			   //实现文件上传
			   String realFilePath=picDir+"/"+fileNowName;
			   uploadFile.transferTo(new File(realFilePath));
			   
			   //将真实数据回显
			   result.setHeight(height+"");
			   result.setWidth(width+"");
				
			   
			   
			   
			   
			   
			   
			   //实现虚拟路径拼接
			   String realUrl=urlPath+DatePath+"/"+fileNowName;
			   result.setUrl(realUrl);		
		} catch (IOException e) {

			e.printStackTrace();
			result.setError(1);//文件上传有误
		}

		
		
		return result;
	}

}
