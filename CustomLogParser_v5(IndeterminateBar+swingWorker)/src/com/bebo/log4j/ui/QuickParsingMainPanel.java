package com.bebo.log4j.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bebo.log4j.backend.QuickParserSwingWorker;
import com.bebo.log4j.backend.RetrieveErrorFromFile;
import com.bebo.logparser.LogParserCommon;
import com.bebo.logparser.LogParserConstants;

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
	private QuickParserSwingWorker task;
	
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
	
	public Log4jMainPanel getLog4jMainPanel(){
		return mainPanel;
	}
	
	public void setNnoOfErrorFoundLBL_2(String noOfError){
		noOfErrorFoundLBL_2.setText(noOfError);
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
			noOfErrorFoundLBL_2.setText(".....");
			task=new QuickParserSwingWorker(this);
			task.execute();
		}	
		
		
		if(source==showErrorBTN){
			if(getRetrieveErrorFromFile()!=null){
				getRetrieveErrorFromFile().setVisible(true);
			}
		}
	}

	public RetrieveErrorFromFile getRetrieveErrorFromFile() {
		return retrieveErrorFromFile;
	}

	public void setRetrieveErrorFromFile(RetrieveErrorFromFile retrieveErrorFromFile) {
		this.retrieveErrorFromFile = retrieveErrorFromFile;
	}
	
}
