package com.bebo.remoteexplorer.sshimpl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import jcifs.smb.SmbFile;

import com.bebo.remoteexplorer.RemoteExplorerException;
import com.bebo.remoteexplorer.RemoteFetch;
import com.bebo.remoteexplorer.RemoteFile;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SSHRemoteFetch implements RemoteFetch {

	private SSHRemoteFetch(){}

	public SSHRemoteFetch(SSHLoginCredentials credentials) throws RemoteExplorerException{
		this.sshCredentials = credentials;
		this.login();
	}

	private Session session;
	private SSHLoginCredentials sshCredentials;
	private ChannelSftp sftpChannel;

	@Override
	public boolean login() throws RemoteExplorerException {

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sshCredentials.getUsername(),
					sshCredentials.getServer(), sshCredentials.getPort());
			session.setPassword(sshCredentials.getPassword());
			Hashtable<String, String> hostKeyMap = new Hashtable<String, String>();
			hostKeyMap.put("StrictHostKeyChecking", "no");
			session.setConfig(hostKeyMap);
			System.out.println("Establishing Connection...");
			session.connect();
			System.out.println("Connection established.");
			System.out.println("Crating SFTP Channel.");
			sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			System.out.println("SFTP Channel created.");
		} catch (Exception e) {
			String errorMsg = "Unable to connect to SSH/SFTP server :"+sshCredentials.getServer()+", due to :"+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMsg);
			throw new RemoteExplorerException(errorMsg);
		}
		return true;
	}

	@Override
	public boolean changeDirectory(String dirPath) {

		boolean isChanged = true;
		//System.out.println(sftpChannel.pwd());

		System.out.println(dirPath);
		try {
			sftpChannel.cd(dirPath);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isChanged = false;
		}

		return isChanged;
	}

	@Override
	public List getFilesList(boolean includeFolderFlag) {

		return getFilesListAsString();		
	}

	@Override
	public List getFilesListFromPath(String dirPath, boolean includeFolderFlag) {
		if(changeDirectory(dirPath))
			return getFilesListAsString();
		return null;
	}

	@Override
	public List getFilesListAsString() {
		List<String> fileList = new ArrayList<String>();
		try {
			Vector names = sftpChannel.ls("*");
			System.out.println(names);
			if (names != null) {
				for (Object name : names) {					
					System.out.println(name.toString());
					fileList.add(name.toString());
				}
			}
		} catch (Exception e) {
			throw new RemoteExplorerException("Error while getting the listing from current location due to :"+e.getMessage());
		}
		return fileList;
	}

	@Override
	public List<String> getFilesListAsStringFromPath(String dirPath){
		if(changeDirectory(dirPath))
			return getFilesListAsString();
		return null;
	}

	@Override
	public boolean uploadFile(String remoteFileName, String localFilePath) {
		boolean isFileStored = true;
		try {
			FileInputStream fis = new FileInputStream(localFilePath);
			sftpChannel.put(fis, remoteFileName);
			fis.close();
		} catch (Exception e) {
			System.err.println("Unable to upload file :"+e.getMessage());
			isFileStored = false;
		}
		return isFileStored;
	}

	@Override
	public boolean downloadFile(String remoteFile, String localFilePath) {

		boolean isFileDownloaded = true;

		try {
			FileOutputStream fos = new FileOutputStream(localFilePath);
			sftpChannel.get(remoteFile, fos);
			fos.close();
		} catch (Exception e) {
			System.err.println("Unable to download file :"+e.getMessage());
			isFileDownloaded = false;
		}
		return isFileDownloaded;
	}

	@Override
	public boolean disconnect() {
		if (sftpChannel != null)
		{
			sftpChannel.disconnect();
			System.out.println("Channel disconnected from " + sshCredentials.getServer());
		}
		if (session != null)
		{			
			session.disconnect();
			System.out.println("Session disconnected from " + sshCredentials.getServer());
		}
		return true;
	}

	@Override
	public List<RemoteFile> getFilesListForExplorer() {
		List<RemoteFile> filesList = new ArrayList<RemoteFile>();
		
		try {
			Vector names = sftpChannel.ls("*");
			System.out.println(names);
			if (names != null) {
				ChannelSftp.LsEntry lsEntry = null;		
				for (Object name : names) {
					/*String[] info = getInfoFromString(name.toString());					
					if(info[0].equals(".")){
						continue;
					}*/
					lsEntry = (LsEntry) name;
					RemoteFile remoteFile = new RemoteFile();
					remoteFile.setName(lsEntry.getFilename());
					remoteFile.setDir(lsEntry.getAttrs().isDir());
					remoteFile.setFileSize(lsEntry.getAttrs().getSize());					
					filesList.add(remoteFile);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}
		RemoteFile remoteFile = new RemoteFile();
		remoteFile.setName("..");
		remoteFile.setDir(true);
		filesList.add(remoteFile);
		
		int id = 1;
		Collections.sort(filesList);
		for (Iterator iterator = filesList.iterator(); iterator.hasNext();) {
			remoteFile = (RemoteFile) iterator.next();
			remoteFile.setId(id++);	
		}
		return filesList;
	}

	private String[] getInfoFromString(String fileInfoString){
		String[] words = fileInfoString.split(" ");		
		String name = words[words.length-1];
		System.out.print(name);
		words = new String[2];
		words[0] = name;

		char firstChar = fileInfoString.charAt(0);
		if(firstChar == 'd' || firstChar == 'D'){
			System.out.println(" = Dir");
			words[1] = "dir";
		}else{
			System.out.println(" = File");
			words[1] = "file";
		}	
		// TODO get file size
		return words;

	}

}
