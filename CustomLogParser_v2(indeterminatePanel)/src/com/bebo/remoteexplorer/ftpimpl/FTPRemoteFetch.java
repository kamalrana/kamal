package com.bebo.remoteexplorer.ftpimpl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.bebo.remoteexplorer.RemoteFetch;
import com.bebo.remoteexplorer.RemoteExplorerException;
import com.bebo.remoteexplorer.RemoteFile;

public class FTPRemoteFetch implements RemoteFetch {

	private FTPRemoteFetch(){}

	public FTPRemoteFetch(FTPLoginCredentials credentials) throws RemoteExplorerException{
		this.ftpCredentials = credentials;
		this.login();
	}

	private FTPClient ftp;
	private FTPLoginCredentials ftpCredentials;

	@Override
	public boolean login() throws RemoteExplorerException {
		try
		{
			ftp = new FTPClient();
			int reply;
			if (ftpCredentials.getPort() > 0) {
				ftp.connect(ftpCredentials.getServer(), ftpCredentials.getPort());
			} else {
				ftp.connect((ftpCredentials.getServer()));
			}
			
			             
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply))
			{
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				return false;
			}else{
				System.out.println("Connected to " + ftpCredentials.getServer() + " on " + (ftpCredentials.getPort()>0 ? ftpCredentials.getPort() : ftp.getDefaultPort()));
			}
			
			boolean loginFlag = ftp.login(ftpCredentials.getUsername(), ftpCredentials.getPassword());
			System.out.println("loginFlag-->"+loginFlag);
			reply = ftp.getReplyCode();
			
			if (!FTPReply.isPositiveCompletion(reply))
			{
				ftp.logout();
				ftp.disconnect();
				System.err.println("FTP server refused login.");
				return false;
			}else{
				System.out.println("Successfully logged into the FTP server");
			}
			
			if (ftpCredentials.getBinaryTransferType()) {
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                // in theory this should not be necessary as servers should default to ASCII
                // but they don't all do so - see NET-500
                ftp.setFileType(FTP.ASCII_FILE_TYPE);
            }
			
			if (ftpCredentials.getLocalActiveMode()) {
                ftp.enterLocalActiveMode();
            } else {
                ftp.enterLocalPassiveMode();
            }
			
		}
		catch (IOException e)
		{
			if (ftp.isConnected())
			{
				try
				{
					ftp.disconnect();
				}
				catch (IOException f)
				{
					// do nothing
				}
			}
			System.err.println("Could not connect to server.");
			e.printStackTrace();
			throw new RemoteExplorerException("Unable lo open the FTP location:"+ftpCredentials.getDomain());
		}
		return true;
	}

	@Override
	public boolean changeDirectory(String dirPath){

		try {
			return ftp.changeWorkingDirectory(dirPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<FTPFile> getFilesList(boolean includeFolderFlag) {
		List<FTPFile> fileList = new ArrayList<FTPFile>();

		FTPFile[] ftpFiles = null;
		try {
			ftpFiles = ftp.listFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ftpFiles != null)
		for (FTPFile ftpFile : ftpFiles) {
			// Check if FTPFile is a regular file
			if (ftpFile.getType() == FTPFile.FILE_TYPE) {
				System.out.println("FTPFile: " + ftpFile.getName() + "; "
						+ FileUtils.byteCountToDisplaySize(ftpFile.getSize()));
				fileList.add(ftpFile);
			}
			if (ftpFile.getType() == FTPFile.DIRECTORY_TYPE && includeFolderFlag) {
				System.out.println("FTPFile: " + ftpFile.getName() + "; "
						+ FileUtils.byteCountToDisplaySize(ftpFile.getSize()));
				fileList.add(ftpFile);
			}
		}
		return fileList;
	}

	@Override
	public List<FTPFile> getFilesListFromPath(String dirPath, boolean includeFolderFlag){
		changeDirectory(dirPath);
		getFilesList(includeFolderFlag);
		return null;
	}

	@Override
	public List<String> getFilesListAsString(){
		List<String> fileList = new ArrayList<String>();
		String[] names = null;
		try {
			names = ftp.listNames();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(names);
		if(names != null){
			for (String name : names) {
				System.out.println("Name = " + name);
				fileList.add(name);
			}
		}

		return fileList;
	}

	@Override
	public List<String> getFilesListAsStringFromPath(String dirPath){
		changeDirectory(dirPath);
		return getFilesListAsString();
	}

	@Override
	public boolean uploadFile(String remoteFileName, String localFilePath) {
		boolean isFileStored = false;
		try {
			FileInputStream fis = new FileInputStream(localFilePath);
			isFileStored = ftp.storeFile(remoteFileName, fis);
			fis.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isFileStored;
	}

	@Override
	public boolean downloadFile(String remoteFilePath, String localFilePath){

		System.out.println("Starting download of "+remoteFilePath+" to "+localFilePath);
		boolean isFileDownloaded = false;
		
		try {
			FileOutputStream fos = new FileOutputStream(localFilePath);
			isFileDownloaded = ftp.retrieveFile(remoteFilePath, fos);
			fos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isFileDownloaded;
	}

	@Override
	public boolean disconnect() {
		if (ftp != null && ftp.isConnected())
		{
			try
			{
				ftp.disconnect();
			}
			catch (IOException f)
			{
				System.err.println("IOException, Unable to disconnect from " + ftpCredentials.getServer());
				return false;
			}
			System.out.println("Disconnected from " + ftpCredentials.getServer());
		}
		return true;
	}

	@Override
	public List<RemoteFile> getFilesListForExplorer() {
		List<RemoteFile> filesList = new ArrayList<RemoteFile>();
		
		int id = 1;
		RemoteFile remoteFile = new RemoteFile();
		remoteFile.setName("..");
		remoteFile.setDir(true);
		remoteFile.setId(id++);
		filesList.add(remoteFile);
		
		
		try {
			for (Iterator iterator = getFilesList(true).iterator(); iterator.hasNext();) {
				FTPFile ftpFile = (FTPFile) iterator.next();
				remoteFile = new RemoteFile();
				remoteFile.setName(ftpFile.getName());
				remoteFile.setDir(ftpFile.isDirectory());
				remoteFile.setId(id++);
				remoteFile.setFileSize(ftpFile.getSize());
				filesList.add(remoteFile);
			}
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}
		return filesList;
	}

	
}
