package com.bebo.logparser;

import java.awt.Dimension;

public class LogParserConstants {

	public static String ClassForName = "org.sqlite.JDBC";

	public static String remoteFileChooserWidowTitle = "Remote Location File Chooser";
	
	public static String toolLabel = "Log Mining Tool";
	public static String logFileLabel = "Log file location";
	public static String propertyLabel = "Property file location";
	public static String browseButton = "  Browse  ";
	public static String inputFormatLabel = "Input-Format";
	public static String IISLogFormat=   "      IISW3C           ";
	public static String log4jLogFormat= "      Log4j            ";
	
	public static String fromDateLBL = "From date";
	public static String toDateLBL = "To date";
	public static String logLevelLBL = " Log level";
	public static String textToFindInLogLBL = "Text to find";
	public static String parseErrorLebel = "Parse Errors";
	public static String noOfErrorFoundLBL_1 = "No Of Errors Found: ";
	
	public static String searchLogBTN = "Show Result";
	public static String parseLogBTN = "Parse Logs";
	public static String resetBTN = "Reset";
	
	public static String resetLabel = " Reset ";
	public static String viewGraphLabel = " View graph ";
	public static String groupLabel = "Group By";
	public static String quickParsingLabel = "Quick Parsing For Errors";
	public static String fullParsingLabel = "Full Parsing For Errors";	
	public static String showErrorLabel = "Show Errors";
	public static String selectLabel = "Select";
	public static String whereClauseLabel = "Where Clause";
	public static String orderByLabel = "Order By";
	public static String connTypeLabel = "Connection Type";
	
	public static String selectCriteriaBTN = "Select Criteria";
	public static String viewResultBTN = "View Result";
	public static String viewStatiticsBTN = "View Statitics";
	public static String viewLogFileBTN = "View Log File";
	public static String submitQueryBTN = "Submit Query"; 
	public static String openLocationBTN = "Open Location";
	
	public static String ascRADBTN = "Asc";
	public static String descRADBTN = "Desc";
	
	public static String selectOption = "Select";

	public static Dimension secondMainPanelSize = new Dimension( 780 , 520 );
	
	public static String logFilePathMSG = "Log file Path required.";
	public static String whereClauseMSG = "Please select atleast one where clause";
	public static String selectClauseMSG = "Please select atleast one select clause for result display";
	public static String fileNotFoundMSG = "File doesn't exist";
	public static String noOfLinesMSG = "No. of Lines = ";
	
	public static String fileNotFoundError = "File Not Found Exception : ";
	public static String IOError = "Input Output Exception : ";
	
	public static String localLocation =  "Local Location"; 
	public static String remoteLocation =  "Remote Location";
	
	public static String fileType_Log =  "Log File";
	public static String fileType_Prop =  "Prop File";
	
	public static String userName = "User Name";
	public static String password = "Password";
	public static String serverULR_IP = "Server URL / IP";
	public static String ftpPort = "FTP Port";
	public static String machineName = "Shared Location";
	public static String binaryTransfer = "Set Binary Transfer";
	public static String lovalActiveMode = "Set Local Active Mode";
	public static String domain = "Domain";
	
	public static String serverURL_IP_ToolTip = "example: 192.16.17.80 or ftp.testserver.com";
	public static String sharedLocation_ToolTip = "example: 192.16.17.60/Shared";

	public static String reqlogFileName;

	public static String conversionPattern;
	
	public static String dbFileLocation;

	public static String connectionString="jdbc:sqlite:";
	
	public static String tableName = "LogDataTable";
	
	public static String selectQuery = "select date,level,message from "+tableName;
	
	public static String countQuery = "select count(level) from "+tableName;
	
	public static String createTableQuery = "create table "+tableName+" (DATE date,LEVEL text,MESSAGE String)";
	
	public static String insetSql = "insert into "+tableName+" values(?,?,?)";
	
	public static String popUpMessage;
	

}
