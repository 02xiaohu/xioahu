package com.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


@Controller
public class FileUploadController implements ServletContextAware {

	private ServletContext servletContext;
	
 
	public void setServletContext(ServletContext  context) {
		this.servletContext  = context;
	}
	
	@RequestMapping(value="/upload.do")
	public String handleUploadData(String name,@RequestParam("myFile")CommonsMultipartFile file){
		if (!file.isEmpty()) {
			   String path = this.servletContext.getRealPath("/upload/");  //��ȡ���ش洢·��
			   System.out.println(path);
			   String fileName = file.getOriginalFilename();
			   String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
			   System.out.println(fileName); 
			   System.out.println(fileType); 
			   File file1 = new File(path,fileName); //�½�һ���ļ�
			     try {
				byte[] b= file.getBytes();//���ϴ����ļ�д���½����ļ���
			    FileOutputStream fos = new FileOutputStream(file1);
			    fos.write(b);
			    fos.flush();
				fos.close();
			   } catch (Exception e) {
				    e.printStackTrace();
			   }
			   return "redirect:upload_ok.jsp";
			}else{
				return "redirect:upload_error.jsp";
			}
	}
}

