package com.redcms.servlet.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

import com.redcms.db.Db;
@WebServlet("/admin/uploadpic/imguploadpictures")
public class ImageUploads2 extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		int lastid=0;
		try {
			//文件保存目录路径
			String savePath = this.getServletContext().getRealPath("/") + "ups/";

			//文件保存目录URL
			String saveUrl  = request.getContextPath() + "/ups/";

			//定义允许上传的文件扩展名
			HashMap<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");
			extMap.put("flash", "swf,flv");
			extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
			extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

			//最大文件大小
			long maxSize = 1000000;


			if(!ServletFileUpload.isMultipartContent(request)){
				out.println(getError("请选择文件。"));
				return;
			}
			//检查目录
			File uploadDir = new File(savePath);
			if(!uploadDir.isDirectory()){
				out.println(getError("上传目录不存在。"));
				return;
			}
			//检查目录写权限
			if(!uploadDir.canWrite()){
				out.println(getError("上传目录没有写权限。"));
				return;
			}

			String dirName = request.getParameter("dir");
			if (dirName == null) {
				dirName = "image";
			}
			if(!extMap.containsKey(dirName)){
				out.println(getError("目录名不正确。"));
				return;
			}
			//创建文件夹
			savePath += dirName + "/";
			saveUrl += dirName + "/";
			File saveDirFile = new File(savePath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			savePath += ymd + "/";
			saveUrl += ymd + "/";
			File dirFile = new File(savePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			List<FileItem> items =upload.parseRequest(request);
			Iterator<FileItem> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				String fileName = item.getName();
				long fileSize = item.getSize();
				if (!item.isFormField()) {
					//检查文件大小
					if(item.getSize() > maxSize){
						out.println(getError("上传文件大小超过限制。"));
						return;
					}
					//检查扩展名
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
						out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
						return;
					}

					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
					try{
						File uploadedFile = new File(savePath, newFileName);
						item.write(uploadedFile);
						Db.update("insert into pictures(path,priority) values(?,?)",saveUrl+"/"+newFileName,10);
						Object lastobj=Db.query("select LAST_INSERT_ID() from dual", new ArrayHandler())[0];
					
						if(lastobj instanceof Long)
						{
							lastid=((Long)lastobj).intValue();
						}else if(lastobj instanceof BigInteger)
						{
							lastid=((BigInteger)lastobj).intValue();
						}
						
						
						
					}catch(Exception e){
						out.println(getError("上传文件失败。"));
						return;
					}

				
				
					
					JSONObject obj = new JSONObject();
					obj.put("error", 0);
					obj.put("url", saveUrl + newFileName);
					obj.put("upid", lastid);
					out.println(obj.toJSONString());
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toJSONString();
	}
}
