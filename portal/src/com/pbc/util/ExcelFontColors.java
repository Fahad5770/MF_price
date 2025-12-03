package com.pbc.util;

import org.apache.poi.ss.usermodel.IndexedColors;

public class ExcelFontColors {

	private short white = 0;
	private short black = 0;

	public ExcelFontColors() {
		white = IndexedColors.WHITE.getIndex();
		black = IndexedColors.BLACK.getIndex();
	}

	public short getWhite() {
		return white;
	}

	public short getBlack() {
		return black;
	}

}
