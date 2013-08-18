package com.bebo.remoteexplorer.sharedimpl;

import com.bebo.remoteexplorer.ConnectionTypes;
import com.bebo.remoteexplorer.LoginCredentials;
import com.bebo.remoteexplorer.RemoteExplorerException;

public class SharedLoginCredentials implements LoginCredentials{
	
	private ConnectionTypes connType;
	String domain;
	String username;
	String password;
	String serverWithPath;
	
	private SharedLoginCredentials(){
		
	}	
	
	/**
	 * 
	 * @param connType
	 * @param domain
	 * @param username
	 * @param password
	 * @param serverWithPath
	 * @throws RemoteExplorerException
	 */
	public SharedLoginCredentials(ConnectionTypes connType, String domain, String username, String password, String serverWithPath) throws RemoteExplorerException{
		
		if(connType.getConnectionCode() != 3){
			throw new RemoteExplorerException("You selected incorrect connection type for Shared Location while providing credentials");
		}
		
		this.connType = connType;
		
		if(username == null || username.length() <= 0){
			throw new RemoteExplorerException("You must provide a username while providing credentials");
		}
		this.username = username;
		
		if(username != null && (password == null || password.length() <= 0)){
			throw new RemoteExplorerException("You must provide a password for the user while providing credentials");
		}
		this.password = password;
		
		if(serverWithPath == null || serverWithPath.length() <= 0){
			throw new RemoteExplorerException("You must provide a server/URL while providing credentials");
		}		
		this.serverWithPath = serverWithPath;	
		
		if(domain == null){
			this.domain = "";
		}else{
			this.domain = domain;
		}
	}
	
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getServer() {
		return serverWithPath;
	}
	public int getPort() {
		throw new UnsupportedOperationException("This method is not suported in Shared Location. Please use proper connection type.");
	}
	public boolean getLocalActiveMode() {
		throw new UnsupportedOperationException("This method is not suported in Shared Location. Please use proper connection type.");
	}
	public boolean getBinaryTransferType() {
		throw new UnsupportedOperationException("This method is not suported in Shared Location. Please use proper connection type.");
	}
	public String getDomain(){
		return domain;
	}
	@Override
	public int getConnectionTypeCode() {
		return connType.getConnectionCode();
	}
	@Override
	public String getConnectionTypeName() {
		return connType.getConnectionName();
	}
}
