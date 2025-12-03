package com.pbc.bi;

import java.io.FileOutputStream;
import java.io.IOException;
 









import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pbc.common.Distributor;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;

public class NetTaxRate {

		
	
	public static void main(String[] args) throws DocumentException, IOException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		 /*
		 
		Datasource ds = new Datasource();
		ds.createConnection();		
		Statement s = ds.createStatement();
		 
		 
	 Datasource dsSAP = new Datasource();
	 dsSAP.createConnectionToSAPDB();
	 Statement sSAP = dsSAP.createStatement();
			
	 
		s.executeUpdate("CREATE  TABLE "+ds.logDatabaseName()+".net_tax_rate_2013 (vbeln bigint(11), matnr bigint(11),rate double, fkimg double, kurrf_dat date)");
		
		ResultSet rs = sSAP.executeQuery("select vbrk.vbeln, vbrp.matnr, (vbrp.kzwi1 - vbrp.mwsbp - (vbrp.kzwi4*-1) - (vbrp.kzwi5*-1))/vbrp.fkimg rate,vbrp.fkimg, vbrk.kurrf_dat from sapsr3.vbrp vbrp join sapsr3.vbrk vbrk on vbrp.vbeln = vbrk.vbeln where vbrp.pstyv != 'TANN' and vbrk.fkart in ('ZDIS', 'ZMRS', 'ZDFR') and vbrk.fksto != 'X' and vbrk.kurrf_dat between '20130701' and '20131231'");
		while(rs.next()){
			s.executeUpdate("insert into "+ds.logDatabaseName()+".net_tax_rate_2013(vbeln,matnr,rate,fkimg,kurrf_dat) values("+rs.getLong("vbeln")+","+rs.getLong("matnr")+","+rs.getDouble("rate")+","+rs.getDouble("fkimg")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("kurrf_dat")))+")");
		}
			
		ds.dropConnection();
		dsSAP.dropConnection();
		*/

Datasource ds = new Datasource();
ds.createConnection();
//ds.startTransaction();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

//try{

	/*
	ResultSet rs = s.executeQuery("SELECT id, created_on_date, distributor_id, order_id, order_created_on_date FROM inventory_sales_adjusted where id > 2689139 order by id");
	while(rs.next()){
		
		String OrderCreatedOnDate = null; 
		if (rs.getString("order_created_on_date") == null){
			OrderCreatedOnDate = null;
		}else{
			OrderCreatedOnDate = "'"+rs.getString("order_created_on_date")+"'";
		}
		
		s2.executeUpdate("update inventory_sales_adjusted_products set cache_created_on_date='"+rs.getString("created_on_date")+"', cache_distributor_id="+rs.getString("distributor_id")+", cache_order_id="+rs.getString("order_id")+", cache_order_created_on_date="+OrderCreatedOnDate+" where id="+rs.getLong("id"));
	}
	*/
	
	/*
	ResultSet rs2 = s.executeQuery("SELECT product_id, package_id, brand_id FROM inventory_products_view where category_id=1");
	while(rs2.next()){
		
		s2.executeUpdate("update inventory_sales_adjusted_products set cache_package_id="+rs2.getInt("package_id")+", cache_brand_id="+rs2.getInt("brand_id")+" where product_id="+rs2.getInt("product_id"));
	}
	*/
	//ds.commit();
	/*
	ResultSet rs = s.executeQuery("SELECT id, booked_by FROM inventory_sales_adjusted where id > 1950000 order by id");
	while(rs.next()){
		s2.executeUpdate("update inventory_sales_adjusted_products set cache_booked_by="+rs.getString("booked_by")+" where id="+rs.getLong("id"));
	}
	*/


	ResultSet rs2 = s.executeQuery("SELECT product_id, unit_per_sku, lrb_type_id FROM inventory_products_view where category_id=1");
	while(rs2.next()){
		s2.executeUpdate("update inventory_sales_adjusted_products set cache_units_per_sku="+rs2.getInt("unit_per_sku")+", cache_lrb_type_id="+rs2.getInt("lrb_type_id")+" where product_id="+rs2.getInt("product_id"));
	}
	
	s2.close();
	s.close();
	c.close();
			
	}
	
		
}
