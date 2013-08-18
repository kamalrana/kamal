package com.bebo.log4j.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bebo.logparser.LogParserConstants;

public class DBOperation {
	private Connection connection = null;
	private Statement stmt = null;
	private PreparedStatement ps = null;
	BufferedReader br = null;
	// String dbName ="c:/sqlite/testDB.db";
	boolean b = true;
	String date = "";
	String level = "";
	private File dbFile;
	private String dbName;
	private boolean deleteFlag;

	public boolean operation(String datePattern, String fileLocation) {
		try {
			 dbName = LogParserConstants.dbFileLocation;
			 
			dbFile = new File(dbName);
			
			deleteOldDbFIle(dbFile, dbName);

			createConnection();

			stmt.executeUpdate(LogParserConstants.createTableQuery);

			stmt.executeUpdate("PRAGMA synchronous=OFF");
			
			stmt.executeUpdate("PRAGMA journal_mode = WAL");

			stmt.executeUpdate("PRAGMA locking_mode=OFF");

			System.out.println("transaction begin");

			stmt.executeUpdate("BEGIN TRANSACTION");

			ps = getConnection().prepareStatement(LogParserConstants.insetSql);

			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileLocation))));
			
			String line="";
			
			while ((line=br.readLine())!=null) {
				line = line.trim();
//				System.out.println("line is :: " + line);
				if (line.contains("DEBUG")) {
					level = "DEBUG";
				} else if (line.contains("INFO")) {
					level = "INFO";
				} else if (line.contains("FATAL")) {
					level = "FATAL";
				} else if (line.contains("ERROR")) {
					level = "ERROR";
				} else if (line.contains("WARN")) {
					level = "WARN";
				} else if (line.contains("TRACE")) {
					level = "TRACE";
				}

				Pattern p = Pattern.compile(datePattern);
				Matcher m = p.matcher(line);

				date = "";
				// System.out.println("datePattern is " + m.pattern());
				while (m.find()) {
//					System.out.println(m.start() + ">>" + m.group());
					date = m.group();
					break;
				}
				if (date == "")
					continue;

				ps.setString(1, date);
				ps.setString(2, level);
				ps.setString(3, line);
				ps.addBatch();
				ps.executeBatch();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			stmt.executeUpdate("END TRANSACTION");
			ps.close();
			br.close();
			closeConnection();
		} catch (Exception e) {
			System.out.println("Exception catch ");
			e.printStackTrace();
		}
		return true;
	}

	private void deleteOldDbFIle(File dbFile, String dbName) throws IOException {
		System.out.println("LogParserConstants.dbFileLocation :: "+LogParserConstants.dbFileLocation);
		if(dbFile.exists()){
			System.out.println("dbName :: " + dbName);
			System.out.println("existing db file deleted "+dbFile.delete());
		}
		
		if (!dbFile.exists()) {
			String str = "";
			if(dbName.lastIndexOf("\\")>0){
				str = dbName.substring(0, dbName.lastIndexOf("\\") + 1);
				str += "sqlite3.exe "+ dbName.substring(dbName.lastIndexOf("\\") + 1,dbName.length());
			}
			else{
				str = dbName.substring(0, dbName.lastIndexOf("/") + 1);
				str += "sqlite3.exe "+ dbName.substring(dbName.lastIndexOf("/") + 1,dbName.length());
			}
				
			System.out.println("str is :: " + str);
			
			Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", str });
			
			if(dbFile.exists())
			System.out.println("new db File Created");
		}
	}

	public void createConnection() {
		try {
			Class.forName(LogParserConstants.ClassForName);
			setConnection(DriverManager.getConnection(LogParserConstants.connectionString+LogParserConstants.dbFileLocation));
			stmt = getConnection().createStatement();
			System.out.println("connection created");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void closeConnection(){
		try {
			System.out.println("closing db connection");
			stmt.close();
			getConnection().close();
			System.out.println("statement is close ");
			System.out.println("connection is close  ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
