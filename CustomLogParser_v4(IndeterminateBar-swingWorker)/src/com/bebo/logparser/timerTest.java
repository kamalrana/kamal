package com.bebo.logparser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

public class timerTest extends JFrame{
	public static Log4jBottomPanel bp;
	public static Statement stmt;
	public static Connection connection;
	public timerTest() {
	bp=new Log4jBottomPanel();
	add(bp);
	pack();
	setVisible(true);
	
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
public static void main(String[] args) throws SQLException {
//	new timerTest();
//	bp.getTimer().start();
	
for(int i=0;i<=10;i++){
	try {
//		System.out.println("making connection");
		Class.forName(LogParserConstants.ClassForName);
		connection =DriverManager.getConnection(LogParserConstants.connectionString+LogParserConstants.dbFileLocation);
		stmt = connection.createStatement();
//		System.out.println("connection created");
		System.out.println("creating table "+stmt.executeUpdate(LogParserConstants.createTableQuery));
	}catch (Exception e) {
		if(e.getMessage().contains("table LogDataTable already exists")){
			if(stmt.executeUpdate("drop table "+LogParserConstants.tableName) > 0){
				System.out.println("table droped");
				System.out.println("table created :: "+stmt.executeUpdate(LogParserConstants.createTableQuery));
			}
			else
				System.out.println("unable to create table");
			e.printStackTrace();
		}
		else
		e.printStackTrace();
		 
	}
	finally{
		stmt.close();
		connection.close();
	}
}
}
}
