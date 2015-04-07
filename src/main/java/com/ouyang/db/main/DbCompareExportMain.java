package com.ouyang.db.main;

import java.util.Properties;

import com.ouyang.db.config.PropertiesLoader;
import com.ouyang.db.handler.DbCompareExportHandler;
import com.ouyang.db.handler.DbQueryWrapper;

public class DbCompareExportMain {
	
	public static void main(String[] args) throws Exception {
//		String fileName = args[0];
		String fileName = "compare-db-info";
		Properties prop = initProperties(fileName);
		if(prop == null){
			//读取DB文件进行分析比较
			throw new RuntimeException("配置文件找不到，请检查！");
		}else{
			//连接数据库进行分析比较
			String dbType = getDbTypes(prop);
			if(null == dbType){
				throw new RuntimeException("不支持的数据库配置类型！");
			}
		}
		executeDbCompareExportHandler(fileName,prop);
//		DbCompareExportHandler dbCompareExportHandler = initDbCompareExportHandler(prop);
//		if(dbCompareExportHandler == null){
//			throw new RuntimeException("不支持的数据库配置类型！");
//		}
//		executeDbCompareExportHandler(fileName, dbCompareExportHandler, prop);
	}

	private static void executeDbCompareExportHandler(String fileName,
			Properties prop) {
		if("compare-company-init-info".equals(fileName)) {
			
		} else if ("compare-db-info".equals(fileName)) {
			DbQueryWrapper dbqw = new DbQueryWrapper();
			dbqw.exportCompareDbInfo(prop);

		} else if ("export-info-data".equals(fileName)) {

		} else if ("export-sql".equals(fileName)) {

		} else {
			throw new RuntimeException("配置执行类型有错误，请检查！");
		}
		
	}

	private static String getDbTypes(Properties prop) {
		String dbType = (String)prop.get("db.type");
		if("mysql".equals(dbType)){
			return dbType;
		}else if ("oracle".equals(dbType)){
			return dbType;
		}else{			
			return null;
		}
	}

	/**
	 * 执行Handler类
	 * @param fileName
	 * @param dbCompareExportHandler
	 * @param prop
	 */
	private static void executeDbCompareExportHandler(String fileName,
			DbCompareExportHandler dbCompareExportHandler, Properties prop) {
		// TODO Auto-generated method stub
		if("compare-company-init-info".equals(fileName)) {
			dbCompareExportHandler.exportCompareCompanyInitInfo(prop);
		} else if ("compare-db-info".equals(fileName)) {
			DbQueryWrapper dbqw = new DbQueryWrapper();
			dbqw.exportCompareDbInfo(prop);
//			dbCompareExportHandler.exportCompareDbInfo(prop);
		} else if ("export-info-data".equals(fileName)) {
			dbCompareExportHandler.exportInfoData(prop);
		} else if ("export-sql".equals(fileName)) {
			dbCompareExportHandler.exportSql(prop);
		} else {
			throw new RuntimeException("配置执行类型有错误，请检查！");
		}
	}

	/**
	 * 初始化DbCompareExportHandler类
	 * @param prop
	 * @return
	 */
	private static DbCompareExportHandler initDbCompareExportHandler(Properties prop) {
		// TODO Auto-generated method stub
		String dbType = (String)prop.get("db.type");
		if("mysql".equals(dbType)){
			
		} else if ("oracle".equals(dbType)){
			
		}
		return null;
	}

	/**
	 * 初始化配置文件
	 * @param fileName
	 * @return
	 */
	private static Properties initProperties(String fileName) {
		// TODO Auto-generated method stub
		PropertiesLoader propertiesLoader = new PropertiesLoader(fileName);
		return propertiesLoader.getProp();
	}
}
