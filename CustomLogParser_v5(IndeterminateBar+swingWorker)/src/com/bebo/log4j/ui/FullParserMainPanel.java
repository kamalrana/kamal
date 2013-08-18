package com.bebo.log4j.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bebo.log4j.backend.FullParseSwingWorker;
import com.bebo.log4j.backend.FullParsingResultView;
import com.bebo.log4j.backend.PatternConversion;
import com.bebo.log4j.db.DBOperation;
import com.bebo.logparser.LogParserCommon;
import com.bebo.logparser.LogParserConstants;
import com.toedter.calendar.JDateChooser;

public class FullParserMainPanel extends JPanel implements ActionListener{

	private JLabel fromDateLBL;
	private JLabel toDateLBL;
	private JLabel logLevelLBL;
	private JLabel textToFindInLogLBL;
	
	private JDateChooser fromDate;
	private JDateChooser toDate;
	private JComboBox logLevel;
	private JTextField textToFindInLogTXT;
	
	/*private JButton parseLogsBTN;*/
	private JButton fullParsingBTN;
	private JButton searchLogBTN;
	private JButton resetBTN;

	private GridBagLayout layout;
	private GridBagConstraints cons;
	private String reqDateFormat = "";
	private PatternConversion patternConversion;
	private DBOperation dbOperation;
	private String levelS="";
	private String startDate="";
	private String endDate="";
	private Log4jMainPanel mainPanel;
	private FullParseSwingWorker fullParseSwingWorker;
	
	public FullParserMainPanel( Log4jMainPanel mainPanel ) {
		this.setMainPanel(mainPanel);
		fromDateLBL = new JLabel( LogParserConstants.fromDateLBL );
		toDateLBL = new JLabel( LogParserConstants.toDateLBL );
		logLevelLBL = new JLabel( LogParserConstants.logLevelLBL );
		textToFindInLogLBL = new JLabel( LogParserConstants.textToFindInLogLBL );
		
		fromDate = new JDateChooser();
		fromDate.setPreferredSize( new Dimension( 200 , 30 ) );
		toDate = new JDateChooser();
		toDate.setPreferredSize( new Dimension( 200 , 30 ) );
		String levels []={ "SELECT", "DEBUG", "ERROR" ,"WARN" , "FATAL" , "INFO" };
		logLevel = new JComboBox( levels );
		logLevel.setPreferredSize( new Dimension( 150 , 30 ) );
		setTextToFindInLogTXT(new JTextField());
		getTextToFindInLogTXT().setPreferredSize( new Dimension( 150 , 30 ) );
				
		/*parseLogsBTN = new JButton( LogParserConstants.parseLogBTN );
		parseLogsBTN.setPreferredSize( new Dimension( 155 , 30 ) );
		parseLogsBTN.setBorder( BorderFactory.createRaisedBevelBorder() );*/
		
		Dimension buttonSize = new Dimension( 100 , 30 );
		searchLogBTN = new JButton( LogParserConstants.searchLogBTN );
		searchLogBTN.setPreferredSize( buttonSize );
		resetBTN = new JButton( LogParserConstants.resetBTN );
		resetBTN.setPreferredSize( buttonSize );
		
		fullParsingBTN = new JButton( LogParserConstants.fullParsingLabel );
		Dimension fullParsingSize = new Dimension( 170 , 30 );		
		fullParsingBTN.setPreferredSize( fullParsingSize );
		fullParsingBTN.setName( LogParserConstants.fullParsingLabel );
		
		Dimension  size = new Dimension( 520 , 380 );
		setSize( size );
		setPreferredSize( size );
		setBorder( BorderFactory.createLoweredBevelBorder() );
		layout = new GridBagLayout();
		setLayout( layout );
		
		addComponents();
		addListeners();
	}
	private void addComponents(){
		cons = new GridBagConstraints();
		
		LogParserCommon.setInsets( cons , 2 , 2 , 60 , 2 );
		
		LogParserCommon.setConstraints( cons , 0 , 0 , 2 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( fullParsingBTN , cons );
		
		LogParserCommon.setInsets( cons , 2 , 2 , 2 , 2 );
		
		LogParserCommon.setConstraints( cons , 0 , 1 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( fromDateLBL , cons );
		
		LogParserCommon.setConstraints( cons , 1 , 1 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( fromDate , cons );
		
		LogParserCommon.setConstraints( cons , 0 , 2 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( toDateLBL , cons );
		LogParserCommon.setConstraints( cons , 1 , 2 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( toDate , cons );
		
		LogParserCommon.setConstraints( cons , 0 , 3 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( logLevelLBL , cons );
		LogParserCommon.setConstraints( cons , 1 , 3 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( logLevel , cons );
		
		LogParserCommon.setConstraints( cons , 0 , 4 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( textToFindInLogLBL , cons );
		LogParserCommon.setConstraints( cons , 1 ,4 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( getTextToFindInLogTXT() , cons );
		
		LogParserCommon.setConstraints( cons , 1 , 5 , 2 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( searchLogBTN , cons );
		LogParserCommon.setConstraints( cons , 1 , 5 , 2 , 1 , GridBagConstraints.NONE , GridBagConstraints.EAST );
		add( resetBTN , cons );
	}
	private void addListeners(){
		fullParsingBTN.addActionListener( this );
		resetBTN.addActionListener( this );
		searchLogBTN.addActionListener(this);
	}
	public void actionPerformed( ActionEvent event ){
		
		Object source = event.getSource();
		if( source == fullParsingBTN ){			
			LogParserCommon.deactivateComponents( getMainPanel().getQuickParsingMainPanel() );
			LogParserCommon.activateComponents( getMainPanel().getFullParserMainPanel() );
			
			fullParseSwingWorker = new FullParseSwingWorker(this);
			fullParseSwingWorker.execute();
			
		}
		if( source == resetBTN ){
			resetValues();
		}
		if(source==searchLogBTN){
			FullParsingResultView fullParsingResultView =  new FullParsingResultView(this);
			fullParsingResultView.execute();
			}
		}
	
	private void resetValues(){
		fromDate.setDate( null );
		toDate.setDate( null );
		
		getTextToFindInLogTXT().setText( "" );
		logLevel.setSelectedIndex( 0 );
	}

	public DBOperation getDbOperation() {
		return dbOperation;
	}
	public void setDbOperation(DBOperation dbOperation) {
		this.dbOperation = dbOperation;
	}
	public String getLevelS() {
		return levelS;
	}
	public void setLevelS(String levelS) {
		this.levelS = levelS;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public JTextField getTextToFindInLogTXT() {
		return textToFindInLogTXT;
	}
	public void setTextToFindInLogTXT(JTextField textToFindInLogTXT) {
		this.textToFindInLogTXT = textToFindInLogTXT;
	}
	public Log4jMainPanel getMainPanel() {
		return mainPanel;
	}
	public void setMainPanel(Log4jMainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	public PatternConversion getPatternConversion() {
		return patternConversion;
	}
	public void setPatternConversion(PatternConversion patternConversion) {
		this.patternConversion = patternConversion;
	}
	public String getReqDateFormat() {
		return reqDateFormat;
	}
	public void setReqDateFormat(String reqDateFormat) {
		this.reqDateFormat = reqDateFormat;
	}
	public String getLogLevel() {
		String selectdLevel = (String) logLevel.getSelectedItem();
		return selectdLevel;
	}
	public Date getFromDate() {
		return fromDate.getDate();
	}
	public Date getToDate(){
		return toDate.getDate();
	}
	
}
