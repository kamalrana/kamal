package com.bebo.remoteexplorer;

public interface LoginCredentials {

	public String getUsername();
	
	public String getPassword();
	
	public String getServer();
	
	public int getPort();
	
	public int getConnectionTypeCode();
	
	public String getConnectionTypeName();
}
