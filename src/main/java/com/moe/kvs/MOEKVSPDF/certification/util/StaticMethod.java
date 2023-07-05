package com.moe.kvs.MOEKVSPDF.certification.util;

public class StaticMethod {
	
	public static String fetchGender(Object data) {
		
		if(data ==null) {
			return "";
		}else {
			return String.valueOf(data).equalsIgnoreCase("1")?"Male":String.valueOf(data).equalsIgnoreCase("2")?"Female":String.valueOf(data).equalsIgnoreCase("3")?"Transgender":"";	
		}	
	}
	
	public static String fetchStaffType(String data) {
		String str ="";
		switch (data) {
		case "1":
			 str = "Teaching";
			break;
		case "2":
			 str = "Non-Teaching";
			break;
		}
		return str;
		
	}
	
	public static String fetchMaritalStatus(String data) {
		String str ="";
		switch (data) {
		case "1":
			 str = "Married";
			break;
		case "4":
			 str = "Single";
			break;
		case "7":
			 str = "Widow/Widower";
			break;
		}
		return str;
		
	}
	
	public static String fetchDisablityType(String data) {
		String str ="";
		switch (data) {
		case "1":
			 str = "Visually Handicapped";
			break;
		case "2":
			 str = "Hearing Impairment";
			break;
		case "3":
			 str = "Physically Disabled";
			break;
		}
		return str;
		
	}


}
