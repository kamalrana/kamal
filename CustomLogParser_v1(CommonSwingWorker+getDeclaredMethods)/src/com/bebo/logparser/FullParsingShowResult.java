package com.bebo.logparser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class FullParsingShowResult {
private FullParserMainPanel fullParserMainPanel;
private String redDateFormat;
private Long startmili;

public FullParsingShowResult(FullParserMainPanel fullParserMainPanel) {
	this.fullParserMainPanel = fullParserMainPanel;
	startmili = System.currentTimeMillis();
	}

	public void C(){
		System.out.println("begin c");
		DateFormat df = new SimpleDateFormat(fullParserMainPanel.getReqDateFormat());
		String selectedLevel = fullParserMainPanel.getLogLevel();
		fullParserMainPanel.setLevelS( selectedLevel );
        
		Date fromDate = fullParserMainPanel.getFromDate();
		String startDate = null;
		String endate = null;
        if(fromDate!=null){
        	startDate = df.format(fromDate);
        	fullParserMainPanel.setStartDate(startDate);
        }
        Date toDate = fullParserMainPanel.getToDate();
        if(toDate!=null){
        	endate = df.format(toDate);
        	fullParserMainPanel.setEndDate(endate);
        }
        System.out.println(selectedLevel+"--"+startDate+"--"+endate);
	}
	
public void D(){
	System.out.println("begin d");
	LazyGridView lazyGridView= new LazyGridView(fullParserMainPanel);
    System.out.println("prefereed size :: "+lazyGridView.getPreferredSize());
    System.out.println("row count is :: "+lazyGridView.getJTable1().getModel().getRowCount());
    
    if(lazyGridView.getJTable1().getModel().getRowCount()==0){
    	// making view object enable for garbage collection
    	lazyGridView=null;
    	JOptionPane.showMessageDialog(null, "No Result Found For Provided criteria");
    }
    else
        lazyGridView.setVisible(true);
    
}

public void A(){
	System.out.println("begin a");
	Long endMili = System.currentTimeMillis();
	Long millis = endMili - startmili;

	System.out.println(String.format(
			"%d min, %d sec",
			TimeUnit.MILLISECONDS.toMinutes(millis),
			TimeUnit.MILLISECONDS.toSeconds(millis)
					- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
							.toMinutes(millis))));
}

}
