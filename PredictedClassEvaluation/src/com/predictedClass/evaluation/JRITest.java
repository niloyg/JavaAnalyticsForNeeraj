package com.predictedClass.evaluation;

import java.io.File;
import java.io.InputStreamReader;


import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class JRITest {

	static {
		System.loadLibrary("jri");
		System.loadLibrary("plyr");
		
	}
	
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
		//In R, call rnorm(4), which generates 4 numbers from a standard
		//normal distribution.
		//The result will be stored in re.
		REXP rn = re.eval("rnorm(4)");
		
		double[] rnd = rn.asDoubleArray();
		//Let's see the variables.
		for(int i=0; i<rnd.length; i++)
		System.out.print(rnd[i] + " ");
		System.out.println();
		
		
		re.eval("pump67 <- read.csv (file=\"C://Users//Abhijit//Classification67vacbreak.csv\",head=TRUE,sep=\",\")");
		
		re.eval("smp <- floor(0.7 * nrow(pump67)) ");
		re.eval("set.seed(123)");
		re.eval("train_ind <- sample(seq_len(nrow(pump67)), size = smp)");
		re.eval("train <- pump67[train_ind, ]");
		re.eval("test <- pump67[-train_ind, ]");
		re.eval("print(test)");
		re.eval("library(plyr)");
		re.eval("count(train, \"Pressure\")");
		re.eval("count(test, \"Pressure\")");
		re.eval("library(ada)");
		re.eval("default=rpart.control()");
		re.eval("stump=rpart.control(cp=-1,maxdepth=1,minsplit=0)");
		re.eval("print(stump)");
		re.eval("four=rpart.control(cp=-1,maxdepth=2,minsplit=0)");
		re.eval("print(four)");
		
		System.out.println("Before Ada call");
		re.eval("ada_67<-ada(Pressure~.,data=train,iter=100,loss=\"discrete\",type=\"discrete\",control=default)");
		re.eval("print(ada_67)");
		System.out.println("After Ada call");
		System.out.println("Before predict call");
		re.eval("pred_67 <- predict(ada_67,test[,-6])");
		re.eval("print(pred_67)");
		System.out.println("After predict call");
		REXP result=re.eval("table(test$Pressure, pred_67)");
		
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
