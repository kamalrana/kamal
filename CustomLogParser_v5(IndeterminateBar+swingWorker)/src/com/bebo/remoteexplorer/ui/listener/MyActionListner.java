package com.bebo.remoteexplorer.ui.listener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.bebo.logparser.LogParser;
import com.bebo.logparser.LogParserConstants;
import com.bebo.remoteexplorer.ConnectionTypes;
import com.bebo.remoteexplorer.LoginCredentials;
import com.bebo.remoteexplorer.RemoteExplorerException;
import com.bebo.remoteexplorer.RemoteFetch;
import com.bebo.remoteexplorer.ftpimpl.FTPLoginCredentials;
import com.bebo.remoteexplorer.ftpimpl.FTPRemoteFetch;
import com.bebo.remoteexplorer.sharedimpl.SharedLoginCredentials;
import com.bebo.remoteexplorer.sharedimpl.SharedRemoteFetch;
import com.bebo.remoteexplorer.sshimpl.SSHLoginCredentials;
import com.bebo.remoteexplorer.sshimpl.SSHRemoteFetch;
import com.bebo.remoteexplorer.ui.RemoteFileChooser;
import com.bebo.remoteexplorer.ui.RemoteFileGridPanel;

public class MyActionListner implements ActionListener{

	private LoginCredentials credentials;
	private LogParser mainFrame;
	private RemoteFileChooser fileChooser; 

	public MyActionListner(LogParser mainFrame, JDialog dialog){	
		System.out.println("---3->"+mainFrame);
		this.mainFrame = mainFrame;
		if(dialog instanceof RemoteFileChooser){
			fileChooser = (RemoteFileChooser) dialog;
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent event) {

		if(event.getSource().getClass().equals(JButton.class)){

			JButton button = (JButton)event.getSource();

			if(button.getName().equals(RemoteFileGridPanel.OPEN_LOC_BTN_NAME)){

				RemoteFetch remoteConnection =  null;
				try {
					//fileChooser = ((RemoteFileChooser) parentWindow);
					this.credentials = fileChooser.getLoginCredentials();
					//this.credentials = getFTPCredentials();
					//this.credentials = getSSHCredentials();
					//this.credentials = getSharedCredentials();
					switch (credentials.getConnectionTypeCode()) {
					case 1:
						remoteConnection = new FTPRemoteFetch((FTPLoginCredentials) credentials);
						break;

					case 2:
						remoteConnection = new SSHRemoteFetch((SSHLoginCredentials) credentials);
						break;

					case 3:
						remoteConnection = new SharedRemoteFetch((SharedLoginCredentials) credentials);
						break;

					case 4:
						remoteConnection = new SSHRemoteFetch((SSHLoginCredentials) credentials);
						break;

					}
				
					fileChooser.dispose();
					
					System.out.println("---5->"+mainFrame);
					RemoteFileGridPanel fileWindow = new RemoteFileGridPanel(mainFrame, fileChooser, "File/Folder List from :"+credentials.getConnectionTypeName()+" - "+credentials.getServer(), java.awt.Dialog.ModalityType.DOCUMENT_MODAL, remoteConnection);	      
					fileWindow.paint();
					//fileWindow.setLocationRelativeTo(mainFrame);
					fileWindow.setModal(true);
					fileWindow.pack();
					fileWindow.setVisible(true);
					//fileChooser.dispose();
					

				} catch (RemoteExplorerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//throw new RemoteExplorerException(e1.getMessage());
					JOptionPane.showMessageDialog(mainFrame, e1.getMessage());
				} finally{
					try {
						if(remoteConnection != null)
							remoteConnection.disconnect();
					} catch (RemoteExplorerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}		
		}

	}

}
