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
import java.util.Arrays;
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
public class QuickParsingMainPanel extends JPanel implements ActionListener,PropertyChangeListener{
	
	private JButton showErrorBTN;
	private JButton parseErrorBTN;
	private JLabel noOfErrorFoundLBL_1;
	private JLabel noOfErrorFoundLBL_2;
	private JButton quickParsingBTN;
	private Log4jMainPanel mainPanel;
	private SwingTaskWorker task;
	private RetrieveErrorFromFile retrieveErrorFromFile;	
	boolean breakFlag = false;
	
	public QuickParsingMainPanel( Log4jMainPanel mainPanel ) {
		this.mainPanel = mainPanel;
		showErrorBTN = new JButton( LogParserConstants.showErrorLabel ); 
		parseErrorBTN = new JButton( LogParserConstants.parseErrorLebel ); 
		noOfErrorFoundLBL_1 = new JLabel( LogParserConstants.noOfErrorFoundLBL_1 );
		setNoOfErrorFoundLBL_2(new JLabel());
		
		Dimension conponentSize = new Dimension( 150, 30 );
		
		showErrorBTN.setPreferredSize( conponentSize );
		parseErrorBTN.setPreferredSize( conponentSize );
		noOfErrorFoundLBL_1.setPreferredSize( conponentSize );
		getNoOfErrorFoundLBL_2().setPreferredSize( conponentSize );		
		
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
		add( getNoOfErrorFoundLBL_2() , cons );
		
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
	
	public Log4jMainPanel getLog4jMainPanel(){
		return mainPanel;
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
			parseErrorBtnActionPerformed();
		}	
		if(source==showErrorBTN){
			Log4jBottomPanel.log4jProgressBar.setValue( 0 );
			if(getRetrieveErrorFromFile()!=null){
				getRetrieveErrorFromFile().setVisible(true);
				Log4jBottomPanel.log4jProgressBar.setValue(Log4jBottomPanel.log4jProgressBar.getMaximum());
			}
		}
	}
	private void parseErrorBtnActionPerformed() {
		System.out.println("quick error button");
		
		if(!LogParserCommon.initialValidation(mainPanel))
		{
			Log4jBottomPanel.log4jProgressBar.setValue(Log4jBottomPanel.log4jProgressBar.getMaximum());
			JOptionPane.showMessageDialog(null, LogParserConstants.popUpMessage);
			System.out.println("initail validation fail");
			return ;
		}
//		Log4jBottomPanel.log4jProgressBar.setValue(0);

		QuickParsingTask quickParsingTask = new QuickParsingTask(this);
		task = new SwingTaskWorker(quickParsingTask);
			task.addPropertyChangeListener( this );
			task.execute();
			
		
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ( "progress" == evt.getPropertyName() ) {
			int progress = ( Integer ) evt.getNewValue();
			System.out.println("setting progress to :: "+progress);
			Log4jBottomPanel.log4jProgressBar.setValue( progress );
		}
	}

	/*class Task extends SwingWorker<Void, Void> {
		
		 * Main task. Executed in background thread.
		 
		@Override
		public Void doInBackground() {

			try{
				Random random = new Random();
				int progress = 0;
				//Initialize progress property.
				setProgress(0);

				TaskPerformed tp = new TaskPerformed();
				Class c = tp.getClass();

				Method [] methodArray = c.getDeclaredMethods();
				
				System.out.println("Method list :: "+Arrays.asList(methodArray));
				for( Method method : methodArray ){

					progress += random.nextInt( 10 );
					setProgress( Math.min( progress , 100 ) );
					
					method.invoke( tp );
					
					if(breakFlag){
						setProgress( 100 );
						return null;
					}	
					progress += random.nextInt( 10 );
					setProgress( Math.min( progress, 100 ) );

				}
				while (progress < 100) {
					//Sleep for up to one second.
					try {
						Thread.sleep(random.nextInt(1000));
					} catch (InterruptedException ignore) {}
					//Make random progress.
					progress += random.nextInt(10);
					setProgress(Math.min(progress, 100));
				}
			}catch( Exception ie )
			{
				ie.printStackTrace();

			}
			return null;
		}

		
		 * Executed in event dispatching thread
		 
		@Override
		public void done() {

		}
	}*/

	public RetrieveErrorFromFile getRetrieveErrorFromFile() {
		return retrieveErrorFromFile;
	}

	public void setRetrieveErrorFromFile(RetrieveErrorFromFile retrieveErrorFromFile) {
		this.retrieveErrorFromFile = retrieveErrorFromFile;
	}

	public JLabel getNoOfErrorFoundLBL_2() {
		return noOfErrorFoundLBL_2;
	}

	public void setNoOfErrorFoundLBL_2(JLabel noOfErrorFoundLBL_2) {
		this.noOfErrorFoundLBL_2 = noOfErrorFoundLBL_2;
	}

	/*class TaskPerformed{
		private String reqDateFormat;
		private PatternConversion patternConversion;
		private String datePattern;
		Long startmili = System.currentTimeMillis();
		public void B(){
			//initailValidation
			if(!LogParserCommon.initialValidation(mainPanel))
			{
				Log4jBottomPanel.log4jProgressBar.setValue(Log4jBottomPanel.log4jProgressBar.getMaximum());
				JOptionPane.showMessageDialog(null, LogParserConstants.popUpMessage);
				System.out.println("initail validation fail");
				breakFlag = true;
			}
		}
		
		public void C(){
//			getDateFormat
			patternConversion = new PatternConversion();

			System.out.println("conversion Pattern is " + LogParserConstants.conversionPattern);
			
			// getting date format from property file
			reqDateFormat = patternConversion.getReqDateFormat(LogParserConstants.conversionPattern);
			
			System.out.println("reqDateFormat " + reqDateFormat);
		}
		
		public void D(){
//			getDatePattern
			datePattern = patternConversion.getDatePattern(reqDateFormat);
//			System.out.println("date pattern from date format :: "+datePattern);
//	                
		}
		
		public void E(){
//			getErrorFromFile
			String logFileName = mainPanel.getHeaderPanel().getLogFile_TXT().getText();
            
			setRetrieveErrorFromFile(new RetrieveErrorFromFile( datePattern,  logFileName));
			
			 getNoOfErrorFoundLBL_2().setText(getRetrieveErrorFromFile().getNoOfErrors());
		        
		     System.out.println("no of errors :: "+getRetrieveErrorFromFile().getNoOfErrors());
		}
		
		public void A(){
//			timeTaken
			LogParserCommon.getTimeTaken("quick error mechanism", startmili);
			breakFlag=true;
		}
	}*/
}
