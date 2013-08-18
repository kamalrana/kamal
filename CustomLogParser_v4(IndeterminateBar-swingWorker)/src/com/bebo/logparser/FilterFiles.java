package com.bebo.logparser;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.LineNumberReader;
import org.apache.log4j.Logger;

class FilterFiles implements FilenameFilter {

    static Logger logger = Logger.getLogger(FilterFiles.class.getName());
	String ext, fileName;
	FilterFiles(String fileName, String ext)
	{
		try{

			this.fileName = fileName;
			this.ext = "."+ext;

		}catch(Exception e){
			logger.error("Error occured in FilterFiles class constructor " + e.getMessage());
		}
	}

	public boolean accept(File dir, String name) {

		return name.startsWith(fileName) && name.endsWith( ext );
	}
}
