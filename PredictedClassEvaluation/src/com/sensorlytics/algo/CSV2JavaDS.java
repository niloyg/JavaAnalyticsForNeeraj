package com.sensorlytics.algo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CSV2JavaDS {
           public static HashMap<String,String> csvToStringStringHashMap (String iFileName){
                //Form combined hm
                HashMap<String, String> hm = new HashMap<String, String>();
                try {
                                BufferedReader br = new BufferedReader(new FileReader(iFileName));

                                /*** In order to ignore the header row ****/
                                String line = br.readLine();
                                /*** In order to ignore the header row ****/

                                line = br.readLine();
                                while(line!=null) {
                                         String[] b = line.split(",");
                                         //System.out.println("The string is: "+b[0]);
                                         if (b.length > 1) {
                                                 String ts = b[0];
                                                 String value   = b[1];
                                                 hm.put(b[0], b[1]);
                                         }
                                         line = br.readLine();
                                }
                                br.close();
                        } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                System.out.println("size of the hm is: "+hm.size());
                return hm;
            }
           
           public static HashMap<String,double []> csvToStringDoubleHashMap (String iFileName){
               //Form combined hm
               HashMap<String, double []> hm = new HashMap<String, double []>();
               try {
                               BufferedReader br = new BufferedReader(new FileReader(iFileName));

                               /*** In order to ignore the header row ****/
                               String line = br.readLine();
                               /*** In order to ignore the header row ****/

                               line = br.readLine();
                               while(line!=null) {
                                        String[] b = line.split(",");
                                        //System.out.println("The string is: "+b[0]);
                                        if (b.length > 1) {
                                                String ts = b[0];
                                                double [] value   = {Double.parseDouble(b[1]),Double.parseDouble(b[2]),Double.parseDouble(b[3])};
                                                hm.put(b[0], value);
                                        }
                                        line = br.readLine();
                               }
                               br.close();
                       } catch (FileNotFoundException e) {
                               // TODO Auto-generated catch block
                               e.printStackTrace();
                       } catch (IOException e) {
                               // TODO Auto-generated catch block
                               e.printStackTrace();
                       }
               System.out.println("size of the hm is: "+hm.size());
               return hm;
           }


           public static void main(String[] args) {
                   String file = "../Utilities/22_06/pbByNN/cleanSTATE";
                   HashMap<String, String> hm = csvToStringStringHashMap(file);
                   //System.out.println("HashMap is:\n"+hm);
           }
}