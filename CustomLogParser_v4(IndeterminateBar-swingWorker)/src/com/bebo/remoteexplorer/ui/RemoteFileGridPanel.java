package com.bebo.remoteexplorer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.WindowConstants;

import com.bebo.logparser.LogParser;
import com.bebo.remoteexplorer.RemoteFetch;
import com.bebo.remoteexplorer.RemoteFile;
import com.bebo.remoteexplorer.ui.listener.MyMouseListener;

@SuppressWarnings("serial")
public class RemoteFileGridPanel extends JDialog{  

	public static String OPEN_LOC_BTN_NAME = "Open Remote Location";	
	private boolean isFileDownloaded;
	private RemoteFileJPanel[] filesPanels;
	private List<RemoteFile> filesList;
	private RemoteFetch remoteConnection;
	private List<Component> mainCompList;
	private JProgressBar progressBar;
	public String filePath;
	private LogParser mainFrame;
	private RemoteFileChooser fileChooser;
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public RemoteFileChooser getFileChooser(){
		return fileChooser;
	}
	
	public RemoteFileGridPanel(LogParser frame, RemoteFileChooser fileChooser, String str, Dialog.ModalityType type, RemoteFetch connection){
		super(frame, str, type);
		this.remoteConnection = connection;
		this.fileChooser = fileChooser;
		this.filesList = connection.getFilesListForExplorer();
		System.out.println("---3->"+frame);
		this.mainFrame = frame;
		setLayout(new BorderLayout());		
		addClosePrompt();
	}	

	public void removeComponents(){
		for (Iterator iterator = mainCompList.iterator(); iterator.hasNext();) {
			Component comp = (Component) iterator.next();
			remove(comp);
		}
	}
	

	public void paint() {
		mainCompList = new ArrayList<Component>();
		JPanel headerPanel = getHeaderPanel("Select a file (double click to download/open a folder)");
		add(headerPanel, BorderLayout.NORTH);	
		mainCompList.add(headerPanel);
		JScrollPane pane = getTreeScrollPane();
		add(pane, BorderLayout.CENTER);
		mainCompList.add(pane);
	}
	
	public JPanel getHeaderPanel(String titleBarLabel){
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(titleBarLabel, JLabel.LEFT), BorderLayout.WEST);
		p.setPreferredSize(new Dimension(RemoteFilePanelConstants.FILE_TREE_PANEL_WIDTH, 30));
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		return p;
	}
	
	public JScrollPane getTreeScrollPane(){
		JPanel mainPanel = new JPanel();
		
		Collections.sort(filesList);

		int rows = filesList.size();
		System.out.println("Number of file-folders:"+rows);
		filesPanels = null;
		filesPanels = new RemoteFileJPanel[rows];
		if(rows < 20){
			mainPanel.setPreferredSize(new Dimension(RemoteFilePanelConstants.FILE_TREE_PANEL_WIDTH, RemoteFilePanelConstants.FILE_TREE_PANEL_HEIGHT));
		}else{
			mainPanel.setPreferredSize(new Dimension(RemoteFilePanelConstants.FILE_TREE_PANEL_WIDTH, RemoteFilePanelConstants.FILE_TREE_PANEL_HEIGHT_MULTIPLIER*rows));
		}
		mainPanel.setLayout(new FlowLayout());
		MyMouseListener myListener = new MyMouseListener(mainFrame, this);
		
		for (int row = 0; row < rows; row++) {
			RemoteFile file = filesList.get(row); 			
			RemoteFileJPanel panel = new RemoteFileJPanel(file, myListener);
			mainPanel.add(panel);
			filesPanels[row] = panel;
		}
						
		JScrollPane scroller = new JScrollPane(mainPanel);
		scroller.setPreferredSize(new Dimension(RemoteFilePanelConstants.FILE_TREE_SCROLLER_WIDTH, RemoteFilePanelConstants.FILE_TREE_PANEL_HEIGHT));
		
		return scroller;
	}
	public void setFileList(List<RemoteFile> filesList){
		this.filesList = filesList;
	}
	
	private void addClosePrompt(){
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {            	
				if(!isFileDownloaded){
					int confirm = JOptionPane.showOptionDialog(ev.getWindow(),
							"Are You Sure to Close without downloading a file?",
							"Exit Confirmation", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (confirm == JOptionPane.YES_OPTION) {
						ev.getWindow().dispose();
					}
				}else{
					ev.getWindow().dispose();
				}
			}
		});
		setResizable(false);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	public void fileLabelPressed(RemoteFileJPanel panel) {
		
		System.out.println("Panel name >"+panel.getRemoteFileForPanel().getName()+"<");
		System.out.println("Panel file id >"+panel.getRemoteFileForPanel().getId()+"<");
		
		for (int row = 0; row < filesPanels.length; row++) {
		
			if(filesPanels[row].getRemoteFileForPanel().getId() == panel.getRemoteFileForPanel().getId()) {
				filesPanels[row].setBorder(BorderFactory.createLineBorder(Color.red, 2));
			}else{
				filesPanels[row].setBorder(BorderFactory.createLineBorder(Color.blue));
			}
		}
	}

	public RemoteFetch getRemoteConnection(){
		return this.remoteConnection;
	}	

	 public void setIsFileDownloaded(boolean flag){
		 isFileDownloaded = flag;
	 }
	 
	 public LogParser getMainFrame(){
			return mainFrame;
		}
}