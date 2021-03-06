package com.bebo.logparser;


import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


public class ResultFrame extends javax.swing.JFrame implements ListSelectionListener{

    Object [][] data;
    String [] header;
    String [] selectedValues;
    ArrayList<String []> al =new ArrayList<String[]>();
    ListSelectionModel listSelectionModel;
    JTable dynamicTable;
    Container container;
    JFrame mainFrame;
    
    /** Creates new form ResultFrame */
    public ResultFrame(JFrame mainFrame,File resultedFile,String [] selectedValues) {
    	this.mainFrame = mainFrame;
        initComponents();
        container = mainFrame.getContentPane();
         File file = resultedFile;
         this.selectedValues = selectedValues;

    try{

            FileReader reader = new FileReader( file );
            BufferedReader br =  new BufferedReader(reader);
            String str = "" ;
            str = br.readLine();

            if( str == null || str.length() == 0 ){

                JOptionPane.showMessageDialog(null, "No data found");

                 container.setCursor(null);
            }else{

            header =getData( str );
            br.readLine();

            while( (str = br.readLine())!=null ){
                if(str.startsWith("Statistics"))
                    break;

                al.add(getData(str));
            }

            br.close();
           generateDynamicTable();

            }
      
    }catch(IOException io){
        System.out.println("Error while reading file or File not found "+ io.getMessage());
    }

      
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtArea.setColumns(20);
        txtArea.setRows(5);
        jScrollPane2.setViewportView(txtArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

  

    // Variables declaration - do not modify                     
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtArea;
    // End of variables declaration                   



public void generateDynamicTable(){

     data = new Object[al.size()][header.length];

            for (int i = 0; i < al.size(); i++) {
            String strArr[] = (String[])al.get(i);
            for (int j = 0; j < strArr.length; j++) {
                data[i][j] = strArr[j];
                }
    }
    TableModel model = new AbstractTableModel() {

public int getRowCount() {
    if( data != null )
    return data.length;

    return 0;
}


public int getColumnCount() {
    if( header != null )
    return header.length;

    return 0;
}

public String getColumnName(int col) {
        return header[col];
    }


public Object getValueAt(int paramInt1, int paramInt2) {
    return data[paramInt1][paramInt2];
  }
 };
    dynamicTable = new JTable(model) ;
    jScrollPane1.setViewportView(dynamicTable);
    container.setCursor(null);
    listSelectionModel = dynamicTable.getSelectionModel();
    listSelectionModel.addListSelectionListener(this);
 

 }

    public String[] getData(String data){
     System.out.println("SelectedValues length :" + selectedValues.length);
     System.out.println(" data is : " + data );
        String dataArray[]=new String[selectedValues.length];
        int count =0;
        String temp[] = data.split(" ");
        for (int i = 0; i < temp.length; i++) {
            String s=temp[i];
            if(!s.equals("")){
                System.out.println("s == " + s);
                dataArray[count]=temp[i];
                count++;
            }
        }
        System.out.println("Data array length : " + dataArray.length);
        return dataArray;
    }

    public void valueChanged(ListSelectionEvent e) {

        int rowIndex = dynamicTable.getSelectedRow();
        
        String [] dynamicData = al.get(rowIndex);
       
        txtArea.setText( Arrays.asList(dynamicData).toString() );

    }

}