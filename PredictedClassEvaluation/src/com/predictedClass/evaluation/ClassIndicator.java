package com.predictedClass.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.rosuda.JRI.Rengine;

import com.prediction.exception.PredictionBusinessException;
import com.sensorlytics.algo.CSV2JavaDS;
import com.sensorlytics.algo.ProcessEstimator;

public class ClassIndicator {
	public final static String PREDICTED_CLASS_G_INDICATOR="G";
	public final static String PREDICTED_CLASS_Y_INDICATOR="Y";
	public final static String PREDICTED_CLASS_YR_INDICATOR="YR";
	public final static String PREDICTED_CLASS_R_INDICATOR="R";
	public final static String PREDICTED_CLASS_X_INDICATOR="X";
	public final static String TRAINING_FILE_PATH_GREEN_YELLOWRED="E://data for testing//training.csv";
	public final static String TRAINING_FILE_PATH_YELLOW_RED="E://data for testing//training3.csv";
	public final static String JAVA_LIBRARY_PATH="C:\\Program Files\\R\\R-3.2.1\\library\\rJava\\jri";
	
	
	static {
		System.loadLibrary("jri");
		
		
	}
	
	
	public static JSONObject getPredictedClassInfo(HashMap<String, double []> hmR, HashMap<String, String> hmS) throws PredictionBusinessException, Exception{
		
		System.out.println("Creating Rengine (with arguments)");
		 System.setProperty("java.library.path", JAVA_LIBRARY_PATH);
			
		
		 
		 String[] Rargs = {"--vanilla"};
			Rengine re = new Rengine(Rargs, false, new CallBackListerner());
			
			
			System.out.println("Rengine created, waiting for R");
			// the engine creates R is a new thread, so we should wait until it's
			// ready
			if (!re.waitForR()) {
			System.out.println("Cannot load R");
			throw new PredictionBusinessException("R can not be created now");
			}
		
		
		JSONObject predictedClassJson= new JSONObject();
		List<double []> listOfTestInputs=formCombinedHashMap(hmR,hmS);
		String testFilePath= CsvFileCreation.createCsvFile(listOfTestInputs, PREDICTED_CLASS_X_INDICATOR).replace("\\", "/");
		String [] predictedClassOfGorYR= IdentificationOfProperClass.CallRMethodForPredictedClass(testFilePath,TRAINING_FILE_PATH_GREEN_YELLOWRED, re);
		if(predictedClassOfGorYR[0].equals("G")){
			predictedClassJson.put("PredictedClass", predictedClassOfGorYR[0]);
		}else{
			String [] predictedClassOfYorR= IdentificationOfProperClass.CallRMethodForPredictedClass(testFilePath,TRAINING_FILE_PATH_YELLOW_RED, re);
			predictedClassJson.put("PredictedClass", predictedClassOfYorR[0]);
		}
		
		re.end();
		
		System.out.println("Bye.");
		
	   File f1= new File(testFilePath);
	   boolean deletionStatus= f1.delete();
	   if(deletionStatus){
		   System.out.println("Temp File deleted Succesfully");
	   }
		
		return predictedClassJson;
		
	}
	
	
	public static List<double []> formCombinedHashMap (HashMap<String, double []> hmR, HashMap<String, String> hmS) {
		List<double []> listOfTestRecods= new ArrayList<double []> (); 
		double [] avgVacAndBreak = ProcessEstimator.getModeVacAnBrk(hmS);
		Set setR = hmR.keySet();
		List<String> keyListR = new ArrayList<String>(setR);
		Collections.sort(keyListR);
		Iterator itr = keyListR.iterator();
		
		while(itr.hasNext()){
			Object key =  itr.next();
			if (hmS.containsKey(key)) {
				double values[]=hmR.get(key);
				double X= values[0];
				double Y= values[1];
                double Z= values[2];
				double [] arrayOfTestData= {X,Y,Z,avgVacAndBreak[0],avgVacAndBreak[1]};
				
				listOfTestRecods.add(arrayOfTestData);
			}
		}
		return listOfTestRecods;
	}
	
	public static void main(String args[]){
		
		try {
			System.out.println(getPredictedClassInfo(CSV2JavaDS.csvToStringDoubleHashMap("E:\\data for testing\\Test1YRtesting.csv"),CSV2JavaDS.csvToStringStringHashMap("E:\\data for testing\\cleanSTATE.csv")));
		} catch (PredictionBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
