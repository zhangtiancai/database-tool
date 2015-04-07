package com.ouyang.db.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public abstract class AbstractDbCompareExportHandler implements
		DbCompareExportHandler {

	protected Connection getBaseConnection(Properties prop) {
		Connection conn = null ;
		 try{
				String driver = prop.getProperty("db.driver") ;
				String url = prop.getProperty("base.db.url") ;
				String user = prop.getProperty("base.db.user") ;
				String password = prop.getProperty("base.db.password") ;
				if(null!=driver && null!=url && null!=url && null!=password){
					Class.forName(driver) ;
					conn = DriverManager.getConnection(url, user, password) ;					
				}
				return conn ;				
		 }catch(Exception e){
		    e.printStackTrace() ;
		 }
		 return null ;	
	}
	
	protected Connection getGoalConnection(Properties prop) {
		Connection conn = null ;
		 try{
				String driver = prop.getProperty("db.driver") ;
				String url = prop.getProperty("goal.db.url") ;
				String user = prop.getProperty("goal.db.user") ;
				String password = prop.getProperty("goal.db.password") ;
				if(null!=driver && null!=url && null!=url && null!=password){
					Class.forName(driver) ;
					conn = DriverManager.getConnection(url, user, password) ;					
				}
				return conn ;				
		 }catch(Exception e){
		    e.printStackTrace() ;
		 }
		 return null ;
	}
	
	protected void closeConnection(Connection conn) throws SQLException{
		if (conn != null) {
			 conn.close();
			 conn = null;
		}
	}

}
