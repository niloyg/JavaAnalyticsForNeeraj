package com.predictedClass.evaluation;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class YellowRedBorder{

	static {
		System.loadLibrary("jri");
		System.loadLibrary("plyr");
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Creating Rengine (with arguments)");
		 System.setProperty("java.library.path", "C:\\Program Files\\R\\R-3.2.1\\library\\rJava\\jri");
			//If not started with --vanilla, funny things may happen in this R shell.
		
		 
		 String[] Rargs = {"--vanilla"};
			Rengine re = new Rengine(Rargs, false, new CallBackListerner());
			System.out.println("Rengine created, waiting for R");
			// the engine creates R is a new thread, so we should wait until it's
			// ready
			if (!re.waitForR()) {
			System.out.println("Cannot load R");
			return;
			}
			
			re.eval("training <- read.csv (file=\"E://data for testing//training3.csv\",head=TRUE,sep=\",\")");
			re.eval("test1 <- read.csv (file=\"E://data for testing//Test2YR.csv\",head=TRUE,sep=\",\")");
			
			
			re.eval("library(plyr)");
			re.eval("count(training, \"Pressure\")");
			re.eval("count(test1, \"Pressure\")");
			re.eval("library(ada)");
			re.eval("default=rpart.control()");
			re.eval("stump=rpart.control(cp=-1,maxdepth=1,minsplit=0)");
			
			re.eval("four=rpart.control(cp=-1,maxdepth=2,minsplit=0)");
			
			long startTime=System.currentTimeMillis();
			System.out.println("Ada Start Time : " + startTime);
			re.eval("ada_green_yellow<-ada(Pressure~.,data=training,iter=100,loss=\"discrete\",type=\"discrete\",control=default)");
			long endTime=System.currentTimeMillis();
			System.out.println("Ada End Time : " + endTime);
			
			System.out.println("Time taken by Ada process in sec :" + (endTime-startTime)/1000);
			re.eval("print(ada_green_yellow)");
			System.out.println("After Ada call");
			System.out.println("Before predict call");
			re.eval("pred_green_yellow <- predict(ada_green_yellow,test1[,-6])");
			//re.eval("print(pred_green_yellow)");
			System.out.println("After predict call");
			REXP result=re.eval("table(test1$Pressure, pred_green_yellow)");
			
			
			double[] foreCast=result.asDoubleArray();
			//double[][] foreCast=result.asDoubleMatrix();
			System.out.println(foreCast);
			
			for (int i = 0; i < foreCast.length  ; i++) {
	            System.out.println(foreCast[i]);
	            } 
			System.out.println("Result :"+ result);
			//And that's it! Easy, huh?
			re.end();
			
			System.out.println("Bye.");
			
			
	}

}
