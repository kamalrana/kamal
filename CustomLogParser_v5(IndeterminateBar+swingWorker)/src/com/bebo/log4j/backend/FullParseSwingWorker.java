package com.bebo.log4j.backend;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.bebo.log4j.db.DBOperation;
import com.bebo.log4j.ui.FullParserMainPanel;
import com.bebo.log4j.ui.Log4jBottomPanel;
import com.bebo.log4j.ui.Log4jMainPanel;
import com.bebo.logparser.LogParserCommon;
import com.bebo.logparser.LogParserConstants;

public class FullParseSwingWorker extends SwingWorker<Void, Void>{

	private FullParserMainPanel fullParserMainPanel;
	private Log4jMainPanel mainPanel;

	public FullParseSwingWorker(FullParserMainPanel fullParserMainPanel) {
		this.fullParserMainPanel =fullParserMainPanel;
		mainPanel = fullParserMainPanel.getMainPanel();
	}

	@Override
	protected Void doInBackground() throws Exception {
		Log4jBottomPanel.log4jProgressBar.setIndeterminate(true);
		parsingOperation();
		return null;
	}
	
	private void parsingOperation() {
		if(!LogParserCommon.initialValidation(mainPanel))
		{
			Log4jBottomPanel.log4jProgressBar.setIndeterminate(false);
			JOptionPane.showMessageDialog(null, LogParserConstants.popUpMessage);
			return ;
		}
	    
		System.out.println("conversion pattern :: "+ LogParserConstants.conversionPattern);
		String logFileName = mainPanel.getHeaderPanel().getLogFile_TXT().getText();
		System.out.println("log file location :: "+ logFileName);

		beginOperation( System.currentTimeMillis(),logFileName);
		}

		public void beginOperation(long startmili,String logFileLocation) {
			
			PatternConversion patternConversion = new PatternConversion();
			
			fullParserMainPanel.setPatternConversion(patternConversion);
			
			// getting date format from property file
			String reqDateFormat = patternConversion.getReqDateFormat(LogParserConstants.conversionPattern);
			
			fullParserMainPanel.setReqDateFormat(reqDateFormat);

			String datePattern = patternConversion.getDatePattern(reqDateFormat);

			System.out.println("date pattern :: " + datePattern);

			DBOperation dbOperation = new DBOperation();
			fullParserMainPanel.setDbOperation(dbOperation);
			
			dbOperation.operation(datePattern,logFileLocation);
			
			Long endMili = System.currentTimeMillis();
			Long millis = endMili - startmili;
			System.out.println("End at :" + Calendar.getInstance().getTime());

			System.out.println(String.format(
					"%d min, %d sec",
					TimeUnit.MILLISECONDS.toMinutes(millis),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis))));
		}
	
	@Override
	protected void done() {
		Log4jBottomPanel.log4jProgressBar.setIndeterminate(false);
	}
	
	

}
