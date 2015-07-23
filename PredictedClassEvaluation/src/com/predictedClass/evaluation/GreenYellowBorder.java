package com.predictedClass.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import com.prediction.exception.PredictionBusinessException;

public class GreenYellowBorder{

	public final static String PREDICTED_CLASS_G_INDICATOR="G";
	public final static String PREDICTED_CLASS_Y_INDICATOR="Y";
	public final static String PREDICTED_CLASS_R_INDICATOR="R";
	public final static String JAVA_LIBRARY_PATH="C:\\Program Files\\R\\R-3.2.1\\library\\rJava\\jri";
	public final static String TRAINING_FILE_PATH="E://data for testing//training.csv";
	
	static {
		System.loadLibrary("jri");
		System.loadLibrary("plyr");
		
	}
	
	public static String[] CallRMethodForPredictedClass(List <double[]> listOfTestInputs) throws PredictionBusinessException,Exception{
		System.out.println("Creating Rengine (with arguments)");
		 System.setProperty("java.library.path", JAVA_LIBRARY_PATH);
			//If not started with --vanilla, funny things may happen in this R shell.
		
		 
		 String[] Rargs = {"--vanilla"};
			Rengine re = new Rengine(Rargs, false, new CallBackListerner());
			System.out.println("Rengine created, waiting for R");
			// the engine creates R is a new thread, so we should wait until it's
			// ready
			if (!re.waitForR()) {
			System.out.println("Cannot load R");
			throw new PredictionBusinessException("R can not be created now");
			}
			
			
			String filePath= CsvFileCreation.createCsvFile(listOfTestInputs, PREDICTED_CLASS_G_INDICATOR).replace("\\", "/");
			
			re.eval("training <- read.csv (file=" + "\"" + TRAINING_FILE_PATH + "\"" + ",head=TRUE,sep=\",\")");
		    re.eval("test1 <- read.csv (file=" + "\"" + filePath + "\"" + ",head=TRUE,sep=\",\")");
			
			System.out.println(re.eval("print(test1)"));
			re.eval("library(plyr)");
			re.eval("count(training, \"Pressure\")");
			re.eval("count(test1, \"Pressure\")");
			re.eval("library(ada)");
			re.eval("default=rpart.control()");
			re.eval("stump=rpart.control(cp=-1,maxdepth=1,minsplit=0)");
			
			re.eval("four=rpart.control(cp=-1,maxdepth=2,minsplit=0)");
			System.out.println("Before ada call");
			re.eval("ada_green_yellow<-ada(Pressure~.,data=training,iter=100,loss=\"discrete\",type=\"discrete\",control=default)");
			re.eval("print(ada_green_yellow)");
			System.out.println("After Ada call");
			System.out.println("Before predict call");
			re.eval("pred_green_yellow <- predict(ada_green_yellow,test1[,-6])");
			re.eval("print(pred_green_yellow)");
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
			String [] predictedClass={PREDICTED_CLASS_G_INDICATOR};
			return predictedClass;
			
			
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List <double []> listOfTestInputs = new ArrayList<double []>();
		double [] array1={-0.080907969,0.002040464,0.278699729,20,40};
		double [] array2={-0.090907969,0.003040474,0.378609729,20,40};
		listOfTestInputs.add(array1);
		listOfTestInputs.add(array2);
		try {
			CallRMethodForPredictedClass(listOfTestInputs);
		} catch (PredictionBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
