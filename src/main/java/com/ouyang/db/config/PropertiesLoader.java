package com.ouyang.db.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件
 * @author Ouyang
 *
 */
public class PropertiesLoader {

	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public PropertiesLoader(){
		
	}
	
	public PropertiesLoader(String configFileName){
		fileName = configFileName+".properties";
	}
	
	public Properties getProp(){
		Properties props = new Properties() ;
		InputStream stream = this.getClass().getResourceAsStream("/config/"+fileName);
		if(null!=stream){
			try {
				props.load(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return props;
		}else{
			return null;
		}
	}
	
	
}
