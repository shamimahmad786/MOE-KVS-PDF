package com.moe.kvs.MOEKVSPDF.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate;

//import com.example.MOERADTEACHER.util.QueryResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.moe.kvs.MOEKVSPDF.beans.ExprienceBean;
import com.moe.kvs.MOEKVSPDF.beans.MiscelaneousBean;
import com.moe.kvs.MOEKVSPDF.beans.ReportBeans;
import com.moe.kvs.MOEKVSPDF.certification.util.CertificationGenerateUtil;
import com.moe.kvs.MOEKVSPDF.resource.Header;
import com.moe.kvs.MOEKVSPDF.resource.NativeRepository;
import com.moe.kvs.MOEKVSPDF.resource.PageXofY;
import com.moe.kvs.MOEKVSPDF.resource.QueryResult;





import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpRequest.BodyPublishers;
//import java.net.http.HttpResponse;
//import java.net.http.HttpResponse.BodyHandlers;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
//import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



@RestController
@RequestMapping("/api/kvsreport")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class ReportsCtrl {

	@Autowired
	NativeRepository nativeRepository;
	@Autowired
	CertificationGenerateUtil certificationGenerateUtil;
	
	@Value("${userBucket.path}")
	private String UPLOADED_FOLDER;
	
	
	

	@RequestMapping(value = "/testReport", method = RequestMethod.POST)
	public ResponseEntity<?> testReport() throws Exception {

		System.out.println("called");

		String textContent = "Sapiensplendide noluisse ... mollis verterem alia regione quidam.";

		String simplePdf = "E:\\shamimShared\\simple-text.pdf";

		WriterProperties wp = new WriterProperties();

		wp.setPdfVersion(PdfVersion.PDF_2_0);

		try {
			String header = "pdfHtml Header and footer example using page-events";

			PdfWriter writer = new PdfWriter(simplePdf, wp);

			PdfDocument pdfDocument = new PdfDocument(writer);

			Document document = new Document(pdfDocument);
			document.setMargins(100, 100, 72, 36);

			Header headerHandler = new Header(header);
			PageXofY footerHandler = new PageXofY(pdfDocument);

			pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
			pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

			document.add(new Paragraph(textContent));

			footerHandler.writeTotal(pdfDocument);
			pdfDocument.close();

		} catch (FileNotFoundException e) {

//		             log.error("Creating PDF failed", e);

//		             throw new ITextException(e.getMessage());

		}

		return null;
//	return ResponseEntity.ok(new CustomResponse(1,"sucess",qualificationInterface.deteleEducationalQualification(Integer.parseInt(data)),"200"));	
	}

	@RequestMapping(value = "/getReportData", method = RequestMethod.POST)
	public ResponseEntity<?> getReportData(@RequestBody String data) throws Exception {

		System.out.println("Report Data--->" + data);

		ObjectMapper mapperObj = new ObjectMapper();
		ReportBeans reportPojo = new ReportBeans();
		try {
			reportPojo = mapperObj.readValue(data, new TypeReference<ReportBeans>() {
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("called--->" + reportPojo.getReportValue());

		String Query = "";
		if (reportPojo.getReportId().equalsIgnoreCase("1") && reportPojo.getReportType().equalsIgnoreCase("N")
				|| (reportPojo.getReportType().equalsIgnoreCase("R")
						&& reportPojo.getReportValue().equalsIgnoreCase("999"))) {
			Query = "select * from transfer.report_regionwise_station_school_staff_count";
		} else if (reportPojo.getReportId().equalsIgnoreCase("1") && reportPojo.getReportType().equalsIgnoreCase("R")
				|| (reportPojo.getReportType().equalsIgnoreCase("S")
						&& reportPojo.getReportValue().equalsIgnoreCase("999"))) {

			if (reportPojo.getReportValue().equalsIgnoreCase("999")) {
//				reportPojo.setReportValue("dsa");
				reportPojo.setReportValue(reportPojo.getPrevious());
			}
			Query = "select * from (    \r\n"
					+ "select stn.region_code,stn.region_name ,stn.station_code , stn.station_name,no_of_school,  total_staff_all from (    \r\n"
					+ "select distinct region_code, region_name , station_code , station_name  from transfer.dashboardviewone d where d.region_code ='"
					+ reportPojo.getReportValue() + "'\r\n" + ") stn,\r\n"
					+ "(select sum(total_staff_all ) as total_staff_all,count(*) as no_of_school, station_code , station_name\r\n"
					+ "from transfer.dashboardviewone v\r\n" + "where region_code = '" + reportPojo.getReportValue()
					+ "'   group by station_code , station_name    \r\n" + ") staff\r\n" + "\r\n"
					+ "where stn.station_code = staff.station_code ) gh";
		} else if (reportPojo.getReportId().equalsIgnoreCase("1") && reportPojo.getReportType().equalsIgnoreCase("S")
				|| (reportPojo.getReportType().equalsIgnoreCase("SCH")
						&& reportPojo.getReportValue().equalsIgnoreCase("999"))) {

			if (reportPojo.getReportValue().equalsIgnoreCase("999")) {
//				reportPojo.setReportValue("dsa");
				reportPojo.setReportValue(reportPojo.getPrevious());
			}
			Query = "select * from (    \r\n"
					+ "select stn.region_code,stn.region_name ,stn.station_code , stn.station_name,stn.kv_code,stn.udise_sch_code, stn.kv_name,total_staff_all   from (    \r\n"
					+ "select distinct region_code, region_name , station_code , station_name, d.kv_code, d.udise_sch_code  , d.kv_name  from transfer.dashboardviewone d where d.station_code ='"
					+ reportPojo.getReportValue() + "'\r\n" + ") stn,\r\n"
					+ "(select sum(total_staff_all ) as total_staff_all, kv_code  \r\n"
					+ "from transfer.dashboardviewone v\r\n" + "where station_code = '" + reportPojo.getReportValue()
					+ "'   group by kv_code    \r\n" + ") staff\r\n" + "\r\n"
					+ "where stn.kv_code = staff.kv_code ) gh";
		} else if (reportPojo.getReportId().equalsIgnoreCase("1")
				&& reportPojo.getReportType().equalsIgnoreCase("SCH")) {
			if (reportPojo.getReportValue().equalsIgnoreCase("999")
					|| reportPojo.getReportValue().equalsIgnoreCase("9999")) {
//				reportPojo.setReportValue("dsa");
				reportPojo.setReportValue(reportPojo.getPrevious());
				Query = "select row_number() over (ORDER BY tp.teacher_id) as srl_no, ksm.region_code ,\r\n"
						+ "  ksm.region_name ,\r\n" + "  ksm.station_code ,\r\n" + "  ksm.station_name ,\r\n"
						+ "  ksm.kv_code ,\r\n" + "  ksm.kv_name ,\r\n" + "  ksm.udise_sch_code,\r\n"
						+ "  tp.teacher_name ,\r\n" + "  case\r\n" + "  when (tp.teacher_gender='1') then 'Male'\r\n"
						+ "  else 'Female'\r\n" + "  end as teacher_gender,\r\n" + "  tp.teacher_employee_code ,\r\n"
						+ "  case\r\n" + "      when (tp.verify_flag='SA')  then 'Verified'\r\n"
						+ "      when (tp.verify_flag in ('SE','SES','TA') ) then 'Verification by School Pending'\r\n"
						+ "      when (tp.verify_flag in ('SI','TI') ) then 'Teacher Editing'\r\n"
						+ "      else 'No Action Taken'\r\n" + "       end as verify_flag  \r\n"
						+ "from kv.kv_school_master ksm , public.teacher_profile tp\r\n"
						+ "where ksm.udise_sch_code = tp.current_udise_sch_code\r\n" + "and ksm.station_code = '"
						+ reportPojo.getReportValue() + "'\r\n" + "and  tp.drop_box_flag ='0'  order by ksm.kv_name ";
			} else {

				Query = "select row_number() over (ORDER BY tp.teacher_id) as srl_no, ksm.region_code ,\r\n"
						+ "  ksm.region_name ,\r\n" + "  ksm.station_code ,\r\n" + "  ksm.station_name ,\r\n"
						+ "  ksm.kv_code ,\r\n" + "  ksm.kv_name ,\r\n" + "  ksm.udise_sch_code,\r\n"
						+ "  tp.teacher_name ,\r\n" + "  case\r\n" + "  when (tp.teacher_gender='1') then 'Male'\r\n"
						+ "  else 'Female'\r\n" + "  end as teacher_gender,\r\n" + "  tp.teacher_employee_code ,\r\n"
						+ "  case\r\n" + "      when (tp.verify_flag='SA')  then 'Verified'\r\n"
						+ "      when (tp.verify_flag in ('SE','SES','TA') ) then 'Verification by School Pending'\r\n"
						+ "      when (tp.verify_flag in ('SI','TI') ) then 'Teacher Editing'\r\n"
						+ "      else 'No Action Taken'\r\n" + "       end as verify_flag  \r\n"
						+ "from kv.kv_school_master ksm , public.teacher_profile tp\r\n"
						+ "where ksm.udise_sch_code = tp.current_udise_sch_code\r\n"
						+ "and tp.current_udise_sch_code = '" + reportPojo.getReportValue() + "'\r\n"
						+ "and  tp.drop_box_flag ='0'";
			}

			System.out.println("Query--->" + Query);

		} else if (reportPojo.getReportId().equalsIgnoreCase("2")) {
			Query = "select region_code,region_name,station_code , station_name ,kv_code , kv_name ,udise_sch_code,state_name ,district_name from kv.kv_school_master ksm where ksm.status ='0'  and ksm.school_type ='1'  group by region_code,region_name , station_code , station_name , kv_code , kv_name , udise_sch_code,state_name ,district_name order by  region_name,station_name, kv_name";
		} else if (reportPojo.getReportId().equalsIgnoreCase("3")) {
			Query = "select region_code,region_name,count(*) as no_of_school from kv.kv_school_master ksm where ksm.status ='0'  and ksm.school_type ='1'  group by region_code,region_name order by region_name  ";
		} else if (reportPojo.getReportId().equalsIgnoreCase("4")) {
			Query = "select region_code,region_name,station_code , station_name ,\r\n"
					+ "count(*) as no_of_school from kv.kv_school_master ksm where ksm.status ='0'  and ksm.school_type ='1'  group by region_code,region_name , station_code , station_name  order by region_name , station_name \r\n";
		}else if (reportPojo.getReportId().equalsIgnoreCase("5")) {
			Query = "select  ksm.region_code, ksm.region_name , ksm.kv_name , spm.school_code ,  md.post_code ,md.post_name , ms2.subject_code , ms2.subject_name  , sum(spm.sanctioned_post) as sanctioned_post ,sum( spm.occupied_post) as occupied_post\r\n"
					+ "from uneecops.sanctioned_post_mapping spm , kv.kv_school_master ksm  , uneecops.m_designation md, uneecops.m_subject ms2  \r\n"
					+ "where spm.school_code::varchar = ksm.udise_sch_code::varchar\r\n"
					+ "and spm.shift::numeric = ksm.shift_type::numeric\r\n"
					+ "and spm.post_id = md.id\r\n"
					+ "and spm.subject_id = ms2.id\r\n"
					+ "and ksm.region_code = '"+reportPojo.getReportValue()+"'\r\n"
					+ "group by\r\n"
					+ "  grouping sets  ((ksm.region_code, ksm.region_name , ksm.kv_name , spm.school_code ,  md.post_code ,md.post_name , ms2.subject_code , ms2.subject_name),\r\n"
					+ "  (ksm.region_name, ksm.kv_name ,spm.school_code),())\r\n"
					+ "order by spm.school_code, md.post_code , ms2.subject_code";
		}else if(reportPojo.getReportId().equalsIgnoreCase("6")) {
//			Query="select count(*) as total_no_onboarded_teacher, count(*)  filter(where verify_flag='TA') as teacher_approve , count(*)  filter(where verify_flag='SA') as school_approve from public.teacher_profile tp  where kv_code !='9999' ";
		Query="select count(*) as total_no_onboarded_teacher,\r\n"
				+ "count(*) filter ( where verify_flag='TA') as teacher_approve ,\r\n"
				+ "count(*) filter( where verify_flag = 'SA' or verify_flag = 'TTD') as school_aprove,\r\n"
				+ "count(*) filter ( where  ttp.choice_kv1_station_code is not null) as  no_of_choice_stn,\r\n"
				+ "count(*) filter ( where ttp.transfer_status= '1') as transfer_apply\r\n"
				+ "from public.teacher_transfer_profile ttp , public.teacher_profile tp \r\n"
				+ "where ttp.teacher_id = tp.teacher_id and tp.kv_code <> '9999'";
		
	String	Query2="select * from (\r\n"
				+ "select count(*) filter(where  ttp.trans_emp_is_declaration ='1') as decalaration_done,\r\n"
				+ "count(*) filter(where  ttp.choice_kv1_station_code is not null   ) as station_coice\r\n"
				+ "from public.teacher_transfer_profile ttp \r\n"
				+ ") aa, (\r\n"
				+ "select count(*) filter(where ttd.dc_save_yn ='1') as dc_save,\r\n"
				+ "	   count(*) filter(where ttd.tc_save_yn ='1') as tc_save\r\n"
				+ "from transfer.teacher_transfer_details ttd where ttd.kv_code <> '9999'\r\n"
				+ ") bb"
		;
		}
		
		

		System.out.println("Query--->" + Query);

		QueryResult qrObj = nativeRepository.executeQueries(Query);
		return ResponseEntity.ok(qrObj);
	}

	@RequestMapping(value = "/sentReport", method = RequestMethod.GET)
	public ResponseEntity<byte[]>  sentReport(@RequestParam("teacherId") String teacherId,@RequestParam("reportType") String reporttype) throws Exception {
		String textContent = "Sapiensplendide noluisse ... mollis verterem alia regione quidam.";
//		String data="{'reportType':'2','teacherId':'25423'}";
//		System.out.println("send report--->"+data);
		ObjectMapper mapperObj = new ObjectMapper();
		ReportBeans reportPojo = new ReportBeans();
		Map<Object, Object> maps=new HashMap<Object,Object>();
		Integer reportType=null; 
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			 maps = objectMapper.readValue(data, Map.class);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
		
		maps.put("reportType", reporttype);
		maps.put("teacherId", teacherId);
		
		reportType=Integer.parseInt(String.valueOf(maps.get("reportType")));
		
//		         String simplePdf = "E:\\shamimShared\\teacher-report.pdf";
//		         WriterProperties wp = new WriterProperties();
//		         wp.setPdfVersion(PdfVersion.PDF_2_0);
//		         try {
//		        	 String header = "pdfHtml Header and footer example using page-events";
//		        	 PdfWriter writer = new PdfWriter(simplePdf, wp);
//		                 PdfDocument pdfDocument = new PdfDocument(writer);
//		                 Document document = new Document(pdfDocument);
//		                 document.setMargins(100, 100, 72, 36);
//		              	 Header headerHandler = new Header(header);
//			        	 PageXofY footerHandler = new PageXofY(pdfDocument);
//			        	 pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE,headerHandler);
//			        	 pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,footerHandler);
//			        	 
//			        	 Paragraph paragraph1 = new Paragraph("First paragraph Sudipto");
//			        	 paragraph1.setMarginRight(30);
////			        	 paragraph1.setSpacingAfter(72f);
//			        	 document.add(paragraph1);
//			        	 Paragraph paragraph2 = new Paragraph("Second paragraph");
//			        	 document.add(paragraph2);
//			        	 Table table = new Table(2).useAllAvailableWidth();
//
//			             for (int i = 0; i < 16; i++) {
//			            	 Cell cell = new Cell();
//			            	 cell.add("contents go here");
//			            	 cell.setBorder(Border.NO_BORDER);
//			                 table.addCell(cell);
//			             }
//
//			             document.add(table);
//			        	 
//		             document.add(new Paragraph(textContent));
//		             footerHandler.writeTotal(pdfDocument);
//		             pdfDocument.close();
//		         } catch (FileNotFoundException e) {
//		                 e.printStackTrace(); 
//		         }

//		 Get API Data

		System.out.println("called");
		
		ObjectMapper mapper = new ObjectMapper();
		String layload = String.valueOf(maps.get("teacherId"));
		Map<String, Object> map = null;
		Map<String, Map<String, Object>> dataObj = null;
		Map<String, List<Map<String, Object>>> expObj = null;
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(layload, headers);
//			String access_token_url ="http://10.247.141.239:8080/MOE-RAD-TEACHER/api/teacher/getConfirmedTeacherDetails";
		
//		try {
		String access_token_url = "http://localhost:8080/MOE-RAD-TEACHER/api/teacher/getConfirmedTeacherDetails";

//	    String access_token_url ="http://10.247.141.239:8080/MOE-RAD-TEACHER/api/teacher/getConfirmedTeacherDetails";
		
		try {
		//String access_token_url = "http://10.25.26.251:8014/api/teacher/getConfirmedTeacherDetails";
		response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			System.out.println(response.getBody());
			map = mapper.readValue(response.getBody(), Map.class);
			dataObj = (Map<String, Map<String, Object>>) map.get("response");
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
  // System.out.println("Teacher Details--->"+dataObj.get("schoolDetails"));
		
	
		List<ExprienceBean> expBean = null;
		MiscelaneousBean miscelaneousBean = null;
		ObjectMapper mapper1 = new ObjectMapper();
		expBean = mapper1.readValue(mapper.writeValueAsString(dataObj.get("experience")), new TypeReference<List<ExprienceBean>>(){});
		miscelaneousBean = mapper1.readValue(mapper.writeValueAsString(dataObj.get("transDetails")), MiscelaneousBean.class);
		//String str = mapper1.writeValueAsString(dataObj.get("experience"));
		
		System.out.println("valui" +miscelaneousBean);
		System.out.println("Teacher Details--->"+dataObj.get("experience"));
	
		
		try {
//			File file=new File("E:/uploadDoc/1047/profile_verified_by_teacher.pdf");
////			FileBody filebody = new FileBody(file, ContentType.DEFAULT_BINARY);
////			
//			System.out.println(file.exists());
////			
//			HttpHeaders header = new HttpHeaders();
//			header.setContentType(MediaType.MULTIPART_FORM_DATA);
//			MultiValueMap<String, Object> body
//			  = new LinkedMultiValueMap<>();
//			body.add("file", new FileBody(file, ContentType.DEFAULT_BINARY));
////			body.add("name", "shamim");
//			
//			HttpEntity<MultiValueMap<String, Object>> requestEntity
//			  = new HttpEntity<>(body, header);
//			RestTemplate restTemplate1 = new RestTemplate();
//			
//			ResponseEntity<String> response1 =	 restTemplate1.postForEntity("http://localhost:8686/api/upload",  requestEntity,String.class);	
//			
//			
			
			
			

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
//return null;
		
		return	ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(certificationGenerateUtil.downloadCertificate(dataObj,expBean ,miscelaneousBean,reportType));
	 

	}
	
	@RequestMapping(value = "/sentPdfReport", method = RequestMethod.POST)
	public ResponseEntity<?> sentPdfReportOnMail(@RequestBody String data) throws Exception {
		String textContent = "Sapiensplendide noluisse ... mollis verterem alia regione quidam.";
		
		System.out.println("send report--->"+data);
		ObjectMapper mapperObj = new ObjectMapper();
		ReportBeans reportPojo = new ReportBeans();
		Map<Object, Object> maps=null;
		Integer reportType=null; 
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			 maps = objectMapper.readValue(data, Map.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		reportType=Integer.parseInt(String.valueOf(maps.get("reportType")));
		
//		 Get API Data

		System.out.println("called");
		
		ObjectMapper mapper = new ObjectMapper();
		String layload = String.valueOf(maps.get("teacherId"));
		Map<String, Object> map = null;
		Map<String, Map<String, Object>> dataObj = null;
		Map<String, List<Map<String, Object>>> expObj = null;
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(layload, headers);
		String access_token_url ="http://10.247.141.239:8080/MOE-RAD-TEACHER/api/teacher/getConfirmedTeacherDetails";
		
		try {
		//String access_token_url = "http://10.25.26.251:8014/api/teacher/getConfirmedTeacherDetails";
		response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			System.out.println(response.getBody());
			map = mapper.readValue(response.getBody(), Map.class);
			dataObj = (Map<String, Map<String, Object>>) map.get("response");
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
  // System.out.println("Teacher Details--->"+dataObj.get("schoolDetails"));
		
	
		List<ExprienceBean> expBean = null;
		MiscelaneousBean miscelaneousBean = null;
		ObjectMapper mapper1 = new ObjectMapper();
		expBean = mapper1.readValue(mapper.writeValueAsString(dataObj.get("experience")), new TypeReference<List<ExprienceBean>>(){});
		miscelaneousBean = mapper1.readValue(mapper.writeValueAsString(dataObj.get("transDetails")), MiscelaneousBean.class);
		//String str = mapper1.writeValueAsString(dataObj.get("experience"));
		
		System.out.println("valui" +miscelaneousBean);
		System.out.println("Teacher Details--->"+dataObj.get("experience"));
	
		
		try {
//			File file=new File("E:/uploadDoc/1047/profile_verified_by_teacher.pdf");
////			FileBody filebody = new FileBody(file, ContentType.DEFAULT_BINARY);
////			
//			System.out.println(file.exists());
////			
//			HttpHeaders header = new HttpHeaders();
//			header.setContentType(MediaType.MULTIPART_FORM_DATA);
//			MultiValueMap<String, Object> body
//			  = new LinkedMultiValueMap<>();
//			body.add("file", new FileBody(file, ContentType.DEFAULT_BINARY));
////			body.add("name", "shamim");
//			
//			HttpEntity<MultiValueMap<String, Object>> requestEntity
//			  = new HttpEntity<>(body, header);
//			RestTemplate restTemplate1 = new RestTemplate();
//			
//			ResponseEntity<String> response1 =	 restTemplate1.postForEntity("http://localhost:8686/api/upload",  requestEntity,String.class);	
//			
//			
			
			
			

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
//return null;
		return certificationGenerateUtil.downloadCertificateWithMail(dataObj,expBean ,miscelaneousBean,reportType);

	}
	
	
	
	

	@RequestMapping(value = "/getRegionStationSchool", method = RequestMethod.POST)
	public ResponseEntity<?> getRegionStationSchool() throws Exception {
		QueryResult qrObj = null;
		String query = "select ms.kv_code , ms.school_name, shift  , ms2.station_code , ms2.station_name , mr.region_code ,mr.region_name  \r\n"
				+ "from uneecops.m_schools ms , uneecops.m_station ms2 , uneecops.m_region mr ,\r\n"
				+ "uneecops.school_station_mapping ssm , uneecops.region_station_mapping rsm \r\n"
				+ "where ms.kv_code = ssm.kv_code \r\n" + "and ms.school_status =true \r\n"
				+ "and ms.school_type = 1 \r\n" + "and shift in ('1','0')\r\n"
				+ "and rsm.station_code = ssm.station_code \r\n" + "and rsm.region_code = mr.region_code \r\n"
				+ "and mr.region_type = 3\r\n" + "and mr.is_active = true\r\n"
				+ "and ms2.station_code = ssm.station_code";

		try {
			qrObj = nativeRepository.executeQueries(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ResponseEntity.ok(qrObj);
	}
	
//	@RequestMapping(value = "/profileConfirmByTeacher", method = RequestMethod.POST)
//	public ResponseEntity<?> profileConfirmByTeacher(@RequestBody String data) throws Exception {
////		return sentReport(data);
//	}
	
	
	
	
	
	

}
