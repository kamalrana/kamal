package com.bebo.logparser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.bebo.logparser.LogParserPanel.Task;
import com.bebo.logparser.LogParserPanel.TaskPerformed;

@SuppressWarnings("serial")
public class QuickParsingMainPanel extends JPanel implements ActionListener{
	
	private JButton showErrorBTN;
	private JButton parseErrorBTN;
	private JLabel noOfErrorFoundLBL_1;
	private JLabel noOfErrorFoundLBL_2;
	private JButton quickParsingBTN;
	private Log4jMainPanel mainPanel;
	private boolean stopFlag = false;
	private RetrieveErrorFromFile retrieveErrorFromFile;
	
	public QuickParsingMainPanel( Log4jMainPanel mainPanel ) {
		this.mainPanel = mainPanel;
		showErrorBTN = new JButton( LogParserConstants.showErrorLabel ); 
		parseErrorBTN = new JButton( LogParserConstants.parseErrorLebel ); 
		noOfErrorFoundLBL_1 = new JLabel( LogParserConstants.noOfErrorFoundLBL_1 );
		noOfErrorFoundLBL_2 = new JLabel();
		
		Dimension conponentSize = new Dimension( 150, 30 );
		
		showErrorBTN.setPreferredSize( conponentSize );
		parseErrorBTN.setPreferredSize( conponentSize );
		noOfErrorFoundLBL_1.setPreferredSize( conponentSize );
		noOfErrorFoundLBL_2.setPreferredSize( conponentSize );		
		
		quickParsingBTN = new JButton( LogParserConstants.quickParsingLabel );
		Dimension buttonSize = new Dimension( 100 , 100 );
		quickParsingBTN.setPreferredSize( buttonSize );
		quickParsingBTN.setName( LogParserConstants.quickParsingLabel );
		//quickParsingBTN.setBorder( BorderFactory.createRaisedBevelBorder() );
		
		setLayout( new GridBagLayout() );
		GridBagConstraints cons = new GridBagConstraints();
		LogParserCommon.setInsets( cons , 2 , 2 , 60 , 2 );
		
		LogParserCommon.setConstraints( cons , 0, 0 , 2, 2 , GridBagConstraints.VERTICAL , GridBagConstraints.WEST );
		add( quickParsingBTN , cons );
		
		LogParserCommon.setConstraints( cons , 0, 2 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( noOfErrorFoundLBL_1 , cons );
		LogParserCommon.setConstraints( cons , 1, 2 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( noOfErrorFoundLBL_2 , cons );
		
		LogParserCommon.setConstraints( cons , 0, 3 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( parseErrorBTN , cons );
		
		LogParserCommon.setConstraints( cons , 1, 3 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		add( showErrorBTN , cons );
		
		Dimension  size = new Dimension( 250 , 380 );
		setSize( size );
		setPreferredSize( size );
		showErrorBTN.setPreferredSize( new Dimension( 100 , 30 ) );
		setBorder( BorderFactory.createLoweredBevelBorder() );		
		addListeners();
	}
	private void addListeners(){
		quickParsingBTN.addActionListener(this);
		parseErrorBTN.addActionListener(this);
		showErrorBTN.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if(source==quickParsingBTN){
			LogParserCommon.deactivateComponents( mainPanel.getFullParserMainPanel() );				
			LogParserCommon.activateComponents( mainPanel.getQuickParsingMainPanel() );
		}
		if(source==parseErrorBTN){
			
			Runnable r = new Runnable() {
				@Override
				public void run() {
					parseErrorBtnActionPerformed();
					Log4jBottomPanel.log4jProgressBar.setIndeterminate(false);
				}
			};
			
			Thread t=new Thread(r,"Other then GUI");
			t.start();
			Log4jBottomPanel.log4jProgressBar.setIndeterminate(true);
		}	
		
		
		if(source==showErrorBTN){
			if(retrieveErrorFromFile!=null){
				retrieveErrorFromFile.setVisible(true);
			}
		}
	}
	
	private void parseErrorBtnActionPerformed() {
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
//		System.out.println("date pattern from date format :: "+datePattern);    
		
		String logFileName = mainPanel.getHeaderPanel().getLogFile_TXT().getText();
                
		retrieveErrorFromFile = new RetrieveErrorFromFile( datePattern, logFileName);
//                
        noOfErrorFoundLBL_2.setText(retrieveErrorFromFile.getNoOfErrors());
        
        System.out.println("no of errors :: "+retrieveErrorFromFile.getNoOfErrors());
        
        LogParserCommon.getTimeTaken("quick error mechanism", startmili);
        
	}
}
