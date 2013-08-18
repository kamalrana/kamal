package com.bebo.logparser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import javax.swing.SwingWorker;

public class SwingTaskWorker<T> extends SwingWorker<Void, Void> {
	private Class c;
	private T classInstance;

	public SwingTaskWorker(T parser) {
		c=parser.getClass();
		System.out.println("class name "+c.getName());
		classInstance=parser;
		System.out.println("class instance :: "+classInstance);
	}

	/*
	 * Main task. Executed in background thread.
	 */
	@Override
	public Void doInBackground() {

		try{
			Random random = new Random();
			int progress = 0;
			//Initialize progress property.
			setProgress(0);

			Method [] methodArray = c.getDeclaredMethods();
			System.out.println("list of methods :: "+Arrays.asList(methodArray));
			for( Method method : methodArray ){

				progress += random.nextInt( 10 );
				setProgress( Math.min( progress , 100 ) );
				
				System.out.println( "invoking method :" + method.getName() );
				method.invoke( classInstance );

				progress += random.nextInt( 10 );
				setProgress( Math.min( progress, 100 ) );
			}
			if (progress < 100) {
				setProgress(100);
			}
		}catch( Exception ie )
		{
			ie.printStackTrace();

		}
		return null;
	}

	/*
	 * Executed in event dispatching thread
	 */
	@Override
	public void done() {

	}
}