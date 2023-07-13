package com.moe.kvs.MOEKVSPDF.certification.util;

public class StaticMethod {
	
	public static String fetchGender(Object data) {
		
		if(data ==null) {
			return "";
		}else {
			return String.valueOf(data).equalsIgnoreCase("1")?"MALE":String.valueOf(data).equalsIgnoreCase("2")?"FEMALE":String.valueOf(data).equalsIgnoreCase("3")?"TRANSGENDER":"";	
		}	
	}
	
	public static String fetchStaffType(String data) {
		String str ="";
		switch (data) {
		case "1":
			 str = "TEACHING";
			break;
		case "2":
			 str = "NON-TEACHING";
			break;
		}
		return str;
		
	}
	
	public static String fetchMaritalStatus(String data) {
		String str ="";
		switch (data) {
		case "1":
			
			 str = "MARRIED";
			break;
		case "4":
			 str = "SINGLE";
			break;
		case "7":
			 str = "WIDOW/WINDOWER";
			break;
		}
		return str;
		
	}
	
	public static String fetchDisablityType(String data) {
		String str ="";
		switch (data) {
		case "1":
			 str = "VISUALLY HANDICAPPED";
			break;
		case "2":
			 str = "HEARING IMPAIRMENT";
			break;
		case "3":
			 str = "PHYSICALLY DISABLED";
			break;
		}
		return str;
		
	}


}
