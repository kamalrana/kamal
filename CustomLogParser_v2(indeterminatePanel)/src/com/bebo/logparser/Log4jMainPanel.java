package com.bebo.logparser;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Log4jMainPanel extends JPanel{
		
	private QuickParsingMainPanel quickParsingMainPanel;
	private FullParserMainPanel fullParserMainPanel;
	private HeaderPanel headerPanel;
	private Log4jBottomPanel log4jBottomPanel;
	
	public Log4jBottomPanel getLog4jBottomPanel() {
		return log4jBottomPanel;
	}
	public Log4jMainPanel(HeaderPanel headerPanel) {
		this.setHeaderPanel(headerPanel);
		setSize( LogParserConstants.secondMainPanelSize );
		setPreferredSize( LogParserConstants.secondMainPanelSize );
		
		setLayout( new BorderLayout() );
	
		quickParsingMainPanel =  new QuickParsingMainPanel( this );
		add( quickParsingMainPanel , BorderLayout.WEST );
		
		fullParserMainPanel  = new FullParserMainPanel( this );
		add( fullParserMainPanel , BorderLayout.EAST );
		
		LogParserCommon.deactivateComponents( fullParserMainPanel );
		LogParserCommon.activateComponents( quickParsingMainPanel );
		
		log4jBottomPanel = new Log4jBottomPanel();
		
		add(  log4jBottomPanel , BorderLayout.SOUTH );				
	}
	public QuickParsingMainPanel getQuickParsingMainPanel() {
		return quickParsingMainPanel;
	}
	public void setQuickParsingMainPanel(QuickParsingMainPanel quickParsingMainPanel) {
		this.quickParsingMainPanel = quickParsingMainPanel;
	}
	public FullParserMainPanel getFullParserMainPanel() {
		return fullParserMainPanel;
	}
	public void setFullParserMainPanel(FullParserMainPanel fullParserMainPanel) {
		this.fullParserMainPanel = fullParserMainPanel;
	}
	public HeaderPanel getHeaderPanel() {
		return headerPanel;
	}
	public void setHeaderPanel(HeaderPanel headerPanel) {
		this.headerPanel = headerPanel;
	}	
	
	
}
