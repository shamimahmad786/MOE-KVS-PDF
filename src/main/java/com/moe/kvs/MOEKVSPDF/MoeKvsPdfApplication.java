package com.moe.kvs.MOEKVSPDF;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moe.kvs.MOEKVSPDF.beans.MailBean;
import com.moe.kvs.MOEKVSPDF.resource.NativeRepository;
import com.moe.kvs.MOEKVSPDF.resource.QueryResult;

@SpringBootApplication
@EnableScheduling
public class MoeKvsPdfApplication {
	
	@Autowired
	NativeRepository nativeRepository;

	public static void main(String[] args) {
		SpringApplication.run(MoeKvsPdfApplication.class, args);
	}

	
	
	 @Scheduled(fixedDelay = 3000)
	  public void update() throws InterruptedException {
		  
		  String query="select * from backup.spouse_emp_prob where verify_flag !='S' and verify_flag !='L' limit 5";
		 QueryResult qs= nativeRepository.executeQueries(query);
		   RestTemplate rest=new RestTemplate();
		   System.out.println(qs.getRowValue());
		 if(qs.getRowValue().size()>0) {
			 for(int i=0;i<qs.getRowValue().size();i++) {
			 nativeRepository.updateQueries("update backup.spouse_emp_prob set verify_flag='L' where teacher_mobile='"+qs.getRowValue().get(i).get("teacher_mobile")+"'");
			    String jsonInString =null;
			 try {
		        	MailBean obj=new MailBean();
		        	HttpHeaders headers = new HttpHeaders();
		        	obj.setApplicationName("KVS Teacher");
		        	obj.setApplicationId("1");
		        	obj.setAttachmentYn(0);
		        	System.out.println(String.valueOf(qs.getRowValue().get(i).get("success")));
		        	if(String.valueOf(qs.getRowValue().get(i).get("success")).equalsIgnoreCase("0")) {
		        		
		        		obj.setSubject("Incorrect Spouse Station || KVS Online Transfer Application");	
		        		obj.setContent("Some discrepancy has been found in your data. Kindly correct your spouse station and get it verified from school login at the earliest. \n"
			        			+ "\r\n"
			        			+ "Please do not reply to this email as this is a system-generated email.");
			        	
		        	}else {
		        		System.out.println("In else condition");
		        		obj.setSubject("Spouse Station Corrected || KVS Online Transfer Application");
		        		obj.setContent("Your spouse station has been corrected on KVS online transfer portal . \n"
			        			+ "\r\n"
			        			+ "Please do not reply to this email as this is a system-generated email.");
			        	
		        	}
		        	
		        	obj.setAttachmentPath("xyz");
		        	obj.setEmailTo(String.valueOf(qs.getRowValue().get(i).get("teacher_email")));
		        	obj.setSignature("Dear "+String.valueOf(String.valueOf(qs.getRowValue().get(i).get("teacher_name"))).split(" ")[0]);
		        	

		        	
		        	
		        	obj.setClosing("NIC - Minstry of Education");
		        	obj.setEmailTemplateId("MSG-5836");
		        	obj.setEmailCc("nicsupport-edu@gov.in");
		        	ObjectMapper mapper = new ObjectMapper();
		        	 jsonInString = mapper.writeValueAsString(obj);
		        	 
		        	 
		        	 System.out.println("jsonInString--->"+jsonInString);
		        	 
		             HttpEntity<String> request = new HttpEntity<String>(jsonInString,headers);           
		             try {
		             String url = "http://10.247.141.227:8080/ME-RAD-MESSAGE/api/sendCorrectionMessage";
		             rest.exchange(url, HttpMethod.POST, request,Map.class,1);	
		             nativeRepository.updateQueries("update backup.spouse_emp_prob set verify_flag='S' where teacher_mobile='"+qs.getRowValue().get(i).get("teacher_mobile")+"'");
		             }catch(Exception ex) {
		            	 nativeRepository.updateQueries("update backup.spouse_emp_prob set verify_flag='E' where teacher_mobile='"+qs.getRowValue().get(i).get("teacher_mobile")+"'");
		             	ex.printStackTrace();
		             }

		        }catch(Exception ex) {
		        	nativeRepository.updateQueries("update backup.spouse_emp_prob set verify_flag='E' where teacher_mobile='"+qs.getRowValue().get(i).get("teacher_mobile")+"'");
		        	ex.printStackTrace();
		        }
			 }
		 }
	      
	  }
	
	
}
