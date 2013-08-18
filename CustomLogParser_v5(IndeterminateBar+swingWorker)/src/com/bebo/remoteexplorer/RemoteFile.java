package com.bebo.remoteexplorer;

import java.util.Calendar;

public class RemoteFile implements Comparable<RemoteFile> {
	
	private String name;
	private boolean isDir = false;
	private String filePath;
	private long fileSize;
	private Calendar modifiedDate;
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDir() {
		return isDir;
	}
	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public Calendar getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@Override
	public int compareTo(RemoteFile o) {
		return this.getName().compareTo(o.getName());
	}
	
	/*@Override
	public int compareTo(Object o) {
		return o.toString().compareTo(this.getName());
	}
*/
	
}
