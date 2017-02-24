package com.spark.poc.service;

import java.io.Serializable;
import java.util.Arrays;

public class CSVToJson implements Serializable{

	public static String convertCsvToJson(String[] strArr){
		StringBuilder sb = null;
		for(String st: strArr){
			sb = new StringBuilder();
			sb.append("{");
			sb.append('"'+"accountId"+'"');
			sb.append(":");
			sb.append('"'+strArr[0]+'"').append(",");
			sb.append('"'+"sortId"+'"');
			sb.append(":");
			sb.append('"'+strArr[1]+'"');
			sb.append('"'+"rewardsBalance"+'"');
			sb.append(":");
			sb.append('"'+strArr[2]+'"');
			sb.append('"'+"eligibilityIndicator"+'"');
			sb.append(":");
			sb.append('"'+strArr[3]+'"');
			sb.append("}");
		}
		return sb.toString();
	}
}
