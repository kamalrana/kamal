package com.bebo.remoteexplorer.ui;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import com.bebo.logparser.LogParser;

public class ProgressbarWorker extends SwingWorker{

	private RemoteFileGridPanel fileTreePanel;
	private String filePathToMonitor;
	private long remoteFileSize;
	private LogParser mainFrame;
	
	public ProgressbarWorker(LogParser mainFrame,RemoteFileGridPanel dialog, String filepath, long remoteFileSize){
		this.fileTreePanel = dialog;
		this.filePathToMonitor = filepath;
		this.remoteFileSize = remoteFileSize;
		System.out.println("---7->"+mainFrame);
		this.mainFrame = mainFrame;
	}
	@Override
	protected Void doInBackground() throws Exception {

		File inProgressFile = new File(filePathToMonitor);
		long inProgressFileSize = inProgressFile.length();
		System.out.println("total file size : "+remoteFileSize);
		long initchunk = remoteFileSize/20;
		System.out.println("initial chunk : "+initchunk);
		int progress = 0;
		ProgressMonitor progressMonitor = new ProgressMonitor(fileTreePanel,"Downloading of file in progress...","", 0, 100);
		progressMonitor.setProgress(0);
		
        try {
            Thread.sleep(1000);
            long chunk = 0;
            while (progress < 100 && !isCancelled()) {
                //Sleep for up to half second.
                Thread.sleep(500);
                inProgressFileSize = inProgressFile.length();
                System.out.println("inProgressFileSize : "+inProgressFileSize);
                if(inProgressFileSize >= chunk ){
                	progress += 5;
                	chunk = chunk+initchunk;
                }
                System.out.println("chunk : "+chunk);
                System.out.println("progress : "+progress);
                progressMonitor.setProgress(Math.min(progress, 100));
                if(progressMonitor.isCanceled()){
                	throw new InterruptedException("Download operation canceled by user.");
                }
            }
        } catch (InterruptedException ignore) {
        	System.out.println("Download operation canceled by user.");
        	cancel(true);
        }	
        return null;      
	}

	@Override
    public void done() {
		try {
            Void result = (Void) get();
            System.out.println("Download operation Completed.");                
        } catch (InterruptedException e) {
        	System.out.println("Download operation Interrupted.");
        } catch (CancellationException e) {           
        	System.out.println("Download operation Canceled.");
        } catch (ExecutionException e) {
        	System.out.println("Exception occurred: " + e.getCause());
        }
        Toolkit.getDefaultToolkit().beep();
        setProgress(100);
        fileTreePanel.setIsFileDownloaded(true);
        
        fileTreePanel.getFileChooser().setFilePath(filePathToMonitor);

        JRootPane root = fileTreePanel.getRootPane();
		root.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		root.getGlassPane().setVisible(false);
		JOptionPane.showMessageDialog(fileTreePanel, "File Succesfully downloaded to : "+filePathToMonitor);
    }
}
