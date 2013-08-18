package com.bebo.log4j.ui;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Log4jBottomPanel extends JPanel {
	public static JProgressBar log4jProgressBar;
	public Log4jBottomPanel() {
		
		Dimension  size = new Dimension( 780 , 50  );
		setSize( size );
		setPreferredSize( size );
		setBorder( BorderFactory.createLoweredBevelBorder() );
		log4jProgressBar = new JProgressBar();
		
		add( log4jProgressBar );
	}

}