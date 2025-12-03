package com.pbc.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelColorsInHexa {

	private XSSFColor White = null;
	private XSSFColor Orange = null;
	private XSSFColor Brown = null;
	private XSSFColor LightGray = null;
	private XSSFColor Purple = null;
	private XSSFColor Cadetblue = null;
	private XSSFColor Gray = null;
	private XSSFColor Green = null;
	private XSSFColor Blue = null;
	private XSSFColor LightBlue = null;
	private XSSFColor LightGreen = null;
	private XSSFColor LightYellow = null;
	private XSSFColor Pink = null;
	private XSSFColor SeaGreen = null;
	private XSSFColor Yellow = null;
	

	private List<XSSFColor> ExcelColorsList = new ArrayList<XSSFColor>();

	public ExcelColorsInHexa() {
		White = new XSSFColor(new Color(255, 255, 255));
		Orange = new XSSFColor(new Color(255, 140, 0));
		Brown = new XSSFColor(new Color(78, 53, 36));
		LightGray = new XSSFColor(new Color(227, 227, 227));
		Purple = new XSSFColor(new Color(128, 95, 153));
		Cadetblue = new XSSFColor(new Color(95, 149, 153));
		Gray = new XSSFColor(new Color(162, 162, 163));
		Green = new XSSFColor(new Color(0, 128, 0));
		Blue = new XSSFColor(new Color(0, 0, 255));
		LightBlue = new XSSFColor(new Color(0, 153, 255));
		LightGreen = new XSSFColor(new Color(51, 204, 51));
		LightYellow = new XSSFColor(new Color(255, 217, 102));
		Pink = new XSSFColor(new Color(255, 102, 102));
		SeaGreen = new XSSFColor(new Color(32,178,170));
		Yellow = new XSSFColor(new Color(255, 255, 0));
		setExcelColorsList();
	}

	public void setExcelColorsList() {
		ExcelColorsList.add(Purple);
		ExcelColorsList.add(Cadetblue);
		ExcelColorsList.add(LightGray);
		ExcelColorsList.add(LightBlue);
		ExcelColorsList.add(LightGreen);
		ExcelColorsList.add(LightYellow);
		ExcelColorsList.add(Pink);
		ExcelColorsList.add(SeaGreen);
	}
	
	public XSSFColor getYellow() {
		return Yellow;
	}
	
	public XSSFColor getSeaGreen() {
		return SeaGreen;
	}
	
	public XSSFColor getPink() {
		return Pink;
	}

	public XSSFColor getWhite() {
		return White;
	}

	public XSSFColor getLightBlue() {
		return LightBlue;
	}

	public XSSFColor getLightGreen() {
		return LightGreen;
	}

	public XSSFColor getBlue() {
		return Blue;
	}

	public XSSFColor getBrown() {
		return Brown;
	}

	public XSSFColor getOrange() {
		return Orange;
	}

	public XSSFColor getLightGray() {
		return LightGray;
	}

	public List<XSSFColor> getExcelColorsList() {
		return ExcelColorsList;
	}

	public XSSFColor getPurple() {
		return Purple;
	}

	public XSSFColor getCadetblue() {
		return Cadetblue;
	}

	public XSSFColor getGray() {
		return Gray;
	}

	public XSSFColor getGreen() {
		return Green;
	}

}
