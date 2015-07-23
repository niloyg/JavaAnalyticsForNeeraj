package com.predictedClass.evaluation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvFileCreation {
	
	public static String createCsvFile(List<double []> data, String pressure){
	
		String fileName= null;
		double avgVac=0.0;
		double avgBrk=0.0;
		
		try {
			fileName= "C:\\Users\\Abhijit\\sampleTest"+ System.currentTimeMillis()+".csv";
			BufferedWriter br= new BufferedWriter(new FileWriter(fileName,true));
			StringBuffer sb= new StringBuffer();
			sb.append("X,Y,Z,A,B,Pressure");
			sb.append("\r");
			for(double [] elements : data){
				
				for(int i=0;i< elements.length; i++){
					sb.append(elements[i]);
					sb.append(",");
				
				}
				//avgVac=getAvgVac("");
				sb.append(pressure);
				sb.append("\r");
			}
			
			br.write(sb.toString());
			br.flush();
			return fileName;
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}
			

	}
	
	
	
	public static void main(String args[]){
		List<double []> a= new ArrayList<double []>();
		double [] array={-0.100078,0.41442,0.86689,20,40};
		double [] array1={-0.100068,0.51442,0.96689,20,40};
		a.add(array);
		a.add(array1);
		
		try {
			BufferedWriter br= new BufferedWriter(new FileWriter("C:\\Users\\Abhijit\\sampleTest.csv",true));
			StringBuffer sb= new StringBuffer();
			sb.append("X,Y,Z,A,B,Pressure");
			sb.append("\r");
			for(double [] elements : a){
				
				for(int i=0;i< elements.length; i++){
					sb.append(elements[i]);
					sb.append(",");
				
				}
				sb.append("G");
				sb.append("\r");
			}
			
			br.write(sb.toString());
			br.flush();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}

}
