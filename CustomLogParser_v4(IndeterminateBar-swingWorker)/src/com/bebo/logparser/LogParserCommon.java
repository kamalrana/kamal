package com.bebo.logparser;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LogParserCommon {

	public static void setConstraints( GridBagConstraints cons , int gridx, int gridy , int gridwidth , int gridheight , int fill , int anchor ){
		cons.gridx = gridx;
		cons.gridy = gridy;
		cons.gridwidth = gridwidth;
		cons.gridheight = gridheight;		
		cons.fill = fill; 
		cons.anchor = anchor;		
	}
	public static void setInsets( GridBagConstraints cons , int top , int left , int bottom , int right ){
		cons.insets = new Insets( top , left , bottom , right );
	}
	public static void activateComponents( JPanel panelToActivate ){
		Component components [] = panelToActivate.getComponents();
		for( Component component : components ){
			component.setEnabled( true );
		}
	}
	public static void deactivateComponents( JPanel panelToDeactivate ){
		Component components [] = panelToDeactivate.getComponents();
		for( Component component : components ){
			String name = component.getName();			
			if( name!=null && (name.equals( LogParserConstants.fullParsingLabel ) || name.equals( LogParserConstants.quickParsingLabel ) ) ){
				continue;
			}
			component.setEnabled( false );
		}
	}
	public static boolean initialValidation(Log4jMainPanel mainPanel) {
		HeaderPanel headerPanel = mainPanel.getHeaderPanel();
		
		String propFileName = headerPanel.getPropFile_TXT().getText();
		String logFileName = headerPanel.getLogFile_TXT().getText();
		
		System.out.println("property file is :: "+propFileName);
		System.out.println("log file is :: "+logFileName);
//		System.out.println("LogParserConstants.reqlogFileName :: "+LogParserConstants.reqlogFileName);
		
		if(! (propFileName.length()>0) ){ 
			LogParserConstants.popUpMessage = "Please select proper Log4j Property File";
			return false;
		}
		if(! (logFileName.length()>0)){
			LogParserConstants.popUpMessage = "Please select proper log file";
			return false;
		}
		
		// check wheather correct property file is selected 
			boolean propFileCheck = LogParserCommon.checkPropertyFile(new File(propFileName));
			
			if (propFileCheck) {
				headerPanel.getLblLineNum().setText("Property File Loaded successfully");
	        }
	        else{
	        	headerPanel.getLblLineNum().setText("Please provide valid log4j property file");
	        	LogParserConstants.popUpMessage = "Please provide valid log4j property file";
	        	return false;
	        }
//		}
		
		if(! logFileName.contains(LogParserConstants.reqlogFileName) ){
			LogParserConstants.popUpMessage = "choosen Log file donot matches with one in property file";
			return false;
		}
		// setting db file location
		String dbFileLocation = logFileName.substring( 0 , logFileName.indexOf(LogParserConstants.reqlogFileName) );
		
		System.out.println("db File dir :: "+dbFileLocation);
		
		LogParserConstants.dbFileLocation=dbFileLocation+"testDB.db";
		
		System.out.println("db File actual location :: "+LogParserConstants.dbFileLocation);
		
		return true;
	}
	
	private static boolean checkPropertyFile(File propertieFile) {

        Properties props = new Properties();
        String reqKey = "";
        try {
        	if(!propertieFile.exists()){
        		return false;
        	}
        		
            FileInputStream fis = new FileInputStream(propertieFile);
            // read the property file
            props.load(fis);

            // getting key set from file, saving key set for later iteration
            Set<Object> propKeySet = props.keySet();

            // if file don't begin with log4j standard key format return false
            if(! ( propKeySet.toArray()[0].toString().toLowerCase().contains("log4j") ) ){
                System.out.println("Inital key not matching log4j req. :: "+propKeySet.toArray()[0].toString());
                return false;
            }
            
            // loop to display all keys in property file
           /* for (Iterator iterator = propKeySet.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                System.out.println(key+" :: "+props.getProperty(key));
            }*/
            
            String fileAppenderKey="";
            
			// first iteration through property file to get req. key
            for (Iterator iterator1 = propKeySet.iterator(); iterator1 .hasNext();) {
                String key = (String) iterator1.next();
                
                if (props.getProperty(key).toLowerCase().endsWith("fileappender")) {
                    System.out.println("in fileappedner>> " + key);
                    fileAppenderKey = key;
                    
                    // getting req. log file name
                    if(props.getProperty(key + ".File")!=null)
					LogParserConstants.reqlogFileName = props.getProperty(key + ".File");
                    
                    else if(props.getProperty(key + ".file")!=null)
                    	LogParserConstants.reqlogFileName = props.getProperty(key + ".file");
                    
                    // return false if property file doesn't contain log file name
                    if(LogParserConstants.reqlogFileName==null || LogParserConstants.reqlogFileName.length()==0){
                    	return false;
                    }
                    
                    System.out.println("before split reqlogFileName :: "+LogParserConstants.reqlogFileName);
                    
                    String logFile[] = LogParserConstants.reqlogFileName.split("/");
                    LogParserConstants.reqlogFileName = logFile[logFile.length-1];
                    
                    System.out.println("after split reqlogFileName :: "+LogParserConstants.reqlogFileName);
//                    if (props.getProperty(key + ".File").toLowerCase()
//                            .endsWith(logFileName)) {
//                        System.out.println("Got required key, breaking >>" + key);
//                        reqKey = key;
//                        foundReqKeyFlag = true;
//                        break;
//                    }
                }
                if(key.equalsIgnoreCase(fileAppenderKey+".layout.conversionpattern"))
                { 
                	LogParserConstants.conversionPattern = props.getProperty(key);

                    System.out.println("req. conversion pattern is " + LogParserConstants.conversionPattern);
                return true;
                }
            }

            // second iteration to get conversion pattern
            for (Iterator iterator2 = propKeySet.iterator(); iterator2.hasNext();) {

                String key = (String) iterator2.next();

//                System.out.println(key + "<< >>" + props.getProperty(key));

                if (key.startsWith(reqKey) && key.toLowerCase().contains((".layout.conversionpattern"))) {
                    // System.out.println(object1);
                	LogParserConstants.conversionPattern = props.getProperty(key);

                    System.out.println("req. conversion pattern is " + LogParserConstants.conversionPattern);
                    return true;
                }
            }

            // very essential close of input stream.
            fis.close();

        } catch (IOException e) {
           
        }
        return false;
	}
	public static void getTimeTaken(String mechanism, Long startmili) {
		Long endMili = System.currentTimeMillis();
		Long millis = endMili - startmili;
//		System.out.println("End at :" + Calendar.getInstance().getTime());

		System.out.println("Time taken by :: "+mechanism+" :: "+String.format(
				"%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis))));
		
	}
}
