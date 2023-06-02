package com.moe.kvs.MOEKVSPDF.certification.util;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.itextpdf.io.IOException;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;


@Component
public class CertificationGenerateUtil {
	
	public ResponseEntity<?> downloadCertificate()
			throws IOException, java.io.IOException {
		
		PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

		class HeadertFooterHandler implements IEventHandler {
			protected String info;

			public void setInfo(String info) {
				this.info = info;
			}

			public String getInfo() {
				return info;
			}

			@SuppressWarnings("resource")
			@Override
			public void handleEvent(Event event) {
				PdfDocumentEvent docEvent = (PdfDocumentEvent) event;

				SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a");
				PdfPage page = docEvent.getPage();
				Paragraph p = new Paragraph().setFont(font);
				Rectangle pageSize = page.getPageSize();
				PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();
				PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
				String formattedDate = formatter.format(new Date());
				
			        PdfCanvas aboveCanvas = new PdfCanvas(page.newContentStreamAfter(),
			                page.getResources(), pdfDoc);
			    	ImageData headerLogImagePath = null;
					Image logoInage = null;
			        Rectangle area = page.getPageSize();
			    	String filePath1 ="";
					try {
						filePath1 = ResourceUtils.getFile("classpath:kvs-logo.png").getPath();
						headerLogImagePath = ImageDataFactory.create(filePath1);
					} catch (MalformedURLException | FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
			    
					logoInage = new Image(headerLogImagePath);
					logoInage.setWidth(270);
					logoInage.setFixedPosition(155, 772);
					
			        new Canvas(aboveCanvas, pdfDoc, area)
			                .add(logoInage).setFixedPosition(200, 200, 300);

				new Canvas(pdfCanvas, pdfDoc, pageSize).setFont(font).setFontSize(11)

						.showTextAligned("Page " + Integer.toString(pdfDoc.getPageNumber(page)),
								pageSize.getWidth() / 2, 10, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0)
						
						.showTextAligned("Generated Date " + formattedDate, pageSize.getWidth() / 2+95, 10,
								TextAlignment.LEFT, VerticalAlignment.MIDDLE, 0)


						.showTextAligned(info, pageSize.getWidth() - 60, 30, TextAlignment.CENTER,
								VerticalAlignment.MIDDLE, 0);
				
//				ImageData imageData;
//				Image image = null;
//				try {
//					String filePath = ResourceUtils.getFile("classpath:water-marknew.jpg").getPath();
//					imageData = ImageDataFactory.create(filePath);
//					image = new Image(imageData);
//				} catch (MalformedURLException |FileNotFoundException e1) {
//					e1.printStackTrace();
//				}
//				PdfExtGState gstate = new PdfExtGState();
//				gstate.setFillOpacity(.2f);
//				PdfCanvas canvas = new PdfCanvas(page);
//				canvas.saveState();
//				canvas.setExtGState(gstate);
//				try (Canvas canvas2 = new Canvas(canvas, pdfDoc, pageSize)) {
//					//canvas2.add(image);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				canvas.restoreState();

			}

		}

		HeadertFooterHandler handler = new HeadertFooterHandler();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PdfWriter write = new PdfWriter(byteArrayOutputStream);
		write.setSmartMode(true);

		PdfDocument pdfDoc = new PdfDocument(write);
		pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, handler);
		Document doc = new Document(pdfDoc ,PageSize.A4);
		doc.setMargins(80, 20, 40, 20);
		//doc.setMa

		handler.setInfo("");
	
		float[] columnWidMainTab = { 1 };
		Table mainTable = new Table(UnitValue.createPercentArray(columnWidMainTab));
		mainTable.setWidth(UnitValue.createPercentValue(100));
		
		Table techBasicProfile = fetchBasicProfile(doc, font);
		mainTable.addCell(techBasicProfile);
		Table techGeneralInformation = fetchPersonalInformation(doc, font);
		mainTable.addCell(techGeneralInformation);

		Table techSpousDetails = fetchTechSpousDetails(doc, font);
		mainTable.addCell(techSpousDetails);
		

		Table techWorkExperience = fetchTeachWorkExperience(doc, font);
		mainTable.addCell(techWorkExperience);
		Table  teachOrderOfStationChange = fetchTeachOrderOfStationChange(doc ,font);
		mainTable.addCell(teachOrderOfStationChange);
		
		Table teachOrderOfDisplacementStationChoice  = fetchTeachOrderOfDisplacementStationChoice(doc ,font);
		mainTable.addCell(teachOrderOfDisplacementStationChoice);
		
		//doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
		Table teachMiscellaneous  = fetchTeachMiscellaneous(doc ,font);
		mainTable.addCell(teachMiscellaneous);
	//	addLogo(doc);
		doc.add(mainTable);
		
		doc.close();
		
		
		byteArrayOutputStream.close();
		byte[] bytes = byteArrayOutputStream.toByteArray();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=Teacher Certificate.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(bytes);
	}
	
	public Table fetchBasicProfile(Document doc ,PdfFont f){
		
		float[] columnWidths = {1.9f , 1.2f ,1.7f ,1.4f};
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "BASIC PROFILE", 4, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "KV / Institution Code", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Present KV / Institution Name", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Employee Code", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Teacher Id", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Name", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Gender", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Date of Birth ", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Email", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Mobile Number", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Nature of Appointment", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Marital Status", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Present Station Name ", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Present Station in Present Post Date", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Present KV in Present Post Date ", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Present Post Name", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Present Post Date", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Subject Name", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Staff Type", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);

		
		return table;
				
		
	}
	
	public Table fetchPersonalInformation(Document doc ,PdfFont f){
		
		float[] columnWidths = {1.5f , 1.7f ,1.5f ,1.7f};
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "PERSONAL INFORMATION", 4, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Correspondence Address", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 3, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Correspondence State", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Correspondence District", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Correspondence Pin Code", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 3, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Permanent Address", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 3, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Permanent State", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Permanent District ", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Permanent Pin Code", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 3, 1, f);
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "DISABILITY", 4, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Any kind of Disability", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Type of Disability", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		return table;
				
		
	}

	
	public Table fetchTechSpousDetails(Document doc ,PdfFont f){
		
		float[] columnWidths = {1.7f , 1.5f ,1.7f ,1.5f};
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "SPOUSE DETAILS", 4, 1, f);
		
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "KVS Employee", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Central Government Employee", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "State Government Employee", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "None of these", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Spouse Employee Code", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Spouse Name", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Spouse Position ", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithBorder(table, "Spouse Station", 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);		
		return table;
				
		
	}
	
	public Table fetchTeachWorkExperience(Document doc ,PdfFont f){
		
		float[] columnWidths = {2 , .5f ,.5f ,.5f , 1 ,1 ,1 ,1};
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "WORK EXPERIENCE", 8, 1, f);
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "School Name", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Shift", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "From", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "To", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Nature of Appointment", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Position Held", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Appointed for Subject", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Transfer Ground", 1, 1, f);

		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);		
		return table;
				
		
	}
	public Table fetchTeachOrderOfStationChange(Document doc ,PdfFont f){
		
		float[] columnWidths = {2 , 2 ,2 ,2 , 2};
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "ORDER OF STATION CHOICE", 5, 1, f);
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station One", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station Two", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station three", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station Four", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station Five", 1, 1, f);


		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);

		return table;
				
		
	}
	
	public Table fetchTeachOrderOfDisplacementStationChoice(Document doc ,PdfFont f){
		
		float[] columnWidths = {1.5f , 1.5f ,1.5f ,1.5f , 1.5f};
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "ORDER OF DISPLACEMENT STATION CHOICE", 8, 1, f);
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station One", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station Two", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station three", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station Four", 1, 1, f);
		TeachCertiCommonMethod.createDataHeaderCellTableCenter(table, "Station Five", 1, 1, f);


		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCell(table,  "0" , 1, 1, f);
	
		return table;
				
		
	}
	
	public Table fetchTeachMiscellaneous(Document doc ,PdfFont f){
		
		float[] columnWidths = {4 , .2f};
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setWidth(UnitValue.createPercentValue(100));
		
		TeachCertiCommonMethod.createDataHeaderCellTableCenterWithoutBorder(table, "MISCELLANEOUS", 2, 1, f);
		
		TeachCertiCommonMethod.createDataCellCategoryWithOutBorder(table, "(1) Whether the employee is seeking benefit of spouse who is working at the same station where employee is posted/transfer is being sought for", 1, 1, f);
		TeachCertiCommonMethod.createDataCellWithOutBorder(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithOutBorder(table, "(2) Whether the employee is seeking benefit of medical ground (MDG Ground)", 1, 1, f);
		TeachCertiCommonMethod.createDataCellWithOutBorder(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithOutBorder(table, "(3) Whether the employee is seeking benefit of single parent (SP Ground)", 1, 1, f);
		TeachCertiCommonMethod.createDataCellWithOutBorder(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithOutBorder(table, "(4) Whether the employee is seeking benefit of Death of Family Person (DFP Ground)", 1, 1, f);
		TeachCertiCommonMethod.createDataCellWithOutBorder(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithOutBorder(table, "(5) Whether your are main care-giver to the person with disability in the family (i.e spouse or own son/own daughter)", 1, 1, f);
		TeachCertiCommonMethod.createDataCellWithOutBorder(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithOutBorder(table, "(6) Members of JCM at KVS Regional Office (RJCM) / KVS Headquarters (NJCM)", 1, 1, f);
		TeachCertiCommonMethod.createDataCellWithOutBorder(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithOutBorder(table, "(7) Whether disciplinary proceedings are in progress", 1, 1, f);
		TeachCertiCommonMethod.createDataCellWithOutBorder(table,  "0" , 1, 1, f);
		TeachCertiCommonMethod.createDataCellCategoryWithOutBorder(table, "(8) Period of continuous absence(except maternity leave)", 1, 1, f);
		TeachCertiCommonMethod.createDataCellWithOutBorder(table,  "0" , 1, 1, f);

	   // table.setBorder(Border.NO_BORDER);
		//doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
		return table;
				
		
	}
	
	void addLogo(Document document) {

		ImageData imageData;
		Image image = null;
		try {
			String filePath = ResourceUtils.getFile("classpath:logo-min.jpeg").getPath();
			imageData = ImageDataFactory.create(filePath);
			image = new Image(imageData);
			image.setFixedPosition(220, 730);
		
			document.add(image);
		} catch (MalformedURLException | FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}
