package com.bebo.logparser;

import java.util.Random;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class threadPlugin extends SwingWorker<Void, Void> {

	  int Delay = 100; //Creating a delay or the speed of the progress bar
	  JProgressBar pb; //Constructing Progress Bar
	  int progress=0;
	Random random = new Random();

	  public  threadPlugin(JProgressBar pb) {
		 this.pb = pb;
	  }

	@Override
	protected Void doInBackground() throws Exception {

		  System.out.println("progress bar started");
	      int minimum = Log4jBottomPanel.log4jProgressBar.getMinimum(); 
	      int maximum = Log4jBottomPanel.log4jProgressBar.getMaximum(); 
	      
	      //Initializing Progress from its minimum value 0 to its maximum value 100
	      for (int i = minimum; i < maximum; i++) { 
	        try {
	        	progress+= random.nextInt( 10 );
	          System.out.println("initial value is :: "+progress);
	          Log4jBottomPanel.log4jProgressBar.setValue(progress);

	          if (Log4jBottomPanel.log4jProgressBar.getValue() >= maximum-20) {
	        	  System.out.println("sleeping as value is closer to max");
	          Thread.sleep(5000);
	          }
	           
	          Thread.sleep(Delay); //Implementing the speed of the progress bar
	        } catch (Exception ignoredException) { //Catch an error if there is any
	        }
	      }
	    
		return null;
	}
	 }
