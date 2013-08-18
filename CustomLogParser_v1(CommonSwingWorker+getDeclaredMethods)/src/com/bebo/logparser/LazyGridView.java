package com.bebo.logparser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumnModel;

public class LazyGridView extends JFrame {
   
    private static final Long startmili =System.currentTimeMillis();
    public static String level;
	public static String startDate;
	public static String endDate;
	public static String keyWordToSearch;
	
//	public static String dbName="e:/testDB.db";
	
	private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private JTextArea veiwDataTXT;
    private JScrollPane textAreaScollPane;
    private JPanel bottomPanel;
    private JPanel headerPanel;
    private java.util.List<LogData> list1;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
	private DBOperation dBOperation;
	private FullParserMainPanel fullParserMainPanel;
	
    public LazyGridView(FullParserMainPanel fullParserMainPanel) {
        super("Search Result");

        this.fullParserMainPanel=fullParserMainPanel;
        
        updateFieldValues();

        initComponents();
    }                    
    private void updateFieldValues() {
    	
		level = fullParserMainPanel.getLevelS();
		startDate = fullParserMainPanel.getStartDate();
		endDate = fullParserMainPanel.getEndDate();
		keyWordToSearch = fullParserMainPanel.getTextToFindInLogTXT().getText();
		dBOperation = fullParserMainPanel.getDbOperation();
		
        System.out.println("level is :: "+level);
        System.out.println("start date :: "+ startDate);
        System.out.println("end date :: "+ endDate);
        System.out.println("keyWordToSearch :: "+keyWordToSearch);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        
        list1 = getList();
        jTable1 = new javax.swing.JTable();        
        jScrollPane1 = LazyViewport.createLazyScrollPaneFor( jTable1 );
        veiwDataTXT = new JTextArea();
        textAreaScollPane = new JScrollPane();
        bottomPanel = new JPanel();
        headerPanel = new JPanel();
               
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dBOperation.closeConnection();
                // clean up
                setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            }
        });

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, list1, jTable1);        
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${id}" ) );        
        columnBinding.setColumnName("ID");
        columnBinding.setColumnClass(Integer.class);
        columnBinding.setEditable( false );
       
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${date}"));
        columnBinding.setColumnName("DATE");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable( false );
        
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${level}"));
        columnBinding.setColumnName("LEVEL");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable( false );
        
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${message}"));
        columnBinding.setColumnName("MESSAGE");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable( false );
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        
        jTable1.setSelectionMode(  ListSelectionModel.SINGLE_SELECTION );
        TableColumnModel columnModel = jTable1.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(  30 );
        columnModel.getColumn(0).setMinWidth( 30 );
        columnModel.getColumn(1).setPreferredWidth(  180 );
        columnModel.getColumn(1).setMinWidth( 180 );
        columnModel.getColumn(2).setPreferredWidth( 60 );
        columnModel.getColumn(2).setMinWidth( 60 );
        columnModel.getColumn(3).setPreferredWidth( 950 );
                      
        jScrollPane1.setViewportView( jTable1 );
        jScrollPane1.setPreferredSize( new Dimension( 1040 , 525 ) );
              
        headerPanel.setPreferredSize( new Dimension( 1040 , 555 ) );        
        headerPanel.add( jScrollPane1 );
        
        veiwDataTXT.setPreferredSize( new Dimension( 1040 , 63 ) );
        veiwDataTXT.setLineWrap(true);       
        veiwDataTXT.setWrapStyleWord(true);
        textAreaScollPane.setViewportView( veiwDataTXT );
                
        bottomPanel.add( textAreaScollPane );
        bottomPanel.setPreferredSize( new Dimension( 1040 , 63 ) );
        
        JPanel panel = new JPanel();  
        panel.setLayout( new BorderLayout() );
        panel.add( headerPanel , BorderLayout.CENTER );
        panel.add( bottomPanel , BorderLayout.SOUTH );
        getContentPane().add( panel );
        
        jTable1.addMouseListener( new MouseAdapter() {
			
			public void mouseClicked( MouseEvent mouseEvent ) {				
				veiwDataTXT.setText( (String)jTable1.getValueAt( jTable1.getSelectedRow() , 3 ) );
			}
		});       
        bindingGroup.bind();
        setSize( new Dimension( 1050 , 630 ) );     
        
    }                     

    private List<LogData> getList() {
        List<LogData> toReturn = new ResultList(dBOperation);
        return toReturn;
    }
    
    public JTable getJTable1(){
    	return jTable1;
    }
    
    public static void main(final String args[]) {
    	System.out.println("main method is called of LazyGridView");    	
    	/*
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LazyGridView(args).setVisible(true);
                
                Long endMili = System.currentTimeMillis();
            	Long millis = endMili - startmili;
            	System.out.println("End at :" + Calendar.getInstance().getTime());

            	System.out.println(String.format(
            			"%d min, %d sec",
            			TimeUnit.MILLISECONDS.toMinutes(millis),
            			TimeUnit.MILLISECONDS.toSeconds(millis)
            					- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
            							.toMinutes(millis))));
            }
        });
    */}
    }

 class ResultList extends AbstractList {

    private Connection connection;
    private final ExecutorService ex = Executors.newSingleThreadExecutor();
    private int size = -1;
    private String countQuery="";
    private String selectQuery="";
    //maintain a cache with the LogData instances already created and alive
    private Map<Integer, WeakReference<LogData>> cache = new HashMap<Integer, WeakReference<LogData>>();
    ResultList(DBOperation dBOperation) {
        try {
        	dBOperation.createConnection();
            this.connection = dBOperation.getConnection();
            
            countQuery =  createQuery( LogParserConstants.countQuery );
            System.out.println("row count query :: "+countQuery);
            
            selectQuery = createQuery( LogParserConstants.selectQuery );
            System.out.println("select data query :: "+selectQuery);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }

    public int size() {
        if (this.size == -1) {
            try {
            	System.out.println("count query is called");
                final ResultSet resultset = connection.createStatement().executeQuery( countQuery );
                resultset.next();
                final int toReturn = resultset.getInt(1);
                this.size = toReturn;
            } catch (SQLException ex) {
                Logger.getLogger(ResultList.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return this.size;
    }

    private String createQuery(String Query) {
    	
        if(! (LazyGridView.level.toLowerCase().equals("select")) )
        	Query+=" where level='"+LazyGridView.level+"'";
        
        if((LazyGridView.startDate!=null && LazyGridView.endDate!=null) && ( LazyGridView.startDate.length()>0 && LazyGridView.endDate.length()>0 )){
        	if(Query.contains("where level")){
        		Query+=" and ";
        	}
	        if(!Query.contains("where")){
	        	Query+=" where ";
	        }
        	Query+=" date between '"+LazyGridView.startDate+"' and '"+LazyGridView.endDate+"'";
        }
        
        if( (LazyGridView.keyWordToSearch!=null) && (LazyGridView.keyWordToSearch.length()>0)){
        	if(Query.contains("where")){
        		Query+=" and ";
        	}
        	if(!Query.contains("where")){
            	Query+=" where ";
            }
        	Query+=" message like '%"+LazyGridView.keyWordToSearch+"%'";
        }
		return Query;
	}

	public LogData get(int rowIndex) {
        //this way we ensure that we don't create several LogData instances 
        //for the same id. Otherwise it would be confusing for beansbindings. 
        LogData toReturn = null;
        if (null != this.cache.get(rowIndex)) {
            toReturn = this.cache.get(rowIndex).get();
        }
        if (null == toReturn) {
            toReturn = getItem(rowIndex + 1 );
            this.cache.put(rowIndex, new WeakReference<LogData>(toReturn));
        }

        return toReturn;
    }

    private LogData getItem(final int j) {
        final LogData LogData = new LogData(j);
//System.out.println("getItem :: "+j+"--"+LogData);

        Runnable task = new SwingWorker() {

            private String dateValue;
            private String levelValue;
            private String messageValue;
            

            @Override
            protected Object doInBackground() throws Exception {
                //this is always executed in a different thread from the current thread
                //it doesn't matter if the current thread is the EDT or a thread in the Executor
                final java.sql.Statement stmt = connection.createStatement();

                String sql = selectQuery ;
                int k=j-1;
                if(k>0)
                	sql+=" limit " + k+", "+k;
//                System.out.println("sql is : "+sql);
                ResultSet executeQuery = stmt.executeQuery(sql);
                executeQuery.next();
                dateValue = executeQuery.getString( 1 );
                levelValue = executeQuery.getString( 2 );
                messageValue = executeQuery.getString( 3 );
                return null;
            }

            @Override
            protected void done() {
                //this in the other hand will always be executed on the EDT.
                //This has to be done in the EDT because currently JTableBinding
                //is not smart enough to realize that the notification comes in another 
                //thread and do a SwingUtilities.invokeLater. So we are force to execute this
                // in the EDT. Seee http://markmail.org/thread/6ehh76zt27qc5fis and
                // https://beansbinding.dev.java.net/issues/show_bug.cgi?id=60
    
                LogData.setDate(dateValue);
                LogData.setLevel( levelValue );
                LogData.setMessage(messageValue);
//                Logger.getLogger(ResultList.class.getName()).info("updating LogData " + LogData);
            }
        };

        //NOTE that we don do task.execute()
        //posting the task to an Executor gives more control on 
        //how many threads are created. 
        ex.execute(task);
        return LogData;
    }
}