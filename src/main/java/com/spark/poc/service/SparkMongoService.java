package com.spark.poc.service;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;



public class SparkMongoService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4950331763744098030L;
	static MongoClient client = new  MongoClient("localhost",27017);

	public void insertRecords(List<Document> docList){
		MongoCollection<Document> coll = client.getDatabase("Rewards").getCollection("Customers");
		coll.insertMany(docList);
	}
	public void releaseConnection(){
		System.out.println("closing connections.......");
		client.close();
	}
}
