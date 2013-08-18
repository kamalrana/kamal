package com.bebo.logparser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	private String levelS;
	private String startDate;
	private String endDate;
	private Log4jMainPanel mainPanel;
	private Log4jBottomPanel log4jBottomPanel;
	public FullParserMainPanel( Log4jMainPanel mainPanel ) {
		this.mainPanel = mainPanel;
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
			LogParserCommon.deactivateComponents( mainPanel.getQuickParsingMainPanel() );
			LogParserCommon.activateComponents( mainPanel.getFullParserMainPanel() );
			
			log4jBottomPanel = mainPanel.getLog4jBottomPanel();
			Thread t =new Thread(new Runnable() {
				
				@Override
				public void run() {
					parsingOperation();
					log4jBottomPanel.getTimer().stop();
				}
			});
			t.start();
			log4jBottomPanel.getTimer().start();
		}
		if( source == resetBTN ){
			resetValues();
		}
		if(source==searchLogBTN){
			showResult();
		}
	}
	private void parsingOperation() {
		if(!LogParserCommon.initialValidation(mainPanel))
		{
			log4jBottomPanel.getTimer().stop();
			JOptionPane.showMessageDialog(null, LogParserConstants.popUpMessage);
			return ;
		}
	    
		System.out.println("conversion pattern :: "+ LogParserConstants.conversionPattern);
		String logFileName = mainPanel.getHeaderPanel().getLogFile_TXT().getText();
		System.out.println("log file location :: "+ logFileName);

		beginOperation( System.currentTimeMillis(),logFileName);
		}

		public boolean beginOperation(long startmili,String logFileLocation) {
			
			patternConversion = new PatternConversion();
			// getting date format from property file
			reqDateFormat = patternConversion.getReqDateFormat(LogParserConstants.conversionPattern);

			String datePattern = patternConversion.getDatePattern(reqDateFormat);

			System.out.println("date pattern :: " + datePattern);

			setDbOperation(new DBOperation());
			
			boolean returnedFlag = getDbOperation().operation(datePattern,logFileLocation);
			
			Long endMili = System.currentTimeMillis();
			Long millis = endMili - startmili;
			System.out.println("End at :" + Calendar.getInstance().getTime());

			System.out.println(String.format(
					"%d min, %d sec",
					TimeUnit.MILLISECONDS.toMinutes(millis),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis))));
			
			return returnedFlag;
		}
	
	
	private void resetValues(){
		fromDate.setDate( null );
		toDate.setDate( null );
		
		getTextToFindInLogTXT().setText( "" );
		logLevel.setSelectedIndex( 0 );
	}
	
	private void showResult() {
		if(reqDateFormat.length()==0){
			reqDateFormat = patternConversion.getReqDateFormat(LogParserConstants.conversionPattern);	
		}
		DateFormat df = new SimpleDateFormat(reqDateFormat);

		setLevelS((String) logLevel.getSelectedItem());
        setStartDate("");
        setEndDate("");
        
        if(fromDate.getDate()!=null)
        	setStartDate(df.format(fromDate.getDate()));
        
        if(toDate.getDate()!=null)
        	setEndDate(df.format(toDate.getDate()));
        
        System.out.println(getLevelS()+"--"+getStartDate()+"--"+getEndDate());

        final Long startmili = System.currentTimeMillis();
        
        LazyGridView lazyGridView= new LazyGridView(this);
        System.out.println("row count is :: "+lazyGridView.getJTable1().getModel().getRowCount());
        
        if(lazyGridView.getJTable1().getModel().getRowCount()==0){
        	// making view object enable for garbage collection
        	lazyGridView=null;
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
	
}
