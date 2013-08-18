package com.bebo.remoteexplorer.sharedimpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPFile;

import com.bebo.remoteexplorer.RemoteFetch;
import com.bebo.remoteexplorer.RemoteExplorerException;
import com.bebo.remoteexplorer.RemoteFile;

public class SharedRemoteFetch implements RemoteFetch {

	private SharedRemoteFetch(){}

	public SharedRemoteFetch(SharedLoginCredentials credentials) throws RemoteExplorerException{
		this.sharedCredentials = credentials;
		this.login();
	}

	private NtlmPasswordAuthentication authentication;
	private SharedLoginCredentials sharedCredentials;
	private SmbFile sharedLoc;

	@Override
	public boolean login() throws RemoteExplorerException {		

		try {
			String username = sharedCredentials.getUsername();
			if(sharedCredentials.getDomain() != null && sharedCredentials.getDomain().length() > 0){
				username = sharedCredentials.getDomain()+"\\"+sharedCredentials.getUsername();
			}
			authentication = new NtlmPasswordAuthentication(null, username, sharedCredentials.getPassword());
			System.out.println("Connecting to -"+sharedCredentials.getServer());
			sharedLoc = new SmbFile("smb://" + sharedCredentials.getServer() + "/", authentication);
			sharedLoc.setConnectTimeout(30000);
			if(!sharedLoc.exists()){
				System.out.println("Shared folder: "+sharedCredentials.getServer()+" dosent exist!");
				return false;
			}
			System.out.println("Connected to: "+sharedLoc.getPath());

		} catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteExplorerException("Unable lo open the shared location:"+sharedCredentials.getServer()+", due to :"+e.getMessage());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteExplorerException("Unable lo open the shared location:"+sharedCredentials.getServer()+", due to :"+e.getMessage());
		}


		return true;
	}

	@Override
	public boolean changeDirectory(String dirPath) {
		System.out.println("parent:"+sharedLoc.getParent());
		System.out.println("name:"+sharedLoc.getName());
		String smburl = "smb://" + sharedCredentials.getServer()+"/"+dirPath ;
		if(dirPath.equals("..")){
			String parentsmburl = sharedLoc.getParent();
			String defaultSmbURL = "smb://" + sharedCredentials.getServer()+"/"; 
			if(parentsmburl.length() < defaultSmbURL.length()){
				System.out.println("Already at root shared location");
				return true;
			}else{
				smburl = sharedLoc.getParent();
			}
		}
		try {
			sharedLoc = new SmbFile(smburl, authentication);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<SmbFile> getFilesList(boolean includeFolderFlag) {
		List<SmbFile> fList = new ArrayList<SmbFile>();        
		try {

			SmbFile[] fArr = sharedLoc.listFiles();

			for(int a = 0; a < fArr.length; a++)
			{
				if(fArr[a].isDirectory() && includeFolderFlag){
					fList.add(fArr[a]);
					continue;
				}
				fList.add(fArr[a]);
				System.out.println(fArr[a].getName());				
			}
		}catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}        

		return fList;
	}

	@Override
	public List<SmbFile> getFilesListFromPath(String dirPath, boolean includeFolderFlag) {
		changeDirectory(dirPath);
		return getFilesList(includeFolderFlag);
	}

	@Override
	public List<String> getFilesListAsString() {

		List<String> fList = new ArrayList<String>();        
		try {

			String[] fArr = sharedLoc.list();

			for(int a = 0; a < fArr.length; a++)
			{
				fList.add(fArr[a]);
				System.out.println(fArr[a]);
			}
		}catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}        

		return fList;
	}

	@Override
	public List<String> getFilesListAsStringFromPath(String dirPath) {

		System.out.println("Listing of file on shared location inside "+dirPath);

		changeDirectory(dirPath);
		return getFilesListAsString();		

	}

	@Override
	public boolean uploadFile(String remoteFileName, String filePath){

		System.out.println("Uploading file :" +remoteFileName);

		File localFile = new File(filePath);

		try {
			SmbFile remoteFile = new SmbFile(sharedLoc.getPath()+remoteFileName, authentication);
			SmbFileOutputStream sfos = new SmbFileOutputStream(remoteFile);
			String fileContent = IOUtils.toString(new FileReader(localFile));					
			sfos.write(fileContent.getBytes());
		} catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteExplorerException("Error reading remote location1 :"+remoteFileName);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteExplorerException("Error reading remote location2 :"+remoteFileName);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteExplorerException("Error reading remote location3 :"+remoteFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteExplorerException("Cannot upload the file as it dosent exist on :"+filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteExplorerException("Error reading local file, cannot upload the file");
		}			


		return true;
	}

	@Override
	public boolean downloadFile(String remoteFileName, String localFilePath) throws RemoteExplorerException {

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(localFilePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteExplorerException("Cannot download the file because local path dosent exist as :"+localFilePath);
		}

		try {
			SmbFile fileToDownload = new SmbFile(sharedLoc.getPath()+remoteFileName, authentication);
			BufferedInputStream buf = new BufferedInputStream(new SmbFileInputStream(fileToDownload));
			byte[] b = new byte[8192];
			int n;
			while ((n = buf.read(b)) > 0) {
				fos.write(b, 0, n);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new RemoteExplorerException("Error while downloading the file :"+e.getMessage());
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean disconnect() {
		sharedLoc = null;
		return false;
	}

	@Override
	public List<RemoteFile> getFilesListForExplorer() {
		List<RemoteFile> filesList = new ArrayList<RemoteFile>();
		int id = 1;
		RemoteFile remoteFile = new RemoteFile();
		remoteFile.setName("..");
		remoteFile.setDir(true);
		filesList.add(remoteFile);
		try {
			SmbFile[] fArr = sharedLoc.listFiles();
			for (int i = 0; i < fArr.length; i++) {
				SmbFile smbFile = (SmbFile) fArr[i];
				remoteFile = new RemoteFile();
				remoteFile.setName(smbFile.getName());
				remoteFile.setDir(smbFile.isDirectory());
				remoteFile.setFileSize(smbFile.length());
				filesList.add(remoteFile);
			}
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}
		
		Collections.sort(filesList);
		for (Iterator iterator = filesList.iterator(); iterator.hasNext();) {
			remoteFile = (RemoteFile) iterator.next();
			remoteFile.setId(id++);	
		}
		return filesList;
	}
}
