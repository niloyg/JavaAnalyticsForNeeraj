package com.predictedClass.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import com.prediction.exception.PredictionBusinessException;
import com.sensorlytics.algo.ProcessEstimator;

public class ClassIndicator {
	
	public static JSONObject getPredictedClassInfo(HashMap<String, double []> hmR, HashMap<String, String> hmS) throws PredictionBusinessException, Exception{
		
		List<double []> listOfTestInputs=formCombinedHashMap(hmR,hmS);
		String [] predictedClass= GreenYellowBorder.CallRMethodForPredictedClass(listOfTestInputs);
		
		JSONObject predictedClassJson= new JSONObject();
		predictedClassJson.put("PredictedClass", predictedClass[0]);
		
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
		
		
	}

}
