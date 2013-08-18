package com.bebo.logparser;

import java.awt.Container;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;

public class WhereClauseFrame extends JFrame {

 static Container whereClauseContainer;
 static JFrame frame;
    /** Creates new form WhereClauseFrame */
    public WhereClauseFrame() {
        super("Select Where Clause");
        initComponents();
        setSize( 500 , 580 );
        whereClauseContainer = getContentPane();
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
    }
    private void initComponents() {

        //setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout( getContentPane() );
        getContentPane().setLayout( layout );
        layout.setHorizontalGroup( layout.createParallelGroup( Alignment.LEADING ).addGap( 0 , 400 , Short.MAX_VALUE ) );
        layout.setVerticalGroup( layout.createParallelGroup( Alignment.LEADING ).addGap( 0 , 300 , Short.MAX_VALUE ) );

        pack();
    } 
}
