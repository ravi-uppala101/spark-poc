package com.spark.poc.service;

import static spark.Spark.get;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.bson.Document;

import spark.Request;
import spark.Response;
import spark.Route;

public class SparkApplicationDemo {
	JavaSparkContext sc;
	public static void main(String[] args) {
		SparkApplicationDemo demo = new SparkApplicationDemo();
		get(new Route("/insert") {
	        
			@Override
	        public Object handle(Request request, Response response) {
				long stratTime = System.currentTimeMillis();
				System.out.println("insert called..........");
				demo.processData();
				demo.storeDataToMongo();
				
				long endTime = System.currentTimeMillis();
				System.out.println("total time taken : " +(endTime-stratTime)/1000+ "  secs");
	        	return "Inserted the documents into mongodb";
            }
        });

	}
	
	public void storeDataToMongo(){
		try {
			List<Document> list1 = new ArrayList<Document>();
			sc = new JavaSparkContext(new SparkConf().setMaster("local[*]").setAppName("Spark"));
			JavaRDD<String> transactions = sc.textFile("/temp/csv/customer.json");
			SparkMongoService service = new SparkMongoService();
			transactions.foreachPartition(x->{
				while(x.hasNext()){
					try{
						list1.add(Document.parse(x.next()));
						if(list1.size() == 60000){
							service.insertRecords(list1);
							list1.clear();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				System.out.println("calline last time --  " +list1.size());
				service.insertRecords(list1);
			});
			System.out.println("calling the releasee connections......");
			service.releaseConnection();
			sc.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void processData(){
		  try {
			@SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(new FileReader("/temp/csv/customer.txt"));
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/temp/csv/customer.json")));
			String line = null;
			
				while((line=bufferedReader.readLine()) != null){
					writer.write(CSVToJson.convertCsvToJson(line.split(",")));
					writer.write("\n");
				}
			 writer.flush();
			 writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	}

}
