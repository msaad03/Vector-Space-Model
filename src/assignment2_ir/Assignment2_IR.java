/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2_ir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.lang.Math;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import javafx.util.Pair;
import javax.swing.JTextArea;

/**
 *
 * @author M.Saad
 */

class result
{
    double rank;
    int id;
}


public class Assignment2_IR {
    
    static double alpha = 0.005;
  
    public static void index(String query, JTextArea area) throws FileNotFoundException, IOException
    {
        int i,j,sum=0;
        String line = "";
        String line1;
        String []afterTokenArray ;
        
        int count = new File("ShortStories\\").list().length;
        
        TreeMap<String,ArrayList<Integer>> tfMap = new TreeMap<String,ArrayList<Integer>>();
         
        TreeMap<String,ArrayList<Double>> tfMap1 = new TreeMap<String,ArrayList<Double>>();       
         
        TreeMap<String,ArrayList<Integer>> Map = new TreeMap<String,ArrayList<Integer>>();
         
        TreeMap <String,Double> idfMap = new TreeMap<String, Double>();
         
        TreeMap <String,ArrayList<Double>> tfidfMap = new TreeMap<String, ArrayList<Double>>();
         
        String [] stopWords = {"a","is","the","of","all","and","to","can","be","as","once","for","at","am","are","has","have","had","up","his","her","in","on","no","we","do"};
        List<String> list1 = Arrays.asList(stopWords);
        for(i=0;i<=(count-1);i++)
        {
            FileReader in = new FileReader("ShortStories\\" + (i+1) + ".txt");
            BufferedReader br = new BufferedReader(in);
            
            while ((line = br.readLine()) != null) 
            {
                line1 =line.replaceAll("\\.", "").replaceAll("\\&", "").replaceAll("\\‘", "").replaceAll("th", "").replaceAll("\\$", "").replaceAll("\\*", "").replaceAll("\\'", "").replaceAll("\\,", "").replaceAll("\\?", "").replaceAll("\\!", "").replaceAll("\\;", "").replaceAll("\\-", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("<", "").replaceAll(">", "").replaceAll("_", "").replaceAll("\"", "").replaceAll(":", "").replaceAll("\\(", "").replaceAll("\\)", "");
                line1 = line1.toLowerCase();
                afterTokenArray = line1.split("\\s+");
                for(j=0;j<afterTokenArray.length;j++)
                {
                    if(!(afterTokenArray[j].equals("")))
                    {
                        if(!(list1.contains(afterTokenArray[j])))
                        {
                            if(Map.containsKey(afterTokenArray[j]))
                            {
                                ArrayList list = Map.get(afterTokenArray[j]);
                                if(!list.contains(i))
                                list.add(i);
                                
                                
                                ArrayList tfListExisted = tfMap.get(afterTokenArray[j]);
                                int tf = tfMap.get(afterTokenArray[j]).get(i);
                               
                                tf++;
                                tfListExisted.set(i,tf);
                            }

                            else
                            { 
                                ArrayList<Integer> a = new ArrayList<Integer>();
                                a.add(i);
                                Map.put(afterTokenArray[j], a);
                                
                                tfMap.put(afterTokenArray[j], new ArrayList<Integer>(Collections.nCopies(count, 0)));
                                
                                tfMap.get(afterTokenArray[j]).set(i, 1);
                                
                                
                            }
                        }      
                    }
                
                }
               
            }
        }
        
        for (Map.Entry<String,ArrayList<Integer>> entry : tfMap.entrySet()) 
        {
            ArrayList tfList = tfMap.get(entry.getKey());
            ArrayList <Double> logtf = new ArrayList<Double>();
            
            double ans;
            for(int k=0; k< tfList.size(); k++)
            {
                if(entry.getValue().get(k) != 0)
                {
                     ans = 1 + Math.log10(entry.getValue().get(k));
                }
                
                else
                {
                    ans =0;
                }
                
                logtf.add(k, ans);
                
            }
            
            tfMap1.put(entry.getKey(), logtf);
            
        }
        
        for (Map.Entry<String,ArrayList<Integer>> entry : Map.entrySet()) 
        {
            ArrayList dfList = Map.get(entry.getKey());
            
            double idf= Math.log10((double)count/dfList.size());
            
            
            idfMap.put(entry.getKey(), idf);
            
        }
        
        BufferedWriter bufferWriter = null;
        FileWriter fileCursor = null;
        String FILENAME = "idf.txt";
        
	fileCursor = new FileWriter(FILENAME);
	bufferWriter = new BufferedWriter(fileCursor);
	 
        for (Map.Entry<String, Double> entry : idfMap.entrySet()) 
        {
            bufferWriter.write(entry.getKey() + "\t" + entry.getValue());
            bufferWriter.newLine();
            sum++;
            bufferWriter.flush();  
        }
        
        bufferWriter.close();
        
        System.out.println();
        
        for(int k=0; k<count; k++)
        {
            for (Map.Entry<String, Double> entry : idfMap.entrySet()) 
            {
                
                if(!tfidfMap.containsKey(entry.getKey()))
                {
                    ArrayList list = tfMap1.get(entry.getKey());
                    
                    double ans = (double)list.get(k) * idfMap.get(entry.getKey());
                    
                    ArrayList<Double> tfidfList = new ArrayList<Double>();
                    
                    tfidfList.add(k,ans);
                
                    tfidfMap.put(entry.getKey(), tfidfList);
                
                }
                else
                {
                    
                    ArrayList list = tfMap1.get(entry.getKey());
                    double ans = (double)list.get(k) * idfMap.get(entry.getKey());
                    
                    ArrayList list2 = tfidfMap.get(entry.getKey());
                    
                    list2.add(k,ans);
                    
                }
                                
            } 
        }
        
        bufferWriter = null;
        fileCursor = null;
        FILENAME = "tfidf.txt";
        
	fileCursor = new FileWriter(FILENAME);
	bufferWriter = new BufferedWriter(fileCursor);
	 
        for (Map.Entry<String, ArrayList<Double>> entry : tfidfMap.entrySet()) 
        {
            bufferWriter.write(entry.getKey() + "\t" + entry.getValue());
            bufferWriter.newLine();
            sum++;
            bufferWriter.flush();  
        }
 
            bufferWriter.close();
        
        double docMagnitude [] = new double[count];
        Arrays.fill(docMagnitude, 0.0);
        
        for(int k=0; k< count; k++)
        {
            for (Map.Entry<String, ArrayList<Double>> entry : tfidfMap.entrySet()) 
            {
                docMagnitude[k] += Math.pow(entry.getValue().get(k),2);
            }
            
            docMagnitude[k] = Math.sqrt(docMagnitude[k]);
        }
        
//        for(int k=0;k<docMagnitude.length;k++)
//        {
//            System.out.println((k+1) + " " + docMagnitude[k]);
//        }
//        
//        
//        
//        
//        //Query Processing
//        
         TreeMap<String,Integer> querytfMap = new TreeMap<String,Integer>();
         
         TreeMap<String,Double> querylogtfMap = new TreeMap<String,Double>();       
         
         TreeMap<String,ArrayList<Integer>> queryMap = new TreeMap<String,ArrayList<Integer>>();
         
         TreeMap <String,Double> querytfidfMap = new TreeMap<String, Double>();
         
         query = query.toLowerCase();
         
         query =query.replaceAll("\\.", "").replaceAll("\\—", "").replaceAll("\\’", "").replaceAll("\\&", "").replaceAll("\\‘", "").replaceAll("\\’", "").replaceAll("th", "").replaceAll("\\$", "").replaceAll("\\*", "").replaceAll("\\'", "").replaceAll("\\,", "").replaceAll("\\?", "").replaceAll("\\!", "").replaceAll("\\;", "").replaceAll("\\-", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("<", "").replaceAll(">", "").replaceAll("_", "").replaceAll("\"", "").replaceAll(":", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\/", "");
        
         String [] simpleQuery;
        
         simpleQuery = query.split(" ");
         
            for (Map.Entry<String, ArrayList<Double>> entry : tfidfMap.entrySet()) 
            {
                querytfMap.put(entry.getKey(), 0);
            }
            int check = 0;
            for(int ii=0; ii<simpleQuery.length; ii++)
            {
                if(querytfMap.containsKey(simpleQuery[ii]))
                {
                    int tf = querytfMap.get(simpleQuery[ii]);
                    tf++;
                    
                    querytfMap.put(simpleQuery[ii], tf);
                }
                
                else
                {
                    check++;
                    continue;
                }
                
            }
            
            if(check == simpleQuery.length)
            {
                 return;
            }
             
            for (Map.Entry<String,ArrayList<Integer>> entry : tfMap.entrySet()) 
            {
                int tf = querytfMap.get(entry.getKey());
                
                double tf1 = (double)tf;
                
                if(tf1 == 0.0)
                {
                    querylogtfMap.put(entry.getKey(), 0.0);
                }
                
                else
                {
                    querylogtfMap.put(entry.getKey(), (1+ Math.log10(tf1)));
                }
                
            }
            
            for (Map.Entry<String, Double> entry : querylogtfMap.entrySet()) 
            {
                double idf = idfMap.get(entry.getKey());
                double tf = querylogtfMap.get(entry.getKey());
                
                querytfidfMap.put(entry.getKey(), tf*idf);
                
            }
            double queryMagnitude=0.0;
            
            for (Map.Entry<String, Double> entry : querytfidfMap.entrySet()) 
            {
                queryMagnitude += Math.pow(entry.getValue(),2);
            }
            
            queryMagnitude = Math.sqrt(queryMagnitude);
            
            System.out.println();
            class Rank 
            {
                public double rank;
                public int id;;
            }
            
            class sorted implements Comparator<Rank>
            {

                @Override
                public int compare(Rank o1, Rank o2) 
                {
                    if(o1.rank > o2.rank)
                        return -1;
                    
                    if(o1.rank < o2.rank)
                        return 1;
                    
                    return 0;
                    
                    
                }
                
            }
            
            Rank ranks[] = new Rank[count];
            
            double numerator=0;
            
            for(int k=0; k<count; k++)
            {
                numerator=0;
                for (Map.Entry<String, Double> entry : querytfidfMap.entrySet()) 
                {
                    numerator += ( ( tfidfMap.get(entry.getKey()).get(k) )* ( querytfidfMap.get(entry.getKey()) ) );
                }
                
                ranks[k] = new Rank();
                ranks[k].rank = numerator / (queryMagnitude * docMagnitude[k]);
                ranks[k].id = k+1;
                
            }
            
            Arrays.sort(ranks, new  sorted());
            for(int kk=0;kk<ranks.length;kk++)
            {
                if(ranks[kk].rank > alpha)
                {
                    area.append("Doc " + ranks[kk].id + " - " + ranks[kk].rank + "\n");
                }
            }
           // System.out.println(queryMagnitude);    
    }

//    public static void main(String[] args) throws IOException 
//    {
//        String query;
//        
//        Scanner obj = new Scanner(System.in);
//        
//        int count = new File("ShortStories\\").list().length;
//                
//        
//        System.out.println("Enter Query  : ");
//        query = obj.nextLine();
//        
//        File f = new File("tfidf.txt");
//        File f1 = new File("idf.txt");
//        if(f.exists() && !f.isDirectory() && f1.exists() && !f1.isDirectory()) 
//        {
//            TreeMap<String,ArrayList<Double>> tfidfMap = new TreeMap<String,ArrayList<Double>>();
//
//            FileReader in = new FileReader("tfidf.txt");
//            BufferedReader br = new BufferedReader(in);
//            String line ;
//            String line1;
//            String afterTokenArray[];
//            double arr;
//       
//            
//            while ((line = br.readLine()) != null) 
//            {
//                line1 =line.replaceAll("\\&", "").replaceAll("\\‘", "").replaceAll("th", "").replaceAll("\\$", "").replaceAll("\\*", "").replaceAll("\\'", "").replaceAll("\\,", "").replaceAll("\\?", "").replaceAll("\\!", "").replaceAll("\\;", "").replaceAll("\\-", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("<", "").replaceAll(">", "").replaceAll("_", "").replaceAll("\"", "").replaceAll(":", "").replaceAll("\\(", "").replaceAll("\\)", "");
//         
//                afterTokenArray = line1.split("\\s+");
//                
//                ArrayList<Double> list = new ArrayList<Double>();
//                for(int j=1;j<afterTokenArray.length;j++)
//                {
//                    arr=Double.parseDouble(afterTokenArray[j]);
//                    
//                    list.add(arr);
//                }
//                
//                tfidfMap.put(afterTokenArray[0], list);
//                
//            }
//            
//        double docMagnitude [] = new double[count];
//        Arrays.fill(docMagnitude, 0.0);
//        
//        for(int k=0; k< count; k++)
//        {
//            for (Map.Entry<String, ArrayList<Double>> entry : tfidfMap.entrySet()) 
//            {
//                docMagnitude[k] += Math.pow(entry.getValue().get(k),2);
//            }
//            
//            docMagnitude[k] = Math.sqrt(docMagnitude[k]);
//        }
//            
//            TreeMap<String,Double> idfMap = new TreeMap<String,Double>();
//
//            in = new FileReader("idf.txt");
//            br = new BufferedReader(in);
//            double arr1;
//            
//            while ((line = br.readLine()) != null) 
//            {
//                line1 =line.replaceAll("\\&", "").replaceAll("\\‘", "").replaceAll("th", "").replaceAll("\\$", "").replaceAll("\\*", "").replaceAll("\\'", "").replaceAll("\\,", "").replaceAll("\\?", "").replaceAll("\\!", "").replaceAll("\\;", "").replaceAll("\\-", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("<", "").replaceAll(">", "").replaceAll("_", "").replaceAll("\"", "").replaceAll(":", "").replaceAll("\\(", "").replaceAll("\\)", "");
//         
//                afterTokenArray = line1.split("\\s+");
//                
//                arr1=Double.parseDouble(afterTokenArray[1]);
//                   
//                idfMap.put(afterTokenArray[0], arr1);
//                
//            }
//            //Query Processing
////        
//         TreeMap<String,Integer> querytfMap = new TreeMap<String,Integer>();
//         
//         TreeMap<String,Double> querylogtfMap = new TreeMap<String,Double>();       
//         
//         TreeMap<String,ArrayList<Integer>> queryMap = new TreeMap<String,ArrayList<Integer>>();
//         
//         TreeMap <String,Double> querytfidfMap = new TreeMap<String, Double>();
//         
//         query = query.toLowerCase();
//         
//         query =query.replaceAll("\\.", "").replaceAll("\\—", "").replaceAll("\\’", "").replaceAll("\\&", "").replaceAll("\\‘", "").replaceAll("\\’", "").replaceAll("th", "").replaceAll("\\$", "").replaceAll("\\*", "").replaceAll("\\'", "").replaceAll("\\,", "").replaceAll("\\?", "").replaceAll("\\!", "").replaceAll("\\;", "").replaceAll("\\-", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("<", "").replaceAll(">", "").replaceAll("_", "").replaceAll("\"", "").replaceAll(":", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\/", "");
//        
//         String [] simpleQuery;
//        
//         simpleQuery = query.split(" ");
//         
//            for (Map.Entry<String, ArrayList<Double>> entry : tfidfMap.entrySet()) 
//            {
//                querytfMap.put(entry.getKey(), 0);
//            }
//            int check = 0;
//            for(int ii=0; ii<simpleQuery.length; ii++)
//            {
//                if(querytfMap.containsKey(simpleQuery[ii]))
//                {
//                    int tf = querytfMap.get(simpleQuery[ii]);
//                    tf++;
//                    
//                    querytfMap.put(simpleQuery[ii], tf);
//                }
//                
//                else
//                {
//                    check++;
//                    continue;
//                }
//                
//            }
//            
//            if(check == simpleQuery.length)
//            {
//                 return;
//            }
//             
//            for (Map.Entry<String,ArrayList<Double>> entry : tfidfMap.entrySet()) 
//            {
//                int tf = querytfMap.get(entry.getKey());
//                
//                double tf1 = (double)tf;
//                
//                if(tf1 == 0.0)
//                {
//                    querylogtfMap.put(entry.getKey(), 0.0);
//                }
//                
//                else
//                {
//                    querylogtfMap.put(entry.getKey(), (1+ Math.log10(tf1)));
//                }
//                
//            }
//            
//            for (Map.Entry<String, Double> entry : querylogtfMap.entrySet()) 
//            {
//                double idf = idfMap.get(entry.getKey());
//                double tf = querylogtfMap.get(entry.getKey());
//                
//                querytfidfMap.put(entry.getKey(), tf*idf);
//                
//            }
//            double queryMagnitude=0.0;
//            
//            for (Map.Entry<String, Double> entry : querytfidfMap.entrySet()) 
//            {
//                queryMagnitude += Math.pow(entry.getValue(),2);
//            }
//            
//            queryMagnitude = Math.sqrt(queryMagnitude);
//      
//           // System.out.println(queryMagnitude);
//            class Rank 
//            {
//                public double rank;
//                public int id;;
//            }
//            
//            class sorted implements Comparator<Rank>
//            {
//
//                @Override
//                public int compare(Rank o1, Rank o2) 
//                {
//                    if(o1.rank > o2.rank)
//                        return -1;
//                    
//                    if(o1.rank < o2.rank)
//                        return 1;
//                    
//                    return 0;
//                    
//                    
//                }
//                
//            }
//            
//            Rank ranks[] = new Rank[count];
//            
//            double numerator=0;
//            
//            for(int k=0; k<count; k++)
//            {
//                numerator=0;
//                for (Map.Entry<String, Double> entry : querytfidfMap.entrySet()) 
//                {
//                    numerator += ( ( tfidfMap.get(entry.getKey()).get(k) )* ( querytfidfMap.get(entry.getKey()) ) );
//                }
//                
//                ranks[k] = new Rank();
//                ranks[k].rank = numerator / (queryMagnitude * docMagnitude[k]);
//                ranks[k].id = k+1;
//                
//            }
//            
//            Arrays.sort(ranks, new  sorted());
//            for(int kk=0;kk<ranks.length;kk++)
//            {
//                if(ranks[kk].rank > alpha)
//                {
//                    System.out.println("Doc " + ranks[kk].id + " - " + ranks[kk].rank);
//                }
//            }
//            
//        }
//        
//        else
//        {
//            index(query);
//        }
//    }

   
    
}
