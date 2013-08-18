package com.bebo.logparser;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

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