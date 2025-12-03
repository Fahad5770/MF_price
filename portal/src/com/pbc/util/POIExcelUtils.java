package com.pbc.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIExcelUtils {

	public static void getStyleWithCell(XSSFWorkbook workbook, XSSFCell headercell, XSSFColor BackgroundColor,
			XSSFCellStyle style, XSSFFont font, short fontColor, boolean isBorder, boolean isBold, short text_align) {
		style = workbook.createCellStyle();
		font = workbook.createFont();
		style.setFillForegroundColor(BackgroundColor);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(text_align);
		if (isBorder)
			getBorders(style);
		getBold(font, style, ((!isBold) ? XSSFFont.BOLDWEIGHT_NORMAL : XSSFFont.BOLDWEIGHT_BOLD));
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		font.setColor(fontColor);
		style.setFont(font);
		headercell.setCellStyle(style);
	}

	public static XSSFCellStyle getStyleWithOutCell(XSSFWorkbook workbook, XSSFColor BackgroundColor,
			XSSFCellStyle style, XSSFFont font, short fontColor, boolean isBorder, boolean isBold, short text_align) {
		style = workbook.createCellStyle();
		font = workbook.createFont();
		style.setFillForegroundColor(BackgroundColor);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(text_align);
		if (isBorder)
			getBorders(style);
		getBold(font, style, ((!isBold) ? XSSFFont.BOLDWEIGHT_NORMAL : XSSFFont.BOLDWEIGHT_BOLD));
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		font.setColor(fontColor);
		style.setFont(font);
		return style;
	}

	public static void SetBackColor(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style,
			XSSFColor BackgroundColor, boolean isBorder) {
		style = workbook.createCellStyle();
		style.setFillForegroundColor(BackgroundColor);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		if (isBorder)
			getBorders(style);
		headercell.setCellStyle(style);
	}

	public static void getBorders(XSSFCellStyle style) {
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	}

	public static void getBold(XSSFFont font, XSSFCellStyle style, short font_weight) {
		font.setBoldweight(font_weight);
		style.setFont(font);// set it to bold
	}

}
