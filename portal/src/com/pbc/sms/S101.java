package com.pbc.sms;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.employee.EmployeeHierarchy;
import com.pbc.util.Datasource;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.net.*;
import java.io.*;
public class S101 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S101() {
        super();
    }

    
    public static void main(String [] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	
        Date YesterdayDate = Utilities.getDateByDays(-1); 

    	Date StartDate = new Date();
    	
    	Date Date90DaysAgo = Utilities.getDateByDays(-90);
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        Date MonthToDateEndDate = new Date();

        
        
    	try {
    	
    	   Datasource ds = new Datasource();
   		   ds.createConnection();
   			
   		   Statement s = ds.createStatement();
          
   		   
          double AmountToday = 0;
         //Amount Today
          ResultSet rs = s.executeQuery("select sum(invoice_amount) invoice_amount from inventory_delivery_note idn where idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs.getDouble(1)));
        	  AmountToday = rs.getDouble(1);
          }
          
        //Amount Month to date
          double AmountMTD = 0;
          ResultSet rs1 = s.executeQuery("select sum(invoice_amount) invoice_amount from inventory_delivery_note idn where idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs1.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs1.getDouble(1)));
        	  AmountMTD = rs1.getDouble(1);
          }
          
          //Converted Cases Today
          double CasesToday = 0;
          ResultSet rs2 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesToday = rs2.getDouble(1);
          }
          
          //Converted Cases Month to date          
          double CasesMTD = 0;
          ResultSet rs3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTD = rs3.getDouble(1);
          }
         
          int DayOfMonth = 1;
          ResultSet rs10 = s.executeQuery("select dayofmonth(cast("+Utilities.getSQLDate(YesterdayDate)+" as date))");
          if (rs10.first()){
        	  DayOfMonth = rs10.getInt(1);
          }
          
          double AverageCases = CasesMTD / DayOfMonth;
          double AverageAmount = AmountMTD / DayOfMonth;
          
    		double CashInflow = 0;
      		ResultSet rs5 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts  glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glci.is_internal = 0 and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+"");
      		if(rs5.first()){
      			CashInflow = rs5.getDouble(1);
      		}
      		double MTDCashInflow = 0;
      		ResultSet rs6 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts  glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glci.is_internal = 0 and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+"");
      		if(rs6.first()){
      			MTDCashInflow = rs6.getDouble(1);
      		}
      		
      		double AverageInflow = MTDCashInflow / DayOfMonth;
          
          
      		
      		
      		
            //Free of Charge
            s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
            String FOCInvoiceIDs = "";
            ResultSet rs11 = s.executeQuery("select group_concat(invoice_no) from inventory_delivery_note idn where idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
            if(rs11.first()){
            	FOCInvoiceIDs = rs11.getString(1);
            }
            
            
            Datasource dsSAP = new Datasource();
            dsSAP.createConnectionToSAPDB();
            Statement sSAP = dsSAP.createStatement();
            
            s.executeUpdate("create temporary table temp_lifting_promotions (vbeln bigint(11), product_id int(11), cases double, bottles double, gross_value double)");
            
            ResultSet rsSAP = sSAP.executeQuery("select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.fkart in ('ZDIS', 'ZMRS', 'ZDFR') and vbrk.fksto != 'X' and vbrk.vbeln in ("+FOCInvoiceIDs+") and vbrp.pstyv = 'TANN'");
            while(rsSAP.next()){
            	
    			long SAPCode = Utilities.parseLong(rsSAP.getString("matnr"));
            	String vrkme = rsSAP.getString("VRKME");
    			boolean isBottle = false;
    			long quantity = rsSAP.getInt("quantity");
    			long VBELN = rsSAP.getLong(1);
    			
    			int ProductID = 0;
    			int CategoryID = 0;
    			ResultSet rs12 = s.executeQuery("select package_id, id, type_id, category_id from inventory_products where sap_code = "+SAPCode);
    			if (rs12.first()){
    				ProductID = rs12.getInt(2);
    				CategoryID = rs12.getInt(4);
    			}
    			
    			if (vrkme.equals("BOT")){
    				isBottle = true;
    			}
    			
    			if (CategoryID == 1){
    			
    				if (isBottle){
    					s.executeUpdate("insert into temp_lifting_promotions (vbeln, product_id, cases, bottles, gross_value) values ("+VBELN+","+ProductID+",0,"+quantity+","+rsSAP.getDouble("gross_value")+")");	
    				}else{
    					s.executeUpdate("insert into temp_lifting_promotions (vbeln, product_id, cases, bottles, gross_value) values ("+VBELN+","+ProductID+","+quantity+",0,"+rsSAP.getDouble("gross_value")+")");
    				}
    				
    				
    			}
    			
            	
            }
            
            double FOCConvertedCases =0;
            double FOCGrossValue = 0;
            ResultSet rs13 = s.executeQuery("select tlp.vbeln, ipv.package_label, ipv.brand_label, ipv.product_id, ((((tlp.cases * ipv.unit_per_sku) + tlp.bottles) * ipv.liquid_in_ml) / ipv.conversion_rate_in_ml) converted_cases, gross_value from temp_lifting_promotions tlp join inventory_products_view ipv on tlp.product_id = ipv.product_id ");
            while(rs13.next()){
            	FOCConvertedCases += rs13.getDouble("converted_cases");
            	FOCGrossValue += rs13.getDouble("gross_value");
            }
            
            
            
            
            // Discounts from SAP
            
            
            Statement sSAP2 = dsSAP.createStatement();
            Statement sSAP3 = dsSAP.createStatement();
            dsSAP.startTransaction();
            
            
            sSAP2.executeUpdate("create global temporary table temp_invoice_list3 (invoice_no VARCHAR2(30 BYTE))");
            
            ResultSet rs14 = s.executeQuery("select invoice_no from inventory_delivery_note idn where idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
            while(rs14.next()){
            	sSAP2.executeUpdate("insert into temp_invoice_list3 values ( '"+ Utilities.addLeadingZeros(rs14.getString("invoice_no"), 10)+"' ) ");
            }
            
            double TotalDiscount = 0;
            ResultSet rsSAP2 = sSAP3.executeQuery("select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.fkart in ('ZDIS','ZMRS','ZDFR') and vbrk.vbeln in (select invoice_no from temp_invoice_list3) order by vbrk.vbeln");
            while(rsSAP2.next()){
            	
            	double UpfrontDiscount = 0;
            	ResultSet rs4 = s.executeQuery("select kbetr from sap_konv where knumv = "+rsSAP2.getLong("knumv")+" and kposn = "+rsSAP2.getInt("posnr"));
            	if (rs4.first()){
            		UpfrontDiscount = rs4.getDouble(1);
            	}
            	UpfrontDiscount = UpfrontDiscount * rsSAP2.getInt("quantity");
            	
            	TotalDiscount += UpfrontDiscount;
            	
            }
            
            TotalDiscount = TotalDiscount * -1;
            
            dsSAP.commit();
            sSAP2.executeUpdate("drop table temp_invoice_list3");
            
            dsSAP.dropConnection();
      		
	        double CasesTodaySD1 = 0;
	        ResultSet rs2SD1 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID +")");
	        if(rs2SD1.first()){
	      	  CasesTodaySD1 = rs2SD1.getDouble(1);
	        }
	        
	        double CasesMTDSD1 = 0;
	        ResultSet rs3SD1 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+")");
	        if(rs3SD1.first()){
	      	  CasesMTDSD1 = rs3SD1.getDouble(1);
	        }
      		
	        double CasesTodaySD2 = 0;
	        ResultSet rs2SD2 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID +")");
	        if(rs2SD2.first()){
	      	  CasesTodaySD2 = rs2SD2.getDouble(1);
	        }
	        
	        double CasesMTDSD2 = 0;
	        ResultSet rs3SD2 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+")");
	        if(rs3SD2.first()){
	      	  CasesMTDSD2 = rs3SD2.getDouble(1);
	        }
	        
	        double CasesTodaySD3 = 0;
	        ResultSet rs2SD3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID +")");
	        if(rs2SD3.first()){
	      	  CasesTodaySD3 = rs2SD3.getDouble(1);
	        }
	        
	        double CasesMTDSD3 = 0;
	        ResultSet rs3SD3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+")");
	        if(rs3SD3.first()){
	      	  CasesMTDSD3 = rs3SD3.getDouble(1);
	        }

	        double CasesTodayBulk = 0;
	        ResultSet rs2Bulk = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where snd_id = 2646)");
	        if(rs2Bulk.first()){
	      	  CasesTodayBulk = rs2Bulk.getDouble(1);
	        }
	        
	        double CasesMTDBulk = 0;
	        ResultSet rs3Bulk = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where snd_id = 2646)");
	        if(rs3Bulk.first()){
	      	  CasesMTDBulk = rs3Bulk.getDouble(1);
	        }
	        

	        double CasesTodayOther = 0;
	        ResultSet rs2Other = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where (snd_id is null or snd_id not in (2646,"+EmployeeHierarchy.getSDHead(1).USER_ID+","+EmployeeHierarchy.getSDHead(2).USER_ID+","+EmployeeHierarchy.getSDHead(4).USER_ID+")))");
	        if(rs2Other.first()){
	      	  CasesTodayOther = rs2Other.getDouble(1);
	        }
	        
	        double CasesMTDOther = 0;
	        ResultSet rs3Other = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and idn.distributor_id in (select distributor_id from common_distributors where (snd_id is null or snd_id not in (2646,"+EmployeeHierarchy.getSDHead(1).USER_ID+","+EmployeeHierarchy.getSDHead(2).USER_ID+","+EmployeeHierarchy.getSDHead(4).USER_ID+")))");
	        if(rs3Other.first()){
	      	  CasesMTDOther = rs3Other.getDouble(1);
	        }
	        
	      double AllAreasToday = CasesTodaySD1+CasesTodaySD2+CasesTodaySD3+CasesTodayBulk;
	      double AllAreasMTD = CasesMTDSD1+CasesMTDSD2+CasesMTDSD3+CasesMTDBulk;
	      double TotalToday = AllAreasToday+CasesTodayOther;
	      double TotalMTD = AllAreasMTD+CasesMTDOther;
	      
          // SMS
          String message = "Lifting "+Utilities.getDisplayDateFormat(YesterdayDate)+":\\n";
          message += "Cases: "+Utilities.getDisplayCurrencyFormatRounded(CasesToday)+"\\n";
          message += "Amount: "+Utilities.getDisplayCurrencyFormatRounded(AmountToday)+"\\n";
          message += "This Month:\\n";
          message += "Cases: "+Utilities.getDisplayCurrencyFormatRounded(CasesMTD)+"\\n";
          message += "Amount: "+Utilities.getDisplayCurrencyFormatRounded(AmountMTD)+"\\n \\n";
          message += "Source Reports: R111, R118, R119, R121, R122";
          
          // WhatsApp with Amount
          String messageWA = "-------------------------\nLifting Summary\n-------------------------\n\n"+Utilities.getDisplayFullDateFormat(YesterdayDate)+"\n";
          messageWA += "\tCases: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
          messageWA += "\tAmount: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AmountToday)+"\n";
          messageWA += "\nThis Month, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWA += "\tCases: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTD)+"\n";
          messageWA += "\tAmount: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AmountMTD)+"\n \n";
          messageWA += "Source Reports: R111, R118, R119, R121, R122";
          
          
          String messageWithoutCash = "-------------------------\nLifting Summary\n-------------------------\n\n"+Utilities.getDisplayFullDateFormat(YesterdayDate)+"\n";
          messageWithoutCash += "\tCases: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
          messageWithoutCash += "\nThis Month, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWithoutCash += "\tCases: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTD)+"\n";
          messageWithoutCash += "\tAverage: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCases)+" / Day\n\n";
          
          
          // WhatsApp with Cash
          String messageWCash = "-------------------------\nLifting Summary\n-------------------------\n\n"+Utilities.getDisplayFullDateFormat(YesterdayDate)+"\n";
          messageWCash += "\tCases: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
          messageWCash += "\tAmount: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AmountToday)+"\n";
          messageWCash += "\nThis Month, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWCash += "\tCases: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTD)+"\n";
          messageWCash += "\tAverage: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCases)+" / Day\n\n";
          messageWCash += "\tAmount: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AmountMTD)+"\n";
          messageWCash += "\tAverage: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageAmount)+" / Day\n";
          messageWCash += "\n----------------------------\nDiscounts\n----------------------------\n\n"+Utilities.getDisplayFullDateFormat(YesterdayDate)+"\n";
          //messageWCash += "\tCases: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(FOCConvertedCases)+" ("+Utilities.getDisplayCurrencyFormatOneDecimal((FOCConvertedCases/CasesToday)*100)+"%) \n";
          messageWCash += "\tPromotion: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(FOCGrossValue)+" ("+Utilities.getDisplayCurrencyFormatOneDecimal((FOCGrossValue/AmountToday)*100)+"%) \n";
          messageWCash += "\tUpfront: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalDiscount)+" ("+Utilities.getDisplayCurrencyFormatOneDecimal((TotalDiscount/AmountToday)*100)+"%) \n";
          messageWCash += "\tTotal Discount: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(FOCGrossValue+TotalDiscount)+" ("+Utilities.getDisplayCurrencyFormatOneDecimal(((FOCGrossValue+TotalDiscount)/AmountToday)*100)+"%) \n";          
 		  messageWCash += "\n----------------------------\nCash/Bank Inflow\n----------------------------\n\n"+Utilities.getDisplayFullDateFormat(YesterdayDate)+"\n";
		  messageWCash += "\tAmount: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashInflow)+"\n";
		  messageWCash += "\nThis Month, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
		  messageWCash += "\tAmount: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(MTDCashInflow)+"\n";
		  messageWCash += "\tAverage: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageInflow)+" / Day\n\n";
 		  messageWCash += "\n----------------------------\nAreas (Converted)\n----------------------------\n\n"+Utilities.getDisplayFullDateFormat(YesterdayDate)+"\n";
		  messageWCash += "\tSD1: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesTodaySD1)+"\n";
		  messageWCash += "\tSD2: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesTodaySD2)+"\n";
		  messageWCash += "\tSD3: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesTodaySD3)+"\n";
		  messageWCash += "\tBulk: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesTodayBulk)+"\n";
		  messageWCash += "\tAll Areas: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(AllAreasToday)+"\n";
		  messageWCash += "\tOther: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesTodayOther)+"\n";
		  messageWCash += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(TotalToday)+"\n";
		  
		  messageWCash += "\nThis Month, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
		  messageWCash += "\tSD1: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesMTDSD1)+"\n";
		  messageWCash += "\tSD2: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesMTDSD2)+"\n";
		  messageWCash += "\tSD3: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesMTDSD3)+"\n";
		  messageWCash += "\tBulk: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesMTDBulk)+"\n";
		  messageWCash += "\tAll Areas: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(AllAreasMTD)+"\n";
		  messageWCash += "\tOther: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(CasesMTDOther)+"\n";
		  messageWCash += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedThousandsOneDecimal(TotalMTD)+"\n";
          // SMS
		  /*
		  Utilities.sendSMS("923444471320", message); // Khawja Obaid
          Utilities.sendSMS("923444471064", message); // Jazeb
          Utilities.sendSMS("923444471999", message); // Asim Maan
          Utilities.sendSMS("923008660885", message); // Nadeem Gulzar
          Utilities.sendSMS("923444471200", message); // Anas Wahab
          Utilities.sendSMS("923444477575", messageWithoutAmount); // Naveed Supply Chain
          Utilities.sendSMS("923444471135", messageWithoutAmount); // Bilal Ahmad
          */
          // WhatsApp Messages with Cash 
          
		  //786
		  //Utilities.sendWhatsApp("923008406444-1408475526", messageWCash, null); // Anas Wahab
		  Utilities.sendTelegram("-134060758", messageWCash, null); // Executive Summary Group
		  Utilities.sendTelegram("-121808227", messageWCash, null); // Daily Summary Group
		  Utilities.sendTelegram("-128535487", messageWithoutCash, null); // Production Group
		  /*
		  Utilities.sendWhatsApp("923444002777", messageWCash, null); // Imran Hashim
		  Utilities.sendWhatsApp("923458468658", messageWCash, null); // Omer Farooq Khan
          Utilities.sendWhatsApp("923018430696", messageWCash, null); // Atiq Baloch
          Utilities.sendWhatsApp("923444471064", messageWCash, null); // Jazeb
          Utilities.sendWhatsApp("923444471320", messageWCash, null); // Khawja Obaid
          Utilities.sendWhatsApp("923444471999", messageWCash, null); // Asim Maan
          Utilities.sendWhatsApp("923008660885", messageWCash, null); // Nadeem Gulzar
          Utilities.sendWhatsApp("923452004454", messageWCash, null); // AAA
          */
		  /*
		  Utilities.sendSMS("923444002777", messageWCash); // Imran Hashim
		  Thread.currentThread().sleep(10000);
		  Utilities.sendSMS("923458468658", messageWCash); // Omer Farooq Khan
		  Thread.currentThread().sleep(10000);
		  Utilities.sendSMS("923018430696", messageWCash); // Atiq Baloch
		  Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471064", messageWCash); // Jazeb
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471320", messageWCash); // Khawja Obaid
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471999", messageWCash); // Asim Maan
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923452004454", messageWCash); // AAA
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471200", messageWCash); // AAA
          Thread.currentThread().sleep(10000);
          // Without Cash
          */
		  /*
		  Utilities.sendWhatsApp("923444477575", messageWithoutCash, null); // Naveed Supply Chain
          Utilities.sendWhatsApp("923444471135", messageWithoutCash, null); // Bilal Ahmad
          Utilities.sendWhatsApp("923444471000", messageWithoutCash, null); // Shafqat Riaz
          */
		  /*
		  Utilities.sendSMS("923444477575", messageWithoutCash); // Naveed Supply Chain
		  Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471135", messageWithoutCash); // Bilal Ahmad
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471000", messageWithoutCash); // Shafqat Riaz
          Thread.currentThread().sleep(10000);
		  */
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    }
    

}
