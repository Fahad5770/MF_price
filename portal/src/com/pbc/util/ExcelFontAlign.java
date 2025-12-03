package com.pbc.util;

import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelFontAlign {

	private short center = 0;
	private short left = 0;
	private short right = 0;

	public ExcelFontAlign() {
		center = CellStyle.ALIGN_CENTER;
		left = CellStyle.ALIGN_LEFT;
		right = CellStyle.ALIGN_RIGHT;
	}

	public short getCenter() {
		return center;
	}

	public short getLeft() {
		return left;
	}

	public short getRight() {
		return right;
	}

}
