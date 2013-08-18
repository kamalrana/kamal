package com.bebo.log4j.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.bebo.log4j.ui.FullParserMainPanel;
import com.bebo.log4j.ui.LazyGridView;
import com.bebo.log4j.ui.Log4jBottomPanel;
import com.bebo.log4j.ui.Log4jMainPanel;
import com.bebo.logparser.LogParserConstants;

public class FullParsingResultView extends SwingWorker<Void,Void>{

	private FullParserMainPanel fullParserMainPanel;
	private Log4jMainPanel mainPanel;

	public FullParsingResultView(FullParserMainPanel fullParserMainPanel) {
		this.fullParserMainPanel = fullParserMainPanel;
		mainPanel = fullParserMainPanel.getMainPanel();
	}

	@Override
	protected Void doInBackground() throws Exception {
		Log4jBottomPanel.log4jProgressBar.setIndeterminate(true);
		showResult();
		return null;
	}
	
	@Override
	protected void done() {
	Log4jBottomPanel.log4jProgressBar.setIndeterminate(false);
	}
	
	private void showResult() {
		if(fullParserMainPanel.getReqDateFormat().length()==0){
			fullParserMainPanel.setReqDateFormat(fullParserMainPanel
					.getPatternConversion().getReqDateFormat(
							LogParserConstants.conversionPattern));	
		}
		DateFormat df = new SimpleDateFormat(fullParserMainPanel.getReqDateFormat());

		String selectedLevel = fullParserMainPanel.getLogLevel();
		fullParserMainPanel.setLevelS(selectedLevel);
        
		Date fromDate = fullParserMainPanel.getFromDate();
        if(fromDate!=null)
        	fullParserMainPanel.setStartDate(df.format(fromDate));
        
        Date toDate = fullParserMainPanel.getToDate();
        
        if(toDate!=null)
        	fullParserMainPanel.setEndDate(df.format(toDate));
        
        System.out.println(selectedLevel+"--"+toDate+"--"+fromDate);

        final Long startmili = System.currentTimeMillis();
        
        LazyGridView lazyGridView= new LazyGridView(fullParserMainPanel);
        
        System.out.println("row count is :: "+lazyGridView.getJTable1().getModel().getRowCount());
        
        if(lazyGridView.getJTable1().getModel().getRowCount()==0){
        	// making view object enable for garbage collection
        	lazyGridView=null;
        	Log4jBottomPanel.log4jProgressBar.setIndeterminate(false);
        	JOptionPane.showMessageDialog(null, "No Result Found For Provided criteria");
        }
        else
            lazyGridView.setVisible(true);
                
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

}
