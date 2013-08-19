package com.bebo.log4j.backend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;

public class RetrieveErrorFromFile extends JFrame {

	private static final long serialVersionUID = -957627482745109113L;
	
	private static Long startmili = System.currentTimeMillis();
	private String noOfErrors;
	public static String datePattern="\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d";
	public static String fileName = "c:/sqlite/log.out";
	private JTextArea textArea;

	private JPanel tableDataPanel;

	private JPanel viewTextPanel;
	private JTable jt;
	public RetrieveErrorFromFile(String datePattern, String logFileName) {
		super("Error Table");

		jt = new JTable(new Table(datePattern, logFileName));

		jt.getColumnModel().getColumn(0).setMinWidth( 180 );
		jt.getColumnModel().getColumn(0).setMaxWidth( 200 );
		jt.getColumnModel().getColumn(1).setMinWidth( 90 );
		jt.getColumnModel().getColumn(1).setMaxWidth( 100 );
        
        tableDataPanel =  new JPanel();
        tableDataPanel.setPreferredSize(new Dimension(1040, 540));
        
        viewTextPanel = new JPanel();
        viewTextPanel.setPreferredSize(new Dimension(1040, 63));
		
        jt.setPreferredScrollableViewportSize(new Dimension(1020, 510));
		jt.setAutoCreateRowSorter(true);
		jt.setFillsViewportHeight(true);
		  
		JScrollPane jcp = new JScrollPane(jt);
		
		tableDataPanel.add(jcp);
		
        textArea = new JTextArea();
        textArea.setLineWrap(true);       
        textArea.setWrapStyleWord(true);
		textArea.setPreferredSize(new Dimension(1040, 63));
		viewTextPanel.add(textArea);
		
		  jt.addMouseListener( new MouseAdapter() {
				
				public void mouseClicked( MouseEvent mouseEvent ) {				
					textArea.setText( (String)jt.getValueAt( jt.getSelectedRow() , 2 ) );
				}
			}); 
		
		 JPanel panel = new JPanel();  
	        panel.setLayout( new BorderLayout() );
	        panel.add( tableDataPanel , BorderLayout.CENTER );
	        panel.add( viewTextPanel , BorderLayout.SOUTH );
	        
	      
		add(panel);
		setResizable(false);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	class Table extends AbstractTableModel {

		int row;
		int col;
		ArrayList tableData = new ArrayList();
		String sql;
		String[] colName = new String[] { "date", "level", "message" };
		BufferedReader br = null;
		HashMap<Integer, HashMap<String, String>> map = new HashMap();
		private boolean flag;
		private boolean flag1;
		String line = "";
		String line1="";
		public Table(String datePattern, String logFileName) {
			String date = "";
			int i = 0;
			try {
				System.out.println("Log file is :: " + logFileName);
				br = new BufferedReader(new InputStreamReader(new FileInputStream(logFileName)));
				
				Pattern p = Pattern.compile(datePattern);
				while ((line = br.readLine()) != null) {
//					line=line.trim();
					Matcher m = p.matcher(line);
					boolean b =m.find();
					if(b && flag){
//						System.out.println("nt matched");
						addToTableData(date);
					}
					if (line.contains("ERROR") && (b) && !flag) {
						flag=true;
						date = "";
						int k=0;
							date = m.group();
//							System.out.println(k+" :: date :: "+date);
					}
					if(flag){
						line1+=line+"\n";
					}
					
				}
				if(flag)
				{
					ArrayList columnData= new ArrayList();
                    columnData.add(date);
                    columnData.add("ERROR");
                    columnData.add(line1);
                    tableData.add(columnData);
					flag=false;
					System.out.println(line1);
					line1="";
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// no. of rows will be no. of errors 
			setNoOfErrors(tableData.size());
		}

		private void addToTableData(String date) {
			ArrayList columnData= new ArrayList();
            columnData.add(date);
            columnData.add("ERROR");
            columnData.add(line1);
            tableData.add(columnData);
			flag=false;
			line1="";
		}

		public String getColumnName(int col) {
	        return colName[col];
	    }
		
		@Override
		public int getRowCount() {
			return tableData.size();
		}

		@Override
		public int getColumnCount() {
			return colName.length;
		}

		@Override
		public Object getValueAt(int row, int column) {
			return ((ArrayList)tableData.get(row)).get(column);
		}
	}

	public String getNoOfErrors() {
		return noOfErrors;
	}

	public void setNoOfErrors(int noOfErrors) {
		this.noOfErrors = String.valueOf(noOfErrors);
	}
	public static void main(String[] args) {
		RetrieveErrorFromFile pane=new RetrieveErrorFromFile(datePattern, fileName);
		pane.setVisible(true);
	}

}

