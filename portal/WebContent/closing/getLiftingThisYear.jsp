<%@ page import="java.util.Date"%><%@ page import="com.pbc.util.Utilities"%><%@ page import="java.util.Calendar"%><%@ page import="com.pbc.util.Datasource"%><%@ page import="com.pbc.util.Datasource"%><%@ page import="java.sql.Statement"%><%@ page import="java.sql.ResultSet"%><%

Date YesterdayDate = Utilities.getDateByDays(-1); 
Date StartDate = new Date();

Calendar cc = Calendar.getInstance();
cc.setTime(YesterdayDate);

int year = cc.get(Calendar.YEAR);
int month = cc.get(Calendar.MONTH);

Date MonthToDateStartDate = Utilities.getStartDateByMonth(1, year);
Date MonthToDateEndDate = new Date();

try {
	   Datasource ds = new Datasource();
	   ds.createConnection();
		
	   Statement s = ds.createStatement();
       double CasesMTD = 0;
       ResultSet rs3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (select distributor_id from common_distributors where snd_id in (2252,2262,2368,2646)) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate));
       if(rs3.first()){
     	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
     	  CasesMTD = rs3.getDouble(1);
       }

	   
	   out.print(Utilities.getDisplayCurrencyFormatRounded(CasesMTD));
	   
}catch(Exception e)
{
    e.printStackTrace();
 }
%>