package com.bebo.remoteexplorer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.bebo.remoteexplorer.RemoteFile;
import com.bebo.remoteexplorer.ui.listener.MyMouseListener;

public class RemoteFileJPanel extends JPanel {

	private RemoteFile panelFile;
	private MyMouseListener myMouseListner;
	
	public RemoteFileJPanel(RemoteFile file, MouseAdapter mouseListener){
		System.out.println("Creating file panel for :"+file.getName());
		this.panelFile = file;
		this.myMouseListner = (MyMouseListener) mouseListener;
		
		setLayout((new BorderLayout()));
		String fileName = panelFile.getName();
		if(fileName.equals("")){
			add(new JLabel(fileName, JLabel.LEFT), BorderLayout.EAST);
			return;
		}
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JLabel(fileName, JLabel.LEFT), BorderLayout.EAST);
		leftPanel.add(new JLabel(file.getId()+" - ", JLabel.LEFT), BorderLayout.WEST);
		
		add(leftPanel, BorderLayout.WEST );
		
		JPanel rightPanel = new JPanel(new BorderLayout());
		if(panelFile.isDir()){
			Icon icon = UIManager.getIcon("FileView.directoryIcon");
			rightPanel.add(new JLabel(icon, JLabel.RIGHT), BorderLayout.WEST);
		}else{
			Icon icon = UIManager.getIcon("FileView.fileIcon");
			rightPanel.add(new JLabel(icon, JLabel.RIGHT), BorderLayout.WEST);				
		}
		rightPanel.add(new JLabel("   ", JLabel.RIGHT), BorderLayout.EAST);
		
		add(rightPanel , BorderLayout.EAST);
		
		/*UIManager.getIcon("FileView.directoryIcon");
		UIManager.getIcon("FileView.fileIcon");
		UIManager.getIcon("FileView.computerIcon");
		UIManager.getIcon("FileView.hardDriveIcon");
		UIManager.getIcon("FileView.floppyDriveIcon");

		UIManager.getIcon("FileChooser.newFolderIcon");
		UIManager.getIcon("FileChooser.upFolderIcon");
		UIManager.getIcon("FileChooser.homeFolderIcon");
		UIManager.getIcon("FileChooser.detailsViewIcon");
		UIManager.getIcon("FileChooser.listViewIcon");*/	
		setPreferredSize(new Dimension(RemoteFilePanelConstants.FILE_TREE_PANEL_WIDTH, RemoteFilePanelConstants.REMOTEFILE_TREE_PANEL_HEIGHT));
		setBorder(BorderFactory.createLineBorder(Color.blue));
		addMouseListener(myMouseListner);
		
	}
	
	public RemoteFile getRemoteFileForPanel(){
		return this.panelFile;
	}
	
}
