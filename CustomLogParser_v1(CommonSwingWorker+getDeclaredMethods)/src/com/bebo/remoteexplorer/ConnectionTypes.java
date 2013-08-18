package com.bebo.remoteexplorer;


public enum ConnectionTypes {
	
	FTP(1, "FTP"), SFTP(2, "SFTP"), SHARED(3, "SHARED"), SSH(4, "SSH");
 
	private int connectionCode;
	private String name;
 
	private ConnectionTypes(int s, String name) {
		this.connectionCode = s;
		this.name = name;		
	}
 
	public int getConnectionCode() {
		return connectionCode;
	}
	
	public String getConnectionName(){
		return name;
	}
	
	@Override
	public String toString(){
		return name;
	}
 
}