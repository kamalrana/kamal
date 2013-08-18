package com.bebo.log4j.backend;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.bebo.log4j.ui.Log4jBottomPanel;
import com.bebo.log4j.ui.Log4jMainPanel;
import com.bebo.log4j.ui.QuickParsingMainPanel;
import com.bebo.logparser.LogParserCommon;
import com.bebo.logparser.LogParserConstants;

public class QuickParserSwingWorker extends SwingWorker<Void, Void>{

	private Log4jMainPanel mainPanel;
	private QuickParsingMainPanel quickParsingMainPanel;

	public QuickParserSwingWorker(QuickParsingMainPanel quickParsingMainPanel) {
		this.mainPanel = quickParsingMainPanel.getLog4jMainPanel();
		this.quickParsingMainPanel = quickParsingMainPanel; 
	}

	@Override
	protected Void doInBackground() throws Exception {
		Log4jBottomPanel.log4jProgressBar.setIndeterminate(true);
		parseErrorBtnAction();
		return null;
	}
	
	@Override
	protected void done() {
		Log4jBottomPanel.log4jProgressBar.setIndeterminate(false);
	}
	
	public void parseErrorBtnAction() {
		System.out.println("quick error button");

		Long startmili = System.currentTimeMillis();
		
		if(!LogParserCommon.initialValidation(mainPanel))
		{
			Log4jBottomPanel.log4jProgressBar.setIndeterminate(false);
			JOptionPane.showMessageDialog(null, LogParserConstants.popUpMessage);
			System.out.println("initail validation fail");
			return ;
		}
		
		PatternConversion patternConversion = new PatternConversion();

		System.out.println("conversion Pattern is " + LogParserConstants.conversionPattern);
		
		// getting date format from property file
		String reqDateFormat = patternConversion.getReqDateFormat(LogParserConstants.conversionPattern);
		
		System.out.println("reqDateFormat " + reqDateFormat);

		String datePattern = patternConversion.getDatePattern(reqDateFormat);
		// System.out.println("date pattern from date format :: "+datePattern);    
		
		String logFileName = mainPanel.getHeaderPanel().getLogFile_TXT().getText();
                
		RetrieveErrorFromFile retrieveErrorFromFile = new RetrieveErrorFromFile( datePattern, logFileName);

		quickParsingMainPanel.setRetrieveErrorFromFile(retrieveErrorFromFile);
		
		quickParsingMainPanel.setNnoOfErrorFoundLBL_2(retrieveErrorFromFile.getNoOfErrors());
        
        System.out.println("no of errors :: "+retrieveErrorFromFile.getNoOfErrors());
        
        LogParserCommon.getTimeTaken("quick error mechanism", startmili);
        
	}

}
