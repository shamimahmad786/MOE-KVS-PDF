package com.moe.kvs.MOEKVSPDF.ctrl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.example.MOERADTEACHER.util.QueryResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.moe.kvs.MOEKVSPDF.beans.ReportBeans;
import com.moe.kvs.MOEKVSPDF.certification.util.CertificationGenerateUtil;
import com.moe.kvs.MOEKVSPDF.resource.Header;
import com.moe.kvs.MOEKVSPDF.resource.NativeRepository;
import com.moe.kvs.MOEKVSPDF.resource.PageXofY;
import com.moe.kvs.MOEKVSPDF.resource.QueryResult;




@RestController
@RequestMapping("/api/kvsreport")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class ReportsCtrl {
	
	@Autowired
	NativeRepository nativeRepository;
	@Autowired
	CertificationGenerateUtil certificationGenerateUtil;
	
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
		                 
			        	 pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE,headerHandler);
			        	 pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,footerHandler);
			        	 
			    
			        	 
			        	 
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
		
		System.out.println("Report Data--->"+data);
		
		ObjectMapper mapperObj = new ObjectMapper();
		ReportBeans reportPojo=new ReportBeans();
		try {
			reportPojo = mapperObj.readValue(data, new TypeReference<ReportBeans>() {
			});
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println("called--->"+reportPojo.getReportValue());
		
		
		String Query="";
		if(reportPojo.getReportId().equalsIgnoreCase("1") &&   reportPojo.getReportType().equalsIgnoreCase("N")  || (reportPojo.getReportType().equalsIgnoreCase("R") && reportPojo.getReportValue().equalsIgnoreCase("999"))) {
		 Query="select * from transfer.report_regionwise_station_school_staff_count";
		}else if(reportPojo.getReportId().equalsIgnoreCase("1") && reportPojo.getReportType().equalsIgnoreCase("R") || (reportPojo.getReportType().equalsIgnoreCase("S") && reportPojo.getReportValue().equalsIgnoreCase("999"))) {
		 
			if(reportPojo.getReportValue().equalsIgnoreCase("999")) {
//				reportPojo.setReportValue("dsa");
				reportPojo.setReportValue(reportPojo.getPrevious());
			}
			Query="select * from (    \r\n"
		  		+ "select stn.region_code,stn.region_name ,stn.station_code , stn.station_name,no_of_school,  total_staff_all from (    \r\n"
		  		+ "select distinct region_code, region_name , station_code , station_name  from transfer.dashboardviewone d where d.region_code ='"+reportPojo.getReportValue()+"'\r\n"
		  		+ ") stn,\r\n"
		  		+ "(select sum(total_staff_all ) as total_staff_all,count(*) as no_of_school, station_code , station_name\r\n"
		  		+ "from transfer.dashboardviewone v\r\n"
		  		+ "where region_code = '"+reportPojo.getReportValue()+"'   group by station_code , station_name    \r\n"
		  		+ ") staff\r\n"
		  		+ "\r\n"
		  		+ "where stn.station_code = staff.station_code ) gh";
		}else if(reportPojo.getReportId().equalsIgnoreCase("1") && reportPojo.getReportType().equalsIgnoreCase("S") || (reportPojo.getReportType().equalsIgnoreCase("SCH") && reportPojo.getReportValue().equalsIgnoreCase("999"))) {
			
			if(reportPojo.getReportValue().equalsIgnoreCase("999")) {
//				reportPojo.setReportValue("dsa");
				reportPojo.setReportValue(reportPojo.getPrevious());
			}
			Query="select * from (    \r\n"
					+ "select stn.region_code,stn.region_name ,stn.station_code , stn.station_name,stn.kv_code,stn.udise_sch_code, stn.kv_name,total_staff_all   from (    \r\n"
					+ "select distinct region_code, region_name , station_code , station_name, d.kv_code, d.udise_sch_code  , d.kv_name  from transfer.dashboardviewone d where d.station_code ='"+reportPojo.getReportValue()+"'\r\n"
					+ ") stn,\r\n"
					+ "(select sum(total_staff_all ) as total_staff_all, kv_code  \r\n"
					+ "from transfer.dashboardviewone v\r\n"
					+ "where station_code = '"+reportPojo.getReportValue()+"'   group by kv_code    \r\n"
					+ ") staff\r\n"
					+ "\r\n"
					+ "where stn.kv_code = staff.kv_code ) gh";
		}else if(reportPojo.getReportId().equalsIgnoreCase("1") && reportPojo.getReportType().equalsIgnoreCase("SCH")) {
			if(reportPojo.getReportValue().equalsIgnoreCase("999") || reportPojo.getReportValue().equalsIgnoreCase("9999")) {
//				reportPojo.setReportValue("dsa");
				reportPojo.setReportValue(reportPojo.getPrevious());
				Query="select row_number() over (ORDER BY tp.teacher_id) as srl_no, ksm.region_code ,\r\n"
						+ "  ksm.region_name ,\r\n"
						+ "  ksm.station_code ,\r\n"
						+ "  ksm.station_name ,\r\n"
						+ "  ksm.kv_code ,\r\n"
						+ "  ksm.kv_name ,\r\n"
						+ "  ksm.udise_sch_code,\r\n"
						+ "  tp.teacher_name ,\r\n"
						+ "  case\r\n"
						+ "  when (tp.teacher_gender='1') then 'Male'\r\n"
						+ "  else 'Female'\r\n"
						+ "  end as teacher_gender,\r\n"
						+ "  tp.teacher_employee_code ,\r\n"
						+ "  case\r\n"
						+ "      when (tp.verify_flag='SA')  then 'Verified'\r\n"
						+ "      when (tp.verify_flag in ('SE','SES','TA') ) then 'Verification by School Pending'\r\n"
						+ "      when (tp.verify_flag in ('SI','TI') ) then 'Teacher Editing'\r\n"
						+ "      else 'No Action Taken'\r\n"
						+ "       end as verify_flag  \r\n"
						+ "from kv.kv_school_master ksm , public.teacher_profile tp\r\n"
						+ "where ksm.udise_sch_code = tp.current_udise_sch_code\r\n"
						+ "and ksm.station_code = '"+reportPojo.getReportValue()+"'\r\n"
						+ "and  tp.drop_box_flag ='0'  order by ksm.kv_name ";
			}else {
			
			Query="select row_number() over (ORDER BY tp.teacher_id) as srl_no, ksm.region_code ,\r\n"
					+ "  ksm.region_name ,\r\n"
					+ "  ksm.station_code ,\r\n"
					+ "  ksm.station_name ,\r\n"
					+ "  ksm.kv_code ,\r\n"
					+ "  ksm.kv_name ,\r\n"
					+ "  ksm.udise_sch_code,\r\n"
					+ "  tp.teacher_name ,\r\n"
					+ "  case\r\n"
					+ "  when (tp.teacher_gender='1') then 'Male'\r\n"
					+ "  else 'Female'\r\n"
					+ "  end as teacher_gender,\r\n"
					+ "  tp.teacher_employee_code ,\r\n"
					+ "  case\r\n"
					+ "      when (tp.verify_flag='SA')  then 'Verified'\r\n"
					+ "      when (tp.verify_flag in ('SE','SES','TA') ) then 'Verification by School Pending'\r\n"
					+ "      when (tp.verify_flag in ('SI','TI') ) then 'Teacher Editing'\r\n"
					+ "      else 'No Action Taken'\r\n"
					+ "       end as verify_flag  \r\n"
					+ "from kv.kv_school_master ksm , public.teacher_profile tp\r\n"
					+ "where ksm.udise_sch_code = tp.current_udise_sch_code\r\n"
					+ "and tp.current_udise_sch_code = '"+reportPojo.getReportValue()+"'\r\n"
					+ "and  tp.drop_box_flag ='0'";
			}
			
			
			System.out.println("Query--->"+Query);
			
		} else if(reportPojo.getReportId().equalsIgnoreCase("2")) {
		Query="select region_code,region_name,station_code , station_name ,kv_code , kv_name ,udise_sch_code,state_name ,district_name from kv.kv_school_master ksm where ksm.status ='0'  and ksm.school_type ='1'  group by region_code,region_name , station_code , station_name , kv_code , kv_name , udise_sch_code,state_name ,district_name order by  region_name,station_name, kv_name";	
		}else if(reportPojo.getReportId().equalsIgnoreCase("3")) {
		Query="select region_code,region_name,count(*) as no_of_school from kv.kv_school_master ksm where ksm.status ='0'  and ksm.school_type ='1'  group by region_code,region_name order by region_name  ";	
			}else if(reportPojo.getReportId().equalsIgnoreCase("4")) {
				Query="select region_code,region_name,station_code , station_name ,\r\n"
						+ "count(*) as no_of_school from kv.kv_school_master ksm where ksm.status ='0'  and ksm.school_type ='1'  group by region_code,region_name , station_code , station_name  order by region_name , station_name \r\n" ;
			}
		
		System.out.println("Query--->"+Query);
		
		QueryResult qrObj=nativeRepository.executeQueries(Query);
		return ResponseEntity.ok(qrObj);
	}
	
	@RequestMapping(value = "/sentReport", method = RequestMethod.GET)
	public ResponseEntity<?> sentReport() throws Exception {
		 String textContent = "Sapiensplendide noluisse ... mollis verterem alia regione quidam.";
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

		return certificationGenerateUtil.downloadCertificate();
		
	}
	
	
	
}
