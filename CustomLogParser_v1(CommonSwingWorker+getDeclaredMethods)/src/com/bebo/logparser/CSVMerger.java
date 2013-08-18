package com.bebo.logparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;


public class CSVMerger {

	String spliFilepath;
	File logFile;
	Logger logger = Logger.getLogger(CSVMerger.class.getName());

	public CSVMerger(String spliFilepath, String logFilePath ) {

		this.spliFilepath = spliFilepath;
		logger.debug("spliFilepath ::: " + spliFilepath );
		logFile = new File( logFilePath );
	}


	public File getResultedCSVFile() {

		String line;
		String ColumnHeader;
		String secondLine;
		String stats = "";
		String dottedFooter = "";
		String elementProcessedStr = "";
		String elementOutputStr = "";
		String executionTimeStr = "";
		double executionTime;
		double totalExecutionTime=0;
		int elementOutputInt;
		int elementProcessedInt;
		int totalElementOutput=0;
		int totalElementProcessed=0;
		File resultedLogFile = null;
		List<String> list;
		LineNumberReader lineNumReader=null;
		SimpleDateFormat df = new SimpleDateFormat("MMM,dd,yyyy-hh_mm_ss");

		try {

			//Getting current date and time
			Calendar c = Calendar.getInstance();
			Date d = c.getTime();
			String date = df.format( d );

			File file = new File( spliFilepath );
			logger.info("file path is " + file.getName());
			String [] files = file.list();
			logger.debug("files " + files);
			logger.debug("files size is " + files.length);
			list = new ArrayList<String>();

			for( String fileName : files ){

				if( fileName.endsWith(".csv") ){

					list.add( fileName );
				}
			}
			logger.debug(" CSV files list for merging is : " + list.size());
			int fileNum = 1;

			resultedLogFile = new File( spliFilepath+"\\"+logFile.getName()+"_LogResult_"+date+".txt");

			FileWriter writer = new FileWriter( resultedLogFile );

			PrintWriter pw = new PrintWriter( writer );

			FileReader reader = null ;

			for( String ffile : list ){

				logger.trace("File read :" + ffile);

				reader = new FileReader( new File(spliFilepath+"\\"+ffile) );

				lineNumReader = new LineNumberReader( reader );

				boolean flag = true;

				while( (line= lineNumReader.readLine())!= null ){

					if( fileNum == 1 ){
						if( lineNumReader.getLineNumber() == 1 ){
							//ColumnHeader = line;
							pw.println( line );
						}
						if( lineNumReader.getLineNumber() == 2 && !line.startsWith("Statistics")){

							secondLine = line;
							pw.println( secondLine );

						}

					}


					if( line.startsWith( "Statistics" )){
						flag = false;
						stats = line;
					}
					if( line.startsWith("--") && lineNumReader.getLineNumber() > 2 ){
						flag = false;
						dottedFooter = line;
					}

					if( line.startsWith("Elements processed:")){
						flag = false;
						elementProcessedStr = line.substring(0, line.indexOf(":")+1);
						elementProcessedInt = Integer.valueOf(line.substring(line.indexOf(":")+1, line.length()).trim());

						totalElementProcessed += elementProcessedInt;
					}
					if( line.startsWith("Elements output:")){
						flag = false;
						elementOutputStr = line.substring(0, line.indexOf(":")+1);
						elementOutputInt = Integer.valueOf(line.substring(line.indexOf(":")+1, line.length()).trim());

						totalElementOutput += elementOutputInt;
					}
					if( line.startsWith("Execution time:")){
						flag = false;
						executionTimeStr = line.substring(0, line.indexOf(":")+1);
						executionTime = Double.valueOf(line.substring(line.indexOf(":")+1, line.indexOf("seconds")).trim());

						totalExecutionTime += executionTime;
					}

					if( !line.equals("") && lineNumReader.getLineNumber() > 2 && flag){
						pw.println( line );


					}else{
						continue;
					}


				}	
				fileNum++;
				lineNumReader.close();

			}

			logger.debug("Stats " + stats);
			logger.debug("Dotted footer " + dottedFooter);
			logger.debug("Element Processed " + totalElementProcessed);
			logger.debug("Element output " + totalElementOutput);
			logger.debug("Execution time : " + totalExecutionTime);
			list = null;

			if( totalElementProcessed > 0 ){
				pw.println( stats );
				pw.println( dottedFooter );
				pw.println( elementProcessedStr + " " + totalElementProcessed);
				pw.println( elementOutputStr + " " + totalElementOutput);
				pw.print( executionTimeStr + " " + totalExecutionTime + "seconds");
			}
			pw.flush();
			pw.close();
			if(writer!= null){
				logger.debug("Writer not null.closing"+writer);
				// writer.flush();
				writer.close();
			}

		}catch( Exception e){
			logger.error("Exception occured in MergeCSVResultInOneFile class " + e.getMessage());
			e.printStackTrace();
		}

		return resultedLogFile;
	}
}
