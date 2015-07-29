package com.feeling.web.common;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.log.LogInfo;
import com.feeling.utils.CryptUtil;
import com.feeling.utils.DateUtil;
import com.feeling.utils.DateUtil.Format;

public class WebFileHelper {
	private static final Logger logger =LogInfo.WEB_LOG;
	private String avatarFilePath = null;
	private String eventFilePath = null;
	private String rootFilePath = null;//文件存储的根磁盘地址
	private String defaultUserPicPath = null;
	private String rootUrlPath = null;//文件存储的根url地址
	private Integer avatarSize = 5;//照片最大【mb】
	private Integer eventFileSize = 50;//每个文件最大【mb】
	private Integer eventFileNums = 1;//事件最大文件数量
	 
	
	public Integer getAvatarSize() {
		return avatarSize;
	}

	public void setAvatarSize(Integer avatarSize) {
		this.avatarSize = avatarSize;
	}

	public Integer getEventFileSize() {
		return eventFileSize;
	}

	public void setEventFileSize(Integer eventFileSize) {
		this.eventFileSize = eventFileSize;
	}

	public Integer getEventFileNums() {
		return eventFileNums;
	}

	public void setEventFileNums(Integer eventFileNums) {
		this.eventFileNums = eventFileNums;
	}

	public String getRootUrlPath() {
		return rootUrlPath;
	}

	public void setRootUrlPath(String rootUrlPath) {
		this.rootUrlPath = rootUrlPath;
	}

	public String getDefaultUserPicPath() {
		return defaultUserPicPath;
	}

	public void setDefaultUserPicPath(String defaultUserPicPath) {
		this.defaultUserPicPath = defaultUserPicPath;
	}

	public String getAvatarFilePath() {
		return avatarFilePath;
	}

	public void setAvatarFilePath(String avatarFilePath) {
		this.avatarFilePath = avatarFilePath;
	}

	public String getEventFilePath() {
		return eventFilePath;
	}

	public void setEventFilePath(String eventFilePath) {
		this.eventFilePath = eventFilePath;
	}

	public String getRootFilePath() {
		return rootFilePath;
	}

	public void setRootFilePath(String rootFilePath) {
		this.rootFilePath = rootFilePath;
	}
	/**
	 * 上传事件对应文件
	 * @param request
	 * @return HashMap<String,String>  path,type
	 * @throws Exception
	 */
	public HashMap<String,String> uploadEvent(HttpServletRequest request) throws Exception{
		String dateFileName = DateUtil.formatDate(new Date(),Format.YYYYMMDD);
		String eventPath = eventFilePath+dateFileName+"/";
		HashMap<String,String> hm = uploadFile(request,eventPath,eventFileNums,eventFileSize);
		return hm;
	}
	
	/**
	 * 获得事件对应文件路径
	 * @param eventFilePath
	 * @return
	 */
	public String getEventUrl(String eventFilePath){
		if(eventFilePath==null){
			return null;
		}
		return rootUrlPath+eventFilePath;
	}
	
	/**
	 * 获得用户头像
	 * @param userAvatar
	 * @return String
	 */
	public String getUserAvatarUrl(String userAvatar){
		if(StringUtils.isEmpty(userAvatar)){
			return defaultUserPicPath;
		}else{
			return rootUrlPath+userAvatar;
		}
	}
	/**
	 * 上传用户头像照片【需要用户id】
	 * @param request
	 * @param uid
	 * @throws Exception
	 */
	public String uploadUserAvatar(HttpServletRequest request,Integer uid) throws Exception{
		if(uid==null){
			return null;
		}
	 
		String userAvarat = avatarFilePath+uid+"/";
		HashMap<String,String> hm = uploadFile(request,userAvarat,1,avatarSize);
		if(hm!=null&&!hm.isEmpty()){
			return hm.keySet().iterator().next();
		}
		return null;
	}
	
	public HashMap<String,String> uploadFile(HttpServletRequest request,String path,int fileNums,int fileSize) throws Exception{
    	HashMap<String,String> hm =null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)){ 
       	//转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request; 
       	//取得request中的所有文件名  
           Iterator<String> iter = multiRequest.getFileNames();
           hm = new HashMap<String,String>();
           int count =0;
           while(iter.hasNext()){  
        	   //文件数达到上限
        	   if(count>=fileNums){
        		   break;
        	   }
               //记录上传过程起始时的时间，用来计算上传时间  
               long begin =  System.currentTimeMillis();  
               //取得上传文件  
               MultipartFile file = multiRequest.getFile(iter.next());  
               String myFileName =null;
               String pathUp =null; //定义上传路径
               String fileNamePre = null;
               String fileType = null;
               if(file != null){  
                   //取得当前上传文件的文件名称  
                   myFileName = file.getOriginalFilename();  
                   //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                   if(myFileName.trim() !=""){  
                	   //getSize 是字节数 所以要转换成mb 进行对比
                	   int size = (int)(file.getSize()/1048576);
                	   if(size > fileSize){
                		   throw new OptException(ReturnCodeEnum.FILE_LARGE_ERROR,String.valueOf(fileSize));
                	   }
                	   String fileName = file.getOriginalFilename();
                       String fileNames[] = fileName.split("[.]");
                       if(fileNames.length>=2){
                    	   fileType=fileNames[fileNames.length-1];
                    	   fileNamePre=fileName.replace(fileType, "");
                    	   fileNamePre=fileNamePre.replace(".", "");
                       }
                       if(fileNamePre==null){
                    	   continue;
                       }
                       //重命名上传后的文件名  
                       Random random = new Random();
                       int r = random.nextInt(1)%(1000) + 1;
                       fileName = CryptUtil.encrypt(System.currentTimeMillis()+""+r);
                       //定义上传路径  
                       File directory =new File(rootFilePath+path);    
                       //如果文件夹不存在则创建    
                       if(!directory .exists()  && !directory .isDirectory())      
                       {       
                    	   directory.mkdirs();    
                       }  
                       pathUp = path + fileName+"."+fileType;  
                       File localFile = new File(rootFilePath+pathUp); 
                     //记录上传该文件后的时间  
                       hm.put(pathUp, fileType);
                       count++;
                       try {
							file.transferTo(localFile);
						} catch (Exception e) {
							e.printStackTrace();
						} 
                   }  
               }  
               long finaltime =   System.currentTimeMillis();  
               logger.info("upload file:["+myFileName+"] use time is ["+(finaltime-begin)+"]ms ");
           }  
        }
        return hm;
    }
}
