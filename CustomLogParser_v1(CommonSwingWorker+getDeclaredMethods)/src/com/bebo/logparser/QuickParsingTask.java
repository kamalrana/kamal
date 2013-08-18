package com.bebo.logparser;

import javax.swing.JOptionPane;


public class QuickParsingTask{
	private String reqDateFormat;
	private PatternConversion patternConversion;
	private String datePattern;
	Long startmili = System.currentTimeMillis();
	private QuickParsingMainPanel quickParsingMainPanel;
	private Log4jMainPanel log4jMainPanel;
	private RetrieveErrorFromFile retrieveErrorFromFile;
	
	public QuickParsingTask(QuickParsingMainPanel quickParsingMainPanel) {
		this.quickParsingMainPanel = quickParsingMainPanel;
		log4jMainPanel = quickParsingMainPanel.getLog4jMainPanel();
	}

	/*public boolean B(){
		//initailValidation
		if(!LogParserCommon.initialValidation(log4jMainPanel))
		{
			Log4jBottomPanel.log4jProgressBar.setValue(Log4jBottomPanel.log4jProgressBar.getMaximum());
			JOptionPane.showMessageDialog(null, LogParserConstants.popUpMessage);
			System.out.println("initail validation fail");
			return true;
		}
		return false;
	}*/
	
	public void C(){
//		getDateFormat
		patternConversion = new PatternConversion();

		System.out.println("conversion Pattern is " + LogParserConstants.conversionPattern);
		
		// getting date format from property file
		reqDateFormat = patternConversion.getReqDateFormat(LogParserConstants.conversionPattern);
		
		System.out.println("reqDateFormat " + reqDateFormat);
	}
	
	public void D(){
//		getDatePattern
		datePattern = patternConversion.getDatePattern(reqDateFormat);
//		System.out.println("date pattern from date format :: "+datePattern);
	}
	
	public void E(){
//		getErrorFromFile
		String logFileName = log4jMainPanel.getHeaderPanel().getLogFile_TXT().getText();
        
		retrieveErrorFromFile = new RetrieveErrorFromFile( datePattern,  logFileName);
		
		quickParsingMainPanel.setRetrieveErrorFromFile(retrieveErrorFromFile);
		
		quickParsingMainPanel.getNoOfErrorFoundLBL_2().setText(retrieveErrorFromFile.getNoOfErrors());
	        
	     System.out.println("no of errors :: "+retrieveErrorFromFile.getNoOfErrors());
	}
	
	public void A(){
//		timeTaken
		LogParserCommon.getTimeTaken("quick error mechanism", startmili);
	}
	
}