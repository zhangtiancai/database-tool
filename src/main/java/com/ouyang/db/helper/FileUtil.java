package com.ouyang.db.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * 文件工具，用于创建文件夹，生成文件
 * @Author: zhangtc
 * @Date: 2015-4-7 上午11:10:04 
 * @ModifyUser: zhangtc
 * @ModifyDate: 2015-4-7 上午11:10:04 
 * @Version:V7.5
 */
public class FileUtil {

	/**
	 * 
	 * @Discription:创建文件夹/生成文件
	 * @param pathName
	 * @param name void
	 * @Author: zhangtc
	 * @Date: 2015-4-7 上午11:08:12
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-7 上午11:08:12
	 */
	public static void makeFile(String pathName, String name) {
		if(null!=pathName){
			File path=new File(pathName);   
			File dir=new File(path,name);
			File file = new File(pathName);   
			if(!file.exists() && !file.isDirectory()){
				file.mkdir();//创建文件夹		 
			}else{
				if(!dir.exists()){
					try {
						dir.createNewFile();//生成分析日志文件
					} catch (IOException e) {
						e.printStackTrace();
					}  
				}
				
			}
		}		
	}
	
	/**
	 * 
	 * @Discription:生成内容文件
	 * @param fileName
	 * @param content
	 * @throws IOException void
	 * @Author: zhangtc
	 * @Date: 2015-4-7 上午11:15:23
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-7 上午11:15:23
	 */
	public static void writeFile(String fileName,String content) throws IOException{
		if(null!=content){
			FileOutputStream out = new FileOutputStream(new File(fileName));
			out.write(content.getBytes());
			out.close();		
		}
	}
	
}
