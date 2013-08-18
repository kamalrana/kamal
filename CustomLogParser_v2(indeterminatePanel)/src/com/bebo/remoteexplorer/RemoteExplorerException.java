package com.bebo.remoteexplorer;

public class RemoteExplorerException extends RuntimeException {
	
	public RemoteExplorerException(){
		super("Logger exception occured while performing requested operation.");
	}

	public RemoteExplorerException(String message) {
        super(message);
    }

    public RemoteExplorerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
