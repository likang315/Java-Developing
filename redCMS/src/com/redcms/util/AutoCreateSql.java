package com.redcms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.redcms.db.ConnectionManager;

/**
 * 依据数据表自动生成SQL语句
 * @author likang
 *
 */
public class AutoCreateSql {

	public static void main(String[] args)throws Exception
	{
		String tablename="data";
		String dataBasename="redcmsv6";
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/redcmsv6","root","mysql");
		
		Statement stat=con.createStatement();
		ResultSet rs=stat.executeQuery("select * from "+tablename);
		ResultSetMetaData rmd=rs.getMetaData();
		
		int count=rmd.getColumnCount();
		//存储所有字段名
		List<String> flist=new ArrayList<String>();
		for(int i=0;i<count;i++)
		{
			if("id".equals(rmd.getColumnName(i+1)))
				continue;
			flist.add(rmd.getColumnName(i+1));
		}
		
	
		StringBuilder field=new StringBuilder();
		StringBuilder query=new StringBuilder();
		StringBuilder update=new StringBuilder();
		
		for(int i=0;i<flist.size();i++)
		{
			if(i<flist.size()-1)
			{
				field.append(flist.get(i)+",");
				query.append("?,");
				update.append(flist.get(i)+"=?,");
			}
			else
			{
				field.append(flist.get(i));	
				query.append("?");
				update.append(flist.get(i)+"=?");
			}
		}
		
		
		String s1="insert into "+tablename+"("+field.toString()+") values("+query.toString()+")";
		System.out.println(s1);
     
		
		String s2="update "+tablename+" set "+update.toString()+" where id=?";
		System.out.println(s2);
		
		System.out.println("delete from "+tablename+" where id=?");
		
	}

}
