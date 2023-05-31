package com.moe.kvs.MOEKVSPDF.resource;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
//import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;

public class Header implements IEventHandler{

	  String header;
	    public Header(String header) {
	        this.header = header;
	    }
	    @Override
	    public void handleEvent(Event event) {
	        //Retrieve document and
	        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
	        PdfDocument pdf = docEvent.getDocument();
	        PdfPage page = docEvent.getPage();
	        Rectangle pageSize = page.getPageSize();
	        PdfCanvas pdfCanvas = new PdfCanvas(
	                page.getLastContentStream(), page.getResources(), pdf);
	        Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
	      
	        String imageUrl = "http://www.google.com/intl/en_ALL/" 
	                  + "images/logos/images_logo_lg.gif";
//	        InputStream ims = new FileInputStream("C:\\WorkSpace\\teacherModule\\LoadbalanceTest\\kvslogo.jpg");
//	        byte[] bytes = IOUtils.toByteArray(ims);
//	        InputStream ims = getAssets().open("myImage.png");
//	           Bitmap bmp = BitmapFactory.decodeStream(ims);
//	           ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	           bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        Image  images=null;
	        try {
				  images =new Image(ImageDataFactory.create("C:\\\\WorkSpace\\\\teacherModule\\\\LoadbalanceTest\\\\kvslogo.jpg"));
				  images.scaleToFit(300,300);  
				  images.setFixedPosition((PageSize.A4.getWidth() - images.getImageScaledWidth()) / 2, (float) (PageSize.A4.getHeight() - 100));
//				  images.setAlignment(Element.ALIGN_CENTER);     
//				  images.setFixedPosition(0, (float) (PageSize.A4.getHeight() - 20.0));
	        } catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        System.out.println("images--->"+images);
	        canvas.add(images);
//	        C:\WorkSpace\teacherModule\LoadbalanceTest
//	        Image imgSoc = Image.getInstance("C:\\...\\Logo.jpg");
//            imgSoc.scaleToFit(110,110);
//            imgSoc.setAbsolutePosition(390, 720);
	        
//	        canvas.setFontSize(18f);
//	        //Write text at position
//	        canvas.showTextAligned(header,
//	                pageSize.getWidth() / 2,
//	                pageSize.getTop() - 30, TextAlignment.CENTER);
	    }

}
