package com.bebo.logparser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.bebo.log4j.ui.Log4jMainPanel;
import com.bebo.remoteexplorer.ui.RemoteFileChooser;

@SuppressWarnings("serial")
public class HeaderPanel extends JPanel implements ActionListener{
	private Logger logger = Logger.getLogger( HeaderPanel.class.getName() );
	JLabel tool_LBL;
	JLabel logFileLocation_LBL;
	JLabel propFileLocation_LBL;
	JButton logFile_BTN;
	JButton propFile_BTN;
	
	JLabel inputFormat_Lbl;
	JComboBox inputFormat_CMB;
	JComboBox logFileLocation_CMB;
	JComboBox propFileLocation_CMB;
	
	GridBagConstraints gridBagCons;
	GridBagLayout gridBagLayout;
	private String logFormat;
	private JLabel lblLineNum;
	private JTextField logFile_TXT;
	private JTextField propFile_TXT;
	JFileChooser chooser;
	LogParser mainFrame;
	
	public HeaderPanel( LogParser frame ) {
		
		 mainFrame = frame;
		 tool_LBL = new JLabel( LogParserConstants.toolLabel );
		 logFileLocation_LBL = new JLabel( LogParserConstants.logFileLabel );
		 propFileLocation_LBL = new JLabel( LogParserConstants.propertyLabel );
		 inputFormat_Lbl = new JLabel( LogParserConstants.inputFormatLabel );
		 lblLineNum = new JLabel();
		 inputFormat_CMB = new JComboBox( getInputFormats() );
		 logFileLocation_CMB = new JComboBox( getFileLocations() );
		 propFileLocation_CMB = new JComboBox( getFileLocations() );
		 
		 logFile_BTN = new JButton( LogParserConstants.browseButton );
		 propFile_BTN = new JButton( LogParserConstants.browseButton );
		 logFile_TXT = new JTextField( 21 );
		 propFile_TXT = new JTextField( 21 );
		 gridBagLayout = new GridBagLayout();
		 gridBagCons = new GridBagConstraints();
		 
		 logFormat = LogParserConstants.IISLogFormat;
		 addComponent();
		 addListeners();
		 
		 propFileLocation_CMB.setEnabled( false );
		 propFile_TXT.setEnabled( false );
		 propFile_BTN.setEnabled( false );
	}
	
	private String[] getInputFormats(){
		String inputFormat[] = { LogParserConstants.IISLogFormat , LogParserConstants.log4jLogFormat };
		return inputFormat;
	}
	private String[] getFileLocations(){
		String inputFormat[] = { LogParserConstants.localLocation , LogParserConstants.remoteLocation };
		return inputFormat;
	}
	
	private void  addListeners(){
				
		inputFormat_CMB.addActionListener( this );
		logFile_BTN.addActionListener( this );
		propFile_BTN.addActionListener( this );
		
		logFile_TXT.addFocusListener( new java.awt.event.FocusAdapter() {
			public void focusLost( FocusEvent evt ) {
				txtLogFileFocusLost( evt );
			}
		});	
	}
	public void actionPerformed( ActionEvent event ){
		
		Object component = event.getSource();
		if( component == logFile_BTN ){
			
			String logFileLocation = ( String )logFileLocation_CMB.getSelectedItem();
			if( LogParserConstants.localLocation.equals( logFileLocation ) ){
				chooseLocalLogFile();
			}else{
				chooseRemoteFile( LogParserConstants.fileType_Log );
			}
		}
		if( component == inputFormat_CMB ){
			changePanel();
		}
		if( component == propFile_BTN ){
			
			
			String propFileLocation = ( String )propFileLocation_CMB.getSelectedItem();
			if( LogParserConstants.localLocation.equals( propFileLocation ) ){
				chooseLocalPropFile();
			}else{
				chooseRemoteFile( LogParserConstants.fileType_Prop );
			}
		}
	}
	private void chooseRemoteFile( String fileType ){
			System.out.println("---1->"+mainFrame);
		new RemoteFileChooser( fileType , mainFrame );
	}
	
	private void changePanel(){
		String selectedFormat = (String)inputFormat_CMB.getSelectedItem();
		
		if( !logFormat.equals( selectedFormat ) && selectedFormat.equals( LogParserConstants.IISLogFormat ) ){
			propFileLocation_CMB.setEnabled( false );
			propFile_TXT.setEnabled( false );
			propFile_BTN.setEnabled( false );
			logFormat = selectedFormat;		
			LogParserPanel logParserPanel = new LogParserPanel( mainFrame );
			mainFrame.removeAndAddPanel( logParserPanel , mainFrame.getLog4jMainPanel() , BorderLayout.CENTER );
			mainFrame.setLogParserPanel( logParserPanel ); 
		}
		if( !logFormat.equals( selectedFormat ) && selectedFormat.equals( LogParserConstants.log4jLogFormat ) ){
			propFileLocation_CMB.setEnabled( true );
			propFile_TXT.setEnabled( true );
			propFile_BTN.setEnabled( true );
			logFormat = selectedFormat;	
			Log4jMainPanel log4jMainPanel = new Log4jMainPanel(this);					
			mainFrame.removeAndAddPanel( log4jMainPanel , mainFrame.getLogParserPanel()  , BorderLayout.CENTER );
			mainFrame.setLog4jMainPanel( log4jMainPanel ); 
		}
	}
	private void chooseLocalLogFile() {                                         
		
		chooser = new JFileChooser();

		chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
		int returnedValue = chooser.showOpenDialog( this );

		if ( returnedValue == JFileChooser.APPROVE_OPTION )
		{
			File file = chooser.getSelectedFile();
			String path = file.getAbsolutePath();
			logFile_TXT.setText( path );

			// what's the need for displaying no. of lines in log file
			/*try{
				LineNumberReader lnr = new LineNumberReader( new FileReader( new File( path ) ) );

				while( lnr.readLine() != null ){

					LogParserPanel.count = lnr.getLineNumber();
				}
				lblLineNum.setText( LogParserConstants.noOfLinesMSG + LogParserPanel.count );
				
				lblLineNum.setForeground( Color.red );
			}catch(FileNotFoundException fe){
				logger.error( LogParserConstants.fileNotFoundError + fe.getMessage() );
			}catch( IOException ie ){
				logger.error( LogParserConstants.IOError + ie.getMessage() );
			}*/
		}
	}   
	private void chooseLocalPropFile() {                                         
		
		chooser = new JFileChooser();

		chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
		int returnedValue = chooser.showOpenDialog( this );

		if ( returnedValue == JFileChooser.APPROVE_OPTION )
		{
			File file = chooser.getSelectedFile();
			String path = file.getAbsolutePath();
			propFile_TXT.setText( path );
		}
	}  
	private void txtLogFileFocusLost(java.awt.event.FocusEvent evt) {                                     
		
		String fileName = logFile_TXT.getText();

		File file = new File(fileName);

		if( fileName.length() > 0 && !file.exists() ){
			lblLineNum.setText( LogParserConstants.fileNotFoundMSG );
			
			lblLineNum.setForeground( Color.BLUE );
		}
	} 
	private void addComponent(){
		
		setLayout( gridBagLayout );
		
		Dimension size = new Dimension( 780 , 180 );
		setSize( size );
		setPreferredSize( size );
		
		LogParserCommon.setInsets( gridBagCons , 0 , 30 , 0 , 0 );
		
		LogParserCommon.setConstraints( gridBagCons , 1 , 0 , 2 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		tool_LBL.setForeground( new Color( 200 , 30 , 100 ) );
		tool_LBL.setFont( new Font( Font.SANS_SERIF , Font.BOLD , 17 ) ) ;		
		add( tool_LBL , gridBagCons );
		
		LogParserCommon.setInsets( gridBagCons, 20 , 30 , 2 , 2 );
		
		LogParserCommon.setConstraints( gridBagCons , 2 , 1 , GridBagConstraints.REMAINDER , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( lblLineNum , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 0 , 1 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( inputFormat_Lbl , gridBagCons );
				
		LogParserCommon.setConstraints( gridBagCons , 1 , 1 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( inputFormat_CMB , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 0 , 2 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( logFileLocation_LBL , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 1 , 2 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		add( logFileLocation_CMB , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 2 , 2 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		add( logFile_TXT , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 3 , 2 , 2 , 1 , GridBagConstraints.BOTH , GridBagConstraints.CENTER );
		add( logFile_BTN , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 0 , 3 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( propFileLocation_LBL , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 1 , 3 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		add( propFileLocation_CMB , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 2 , 3 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		add( propFile_TXT , gridBagCons );
		
		LogParserCommon.setConstraints( gridBagCons , 3 , 3 , 2 , 1 , GridBagConstraints.BOTH , GridBagConstraints.CENTER );
		add( propFile_BTN , gridBagCons );
				
	}
	public String getLogFormat() {
		return logFormat;
	}
	public void setLogFormat(String logFormat) {
		this.logFormat = logFormat;
	}
	public JLabel getLblLineNum() {
		return lblLineNum;
	}
	public void setLblLineNum(JLabel lblLineNum) {
		this.lblLineNum = lblLineNum;
	}
	public JTextField getLogFile_TXT() {
		return logFile_TXT;
	}
	public void setLogFile_TXT(JTextField logFile_TXT) {
		this.logFile_TXT = logFile_TXT;
	}
	public JTextField getPropFile_TXT() {
		return propFile_TXT;
	}
	public void setPropFile_TXT(JTextField propFile_TXT) {
		this.propFile_TXT = propFile_TXT;
	}
}
