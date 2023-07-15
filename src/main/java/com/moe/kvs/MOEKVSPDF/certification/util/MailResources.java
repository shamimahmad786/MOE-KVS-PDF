package com.moe.kvs.MOEKVSPDF.certification.util;

import com.moe.kvs.MOEKVSPDF.beans.MailBean;

public class MailResources {

	
	public void sendProfileConfirmationByTeacher(String email,String firstname,String mobile,String attachmentpath) {
		
		try {
								MailBean obj = new MailBean();
								obj.setApplicationName("Kvs Teacher");
								obj.setApplicationId("1");
								obj.setEmailTemplateId("MSG-5836");
								obj.setEmailTo(email);
								obj.setSubject("Teacher Transfer Module- Profile Confirmation by Teacher");
								obj.setSignature("Dear " + firstname);
								obj.setContent(" You have confirm profile data in Kvs teacher transfer module ");
								obj.setClosing("KVS Headquarter");
								obj.setMobile(mobile);
								obj.setUserid("");
								obj.setName(firstname);
								obj.setAttachmentYn(null);
								obj.setAttachmentPath(null);

//								restMailService.getPostsPlainJSON(obj); // temporary commented for local only
								
								
							} catch (Exception ex) {
								ex.printStackTrace();
							}  
	}
	
}
