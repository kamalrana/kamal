package com.bebo.logparser;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import org.apache.log4j.Logger;

class FileSplitter
{
    static Logger logger = Logger.getLogger(FileSplitter.class.getName());

  FileInputStream fin;
  FileOutputStream fout;
  int len;
  int splitlen;
  String str;
  String tempFilePath;
  String logFilepath;

  FileSplitter(String fileName, int splitlength, String logfilename, String tempFilePath)
  {
    try
    {
      this.fin = new FileInputStream(fileName);
      this.logFilepath = fileName;
      this.str = logfilename;
      this.len = 0;
      this.splitlen = splitlength;
      this.tempFilePath = tempFilePath;
      Split();
    }
    catch (FileNotFoundException e)
    {
      logger.error("File not found occured in SplitFiles class " + e.getMessage());
    }
    catch (IOException e)
    {
      logger.error("IO exception occured in SplitFiles class " +e.getMessage());
    }
  }

  void Split()
  {
    try
    {
      int i = 0;

      LineNumberReader lnr = new LineNumberReader(new FileReader(this.logFilepath));
      int c = this.fin.read();

      String firstLine = null;
      String secondLine = null;
      String customHeader = null;
     

      int lineNum = 2;
      while ((firstLine = lnr.readLine()) != null)
      {
    	  logger.debug("split file to be path "+tempFilePath+File.separator+this.str + "." + (i + 1) + ".spt");
    	File sptFile = new File(tempFilePath+File.separator+this.str + "." + (i + 1) + ".spt");
    	if(sptFile.exists()){
    		sptFile.delete();
    	}
    	sptFile.createNewFile();
        FileWriter fw = new FileWriter(sptFile);
        BufferedWriter bw = new BufferedWriter(fw);

        if (customHeader != null)
        {
          bw.write(customHeader + "\n");
        }
        String line;
        boolean flag = false;
        //while (((line = lnr.readLine()) != null) && (lineNum <= this.splitlen))
        while ((lineNum <= this.splitlen) && ((line = lnr.readLine()) != null) )
        {
          //String line;
          if (line.startsWith("#Fields:")) {
            customHeader = line;
          }

          if (firstLine != null) {
            bw.write(firstLine + "\n");
          }
         /* if (secondLine != null) {
            bw.write(secondLine + "\n");
          }*/
          
          if( flag ){
        	  
        	  bw.write("\n");
          }
                              
          bw.write(line);
          lineNum++;
          firstLine = null;
          flag = true;
         // secondLine = null;
        }
        logger.debug("Line Number :"+lineNum);

       /* if (lineNum > this.splitlen) {
          secondLine = line;
        }*/
        bw.flush();
        bw.close();
        //lineNum = 3;
        lineNum = 2;
        i++;
      }

    }
    catch (Exception e)
    {
      logger.error("Exception occured in SplitFiles class "+ e.getMessage());
      e.printStackTrace();
    }
  }
}