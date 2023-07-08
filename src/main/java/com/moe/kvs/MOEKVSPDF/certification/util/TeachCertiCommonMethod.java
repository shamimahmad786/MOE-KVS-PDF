package com.moe.kvs.MOEKVSPDF.certification.util;

import com.itextpdf.io.IOException;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class TeachCertiCommonMethod {
	
	public static void createDataCellCategoryWithBorder(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {
		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(11));
		table.addCell(cell);

	}
	
	public static void createHeaderCellLeft(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {
		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(11));
		cell.setHorizontalAlignment(HorizontalAlignment.LEFT);
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		table.addCell(cell);

	}


	public static void createDataCell(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {
		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(9));
		cell.setMinHeight(14);
		cell.setTextAlignment(TextAlignment.CENTER);
		table.addCell(cell);

	}
	public static void createDataCellLeft(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {
		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(9));
//		cell.setMinHeight(14);
		cell.setTextAlignment(TextAlignment.LEFT);
		table.addCell(cell);

	}
	
	public static void createDataCellWithOutBorder(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {
		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(9));
		cell.setMinHeight(14);
		cell.setTextAlignment(TextAlignment.CENTER);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);

	}
	
	public static void createDataCellCategoryWithOutBorder(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {

		// PdfFont f = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(11)).setPaddingTop(-3);
		cell.setMinHeight(14);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);

	}
	public static void createDataCellWithOutBorderLeft(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {

		// PdfFont f = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(11)).setPaddingTop(-3);
//		cell.setMinHeight(14);
		cell.setTextAlignment(TextAlignment.CENTER);
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);

	}
	
	public static void createDataHeaderCellTableCenter(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {

		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(10));
	//	Color bgColour = new DeviceRgb(214, 208, 184);
		Color bgColour = new DeviceRgb(253, 228, 184);
		cell.setBackgroundColor(bgColour);
		cell.setTextAlignment(TextAlignment.CENTER);
		//cell.setTextAlignment();
		cell.setBold();
		table.addCell(cell);

	}
	
	public static void createDataHeaderCellTableCenterWithoutBorder(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {

		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(10));
		//Color bgColour = new DeviceRgb(214, 208, 184);
		Color bgColour = new DeviceRgb(253, 228, 184);
		cell.setBackgroundColor(bgColour);
		cell.setTextAlignment(TextAlignment.CENTER);
		cell.setBorder(Border.NO_BORDER);
		//cell.setTextAlignment();
		cell.setBold();
		table.addCell(cell);

	}
	public static void createHeadingWithoutBorderBold(Table table, String content, int colspan, int rowspan, PdfFont f)
			throws IOException {

		Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(f).setFontSize(10));
		//Color bgColour = new DeviceRgb(214, 208, 184);
//		Color bgColour = new DeviceRgb(253, 228, 184);
//		cell.setBackgroundColor(bgColour);
//		cell.setTextAlignment(TextAlignment.CENTER);
		cell.setBorder(Border.NO_BORDER);
//		cell.setBold();
		//cell.setTextAlignment();
		cell.setBold();
		table.addCell(cell);

	}
	
}
