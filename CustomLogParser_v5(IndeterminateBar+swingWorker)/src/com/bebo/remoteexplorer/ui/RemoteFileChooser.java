package com.bebo.remoteexplorer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.bebo.logparser.LogParser;
import com.bebo.logparser.LogParserCommon;
import com.bebo.logparser.LogParserConstants;
import com.bebo.remoteexplorer.ConnectionTypes;
import com.bebo.remoteexplorer.LoginCredentials;
import com.bebo.remoteexplorer.ftpimpl.FTPLoginCredentials;
import com.bebo.remoteexplorer.sharedimpl.SharedLoginCredentials;
import com.bebo.remoteexplorer.sshimpl.SSHLoginCredentials;
import com.bebo.remoteexplorer.ui.listener.MyActionListner;

public class RemoteFileChooser extends JDialog implements ActionListener{

	private JComboBox connTypeCMB;
	private JLabel connTypeLBL;
	private JLabel userNameLBL;
	private JLabel passwordLBL;
	private JLabel serverURL_IPLBL;
	private JLabel ftpPortLBL;
	private JLabel machineName_IPLBL;
	private JLabel localActiveModeLBL;
	private JLabel binaryTransferLBL;
	private JLabel domainLBL;

	private JTextField userNameTXT;
	private JPasswordField passwordTXT;
	private JTextField serverURL_IPTXT;
	private JTextField ftpPortTXT;
	private JTextField machineName_IPTXT;
	private JTextField domainTXT;

	private JCheckBox binaryTransferCKB;
	private JCheckBox localActiveModeCKB;
	private GridBagConstraints cons;
	private LogParser mainFrame;

	private JButton openlocationBTN;

	private String fileType;

	public RemoteFileChooser( String FileType , LogParser mainFrame ) {
		super(mainFrame, "Remote File Downloader", java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
		this.mainFrame = mainFrame;
		this.fileType = FileType;
		addComponent();
		addListeners();
		hideInitialComponents();
		setSize( new Dimension( 500 , 400 ) );
		setModal( true );
		setVisible( true );	
		System.out.println("---2->"+mainFrame);	
	}
	private void addListeners(){
		connTypeCMB.addActionListener( this );
		openlocationBTN.setName(RemoteFileGridPanel.OPEN_LOC_BTN_NAME);
		openlocationBTN.addActionListener( new MyActionListner(mainFrame, this) );
	}
	public void actionPerformed( ActionEvent event ){
		Object component = event.getSource();	
		if( component == connTypeCMB ){
			String connType = String.valueOf( connTypeCMB.getSelectedItem() );		
			if( connType.equals( ConnectionTypes.FTP.name() ) ){
				domainTXT.setEnabled( false );
				machineName_IPTXT.setEnabled( false );
				ftpPortTXT.setEnabled( true );
				serverURL_IPTXT.setEnabled( true );
				binaryTransferCKB.setEnabled( true );
				localActiveModeCKB.setEnabled( true );		
				ftpPortTXT.setText( "21" );			
			}
			if( connType.equals( ConnectionTypes.SSH.name() ) || connType.equals( ConnectionTypes.SFTP.name() )){
				domainTXT.setEnabled( false );
				machineName_IPTXT.setEnabled( false );
				ftpPortTXT.setEnabled( true );
				serverURL_IPTXT.setEnabled( true );
				binaryTransferCKB.setEnabled( false );
				localActiveModeCKB.setEnabled( false );	
				ftpPortTXT.setText( "22" );			
			}
			if( connType.equals( ConnectionTypes.SHARED.name() ) ){			
				domainTXT.setEnabled( true );
				machineName_IPTXT.setEnabled( true );
				serverURL_IPTXT.setEnabled( false );
				binaryTransferCKB.setEnabled( false );
				localActiveModeCKB.setEnabled( false );
				ftpPortTXT.setEnabled( false );
				ftpPortTXT.setText( "" );	
			}
		}

	}
	private void hideInitialComponents(){
		domainTXT.setEnabled( false );
		machineName_IPTXT.setEnabled( false );
	}
	private void addComponent(){

		Container container = getContentPane();
		container.setLayout( new BorderLayout() );

		connTypeLBL = new JLabel( LogParserConstants.connTypeLabel );
		connTypeCMB = new JComboBox( ConnectionTypes.values() );
		connTypeCMB.setPreferredSize( new Dimension( 100 ,  30 ) );

		Dimension labelDimention = new Dimension( 130 , 30 );
		userNameLBL = new JLabel( LogParserConstants.userName  );
		userNameLBL.setPreferredSize( labelDimention );

		passwordLBL = new JLabel( LogParserConstants.password  );
		passwordLBL.setPreferredSize( labelDimention );
		serverURL_IPLBL = new JLabel( LogParserConstants.serverULR_IP  );
		serverURL_IPLBL.setPreferredSize( labelDimention );
		ftpPortLBL = new JLabel( LogParserConstants.ftpPort  );
		ftpPortLBL.setPreferredSize( labelDimention );
		machineName_IPLBL = new JLabel( LogParserConstants.machineName  );
		machineName_IPLBL.setPreferredSize( labelDimention );
		binaryTransferLBL = new JLabel( LogParserConstants.binaryTransfer  );
		binaryTransferLBL.setPreferredSize( labelDimention );
		localActiveModeLBL = new JLabel( LogParserConstants.lovalActiveMode  );
		localActiveModeLBL.setPreferredSize( labelDimention );
		domainLBL = new JLabel( LogParserConstants.domain  );
		domainLBL.setPreferredSize( labelDimention );

		Dimension textDimention = new Dimension( 230 , 30 );
		userNameTXT = new JTextField();
		userNameTXT.setPreferredSize( textDimention );		
		passwordTXT = new JPasswordField();
		passwordTXT.setPreferredSize( textDimention );
		serverURL_IPTXT = new JTextField();
		serverURL_IPTXT.setPreferredSize( textDimention );		
		serverURL_IPTXT.setToolTipText( LogParserConstants.serverURL_IP_ToolTip );
		ftpPortTXT = new JTextField();		
		ftpPortTXT.setPreferredSize( textDimention );
		ftpPortTXT.setText( "21" );
		machineName_IPTXT = new JTextField();		
		machineName_IPTXT.setToolTipText( LogParserConstants.sharedLocation_ToolTip );
		machineName_IPTXT.setPreferredSize( textDimention  );
		domainTXT = new JTextField();
		domainTXT.setPreferredSize( textDimention  );

		binaryTransferCKB = new JCheckBox();
		localActiveModeCKB = new JCheckBox();

		openlocationBTN = new JButton( LogParserConstants.openLocationBTN );

		JPanel headerPanel = new JPanel();		
		headerPanel.setPreferredSize( new Dimension(  500 ,  50 )  );

		headerPanel.add( connTypeLBL );
		headerPanel.add( connTypeCMB );
		headerPanel.setBorder( BorderFactory.createMatteBorder( 0 , 0 , 1 , 0, Color.BLACK ) );

		JPanel otherComponentPanel = new JPanel();		
		otherComponentPanel.setPreferredSize( new Dimension( 500 , 450 ) );
		cons = new GridBagConstraints();	
		otherComponentPanel.setLayout( new GridBagLayout() );		

		LogParserCommon.setConstraints( cons , 0 , 0 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( userNameLBL , cons );
		LogParserCommon.setConstraints( cons , 1 , 0 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( userNameTXT , cons );

		LogParserCommon.setConstraints( cons , 0 , 1 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( passwordLBL , cons );
		LogParserCommon.setConstraints( cons , 1 , 1 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( passwordTXT , cons );

		LogParserCommon.setConstraints( cons , 0 , 2 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( serverURL_IPLBL , cons  );
		LogParserCommon.setConstraints( cons , 1 , 2 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( serverURL_IPTXT , cons  );

		LogParserCommon.setConstraints( cons , 0 , 3 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( ftpPortLBL , cons  );
		LogParserCommon.setConstraints( cons , 1 , 3 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( ftpPortTXT , cons  );

		LogParserCommon.setConstraints( cons , 0 , 4 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( machineName_IPLBL , cons  );
		LogParserCommon.setConstraints( cons , 1 , 4 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( machineName_IPTXT , cons  );

		LogParserCommon.setConstraints( cons , 0 , 5 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( domainLBL , cons  );
		LogParserCommon.setConstraints( cons , 1 , 5 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( domainTXT , cons  );

		LogParserCommon.setConstraints( cons , 0 , 6 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( binaryTransferLBL , cons  );
		LogParserCommon.setConstraints( cons , 1 , 6 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( binaryTransferCKB , cons  );

		LogParserCommon.setConstraints( cons , 0 , 7 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( localActiveModeLBL , cons  );
		LogParserCommon.setConstraints( cons , 1 , 7 , 1, 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		otherComponentPanel.add( localActiveModeCKB  , cons );	

		LogParserCommon.setConstraints( cons , 0 , 8 , 2 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		otherComponentPanel.add( openlocationBTN  , cons );	

		container.add( headerPanel , BorderLayout.NORTH );
		container.add( otherComponentPanel , BorderLayout.CENTER );

	}

	public LoginCredentials getLoginCredentials(){

		String userName = userNameTXT.getText();			

		String password = passwordTXT.getText();

		String serverURL_IP = serverURL_IPTXT.getText();		

		String portStr = "";;

		if(ftpPortTXT.getText() != null && ftpPortTXT.getText().length() > 0){
			portStr = ftpPortTXT.getText();
		}


		String machineName_IP = machineName_IPTXT.getText();

		String domain = domainTXT.getText();

		boolean binaryTransfer = binaryTransferCKB.isSelected();

		boolean localActiveMode = localActiveModeCKB.isSelected();

		String connType = String.valueOf( connTypeCMB.getSelectedItem() );	

		if( connType.equals( ConnectionTypes.FTP.name() ) ){

			return new FTPLoginCredentials(ConnectionTypes.FTP, userName, password, serverURL_IP, Integer.valueOf(portStr), binaryTransfer, localActiveMode );
		}
		if( connType.equals( ConnectionTypes.SSH.name() ) || connType.equals( ConnectionTypes.SFTP.name() )){

			return new SSHLoginCredentials(ConnectionTypes.SSH, userName, password, serverURL_IP, Integer.valueOf(portStr));
		}
		if( connType.equals( ConnectionTypes.SHARED.name() ) ){			

			return new SharedLoginCredentials(ConnectionTypes.SHARED, domain, userName, password, machineName_IP);
		}

		return null;		

	}


	public void setFilePath(String filePath){

		if(this.fileType.equals(LogParserConstants.fileType_Log)){
			mainFrame.setLogFilePath(filePath);
			mainFrame.getHeaderPanel().getLogFile_TXT().setText(filePath);
		}
		if(this.fileType.equals(LogParserConstants.fileType_Prop)){
			mainFrame.setPropFilePath(filePath);
			mainFrame.getHeaderPanel().getPropFile_TXT().setText(filePath);
		}
	}
	
}
