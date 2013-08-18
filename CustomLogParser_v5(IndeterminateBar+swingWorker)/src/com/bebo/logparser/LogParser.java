package com.bebo.logparser;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.PropertyConfigurator;

import com.bebo.log4j.ui.Log4jMainPanel;

@SuppressWarnings("serial")
public class LogParser extends JFrame {
	
	private Container container;
	private HeaderPanel headerPanel;
	private BorderLayout borderLayout;
	private String logFilePath = null;
	private String propFilePath = null;
	private LogParserPanel logParserPanel = null;
	private Log4jMainPanel log4jMainPanel = null;
	
	public LogParser() {
		
		super( LogParserConstants.toolLabel );
		initLogger();				
		setMenu();		
		container = getContentPane();
		borderLayout = new BorderLayout();
		container.setLayout( borderLayout );
		headerPanel = new HeaderPanel( this );		
		logParserPanel = new LogParserPanel( this );
		
		container.add(  headerPanel , BorderLayout.NORTH );		
		container.add(  logParserPanel , BorderLayout.CENTER );
		setResizable( false );
		Dimension size = new Dimension( 780, 700 );
		setSize( size );
		setPreferredSize( size );		
		container.setVisible( true );
		setVisible( true );		
				
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
	}
	public void removeAndAddPanel( JPanel panelToAdd , JPanel panelToRemove , String location ){
		
		container.setVisible( false );
		container.remove( panelToRemove );		
		container.add( panelToAdd , location );
		container.setVisible( true );
		
	}
	private void initLogger(){	
		try {
			PropertyConfigurator.configure( new FileInputStream( "./log4j.properties" ) );
		} catch ( FileNotFoundException e ) {
			System.out.println( "Error "+ e );
			e.printStackTrace();
		}
	}
	public Log4jMainPanel getLog4jMainPanel() {
		return log4jMainPanel;
	}

	public void setLog4jMainPanel(Log4jMainPanel log4jMainPanel) {
		this.log4jMainPanel = log4jMainPanel;
	}

	public LogParserPanel getLogParserPanel() {
		return logParserPanel;
	}

	public void setLogParserPanel( LogParserPanel logParserPanel ) {
		this.logParserPanel = logParserPanel;
	}
	public String getLogFilePath() {
		return logFilePath;
	}
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	
	public String getPropFilePath() {
		return propFilePath;
	}
	public void setPropFilePath(String propFilePath) {
		this.propFilePath = propFilePath;
	}
	
	public HeaderPanel getHeaderPanel() {
		return headerPanel;
	}
	public void setHeaderPanel(HeaderPanel headerPanel) {
		this.headerPanel = headerPanel;
	}
	private void setMenu(){
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu( "File" );
		JMenuItem menuItem = new JMenuItem( "Exit" );
		menu.add( menuItem );
		menuBar.add( menu );
		menuItem.addActionListener( new ActionListener() {			
			public void actionPerformed(ActionEvent paramActionEvent) {
				System.exit( 0 );
			}
		});		
		setJMenuBar( menuBar );		
	}
	public static void main(String[] args) {		
		try {
			for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() ) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch ( ClassNotFoundException ex ) {
			
			Logger.getLogger( LogParser.class.getName() ).log( Level.SEVERE , null , ex );
		} catch ( InstantiationException ex ) {
			
			Logger.getLogger( LogParser.class.getName() ).log( Level.SEVERE , null , ex );
		} catch (IllegalAccessException ex) {
			
			Logger.getLogger( LogParser.class.getName() ).log( Level.SEVERE , null , ex );
		} catch ( UnsupportedLookAndFeelException ex) {
			
			Logger.getLogger( LogParser.class.getName() ).log( Level.SEVERE , null , ex );
		}		
		new LogParser();
	}
}
