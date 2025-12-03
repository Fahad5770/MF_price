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
public class S210LiftingUpdateClosing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S210LiftingUpdateClosing() {
        super();
    }

    
    public static void main(String [] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	Date YesterdayDate = Utilities.getDateByDays(-1); 
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        
        
        
        
    	try {
    	
    	   Datasource ds = new Datasource();
   		   ds.createConnection();
   			
   		   Statement s = ds.createStatement();
          
          //Converted Cases Today
          double CasesToday = 0;
          ResultSet rs2 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2368) or snd_id in (2646)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesToday = rs2.getDouble(1);
          }
          
          	double CashToday = 0;
			ResultSet crs2 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2368) and glci.is_internal = 0 ");
			if(crs2.first()){
				CashToday = crs2.getDouble(1);
			}
          
          
          //Converted Cases Today
          double CasesTodaySD3 = 0;
          ResultSet rs2SD3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2252)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD3.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD3 = rs2SD3.getDouble(1);
          }
          
        	double CashTodaySD3 = 0;
			ResultSet crs3 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2252) and glci.is_internal = 0 ");
			if(crs3.first()){
				CashTodaySD3 = crs3.getDouble(1);
			}
          
          
          
          //Converted Cases Today
          
			double CasesTodaySD2 = 0;
          ResultSet rs2SD2 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3498)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD2 = rs2SD2.getDouble(1);
          }
          
      		double CashTodaySD2 = 0;
			ResultSet crs4 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where rsm_id = 1910) and glci.is_internal = 0 ");
			if(crs4.first()){
				CashTodaySD2 = crs4.getDouble(1);
			}
			double CasesTodaySD4 = 0;
	          ResultSet rs2SD4 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3645)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	          if(rs2SD4.first()){
	        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
	        	  CasesTodaySD4 = rs2SD4.getDouble(1);
	          }
	          
	      		double CashTodaySD4 = 0;
				ResultSet crs14 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 3645) and glci.is_internal = 0 ");
				if(crs14.first()){
					CashTodaySD4 = crs14.getDouble(1);
				}

				double CasesTodayKA = 0;
		          ResultSet rs2KA = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3644)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
		          if(rs2KA.first()){
		        	  CasesTodayKA = rs2KA.getDouble(1);
		          }
		          
		      		double CashTodayKA = 0;
					ResultSet crs15 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 3644) and glci.is_internal = 0 ");
					if(crs15.first()){
						CashTodayKA = crs15.getDouble(1);
					}
          
          /*
          //Converted Cases Today
          double CasesTodaySD2B = 0;
          ResultSet rs2SD2B = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in (2308)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD2B.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD2B = rs2SD2B.getDouble(1);
        	  
          }*/

    		double CashTodaySD2B = 0;
			ResultSet crs5 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where rsm_id = 2308) and glci.is_internal = 0 ");
			if(crs5.first()){
				CashTodaySD2B = crs5.getDouble(1);
			}
          
          /*
          //Converted Cases Today
          double CasesTodayBulk = 0;
          ResultSet rs2Bulk = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2646)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2Bulk.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodayBulk = rs2Bulk.getDouble(1);
        	  
          }
          */
    		double CashTodayBulk = 0;
			ResultSet crs6 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2646) and glci.is_internal = 0 ");
			if(crs6.first()){
				CashTodayBulk = crs6.getDouble(1);
			}
          
          
          
          //Converted Cases Month to date          
          double CasesMTD = 0;
          ResultSet rs3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2368) or snd_id in (2646)) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTD = rs3.getDouble(1);
          }
        	double CashMTD = 0;
			ResultSet crs7 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2368) and glci.is_internal = 0 ");
			if(crs7.first()){
				CashMTD = crs7.getDouble(1);
			}
          
          double CasesMTDSD3 = 0;
          ResultSet rs3SD3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2252)) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3SD3.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDSD3 = rs3SD3.getDouble(1);
          }
          	double CashMTDSD3 = 0;
			ResultSet crs8 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2252) and glci.is_internal = 0 ");
			if(crs8.first()){
				CashMTDSD3 = crs8.getDouble(1);
			}
          
			  double CasesMTDSD4 = 0;
			  ResultSet rs3SD4 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3645)) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
			  if(rs3SD4.first()){
				  CasesMTDSD4 = rs3SD4.getDouble(1);
			  }
	      	double CashMTDSD4 = 0;
			ResultSet crs18 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 3645) and glci.is_internal = 0 ");
			if(crs18.first()){
				CashMTDSD4 = crs18.getDouble(1);
			}

			  double CasesMTDKA = 0;
			  ResultSet rs3KA = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3644)) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
			  if(rs3KA.first()){
				  CasesMTDKA = rs3KA.getDouble(1);
			  }
	      	double CashMTDKA = 0;
			ResultSet crs19 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 3644) and glci.is_internal = 0 ");
			if(crs19.first()){
				CashMTDKA = crs19.getDouble(1);
			}			
			
          double CasesMTDSD2 = 0;
          ResultSet rs3SD2 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3498)) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3SD2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDSD2 = rs3SD2.getDouble(1);
          }
          /*
        	double CashMTDSD2 = 0;
			ResultSet crs9 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where rsm_id = 1910) and glci.is_internal = 0 ");
			if(crs9.first()){
				CashMTDSD2 = crs9.getDouble(1);
			}
          
          
          double CasesMTDSD2B = 0;
          ResultSet rs3SD2B = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in (2308)) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3SD2B.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDSD2B = rs3SD2B.getDouble(1);
          }
          
      		double CashMTDSD2B = 0;
			ResultSet crs10 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where rsm_id = 2308) and glci.is_internal = 0 ");
			if(crs10.first()){
				CashMTDSD2B = crs10.getDouble(1);
			}
          
          
          double CasesMTDBulk = 0;
          ResultSet rs3Bulk = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2646)) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3Bulk.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDBulk = rs3Bulk.getDouble(1);
          }

    		double CashMTDBulk = 0;
			ResultSet crs11 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2646) and glci.is_internal = 0 ");
			if(crs11.first()){
				CashMTDBulk = crs11.getDouble(1);
			}
          */

          int DayOfMonth = 1;
          ResultSet rs10 = s.executeQuery("select dayofmonth(cast("+Utilities.getSQLDate(YesterdayDate)+" as date))");
          if (rs10.first()){
        	  DayOfMonth = rs10.getInt(1);
          }
          
          
          double TotalMTD = CasesMTD+CasesMTDSD3+CasesMTDSD2+CasesMTDSD4+CasesMTDKA;
          double TotalMTDCash = CashMTD+CashMTDSD3;
          
          double AverageCases = CasesMTD / DayOfMonth;
          double AverageCasesSD3 = CasesMTDSD3 / DayOfMonth;
          //double AverageCasesSD2 = CasesMTDSD2 / DayOfMonth;
          //double AverageCasesSD2B = CasesMTDSD2B / DayOfMonth;
          //double AverageCasesBulk = CasesMTDBulk / DayOfMonth;
          double AverageCasesTotal = TotalMTD / DayOfMonth;
          
          double AverageCash = CashMTD / DayOfMonth;
          double AverageCashSD3 = CashMTDSD3 / DayOfMonth;
          //double AverageCashSD2 = CashMTDSD2 / DayOfMonth;
          //double AverageCashSD2B = CashMTDSD2B / DayOfMonth;
          //double AverageCashBulk = CashMTDBulk / DayOfMonth;
          double AverageCashTotal = TotalMTDCash / DayOfMonth;
          
          
          String messageWA = "---------------------------\nLifting Update (CC)\n---------------------------\n\nToday\n";
          
          messageWA += "\tSaeed Abbas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
          messageWA += "\tJawad Akram: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD2)+"\n";
          messageWA += "\tArshad Mehboob: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD3)+"\n";
          messageWA += "\tMuhammad Ajmal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD4)+"\n";
          messageWA += "\tKhurram Jaffar: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodayKA)+"\n";
          //messageWA += "\tRana Attique: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD2)+"\n";
          //messageWA += "\tM Ilyas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD2B)+"\n";
          //messageWA += "\tJawad (Bulk): "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodayBulk)+"\n";
          messageWA += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday+CasesTodaySD2+CasesTodaySD3+CasesTodaySD4+CasesTodayKA)+"\n";
          
          messageWA += "\nMTD, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWA += "\tSaeed Abbas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTD)+"\n";
          messageWA += "\tJawad Akram: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD2)+"\n";
          messageWA += "\tMuhammad Ajmal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD4)+"\n";
          messageWA += "\tKhurram Jaffar: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDKA)+"\n";
          //messageWA += "\tRana Attique: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD2)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesSD2)+"/Day)\n";
          //messageWA += "\tM Ilyas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD2B)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesSD2B)+"/Day)\n";
          //messageWA += "\tJawad (Bulk): "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDBulk)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesBulk)+"/Day)\n";
          messageWA += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTD)+"\n\n"+Utilities.getDisplayDateTimeFormat(new Date());

          
          String messageWAC = "-------------------------\nCash Update\n-------------------------\n\nToday\n";
          messageWAC += "\tSaeed Abbas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashToday)+"\n";
          messageWAC += "\tArshad Mehboob: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD3)+"\n";
          messageWAC += "\tRana Attique: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD2)+"\n";
          messageWAC += "\tM Ilyas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD2B)+"\n";
          messageWAC += "\tJawad (Bulk): "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodayBulk)+"\n";
          messageWAC += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashToday+CashTodaySD3+CashTodaySD2+CashTodaySD2B+CashTodayBulk+CashTodaySD4+CashTodayKA)+"\n";
          
          messageWAC += "\nMTD, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWAC += "\tSaeed Abbas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCash)+"/Day)\n";
          messageWAC += "\tArshad Mehboob: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD3)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashSD3)+"/Day)\n";
          //messageWAC += "\tRana Attique: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD2)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashSD2)+"/Day)\n";
          //messageWAC += "\tM Ilyas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD2B)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashSD2B)+"/Day)\n";
          //messageWAC += "\tJawad (Bulk): "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDBulk)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashBulk)+"/Day)\n";
          messageWAC += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTDCash)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashTotal)+"/Day)\n";

          //Utilities.sendWhatsApp("923444471320-1453789939", "Test Message from Theia", null);
          
          /*
          Utilities.sendWhatsApp("923008406444-1408475526", messageWA, null);
          Utilities.sendWhatsApp("923008406444-1408475526", messageWAC, null);
          
          Utilities.sendWhatsApp("923444471320-1453789939", messageWA, null);
          Utilities.sendWhatsApp("923444471320-1459848194", messageWAC, null); // Cash Colleciton Group
          */
          
          /*
          Utilities.sendSMS("923444002777", messageWA); // Imran Hashim
          Thread.currentThread().sleep(5000);
          Utilities.sendSMS("923444002777", messageWAC);// Imran Hashim
          Thread.currentThread().sleep(5000);
          
          Utilities.sendSMS("923444471320", messageWA); // Obaid
          Thread.currentThread().sleep(5000);
          Utilities.sendSMS("923444471320", messageWAC);// Obaid
          Thread.currentThread().sleep(5000);
          
          Utilities.sendSMS("923444471200", messageWA); // Anas
          Thread.currentThread().sleep(5000);
          Utilities.sendSMS("923444471200", messageWAC);// Anas
          Thread.currentThread().sleep(5000);
          */
          
          
          
          
          
          Utilities.sendTelegram("-105413386", messageWA, null);
          
          
          
          
          
          
//          Utilities.sendTelegram("-134060758", messageWA, null);
//          Utilities.sendTelegram("-114584068", messageWA, null);
//          Utilities.sendTelegram("-125339765", messageWA, null);
          
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    }
    

}
