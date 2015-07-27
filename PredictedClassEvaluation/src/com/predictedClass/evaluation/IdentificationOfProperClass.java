package com.predictedClass.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.rosuda.JRI.RFactor;
import org.rosuda.JRI.Rengine;

import com.prediction.exception.PredictionBusinessException;

public class IdentificationOfProperClass{

	public static String PREDICTED_CLASS_INDICATOR="";
	public static String[] CallRMethodForPredictedClass(String testFilePath,String trainingFilePath, Rengine re) throws PredictionBusinessException,Exception{
		
			
			
			re.eval("training <- read.csv (file=" + "\"" + trainingFilePath + "\"" + ",head=TRUE,sep=\",\")");
		    re.eval("test1 <- read.csv (file=" + "\"" + testFilePath + "\"" + ",head=TRUE,sep=\",\")");
			
			//System.out.println(re.eval("print(test1)"));
			
			re.eval("library(ada)");
			re.eval("default=rpart.control()");
			re.eval("stump=rpart.control(cp=-1,maxdepth=1,minsplit=0)");
			re.eval("four=rpart.control(cp=-1,maxdepth=2,minsplit=0)");
		    re.eval("ada_67<-ada(Pressure~.,data=training,iter=100,loss=\"logistic\",type=\"discrete\",control=default)");
			re.eval("pred_67<-predict(ada_67,test1[,-6])");
			System.out.println("After predict");
			
			
			int countOfG=0;
			int countOfYR=0;
			int countOfY=0;
			int countOfR=0;
			double score=0.0;
			
			//System.out.println(x=re.eval("pred_67"));
			
			RFactor f=re.eval("pred_67").asFactor();
			//RVector v = re.eval("data.frame(predict(ada_67,test1[,-6]))").asVector();
			
			System.out.println(f);
			int length=f.size();
			System.out.println("Length :" + length);
			
		System.out.println(	f.at(30));
		
		if(f.at(0).equals(ClassIndicator.PREDICTED_CLASS_G_INDICATOR) || f.at(0).equals(ClassIndicator.PREDICTED_CLASS_YR_INDICATOR)){
			for(int i=0;i<length;i++){
			if(f.at(i).equals(ClassIndicator.PREDICTED_CLASS_G_INDICATOR)){
				countOfG++;
				
			}else{
				countOfYR++;
				
			}
			}
			
			System.out.println("countOfG :" + countOfG);
			System.out.println("countOfYR :" + countOfYR);
			if(countOfG>countOfYR){
				score=((double)countOfG/length)*100;
				System.out.println("Class G dominates with :" + score+"%");
				PREDICTED_CLASS_INDICATOR=ClassIndicator.PREDICTED_CLASS_G_INDICATOR;
			}else{
				score=((double)countOfYR/length)*100;
				System.out.println("Class YR dominates with :" + score+"%");
				PREDICTED_CLASS_INDICATOR=ClassIndicator.PREDICTED_CLASS_YR_INDICATOR;
			}
			
			
		}else if(f.at(0).equals(ClassIndicator.PREDICTED_CLASS_Y_INDICATOR) || f.at(0).equals(ClassIndicator.PREDICTED_CLASS_R_INDICATOR)){
			for(int i=0;i<length;i++){
				if(f.at(i).equals(ClassIndicator.PREDICTED_CLASS_Y_INDICATOR)){
					countOfY++;
					
				}else{
					countOfR++;
					
				}
				}
				
				System.out.println("countOfY :" + countOfY);
				System.out.println("countOfR :" + countOfR);
				if(countOfY>countOfR){
					score=((double)countOfY/length)*100;
					System.out.println("Class Y dominates with :" + score+"%");
					PREDICTED_CLASS_INDICATOR=ClassIndicator.PREDICTED_CLASS_Y_INDICATOR;
				}else{
					score=((double)countOfR/length)*100;
					System.out.println("Class R dominates with :" + score+"%");
					PREDICTED_CLASS_INDICATOR=ClassIndicator.PREDICTED_CLASS_R_INDICATOR;
				}
		}
			

			
			//re.eval("print(pred_67)");
			
			
			
			
			String [] predictedClass={PREDICTED_CLASS_INDICATOR};
			return predictedClass;
			
			
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary("jri");
		List <double []> listOfTestInputs = new ArrayList<double []>();
		double [] array1={-0.080907969,0.002040464,0.278699729,20,40};
		double [] array2={-0.090907969,0.003040474,0.378609729,20,40};
		listOfTestInputs.add(array1);
		listOfTestInputs.add(array2);
		String testFilePath= CsvFileCreation.createCsvFile(listOfTestInputs, ClassIndicator.PREDICTED_CLASS_X_INDICATOR).replace("\\", "/");
		System.out.println("Creating Rengine (with arguments)");
		 System.setProperty("java.library.path", "C:\\Program Files\\R\\R-3.2.1\\library\\rJava\\jri");
			
		
		 
		 String[] Rargs = {"--vanilla"};
			Rengine re = new Rengine(Rargs, false, new CallBackListerner());
			
			
			System.out.println("Rengine created, waiting for R");
			// the engine creates R is a new thread, so we should wait until it's
			// ready
			if (!re.waitForR()) {
			System.out.println("Cannot load R");
			throw new PredictionBusinessException("R can not be created now");
			}
		
		try {
			CallRMethodForPredictedClass(testFilePath,ClassIndicator.TRAINING_FILE_PATH_GREEN_YELLOWRED, re);
		} catch (PredictionBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
