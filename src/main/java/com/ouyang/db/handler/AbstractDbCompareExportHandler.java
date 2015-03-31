package com.ouyang.db.handler;

import java.sql.Connection;
import java.util.Properties;


public abstract class AbstractDbCompareExportHandler implements
		DbCompareExportHandler {

	protected Connection getConnection(Properties prop) {
		return null;
	}
	
	protected void closeConnection(Connection conn){
		
	}

}
