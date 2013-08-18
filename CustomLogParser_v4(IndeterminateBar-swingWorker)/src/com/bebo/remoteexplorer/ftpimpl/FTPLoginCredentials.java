package com.bebo.remoteexplorer.ftpimpl;

import com.bebo.remoteexplorer.ConnectionTypes;
import com.bebo.remoteexplorer.LoginCredentials;
import com.bebo.remoteexplorer.RemoteExplorerException;

public class FTPLoginCredentials implements LoginCredentials{
	
	private ConnectionTypes connType;
	private String username;
	private String password;
	private String server;
	private int port;
	private boolean isBinaryTransfer;
	private boolean isLocalActiveMode;
	
	private FTPLoginCredentials(){
		
	}	
		
	public FTPLoginCredentials(ConnectionTypes connType, String username, String password, String server, int port, boolean isBinaryTransfer, boolean isLocalActiveMode) throws RemoteExplorerException{
		
		if(connType.getConnectionCode() != 1){
			throw new RemoteExplorerException("You selected incorrect connection type for FTP while providing credentials");
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
		
		if(server == null || server.length() <= 0){
			throw new RemoteExplorerException("You must provide a server/URL while providing credentials");
		}		
		this.server = server;
		
		this.port = port;
		if(this.port != 0){
			if(this.connType == ConnectionTypes.FTP){
				this.port = 21;
				this.isBinaryTransfer = isBinaryTransfer;
				this.isLocalActiveMode = isLocalActiveMode;
			}
		}
	}
	

	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getServer() {
		return server;
	}
	public int getPort() {
		return port;
	}
	public boolean getLocalActiveMode() {
		return isLocalActiveMode;
	}
	public boolean getBinaryTransferType() {
		return isBinaryTransfer;
	}	
	public String getDomain(){
		throw new UnsupportedOperationException("This method is not suported in FTP. Please use proper connection type.");
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
