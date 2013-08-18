package com.bebo.remoteexplorer;

import java.io.IOException;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;

import com.bebo.remoteexplorer.RemoteExplorerException;

public interface RemoteFetch {
	
	public boolean login() throws RemoteExplorerException;
	
	public boolean changeDirectory(String dirPath);
	
	public List getFilesList(boolean includeFolderFlag);
	
	public List getFilesListFromPath(String dirPath, boolean includeFolderFlag);
	
	public List<String> getFilesListAsString();
	
	public List<String> getFilesListAsStringFromPath(String dirPath);
	
	public boolean uploadFile(String remoteFileName, String filePath);
	
	public boolean downloadFile(String remoteFilePath, String localFilePath);
	
	public List<RemoteFile> getFilesListForExplorer();
	
	public boolean disconnect() throws RemoteExplorerException;
}
