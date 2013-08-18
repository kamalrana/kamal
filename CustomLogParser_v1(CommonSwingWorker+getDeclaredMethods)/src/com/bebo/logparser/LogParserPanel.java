package com.bebo.logparser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class LogParserPanel extends JPanel  implements PropertyChangeListener{
	
	private Logger logger = Logger.getLogger( LogParserPanel.class.getName() );

	private String [] selectedValues = null;
	private String logFilePathParent = null;
	private String applogParserHome = null;
	private String tempFolderPath = null;
	private String [] splitedFilesArray;
	private File f2;
	private File resultedFile;
	public static int count = 0;
	private String selectClause="";
	private String whereQuery="";
	private Task task;
	private LogParser mainFrame;
	private Container container;
	private WhereClausePanel whereClausePanel;

	private JLabel select_Lbl;
	private JLabel where_Lbl;
	private JLabel lblOrderBy;
	private JLabel lblGroupBy;
	
	private JScrollPane select_SP;
	private JList select_List;
	
	private JButton whereClause_Btn;
	private JButton submitQuery_Btn;
	
	private JButton btnResult;
	private JButton btnStats;
	private JButton btnLogFile;
	private JButton reset_Btn;
	private JButton viewGraph_Btn;
	
	private JRadioButton btnRadioAsc;
	private JRadioButton btnRadioDesc;
	
	private ButtonGroup btnOrderByGroup;
	private String statisticsStr = "";
	
	private JComboBox orderByCMB;
	private JComboBox groupByCMB;
	private Set < String > orderAndGroupBySet;
	
	private JProgressBar progressBar;
	private GridBagLayout layout;
	private GridBagConstraints cons;
		
	public LogParserPanel( LogParser mainFrame ) {
		initComponents();
		btnResult.setVisible( false );
		btnStats.setVisible( false );
		btnLogFile.setVisible( false );
		viewGraph_Btn.setVisible( false );	
		container = mainFrame.getContentPane();
		this.mainFrame = mainFrame;		
	}
	private void initComponents(){
		
		select_Lbl = new JLabel( LogParserConstants.selectLabel );
		select_List = new JList( getListData() );
		select_List.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );		
		select_List.setVisibleRowCount( 4 );
		select_SP = new JScrollPane( select_List );
		select_SP.setPreferredSize( new Dimension( 200 , 80 ) );
		
		where_Lbl = new JLabel( LogParserConstants.whereClauseLabel );				
		whereClause_Btn = new JButton( LogParserConstants.selectCriteriaBTN );		
		btnResult = new JButton( LogParserConstants.viewResultBTN );				
		btnStats = new JButton( LogParserConstants.viewStatiticsBTN );
		btnLogFile = new JButton( LogParserConstants.viewLogFileBTN );	  
	    lblOrderBy = new JLabel( LogParserConstants.orderByLabel );
	    lblGroupBy = new JLabel( LogParserConstants.groupLabel );
	    
	    orderAndGroupBySet = new LinkedHashSet< String >();
	    orderAndGroupBySet.add( LogParserConstants.selectOption );
	    	    
	    orderByCMB = new JComboBox( orderAndGroupBySet.toArray() );
	    
		groupByCMB = new JComboBox( orderAndGroupBySet.toArray() );

		submitQuery_Btn = new JButton( LogParserConstants.submitQueryBTN );	
		progressBar = new JProgressBar();
		reset_Btn = new JButton( LogParserConstants.resetLabel);
		viewGraph_Btn = new JButton( LogParserConstants.viewGraphLabel );
		
		btnRadioAsc = new JRadioButton( LogParserConstants.ascRADBTN );
		btnRadioAsc.setSelected( true );
		btnRadioDesc = new JRadioButton( LogParserConstants.descRADBTN );
		
		btnOrderByGroup = new ButtonGroup();
		btnOrderByGroup.add( btnRadioAsc );
		btnOrderByGroup.add( btnRadioDesc );
		
		setLayout( new BorderLayout() );		
		setSize( LogParserConstants.secondMainPanelSize );
		setPreferredSize( LogParserConstants.secondMainPanelSize );
		setBorder( BorderFactory.createLoweredBevelBorder() );
		
		layout = new GridBagLayout();
		cons = new GridBagConstraints();
		
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout( layout );
		Dimension firstPanelsize = new Dimension( 780 , 410 );
		firstPanel.setSize( firstPanelsize );
		firstPanel.setPreferredSize( firstPanelsize );
		
		LogParserCommon.setInsets( cons , 30 , 4, 2, 2 );
		
		setConstraints( 0 , 0 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		firstPanel.add( select_Lbl , cons );
		
		setConstraints( 1 , 0 , 1 , 1 , GridBagConstraints.BOTH , GridBagConstraints.CENTER );
		firstPanel.add( select_SP , cons );
		
		setConstraints( 2 , 0 , 2 , 1 , GridBagConstraints.NONE , GridBagConstraints.EAST );
		firstPanel.add( where_Lbl , cons );
		
		setConstraints( 4 , 0 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		firstPanel.add( whereClause_Btn , cons );
		
		setConstraints( 4 , 0 , 1 , 1 , GridBagConstraints.BOTH , GridBagConstraints.WEST );
		firstPanel.add( new JLabel("                                                                         ") , cons );
		
		setConstraints( 0 , 1 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		firstPanel.add( lblOrderBy , cons );
		
		setConstraints( 1 , 1 , 1 , 1 , GridBagConstraints.BOTH , GridBagConstraints.WEST );
		firstPanel.add( orderByCMB , cons );
		
		setConstraints( 2 , 1 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		firstPanel.add( btnRadioAsc , cons );
		
		setConstraints( 3 , 1 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.EAST );
		firstPanel.add( btnRadioDesc , cons );
		
		setConstraints( 0 , 2 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		firstPanel.add( lblGroupBy , cons );
		
		setConstraints( 1 , 2 , 1 , 1 , GridBagConstraints.BOTH , GridBagConstraints.WEST );
		firstPanel.add( groupByCMB , cons );
		
		LogParserCommon.setInsets( cons , 30 , 4, 20 , 2 );
		
		setConstraints( 0 , 3 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		firstPanel.add( reset_Btn , cons );
				
		setConstraints( 1 , 3 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		firstPanel.add( submitQuery_Btn , cons );
		
		setConstraints( 1 , 3 , 1 , 1 , GridBagConstraints.NONE , GridBagConstraints.EAST );
		firstPanel.add( btnResult , cons );
		
		setConstraints( 2 , 3 , 2 , 1 , GridBagConstraints.NONE , GridBagConstraints.WEST );
		firstPanel.add( btnStats , cons );
		
		setConstraints( 2 , 3 , 3 , 1 , GridBagConstraints.NONE , GridBagConstraints.CENTER );
		firstPanel.add( btnLogFile , cons );
		setConstraints( 4 , 3 , GridBagConstraints.REMAINDER , 1 , GridBagConstraints.NONE , GridBagConstraints.EAST );
		firstPanel.add( viewGraph_Btn , cons );
				
		JPanel secondPanel = new JPanel();
		Dimension secondPanelsize = new Dimension( 780 , 100 );
		secondPanel.setSize( secondPanelsize );
		secondPanel.setPreferredSize( secondPanelsize );
		secondPanel.setBorder( BorderFactory.createLoweredBevelBorder() );			
		secondPanel.add( progressBar );
		
		add( firstPanel , BorderLayout.NORTH );
		add( secondPanel , BorderLayout.CENTER );
				
		btnLogFile.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				btnLogFileActionPerformed( evt );
			}
		});
		
		btnStats.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnStatsActionPerformed(evt);
			}
		});
		
		btnResult.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnResultActionPerformed(evt);
			}
		});
		
		whereClause_Btn.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				btnWhereActionPerformed( evt );
			}
		});
		viewGraph_Btn.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {

				btnViewGraphActionPerformed( evt );
			}
		});

		submitQuery_Btn.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				btnSubmitActionPerformed( evt );
			}
		});		
		select_List.addMouseListener( new java.awt.event.MouseAdapter() {
			public void mouseClicked( MouseEvent evt) {
				jList1MouseClicked( evt );
			}
		});
	}
	
	private void btnViewGraphActionPerformed( ActionEvent evt ){

        JPanel panel = new JPanel();
        panel.add(new JLabel( "Please make a selection:" ) );
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement( "Line" );
        model.addElement( "Line3D" );
        model.addElement( "SmoothLine" );
        model.addElement( "PieExploded" );
        model.addElement( "PieExploded3D" );
        model.addElement( "ColumnClustered" );
        model.addElement( "ColumnClustered3D" );
        JComboBox comboBox = new JComboBox( model );
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Pie-Chart", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if( result == 0 ){

            String chartTypeValue = (String) comboBox.getSelectedItem();
            try{
                logger.debug( "Select Clause : " + selectClause );
                String commandToRun =
                "\""+applogParserHome+"\\LogParser.exe\" -i:IISW3C -o:chart -chartType:"+chartTypeValue+" -view:on " +
                "\"SELECT "+selectClause+" INTO "+tempFolderPath+"\\PieChartImage.jpg FROM "
                +mainFrame.getLogFilePath()+" where "+whereQuery+"\"";

                logger.debug(commandToRun);

                Process p = Runtime.getRuntime().exec( commandToRun, null );

                String line;
                BufferedReader bri = new BufferedReader
                (new InputStreamReader(p.getInputStream()));
                BufferedReader bre = new BufferedReader
                (new InputStreamReader(p.getErrorStream()));

                logger.error("------Input stream-----");
                String errorStr = "";
                while ((line = bri.readLine()) != null) {
                    logger.info(line);
                }
                bri.close();
                logger.error("------Error stream-----");
                while ((line = bre.readLine()) != null) {

                    if(line != null && line.contains("Error")){

                        errorStr = line; JOptionPane.showMessageDialog(null,errorStr);
                    }
                    logger.error(line);
                }
                bre.close();

                // p.waitFor();
            } catch (IOException e) {
                logger.error("IOException : "+e.getMessage());
            } catch (Exception e){
                logger.error("Exception : "+e.getMessage());
            }
            logger.debug("Query processed :: ");
        }
    }

	private void setConstraints( int gridx, int gridy , int gridwidth , int gridheight , int fill , int anchor ){
		cons.gridx = gridx;
		cons.gridy = gridy;
		cons.gridwidth = gridwidth;
		cons.gridheight = gridheight;		
		cons.fill = fill; 
		cons.anchor = anchor;		
	}
	private String[] getListData(){
		String[] data = { "date", "time", "s-ip", "cs-method", "cs-uri-stem", "cs-uri-query", "s-port", "cs-username", "c-ip", 
				"cs(User-Agent)", "sc-status", "sc-substatus", "sc-win32-status", "time-taken" };
		return data;
	}
	private void btnWhereActionPerformed( ActionEvent evt ) {                                         

		WhereClauseFrame whereClauseFrame = new WhereClauseFrame();
		whereClauseFrame.setResizable( false );
		whereClauseFrame.setVisible( true );
		
		whereClausePanel = new WhereClausePanel( whereClauseFrame );
		whereClausePanel.setBounds( 10 , 10 , 480 , 530 );
		WhereClauseFrame.whereClauseContainer.removeAll();
		WhereClauseFrame.whereClauseContainer.setVisible( false );
		WhereClauseFrame.whereClauseContainer.add( whereClausePanel );
		WhereClauseFrame.whereClauseContainer.setVisible( true );

	}                                        

	private void btnSubmitActionPerformed( ActionEvent evt ) {                                          
		
		mainFrame.setLogFilePath( mainFrame.getHeaderPanel().getLogFile_TXT().getText() );

		if( mainFrame.getLogFilePath().length() == 0 ){
			mainFrame.getHeaderPanel().getLblLineNum().setText( LogParserConstants.logFilePathMSG );
			
			mainFrame.getHeaderPanel().getLblLineNum().setForeground( Color.BLUE );
			logger.error( LogParserConstants.logFilePathMSG );
			return;
		}else if(( whereClausePanel == null) ||  ( whereClausePanel.whereQuery == null || whereClausePanel.whereQuery.length() == 0 )){

			JOptionPane.showMessageDialog( null , LogParserConstants.whereClauseMSG );
			return;
		}else if(selectedValues == null || selectedValues.length == 0){
			JOptionPane.showMessageDialog( null , LogParserConstants.selectClauseMSG );
			return;
		}else{
			submitQuery_Btn.setEnabled( false );
			container.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

			task = new Task();
			task.addPropertyChangeListener( this );
			task.execute();
		}
	}                                         

	private long[] initArray( int length ){

		long [] fileLength = new long[length];
		for( int i=0; i< length; i++){
			fileLength[i] = -1l;
		}
		return fileLength;
	}

	private List<String> getCSVFilesNames( int length ){

		List<String> files = new ArrayList<String>();
		for(int i = 1; i<=length; i++){
			String fileName = "test"+ i +".csv";
			files.add( fileName );

		}

		return files;
	}
	private void jList1MouseClicked( MouseEvent evt) {                                    
	
		Object [] objValues = select_List.getSelectedValues();

		if( objValues.length > 0 ){

			selectedValues = new String[objValues.length];

			for( int i = 0; i < objValues.length; i++ ){

				selectedValues[i] = (String)objValues[i];
			}
			orderAndGroupBySet = new LinkedHashSet< String >();
			orderAndGroupBySet.add( LogParserConstants.selectOption );
			orderAndGroupBySet.addAll( Arrays.asList( selectedValues ) );
			
			groupByCMB.removeAllItems();
			orderByCMB.removeAllItems();
			for( String item : orderAndGroupBySet){
				groupByCMB.addItem( item );
				orderByCMB.addItem( item );
			}
		}
	}                                   

	private void btnResultActionPerformed(java.awt.event.ActionEvent evt) {                                          
		container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		ResultFrame resultFrame = new ResultFrame(mainFrame,resultedFile, selectedValues);
		resultFrame.setVisible(true);
		resultFrame.setResizable( false );

	}  	
	private void btnStatsActionPerformed( ActionEvent evt ) {                                          
		
		JOptionPane.showMessageDialog(mainFrame, statisticsStr);

	}  
	private void btnLogFileActionPerformed(java.awt.event.ActionEvent evt) {                                           
		
		try{
			Desktop.getDesktop().browse( resultedFile.getParentFile().toURI() );

		}catch(IOException io)
		{
			System.out.println("Error: Unable to open log file." + io.getMessage() );
		}
	}                                          

	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange( PropertyChangeEvent evt ) {
		if ( "progress" == evt.getPropertyName() ) {
			int progress = ( Integer ) evt.getNewValue();
			progressBar.setValue( progress );
		}
	}

	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {

			try{

				progressBar.setVisible( true );
				Random random = new Random();
				int progress = 0;
				//Initialize progress property.
				setProgress(0);

				TaskPerformed tp = new TaskPerformed();
				Class c = tp.getClass();

				Method [] methodArray = c.getDeclaredMethods();

				for( Method method : methodArray ){

					progress += random.nextInt( 10 );
					setProgress( Math.min( progress , 100 ) );
					logger.info( "invoking method :" + method.getName() );
					method.invoke( tp );

					progress += random.nextInt( 10 );
					setProgress( Math.min( progress, 100 ) );

				}
				while (progress < 100) {
					//Sleep for up to one second.
					try {
						Thread.sleep(random.nextInt(1000));
					} catch (InterruptedException ignore) {}
					//Make random progress.
					progress += random.nextInt(10);
					setProgress(Math.min(progress, 100));
				}
			}catch( Exception ie )
			{
				logger.error(" Illegal access exception ", ie);

			}
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			submitQuery_Btn.setEnabled( false );
			container.setCursor( null ); //turn off the wait cursor
			btnResult.setVisible( true );
			btnStats.setVisible( true );
			btnLogFile.setVisible( true );
			viewGraph_Btn.setVisible( true );

		}
	}

	class TaskPerformed{

		public void validateAndCreateCSV(){

			Properties prop = null;
			File file = new File( mainFrame.getLogFilePath() );

			long fileLength = file.length();
			long fileSizeKB = fileLength/1024;
			long fileSizeMB = fileSizeKB/1024;

			// file size in MegaBytes
			int fileInMB = (int)fileSizeMB;

			if(prop == null){
				try {
					//ClassLoader loader = Thread.currentThread().getContextClassLoader();
					prop = new Properties();
					prop.load( new FileInputStream("./logparser.properties"));

					applogParserHome = prop.getProperty("logParserHome");				

					tempFolderPath = prop.getProperty("tempfolderpath");
				} catch (IOException e1) {
					logger.error("IOException : "+e1.getMessage());
					prop = null;
				}
		}

		if(tempFolderPath == null || tempFolderPath.equals("") ){

			File logParserSysTempFolder = new File(System.getProperty("java.io.tmpdir")+"/LogParserTemp");
			logger.debug("logParserSysTempFolder " + logParserSysTempFolder);
			if(!logParserSysTempFolder.exists()){
				if(logParserSysTempFolder.mkdirs()){

					logger.error("**** ERROR creating temp folder ****");
				}
			}
			tempFolderPath = logParserSysTempFolder.getAbsolutePath();

			File[] files = logParserSysTempFolder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

		}

		logger.debug("Temp Folder Path is " + tempFolderPath);
		logger.debug("Log Parser home is " + applogParserHome);
		int minFileSize = Integer.parseInt(prop.getProperty("minFileSizeSplitMB"));
		logger.debug("Minimum file size to split : " + minFileSize);

		int noOfLines = 0;

		if( fileInMB > minFileSize ){

			noOfLines = ( count*30 )/100;
		}else{

			noOfLines = count;
		}

		if( mainFrame.getLogFilePath() != null && applogParserHome != null ){

			whereQuery = whereClausePanel.whereQuery;
			logger.debug("Where query is : " + whereQuery);

			if( (whereQuery.length() == 0 || whereQuery == null)){

				JOptionPane.showMessageDialog(null,"Please select one where clause");

			}else{

				f2 = new File(mainFrame.getLogFilePath());
				logFilePathParent =  f2.getParent();

				logger.debug("Log file path parent " + logFilePathParent);

				logger.debug("Log file path " + mainFrame.getLogFilePath());

				FileSplitter ob1=new FileSplitter(mainFrame.getLogFilePath(),noOfLines,file.getName(), tempFolderPath );

				logger.info("File splitted..");

				boolean flag = false;

				for( String str : selectedValues ){

					if(flag){

						selectClause = selectClause+", ";

					}

					selectClause = selectClause+str;
					flag = true;

				}

				if( selectClause == ""){

					selectClause = "*";
				}

				logger.info("Select clause in query is " + selectClause);

				FilenameFilter splittedFilesFilter = new FilterFiles(file.getName(),"spt");
				splitedFilesArray = new File(tempFolderPath).list( splittedFilesFilter );

				logger.debug("Splitted files are " + splitedFilesArray.length);


				for( int i=0; i<splitedFilesArray.length; i++ ){

					try {
						String commandToRun = "\""+applogParserHome+"\\LogParser.exe\" -rtp:-1 -i:IISW3C " +
								"\"SELECT "+selectClause+" INTO "+tempFolderPath+"\\test"+(i+1)+".csv FROM "
								+tempFolderPath+"\\"+ splitedFilesArray[i]+" where "+whereQuery+"\"";

						logger.debug(commandToRun);
						Process p =	   	Runtime.getRuntime().exec( commandToRun, null );


						String line;
						BufferedReader bri = new BufferedReader
								(new InputStreamReader(p.getInputStream()));
						BufferedReader bre = new BufferedReader
								(new InputStreamReader(p.getErrorStream()));
						
						logger.error("------Input stream-----");
						statisticsStr = "";
						while ((line = bri.readLine()) != null) {
							logger.error(line);
							statisticsStr = statisticsStr+line+"\n";
						}
						bri.close();
						logger.error("------Error stream-----");
						while ((line = bre.readLine()) != null) {
							logger.error(line);
						}
						bre.close();
						p.waitFor();
					} catch (IOException e) {
						logger.error("IOException : "+e.getMessage());
					} catch (InterruptedException e) {
						logger.error("InterruptedException : "+e.getMessage());
					}

				}

				List<String> csvFiles = getCSVFilesNames( splitedFilesArray.length );
				long filesSize[] = initArray( csvFiles.size() );
				int counter = 0;

				while( true ){

					for( int i = 0; i < csvFiles.size(); i++ ){

						String csvFileName = csvFiles.get(i);
						try{
							File csvFile = new File( tempFolderPath +"\\"+ csvFileName );

							long csvfileLength = csvFile.length();
							System.out.println("Csv file Length :: "+ csvfileLength);
							long lengthInArray = filesSize[i];
							System.out.println("file Length from array " + lengthInArray);
							if( csvfileLength > lengthInArray && filesSize[i] != -2l ){

								filesSize[i] = csvfileLength;
							}else if( filesSize[i] != -2l ){

								filesSize[i] = -2l;
								counter++;
							}

						}catch( Exception fe){
							logger.error("Exception : "+fe.getMessage());
							System.out.println("File not found."+ fe.getMessage());
						}
					}

					if( counter == csvFiles.size() ){
						break;
					}

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						logger.error("InterruptedException : "+e.getMessage());
					}
				}
			}
		}
	}
	public void mergeAllCSVFiles(){

		/** merging all csv files result in one csv file */
		CSVMerger mergeFileObj = new CSVMerger(tempFolderPath, mainFrame.getLogFilePath());

		resultedFile = mergeFileObj.getResultedCSVFile();
		logger.debug("File merged...");
	}
	public void deleteAllLogFiles(){

		/** Deleting all splitted log files from LogFilePath directory */
		List<String> list;
		//Fetching logFile parent directory
		File logFileParentDir = new File( logFilePathParent );

		//Fetching files from logparser directory
		list = new ArrayList<String>();
		String [] logFiles = logFileParentDir.list();

		for( String file : logFiles ){

			if( file.endsWith(".spt") ){
				list.add( file );
			}
		}
		logger.debug("Number of log files to be deleted "+ list.size());

		//Deleting files from parent log directory
		for( String file : list ){
			File f1 = new File(logFileParentDir, file);
			f1.delete();
		}
		logger.info("log files deleted..");
	}

	public void deleteAllCSVFiles(){

		/** Deleting all csv files from LogParser directory */
		List<String> list;
		//Fetching logparser directory
		File parserHomeDir = new File( tempFolderPath );

		//Fetching files from logparser directory
		list = new ArrayList<String>();
		String [] allFiles = parserHomeDir.list();

		for( String file : allFiles ){

			if( file.endsWith(".csv") ){
				list.add( file );
			}
		}
		logger.debug("Number of csv files to be deleted " + list.size());
		System.out.println(list);
		//Deleting files from logparser directory
		for( String file : list ){
			try{
				File f = new File(parserHomeDir, file);
				boolean isdelete = f.delete();
				//Thread.sleep(1000);

			}catch(Exception ie){}
		}
		logger.info("Csv files deleted...");
	}


}
}
