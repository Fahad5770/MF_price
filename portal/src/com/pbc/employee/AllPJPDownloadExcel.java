package com.pbc.employee;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AllPJPDownloadExcel {
   public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";
   Datasource ds = new Datasource();
   Statement s;
   Statement s2;
   Statement s3;
   Statement s4;
   Date StartDate = Utilities.getDateByDays(0);
   Date EndDate = Utilities.getDateByDays(0);
   Date Yesterday = Utilities.getDateByDays(-1);
   long SND_ID = 0L;
   SXSSFWorkbook workbook = new SXSSFWorkbook();

   public static void main(String[] args) throws Exception {
   }

   public AllPJPDownloadExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
      this.ds.createConnectionToReplica();
      this.s = this.ds.createStatement();
      this.s2 = this.ds.createStatement();
      this.s3 = this.ds.createStatement();
      this.s4 = this.ds.createStatement();
      if (Utilities.getDayOfWeekByDate(this.Yesterday) == 6) {
         this.Yesterday = Utilities.getDateByDays(-2);
      }

   }

   public void createPdf(String filename, long SND_ID) throws IOException, SQLException {
      SXSSFSheet spreadsheet = this.workbook.createSheet("cell types");
  //    int FirstRowCount = 0;
      int RowCount = 0;
      SXSSFRow headerrow1 = spreadsheet.createRow(RowCount);
      SXSSFCell headercell1 = headerrow1.createCell(0);
      
      headercell1 = headerrow1.createCell(0);
      headercell1.setCellValue("PJP ID");
      
      headercell1 = headerrow1.createCell(1);
      headercell1.setCellValue("PJP Name");
      
      headercell1 = headerrow1.createCell(2);
      headercell1.setCellValue("Distributor Name");
      
      headercell1 = headerrow1.createCell(3);
      headercell1.setCellValue("OrderBooker ID");
      
      headercell1 = headerrow1.createCell(4);
      headercell1.setCellValue("OrderBooker Name");
      
      headercell1 = headerrow1.createCell(5);
      headercell1.setCellValue("Outlet ID");
      
      headercell1 = headerrow1.createCell(6);
      headercell1.setCellValue("Outlet Name");
      
      headercell1 = headerrow1.createCell(7);
      headercell1.setCellValue("Address");
      
      headercell1 = headerrow1.createCell(8);
      headercell1.setCellValue("PJP Status");
      
      headercell1 = headerrow1.createCell(9);
      headercell1.setCellValue("Lat");
      
      headercell1 = headerrow1.createCell(10);
      headercell1.setCellValue("Lng");
      
      headercell1 = headerrow1.createCell(11);
      headercell1.setCellValue("Updated On");
      
      headercell1 = headerrow1.createCell(12);
      headercell1.setCellValue("Monday");
      
      headercell1 = headerrow1.createCell(13);
      headercell1.setCellValue("Tuesday");
      
      headercell1 = headerrow1.createCell(14);
      headercell1.setCellValue("Wednesday");
      
      headercell1 = headerrow1.createCell(15);
      headercell1.setCellValue("Thursday");
      
      headercell1 = headerrow1.createCell(16);
      headercell1.setCellValue("Friday");
      
      headercell1 = headerrow1.createCell(17);
      headercell1.setCellValue("Saturday");
      
      headercell1 = headerrow1.createCell(18);
      headercell1.setCellValue("Sunday");
      
      headercell1 = headerrow1.createCell(19);
      headercell1.setCellValue("Channel");
      
      headercell1 = headerrow1.createCell(20);
      headercell1.setCellValue("Last Sale");
      
      headercell1 = headerrow1.createCell(21);
      headercell1.setCellValue("Status");
      
      headercell1 = headerrow1.createCell(22);
      headercell1.setCellValue("Week");
      
      headercell1 = headerrow1.createCell(23);
      headercell1.setCellValue("Geo Fencing");
      
      headercell1 = headerrow1.createCell(24);
      headercell1.setCellValue("Radius");
      long PJPID = 0L;
      String PJPName = "";
      
      CellStyle styleGreen = workbook.createCellStyle();// Create style
      XSSFFont fontGreen = (XSSFFont) workbook.createFont();// Create font
      XSSFColor green = new XSSFColor(new java.awt.Color(0,128,0));
      fontGreen.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
      fontGreen.setColor(green);
      styleGreen.setFont(fontGreen);
      
      CellStyle styleRed = workbook.createCellStyle();// Create style
      XSSFFont fontRed = (XSSFFont) workbook.createFont();// Create font
      fontRed.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
      XSSFColor red = new XSSFColor(new java.awt.Color(255, 0, 0));
      fontRed.setColor(red);
      styleRed.setFont(fontRed);
		
		
		
		
		
		
		
   //   System.out.println("Select distinct dbps.id,(SELECT label FROM pep.distributor_beat_plan where dbps.id=id) pjp_name from distributor_beat_plan_schedule dbps");
      ResultSet rs = this.s.executeQuery("Select distinct dbps.id,(SELECT label FROM pep.distributor_beat_plan where dbps.id=id) pjp_name from distributor_beat_plan_schedule dbps");

      while(rs.next()) {
         PJPID = rs.getLong("id");
         PJPName =  rs.getString("pjp_name");
         String orderBookerName = "";
         int orderBookerId = 0;
         
         ResultSet rs11 = this.s3.executeQuery("SELECT assigned_to,(select DISPLAY_NAME from users where assigned_to=id) assigned_to_name FROM pep.distributor_beat_plan_users where id=" + PJPID);
         if (rs11.first()) {
            orderBookerName =  rs11.getString("assigned_to_name");
            orderBookerId = rs11.getInt("assigned_to");
         }
         
         int pjpStatusID = 0;
         String distributorName = "";
         
         ResultSet rs111 = this.s3.executeQuery("SELECT is_active , distributor_id , (select name from common_distributors cd where cd.distributor_id = dbp.distributor_id) as distributor_name FROM distributor_beat_plan dbp where id=" + PJPID);
         if (rs111.first()) {
            pjpStatusID = rs111.getInt("is_active");
            distributorName = rs111.getString("distributor_name");
         }
         
         String status = (pjpStatusID == 1) ? "Active" : "Inactive";

         long OutletID = 0L;
         String OutletName = "";
         String OutletAddress = "";
         Date LastSale = null;
         int active=0;
         int radius;
         int geoFence;
         System.out.println("Select distinct outlet_id,(SELECT name FROM common_outlets where outlet_id=id) namee, (SELECT Is_Geo_Fence FROM common_outlets where outlet_id=id) geofence,(SELECT Geo_Radius FROM common_outlets where outlet_id=id) radius ,(SELECT address FROM common_outlets where outlet_id=id) addres, is_active from distributor_beat_plan_schedule where id=" + PJPID);
         ResultSet rs2 = this.s4.executeQuery("Select distinct outlet_id,(SELECT name FROM common_outlets where outlet_id=id) namee, (SELECT Is_Geo_Fence FROM common_outlets where outlet_id=id) geofence,(SELECT Geo_Radius FROM common_outlets where outlet_id=id) radius ,(SELECT address FROM common_outlets where outlet_id=id) addres, is_active from distributor_beat_plan_schedule where id=" + PJPID);

         while(rs2.next()) {
            OutletID = rs2.getLong("outlet_id");
            OutletName = rs2.getString("namee");
            OutletAddress = rs2.getString("addres");
            active = rs2.getInt("is_active");
            radius = rs2.getInt("radius");
            geoFence = rs2.getInt("geofence");
            ResultSet rs45 = this.s3.executeQuery("select max(created_on) as last_sales from inventory_sales_adjusted isa where isa.outlet_id =" + OutletID);
            if (rs45.first()) {
               LastSale = rs45.getDate("last_sales");
            }

            ++RowCount;
            SXSSFRow headerrow2 = spreadsheet.createRow(RowCount);
            SXSSFCell headercell2 = headerrow2.createCell(0);
            
            headercell2 = headerrow2.createCell(0);
            headercell2.setCellValue(PJPID);
            
            headercell2 = headerrow2.createCell(1);
            headercell2.setCellValue(PJPName);
            
            headercell2 = headerrow2.createCell(2);
            headercell2.setCellValue(distributorName);
            
            headercell2 = headerrow2.createCell(3);
            headercell2.setCellValue(orderBookerId);
            
            headercell2 = headerrow2.createCell(4);
            headercell2.setCellValue(orderBookerName);
            
            headercell2 = headerrow2.createCell(5);
            headercell2.setCellValue(OutletID);
            
            headercell2 = headerrow2.createCell(6);
            headercell2.setCellValue(OutletName);
            
            headercell2 = headerrow2.createCell(7);
            headercell2.setCellValue(OutletAddress);
            
            headercell2 = headerrow2.createCell(8);
            headercell2.setCellValue(status);
            headercell2.setCellStyle((active == 1) ? styleGreen : styleRed);

            int Mon = 0;
            int tue = 0;
            int Wed = 0;
            int Th = 0;
            int Fri = 0;
            int Sat = 0;
            int Sun = 0;
            int isAlternate = 0;
            ResultSet rs1 = this.s2.executeQuery("Select day_number, is_alternative from distributor_beat_plan_schedule where id=" + PJPID + " and outlet_id=" + OutletID);

            while(rs1.next()) {
               if (rs1.getInt(1) == 2) {
                  Mon = 1;
               } else if (rs1.getInt(1) == 3) {
                  tue = 1;
               } else if (rs1.getInt(1) == 4) {
                  Wed = 1;
               } else if (rs1.getInt(1) == 5) {
                  Th = 1;
               } else if (rs1.getInt(1) == 6) {
                  Fri = 1;
               } else if (rs1.getInt(1) == 7) {
                  Sat = 1;
               } else if (rs1.getInt(1) == 1) {
                  Sun = 1;
               }
               isAlternate = rs1.getInt(2);
            }

            String ChannelName = "";
            double lat=0, lng=0;
            String updatedOn="";
            ResultSet rs112 = this.s2.executeQuery("Select pic_channel_id,(SELECT label FROM  pci_sub_channel where id=pic_channel_id)channelname, lat, lng, updated_on from common_outlets where id=" + OutletID);
            if (rs112.first()) {
               ChannelName = rs112.getString("channelname");
               lat = rs112.getDouble("lat");
               lng = rs112.getDouble("lng");
               updatedOn = Utilities.getDisplayDateFormat(rs112.getDate("updated_on"));
            }

            if (ChannelName == null) {
               ChannelName = "0";
            }

            
            
            headercell2 = headerrow2.createCell(9);
            headercell2.setCellValue(lat);
            headercell2 = headerrow2.createCell(10);
            headercell2.setCellValue(lng);
            headercell2 = headerrow2.createCell(11);
            headercell2.setCellValue(updatedOn);
            
            headercell2 = headerrow2.createCell(12);
            headercell2.setCellValue((double)Mon);
            headercell2.setCellType(0);
            headercell2 = headerrow2.createCell(13);
            headercell2.setCellValue((double)tue);
            headercell2.setCellType(0);
            headercell2 = headerrow2.createCell(14);
            headercell2.setCellValue((double)Wed);
            headercell2.setCellType(0);
            headercell2 = headerrow2.createCell(15);
            headercell2.setCellValue((double)Th);
            headercell2.setCellType(0);
            headercell2 = headerrow2.createCell(16);
            headercell2.setCellValue((double)Fri);
            headercell2.setCellType(0);
            headercell2 = headerrow2.createCell(17);
            headercell2.setCellValue((double)Sat);
            headercell2.setCellType(0);
            headercell2 = headerrow2.createCell(18);
            headercell2.setCellValue((double)Sun);
            headercell2.setCellType(0);
            
            headercell2 = headerrow2.createCell(19);
            headercell2.setCellValue(ChannelName);
            
            headercell2 = headerrow2.createCell(20);
            headercell2.setCellValue(Utilities.getDisplayDateFormat(LastSale));
   
            headercell2 = headerrow2.createCell(21);
            headercell2.setCellValue((active == 1) ? "Active" : "InActive" );
            headercell2.setCellStyle((active == 1) ? styleGreen : styleRed);
            
            headercell2 = headerrow2.createCell(22);
            headercell2.setCellValue((isAlternate == 1) ? "2-4" : "1-3" );
            
            headercell2 = headerrow2.createCell(23);
            headercell2.setCellValue((geoFence == 1) ? "Yes" : "No" );
            
            headercell2 = headerrow2.createCell(24);
            headercell2.setCellValue(radius);
         }
      }

      FileOutputStream out = new FileOutputStream(new File(filename));
      this.workbook.write(out);
      out.close();
      this.ds.dropConnection();
   }

   public void Set2ndHeaderBackColor(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(162, 162, 163));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      XSSFFont font = workbook.createFont();
      font.setFontName("Calibri");
      font.setColor(IndexedColors.WHITE.getIndex());
      style61.setFont(font);
   }

   public void Set2ndHeaderBackColorOrderKPI(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(95, 149, 153));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      XSSFFont font = workbook.createFont();
      font.setFontName("Calibri");
      font.setColor(IndexedColors.WHITE.getIndex());
      style61.setFont(font);
   }

   public void Set2ndHeaderBackColorDeliveryKPI(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(128, 95, 153));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      XSSFFont font = workbook.createFont();
      font.setFontName("Calibri");
      font.setColor(IndexedColors.WHITE.getIndex());
      style61.setFont(font);
   }

   public void Set3rdHeaderBackColor(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(227, 227, 227));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      headercell.setCellStyle(style61);
   }

   public void Set3rdHeaderBackColorOrder(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(202, 232, 234));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      headercell.setCellStyle(style61);
   }

   public void Set3rdHeaderBackColorDelivery(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(240, 230, 247));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColor12(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 192, 197));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorGray(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(245, 245, 245));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorBlue(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(246, 253, 253));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorBlueOneDecimal(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(246, 253, 253));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorPink(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 246, 249));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorPinkOneDecimal(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 246, 249));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorPercent(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorPercentRed(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 192, 197));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorPercentBlue(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(246, 253, 253));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorPercentPink(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 246, 249));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorLeft(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)1);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorLeftAltGray(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(245, 245, 245));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)1);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorLeftTotal(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 203, 173));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)1);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorCenterTotal(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 203, 173));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorRed(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 214, 231));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetNormalCellBackColorRed12(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(192, 0, 0));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)2);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }

   public void SetBorder(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFCellStyle style = workbook.createCellStyle();
      style.setBorderBottom((short)1);
      style.setBorderTop((short)1);
      style.setBorderRight((short)1);
      style.setBorderLeft((short)1);
      style.setAlignment((short)2);
      headercell.setCellStyle(style);
   }

   public void SetNormalCellBackColorLeft2(XSSFWorkbook workbook, XSSFCell headercell) {
      XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));
      XSSFCellStyle style61 = workbook.createCellStyle();
      style61.setFillBackgroundColor((short)12);
      style61.setFillForegroundColor(BackColor2ndHeader);
      style61.setFillPattern((short)1);
      style61.setBorderBottom((short)1);
      style61.setBorderTop((short)1);
      style61.setBorderRight((short)1);
      style61.setBorderLeft((short)1);
      style61.setAlignment((short)1);
      headercell.setCellStyle(style61);
      style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      headercell.setCellStyle(style61);
   }
}