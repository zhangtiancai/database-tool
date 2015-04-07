package com.ouyang.db.handler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ouyang.db.helper.FileUtil;
import com.ouyang.db.helper.ListUtil;
import com.ouyang.db.vo.Column;
import com.ouyang.db.vo.Table;
public class DbQueryWrapper extends AbstractDbCompareExportHandler implements DbCompareExportHandler {

	
	
	/**
	 * 
	 * @Discription:查询所有基库表
	 * @param prop
	 * @param sql
	 * @return
	 * @throws Exception List<Table>
	 * @Author: zhangtc
	 * @Date: 2015-4-2 上午10:01:54
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-2 上午10:01:54
	 */
	private List<Table> queryBaseAllTabs(Properties prop,String sql) throws Exception {
		  Connection conn = null;
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  List<Table> list = new ArrayList<Table>();
		  try {
			   conn = this.getBaseConnection(prop);
			   stmt = conn.prepareStatement(sql);
			   rs = stmt.executeQuery();
			   while (rs.next()) {
				   Table dto = new Table();
				   dto.setId("1");
				   dto.setTabName(rs.getString("TABLE_NAME"));
				   list.add(dto);
			   }
		  } finally {
			  replease(conn, stmt, rs);
		  }
		  return list;
	}
	
	/**
	 * 
	 * @Discription:查询所有对比新库表
	 * @param prop
	 * @param sql
	 * @return
	 * @throws Exception List<Table>
	 * @Author: zhangtc
	 * @Date: 2015-4-2 上午10:02:04
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-2 上午10:02:04
	 */
	private List<Table> queryGoalAllTabs(Properties prop,String sql) throws Exception {
		  Connection conn = null;
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  List<Table> list = new ArrayList<Table>();
		  try {
			   conn = this.getGoalConnection(prop);
			   stmt = conn.prepareStatement(sql);
			   rs = stmt.executeQuery();
			   while (rs.next()) {
				   Table dto = new Table();
				   dto.setId("2");
				   dto.setTabName(rs.getString("TABLE_NAME"));
				   list.add(dto);
			   }
		  } finally {
			  replease(conn, stmt, rs);
		  }
		  return list;
	}

	public void exportCompareCompanyInitInfo(Properties prop) {
		// TODO Auto-generated method stub
		
	}

	public void exportCompareDbInfo(Properties prop) {
		String baseSql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
		"WHERE TABLE_SCHEMA = "+"'"+prop.getProperty("base.db.name")+"'";
		String goalSql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
		"WHERE TABLE_SCHEMA = "+"'"+prop.getProperty("goal.db.name")+"'";
		try {
			List<Table> baseList = queryBaseAllTabs(prop,baseSql);
			List<Table> goalList = queryGoalAllTabs(prop,goalSql);
			List<String> differenceTable = compareTable(baseList,goalList,prop);
			if(differenceTable.size() > 0){
				writeDifferenceData(differenceTable,baseList,prop);			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @Discription:生成差异数据
	 * @param differenceTable
	 * @param baseList
	 * @param prop void
	 * @Author: zhangtc
	 * @Date: 2015-4-3 上午11:12:10
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-3 上午11:12:10
	 */
	private void writeDifferenceData(List<String> differenceTable,
			List<Table> baseList, Properties prop) {
		String name = "数据库表分析结果.txt";
		String pathName = prop.getProperty("export.folder");
		String fileName = pathName+"/"+name;
		makeFile(pathName,name);
		try {
			writeFile(differenceTable,baseList,prop,fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Discription:写入差异化表信息
	 * @param differenceTable
	 * @param baseList
	 * @param prop
	 * @param fileName
	 * @throws IOException void
	 * @Author: zhangtc
	 * @Date: 2015-4-3 上午11:10:52
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-3 上午11:10:52
	 */
	private void writeFile(List<String> differenceTable, List<Table> baseList,
			Properties prop, String fileName) throws IOException {
		String str = "";
		int m = 0;
		int leng = differenceTable.size();
		int baseLeng = baseList.size();
		String content = "";
		String title = prop.getProperty("goal.db.name")+"中多了"+leng+"张表：\n";
		content+=title;
		for (int i = 0; i < leng; i++) {  
			str+=" ["+differenceTable.get(i)+"] ";
			m++;
			if(m == 8){
				str+="\n";
				content+=str;
				str = "";
				m=0;						
			}
		} 	
		content+="----------------------------------------------------------------------------------------------------------------------------------------------\n";
		int index = 0;
		for(int k=0;k<baseLeng;k++){
			String tableName = baseList.get(k).getTabName();
			if(""!=compareTableColumn(tableName,prop)){
				index++;
				content+=index+"."+compareTableColumn(tableName,prop);				
			}
		}
		FileUtil.writeFile(fileName, content);		
	}

	/**
	 * 
	 * @Discription:比较表字段，属性
	 * @param tableName
	 * @param prop
	 * @return boolean true 表示存在字段属性差异
	 * @Author: zhangtc
	 * @Date: 2015-4-3 上午11:22:38
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-3 上午11:22:38
	 */
	private String compareTableColumn(String tableName, Properties prop) {
		String value = "";
		try {
			Table baseTable = queryBaseTableColumnByTable(tableName,prop);
			if(queryTableFlag(tableName,prop)){
				Table goalTable = queryGoalTableColumnByTable(tableName,prop);
				if(null!=baseTable && null!=goalTable){
					if(""!=compareColumnType(baseTable.getColumn(),goalTable.getColumn())){
						value+="["+tableName+"]"+"表字段存在差异？";
						value+=compareColumnType(baseTable.getColumn(),goalTable.getColumn())+"\n";							
					}			
				}
			}else{
				value+="["+tableName+"]"+"表不存在！\n";
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 
	 * @Discription:比较属性，类型差异
	 * @param column
	 * @param column2
	 * @return String
	 * @Author: zhangtc
	 * @Date: 2015-4-3 下午04:18:12
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-3 下午04:18:12
	 */
	private String compareColumnType(List<Column> column, List<Column> column2) {
		int baseLeng = column.size();
		int goalLeng = column2.size();
		List<String> baseList = new ArrayList<String>();
		List<String> goalList = new ArrayList<String>();
		for(int i=0;i<baseLeng;i++){
			baseList.add(column.get(i).getField()+":"+column.get(i).getType());
		}
		for(int i=0;i<goalLeng;i++){
			goalList.add(column2.get(i).getField()+":"+column2.get(i).getType());
		}
		List<String> list = ListUtil.diff(baseList, goalList);
		int leng = list.size();
		String value = "";
		for(int m=0;m<leng;m++){
			value+=" "+list.get(m)+" ";
		}
		return value;
	}

	/**
	 * 
	 * @Discription:查询表是否存在
	 * @param tableName
	 * @param prop
	 * @return
	 * @throws SQLException boolean
	 * @Author: zhangtc
	 * @Date: 2015-4-3 下午02:20:57
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-3 下午02:20:57
	 */
	private boolean queryTableFlag(String tableName, Properties prop) throws SQLException {
		boolean value = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT table_name FROM information_schema.tables WHERE table_name='"+tableName+"' ";
		  try {
			   conn = this.getGoalConnection(prop);
			   stmt = conn.prepareStatement(sql);
			   rs = stmt.executeQuery();
			   while (rs.next()) {
				   if(null!=rs.getString("table_name")){
					   value = true;
				   }
			   }
		  } finally {
			  replease(conn, stmt, rs);
		  }
		return value;
	}

	/**
	 * 
	 * @Discription:生成文件
	 * @param pathName
	 * @param name void
	 * @Author: zhangtc
	 * @Date: 2015-4-3 上午11:11:29
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-3 上午11:11:29
	 */
	private void makeFile(String pathName, String name) {
		FileUtil.makeFile(pathName, name);		
	}

	/**
	 * 
	 * @Discription:mysql 查询表属性结构
	 * @param tableName
	 * @param prop
	 * @return
	 * @throws SQLException Table
	 * @Author: zhangtc
	 * @Date: 2015-4-2 下午05:53:09
	 * @ModifyUser：zhangtc
	 * @ModifyDate: 2015-4-2 下午05:53:09
	 */
	private Table queryBaseTableColumnByTable(String tableName,Properties prop) throws SQLException{		 
		  String sql = "DESC " + tableName;
		  Table table = new Table();
		  List<Column> columnList = new ArrayList<Column>();
		  Connection conn = null;
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  try {
			   table.setTabName(tableName);
			   conn = this.getBaseConnection(prop);
			   stmt = conn.prepareStatement(sql);
			   rs = stmt.executeQuery();
			   while (rs.next()) {
				   Column column = new Column();
				   column.setName(tableName);
				   column.setField(rs.getString("Field"));
				   column.setType(rs.getString("Type"));
				   columnList.add(column);
			   }
			   table.setTabName(tableName);
			   table.setColumn(columnList);
		  } finally {
			  replease(conn, stmt, rs);
		  }
		  return table;
	}
	
	private Table queryGoalTableColumnByTable(String tableName,Properties prop) throws SQLException{		 
		  String sql = "DESC " + tableName;
		  Table table = new Table();
		  List<Column> columnList = new ArrayList<Column>();
		  Connection conn = null;
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  try {
			   table.setTabName(tableName);
			   conn = this.getGoalConnection(prop);
			   stmt = conn.prepareStatement(sql);
			   rs = stmt.executeQuery();
			   while (rs.next()) {
				   Column column = new Column();
				   column.setName(tableName);
				   column.setField(rs.getString("Field"));
				   column.setType(rs.getString("Type"));
				   columnList.add(column);
			   }
			   table.setTabName(tableName);
			   table.setColumn(columnList);
		  } finally {
			  replease(conn, stmt, rs);
		  }
		  return table;
	}

	private List<String> compareTable(List<Table> baseList, List<Table> goalList, Properties prop) {
		int baseLeng = baseList.size();
		int goalLeng = goalList.size();
		List<String> base = new ArrayList<String>();
		List<String> goal = new ArrayList<String>();
		for(int i=0;i<baseLeng;i++){
			Table table = baseList.get(i);
			base.add(table.getTabName());
		}
		for(int i=0;i<goalLeng;i++){
			Table table = goalList.get(i);
			goal.add(table.getTabName());
		}
		List<String> tem = ListUtil.diff(base, goal);
//		if(baseLeng > goalLeng){
//			System.out.println(prop.getProperty("goal.db.name")+"缺少"+(baseLeng-goalLeng)+"张表");		
//		}else{
//			System.out.println(prop.getProperty("goal.db.name")+"增加了"+(goalLeng-baseLeng)+"张表");
//		}
		List<String> increaseList = getIncreaseList(baseList,goalList,tem);
//		List<String> lackList = getLackList(baseList,goalList,tem);
		return increaseList;
	}

	private List<String> getIncreaseList(List<Table> baseList, List<Table> goalList, List<String> tem) {
		int leng = tem.size();
		List<String> increaseList = tem;
		for(int k=0;k<leng-1;k++){
			String name = tem.get(k);
			if(getDifferenceTable(name,baseList)){
				increaseList.remove(k);
			}
		}
		return increaseList;
	}
	
	private List<String> getLackList(List<Table> baseList, List<Table> goalList, List<String> tem) {
		int leng = tem.size();
		List<String> lackList = new ArrayList<String>();
		for(int k=0;k<leng-1;k++){
			String name = tem.get(k);
			if(getDifferenceTable(name,baseList)){
				lackList.add(name);
			}
		}
		return lackList;
	}

	private boolean getDifferenceTable(String string, List<Table> list) {
		boolean value = false;
		int baseLeng = list.size();
		for(int i=0;i<baseLeng;i++){
			String tableName = list.get(i).getTabName();
			if(tableName.equals(string)){
				value = true;
			}
		}
		return value;
	}

	public void exportInfoData(Properties prop) {
		// TODO Auto-generated method stub
		
	}

	public void exportSql(Properties prop) {
		// TODO Auto-generated method stub
		
	}
	
	 private static void replease(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
		  if (rs != null) {
		   rs.close();
		   rs = null;
		  }
		  if (stmt != null) {
		   stmt.close();
		   stmt = null;
		  }
		  if (conn != null) {
		   conn.close();
		   conn = null;
		  }
	}
	
}
