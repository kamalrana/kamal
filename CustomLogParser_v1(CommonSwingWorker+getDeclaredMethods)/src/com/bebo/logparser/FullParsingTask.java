package com.bebo.logparser;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class FullParsingTask {
	private Long startmili ;
	private  Log4jMainPanel log4jMainPanel;
//	private PatternConversion patternConversion;
	private FullParserMainPanel fullParserMainPanel;
private PatternConversion patternConversion;
private String reqDateFormat;
//	private String reqDateFormat;
	
public FullParsingTask(FullParserMainPanel fullParserMainPanel) {
	System.out.println("constructor called of ");
	startmili = System.currentTimeMillis();
	this.fullParserMainPanel=fullParserMainPanel;
	log4jMainPanel = fullParserMainPanel.getMainPanel();
}
	
	public void d() {
		patternConversion = new PatternConversion();
		
		fullParserMainPanel.setPatternConversion(patternConversion);
		
		// getting date format from property file
		reqDateFormat = patternConversion.getReqDateFormat(LogParserConstants.conversionPattern);
		fullParserMainPanel.setReqDateFormat(reqDateFormat);
	}
	
	
	public void c(){
		String logFileName = log4jMainPanel.getHeaderPanel().getLogFile_TXT().getText();
		String datePattern = patternConversion.getDatePattern(reqDateFormat);

		System.out.println("date pattern :: " + datePattern);

		fullParserMainPanel.setDbOperation(new DBOperation());
		
		fullParserMainPanel.getDbOperation().operation(datePattern,logFileName);
		
	}
	
	public void b(){
		Long endMili = System.currentTimeMillis();
		Long millis = endMili - startmili ;

		System.out.println(String.format(
				"%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis))));
	}
}