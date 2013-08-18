package com.bebo.remoteexplorer.ui.listener;

import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import com.bebo.logparser.LogParser;
import com.bebo.remoteexplorer.RemoteFile;
import com.bebo.remoteexplorer.ui.ProgressbarWorker;
import com.bebo.remoteexplorer.ui.RemoteFileGridPanel;
import com.bebo.remoteexplorer.ui.RemoteFileJPanel;

public class MyMouseListener extends MouseAdapter {
	private RemoteFileGridPanel fileGridPanel;
	private LogParser mainFrame;
	

	public MyMouseListener(LogParser mainFrame, RemoteFileGridPanel colorGrid) {
		this.fileGridPanel = colorGrid;
		System.out.println("---6->"+mainFrame);
		this.mainFrame = mainFrame;
	}
	
	public LogParser getMainFrame(){
		return this.mainFrame;
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		System.out.println("Source class:"+event.getSource().getClass());
		if(event.getSource().getClass().equals(RemoteFileJPanel.class)){

			if (event.getClickCount() == 2) {
				System.out.println("double clicked");
				RemoteFileJPanel filePanel = (RemoteFileJPanel) event.getSource();
				RemoteFile file = filePanel.getRemoteFileForPanel();

				System.out.println("clicked on :"+file.getName());

				if(file.isDir()){
					System.out.println("changing dir :"+file.getName());

					if(fileGridPanel.getRemoteConnection().changeDirectory(file.getName())){
						fileGridPanel.setFileList(fileGridPanel.getRemoteConnection().getFilesListForExplorer());
						fileGridPanel.removeComponents();
						fileGridPanel.paint();
						fileGridPanel.setModal(true);
						fileGridPanel.pack();
						fileGridPanel.setVisible(true);
					}else{
						System.out.println("unable to change dir/invalid dir");
					}

				}else{
					System.out.println("download file");
					JFileChooser saveFileDialog = new JFileChooser();
					//Window parentWindow = SwingUtilities.windowForComponent( (RemoteFileJPanel) event.getSource());
					int saveChoice = saveFileDialog.showSaveDialog(fileGridPanel);

					if (saveChoice == JFileChooser.APPROVE_OPTION)
					{
						//Put save file code in here
						File saveFile = saveFileDialog.getSelectedFile();
						System.out.println("Saving file to filepath:"+saveFile.getPath());


						int confirm = JOptionPane.showOptionDialog(fileGridPanel,
								"Are you sure, you want to download :"+file.getName()+" file?",
								"Exit Confirmation", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, null, null);
						if (confirm == JOptionPane.YES_OPTION) {
							//fileGridPanel.removeComponents();
							//fileGridPanel.showFileDownloadProgress(saveFile.getPath(), file);
							//fileGridPanel.setModal(true);
							//fileGridPanel.pack();
							//fileGridPanel.setVisible(true);
							
							//fileGridPanel.getGlassPane().setVisible(true);
							//fileGridPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							
							JRootPane root = fileGridPanel.getRootPane();
							root.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							root.getGlassPane().setVisible(true);
							
							final String filename = file.getName();
							final String saveFilePath = saveFile.getPath();
							final long size = file.getFileSize();	
							
							fileGridPanel.filePath = saveFile.getPath();
							
							Thread th = new Thread() {

					            public synchronized void run() {
					               
					            	ProgressbarWorker worker = new ProgressbarWorker(mainFrame, fileGridPanel, saveFilePath, size);
									//worker.addPropertyChangeListener(fileGridPanel);
									worker.execute();
					            }

					        };
					        th.setPriority(Thread.MAX_PRIORITY);
					        th.start();
							SwingUtilities.invokeLater(new Runnable()
							{
								@Override
								public void run()
								{
									System.out.println("-->"+mainFrame);
									
									fileGridPanel.getRemoteConnection().downloadFile(filename, saveFilePath );
									
								}
							});
							
							
						}						
					}
				}
				return;
			}
			if (event.getClickCount() == 1) {
				System.out.println("single clicked");
				if (event.getButton() == MouseEvent.BUTTON1) {
					fileGridPanel.fileLabelPressed((RemoteFileJPanel)event.getSource());
				}
			}
		}

	}
}