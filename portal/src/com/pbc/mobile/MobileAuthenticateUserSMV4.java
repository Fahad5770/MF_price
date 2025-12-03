// 
// Decompiled by Procyon v0.5.36
// 

package com.pbc.mobile;

import java.io.IOException;
import javax.servlet.ServletException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import com.pbc.employee.OrderBookerDashboard;
import com.pbc.bi.BiProcesses;
import java.util.Date;
import java.util.LinkedHashMap;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(description = "Mobile Authenticate User V4", urlPatterns = { "/mobile/MobileAuthenticateUserSMV4" })
public class MobileAuthenticateUserSMV4 extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("json");
        final PrintWriter out = response.getWriter();
        final JSONObject json = new JSONObject();
        final JSONArray jr = new JSONArray();
        final JSONArray jr_pjp = new JSONArray();
        final JSONArray jr2 = new JSONArray();
        final JSONArray jr3 = new JSONArray();
        final JSONArray jr4 = new JSONArray();
        JSONArray jr5 = new JSONArray();
        final JSONArray jr6 = new JSONArray();
        JSONArray jr7 = new JSONArray();
        final JSONArray jr8 = new JSONArray();
        final JSONArray jr9 = new JSONArray();
        final JSONArray jr10 = new JSONArray();
        final JSONArray jr11 = new JSONArray();
        final JSONArray jr12 = new JSONArray();
        final JSONArray jr13 = new JSONArray();
        final JSONArray jr14 = new JSONArray();
        final JSONArray jr15 = new JSONArray();
        String UserTypeCheck = "";
        final MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
        if (!mr.isExpired()) {
            final long LoginUsername = Utilities.parseLong(mr.getParameter("LoginUsername"));
            final String LoginPassword = Utilities.filterString(mr.getParameter("LoginPassword"), 1, 100);
            final String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
            System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername);
            int LogTypeID = 0;
            final Datasource ds = new Datasource();
            final Datasource dsr = new Datasource();
            try {
                ds.createConnection();
                dsr.createConnectionToReplica();
                final Statement sr = dsr.createStatement();
                final Statement s = ds.createStatement();
                final Statement s2 = ds.createStatement();
                final Statement s3 = ds.createStatement();
                final Statement s4 = ds.createStatement();
                final Statement s5 = ds.createStatement();
                final Statement s6 = ds.createStatement();
                final ResultSet rsD = s.executeQuery("select id from mobile_devices where uuid = '" + DeviceID + "'");
                if (rsD.first()) {
                    final ResultSet rs = s.executeQuery("select md5('" + LoginPassword + "'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT from users where ID=" + LoginUsername + " and IS_ACTIVE=1 ");
                    if (rs.first()) {
                        if (rs.getString(1).equals(rs.getString(2)) | LoginPassword.equals(Utilities.getMobileAdminPassword())) {
                            LogTypeID = 4;
                            json.put("success","true");
                            json.put("UserID", LoginUsername);
                            json.put("DisplayName", rs.getString("DISPLAY_NAME"));
                            json.put("Designation", rs.getString("DESIGNATION"));
                            json.put("Department", rs.getString("DEPARTMENT"));
                            String distributorsList = "";
                            
                            
                            final String sql_dist = "SELECT group_concat(distinct distributor_id) distributor_ids FROM common_distributors where snd_id=" + LoginUsername + " or rsm_id=" + LoginUsername;
                            System.out.println(sql_dist);
                            final ResultSet rs_dist = s6.executeQuery(sql_dist);
                            if (rs_dist.first()) {
                                System.out.println("distributor_ids = " + rs_dist.getString(1));
                                distributorsList = rs_dist.getString(1);
                                if (rs_dist.getString(1) == null) {
                                    final String sql_dist_asm = "SELECT group_concat(distinct distributor_id) distributor_ids FROM distributor_beat_plan where asm_id=" + LoginUsername;
                                    System.out.println(sql_dist_asm);
                                    final ResultSet rs_dist_asm = s5.executeQuery(sql_dist_asm);
                                    if (rs_dist_asm.first()) {
                                        System.out.println("distributor_ids - asm = " + rs_dist_asm.getString(1));
                                        distributorsList = rs_dist_asm.getString(1);
                                    }
                                }
                            }
                            System.out.println("distributorsList = " + distributorsList);
                            // String sql_pjp = "SELECT distinct id, label FROM distributor_beat_plan where distributor_id in (" + distributorsList + ") order by label";
                            String sql_pjp = "select distinct id, label from common_distributors cd join distributor_beat_plan bp on cd.distributor_id=bp.distributor_id where cd.distributor_id in (" + distributorsList + ") and cd.is_active=1 order by label";
                           // String sql_pjp = " SELECT dbp.id, label FROM pep.distributor_beat_plan dbp left join distributor_beat_plan_users dbpu on dbp.id=dbpu.id where dbp.asm_id="+LoginUsername+" or dbpu.assigned_to="+LoginUsername+" order by dbp.distributor_id asc";
                            if(LoginUsername == 1003 || LoginUsername == 204211264 || LoginUsername == 204211220) {
                            //	sql_pjp = "SELECT distinct id, concat((select name from common_distributors cd where cd.distributor_id=bp.distributor_id),' - ',label) as label FROM distributor_beat_plan bp order by label";
                            	sql_pjp = "select distinct id, concat( name ,' - ',label) as label from common_distributors cd join distributor_beat_plan bp on cd.distributor_id=bp.distributor_id where cd.is_active=1";
                            }
                            System.out.println(sql_pjp);
                            final ResultSet rs_pjp = s6.executeQuery(sql_pjp);
                            while (rs_pjp.next()) {
                            	
                            	
                                final LinkedHashMap rows = new LinkedHashMap();
                                rows.put("value", rs_pjp.getString("id"));
                                rows.put("text", rs_pjp.getString("label"));
                                jr_pjp.add(rows);
                            }
                            json.put("pjp_rows",jr_pjp);
                            
                            
                             ResultSet rsCheck = s6.executeQuery("select id from distributor_beat_plan_view where asm_id=" + rs.getString("ID"));
                            if (rsCheck.first()) {
                                UserTypeCheck = "dbpv.asm_id";
                            }
                            else {
                                 ResultSet rsCheck2 = s6.executeQuery("select id from distributor_beat_plan_view where assigned_to=" + rs.getString("ID"));
                                if (rsCheck2.first()) {
                                    UserTypeCheck = "dbpv.assigned_to";
                                }
                                else {
                                    UserTypeCheck = "dbpv.asm_id";
                                }
                            }
                            json.put("BeatPlanID",1);
                            
                            
                            
                            
                            String PJPquery = "select dbpv.outlet_id, co.name outlet_name,dbpv.distributor_id ,dbpv.day_number, coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 where " + UserTypeCheck + " = " + rs.getString("ID");
                           System.out.println(PJPquery);
                            ResultSet rs2 = s3.executeQuery(PJPquery);
                            while (rs2.next()) {
                                final LinkedHashMap rows2 = new LinkedHashMap();
                                rows2.put("OutletID", rs2.getString("outlet_id"));
                                rows2.put("OutletName", rs2.getString("outlet_name"));
                                rows2.put("DayNumber", rs2.getString("day_number"));
                                rows2.put("Owner", rs2.getString("owner"));
                                rows2.put("Address", rs2.getString("address"));
                                rows2.put("Telepohone", rs2.getString("telepohone"));
                                rows2.put("NFCTagID", rs2.getString("nfc_tag_id"));
                                rows2.put("DistributorID", rs2.getString("distributor_id"));
                                jr.add(rows2);
                            }
                            System.out.println("MobileAuthenticateUserV3 User:" + LoginUsername + " Step1");
                            json.put("BeatPlanRows",jr);
                            
                            if (jr_pjp.size() > 0) {
                                json.put("BeatPlanRows", null);
                            }
                             ResultSet rs2ps = s2.executeQuery("SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code=" + rs.getString("ID"));
                            if (rs2ps.first()) {
                                json.put("ProductGroupID", rs2ps.getString("employee_product_group_id"));
                                 //ResultSet rs3pg = s3.executeQuery("SELECT epl.product_id, ib.label brand, ipa.label package, ipa.sort_order as sort_order, ipa.unit_per_case as unit_per_case, ipa.id as package_id, ib.id as brand_id FROM employee_product_groups_list epl, inventory_products ip, inventory_brands ib, inventory_packages ipa where epl.product_id = ip.id and ip.brand_id = ib.id and ip.package_id = ipa.id and epl.product_group_id=" + rs2ps.getString("employee_product_group_id"));
                                ResultSet rs3pg = s3.executeQuery("select  lrb_type_id,brand_label,package_label,(select unit_per_case from inventory_packages where id=ipv.package_id) as unit_per_case,(select sort_order from inventory_packages where id=ipv.package_id) as sort_order,ipv.brand_id,ipv.package_id,ipv.product_id,concat(package_label,' - ',brand_label) as product, (select label from pep.inventory_products_lrb_types where id=lrb_type_id) as lrb_label from inventory_products_view ipv join employee_product_groups_list epl on  epl.product_id = ipv.product_id where epl.product_group_id="+rs2ps.getString("employee_product_group_id")+"  and is_visible=1 order by lrb_type_id");
                                 while (rs3pg.next()) {
                                    final LinkedHashMap rows3 = new LinkedHashMap();
                                    rows3.put("ProductID", rs3pg.getString("product_id"));
                                    rows3.put("Brand", rs3pg.getString("brand_label"));
                                    rows3.put("Package", rs3pg.getString("package_label"));
                                    rows3.put("SortOrder", rs3pg.getString("sort_order"));
                                    rows3.put("UnitPerCase", rs3pg.getString("unit_per_case"));
                                    rows3.put("PackageID", rs3pg.getString("package_id"));
                                    rows3.put("BrandID", rs3pg.getString("brand_id"));
                                    rows3.put("LRB", rs3pg.getString("lrb_label"));
                                    rows3.put("LRBID", rs3pg.getString("lrb_type_id"));
                                    rows3.put("Product", rs3pg.getString("product"));
                                    jr2.add(rows3);
                                }
                                json.put("ProductGroupRows", jr2);
                            }
                             ResultSet rs3 = s2.executeQuery("SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select label from inventory_packages where id=package_id) package_label, (select label from inventory_brands where id=brand_id) brand_label, (select unit_per_case from inventory_packages where id=package_id) unit_per_case, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml FROM inventory_price_list_products where id = 1");
                            while (rs3.next()) {
                                final LinkedHashMap rows3 = new LinkedHashMap();
                                rows3.put("id", rs3.getString("id"));
                                rows3.put("ProductID", rs3.getString("product_id"));
                                rows3.put("PackageID", rs3.getString("package_id"));
                                rows3.put("BrandID", rs3.getString("brand_id"));
                                rows3.put("PackageLabel", rs3.getString("package_label"));
                                rows3.put("BrandLabel", rs3.getString("brand_label"));
                                rows3.put("UnitPerCase", rs3.getString("unit_per_case"));
                                rows3.put("LiquidInML", rs3.getString("liquid_in_ml"));
                                rows3.put("RawCasePrice", rs3.getString("raw_case"));
                                rows3.put("UnitPrice", rs3.getString("unit"));
                                jr3.add(rows3);
                            }
                            json.put("PriceListRows", jr3);
                            System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername + " Step2");
                            final ResultSet rs4 = sr.executeQuery("select distinct assigned_to,(select DISPLAY_NAME from users where id=assigned_to) orderbookerName from distributor_beat_plan_view dbpv  where " + UserTypeCheck + " = " + rs.getString("ID"));
                            while (rs4.next()) {
                                final LinkedHashMap rows4 = new LinkedHashMap();
                                rows4.put("OrderbookerID", rs4.getString("assigned_to"));
                                rows4.put("OrderbookerName", rs4.getString("orderbookerName"));
                                jr12.add(rows4);
                            }
                            json.put("OrderbookersList", jr12);
                            String AllOutletsString = "";
                            int counter = 0;
                             ResultSet rs5 = sr.executeQuery("select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where " + UserTypeCheck + " = " + rs.getString("ID"));
                            while (rs5.next()) {
                                if (counter == 0) {
                                    AllOutletsString = String.valueOf(AllOutletsString) + rs5.getString("outlet_id");
                                }
                                else {
                                    AllOutletsString = String.valueOf(AllOutletsString) + ", " + rs5.getString("outlet_id");
                                }
                                ++counter;
                            }
                            if (AllOutletsString.length() > 0) {
                                final ResultSet rs6 = sr.executeQuery("SELECT * FROM inventory_sales_promotions_active_mview where outlet_id in(" + AllOutletsString + ") order by outlet_id");
                                while (rs6.next()) {
                                    final LinkedHashMap rows5 = new LinkedHashMap();
                                    rows5.put("PromotionID", rs6.getString("product_promotion_id"));
                                    rows5.put("OutletID", rs6.getString("outlet_id"));
                                    jr4.add(rows5);
                                }
                            }
                            if (AllOutletsString.length() > 0) {
                                final ResultSet rs7 = sr.executeQuery("SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in(" + AllOutletsString + ") order by outlet_id");
                                while (rs7.next()) {
                                    final ResultSet rs8 = s3.executeQuery("SELECT package_id, total_units FROM inventory_sales_promotions_products where id=" + rs7.getString("product_promotion_id") + " and type_id = 1");
                                    while (rs8.next()) {
                                        final LinkedHashMap rows6 = new LinkedHashMap();
                                        rows6.put("PromotionID", rs7.getString("product_promotion_id"));
                                        rows6.put("PackageID", rs8.getString("package_id"));
                                        rows6.put("TotalUnits", rs8.getString("total_units"));
                                        jr5 = new JSONArray();
                                        final ResultSet rs9 = s4.executeQuery("SELECT brand_id FROM inventory_sales_promotions_products_brands where id=" + rs7.getString("product_promotion_id") + " and type_id = 1 and package_id = " + rs8.getString("package_id"));
                                        while (rs9.next()) {
                                            final LinkedHashMap rows7 = new LinkedHashMap();
                                            rows7.put("BrandID", rs9.getString("brand_id"));
                                            jr5.add(rows7);
                                        }
                                        rows6.put("Brands", jr5);
                                        jr6.add(rows6);
                                    }
                                }
                            }
                            System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername + " Step3");
                            if (AllOutletsString.length() > 0) {
                                final ResultSet rs10 = sr.executeQuery("SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in(" + AllOutletsString + ") order by outlet_id");
                                while (rs10.next()) {
                                    final ResultSet rs11 = s3.executeQuery("SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id=" + rs10.getString("product_promotion_id") + " and ispp.type_id = 2");
                                    while (rs11.next()) {
                                        final LinkedHashMap rows6 = new LinkedHashMap();
                                        rows6.put("PromotionID", rs10.getString("product_promotion_id"));
                                        rows6.put("PackageID", rs11.getString("package_id"));
                                        rows6.put("TotalUnits", rs11.getString("total_units"));
                                        rows6.put("PackageLabel", rs11.getString("label"));
                                        rows6.put("UnitPerCase", rs11.getString("unit_per_case"));
                                        jr7 = new JSONArray();
                                        final ResultSet rs12 = s4.executeQuery("SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id=" + rs10.getString("product_promotion_id") + " and isppb.type_id = 2 and isppb.package_id = " + rs11.getString("package_id"));
                                        while (rs12.next()) {
                                            final LinkedHashMap rows7 = new LinkedHashMap();
                                            rows7.put("BrandID", rs12.getString("brand_id"));
                                            rows7.put("BrandLabel", rs12.getString("label"));
                                            jr7.add(rows7);
                                        }
                                        rows6.put("Brands", jr7);
                                        jr8.add(rows6);
                                    }
                                }
                            }
                            if (AllOutletsString.length() > 0) {
                                final ResultSet rs13 = sr.executeQuery("SELECT * FROM inventory_price_list_active_view where outlet_id in(" + AllOutletsString + ") ");
                                while (rs13.next()) {
                                    final LinkedHashMap rows5 = new LinkedHashMap();
                                    rows5.put("PriceListID", rs13.getString("price_list_id"));
                                    rows5.put("OutletID", rs13.getString("outlet_id"));
                                    rows5.put("ProductID", rs13.getString("product_id"));
                                    rows5.put("RawCase", rs13.getString("raw_case"));
                                    rows5.put("Unit", rs13.getString("unit"));
                                    jr9.add(rows5);
                                }
                            }
                            System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername + " Step4");
                            if (AllOutletsString.length() > 0) {
                                final ResultSet rs14 = sr.executeQuery(" SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in(" + AllOutletsString + ") ");
                                while (rs14.next()) {
                                    final LinkedHashMap rows5 = new LinkedHashMap();
                                    rows5.put("OutletID", rs14.getString("outlet_id"));
                                    rows5.put("ProductID", rs14.getString("product_id"));
                                    rows5.put("Discount", rs14.getString("discount"));
                                    rows5.put("CreatedOn", rs14.getString("created_on"));
                                    jr11.add(rows5);
                                }
                            }
                            final ResultSet rs15 = s2.executeQuery("SELECT feature_id FROM user_access where user_id=" + LoginUsername);
                            while (rs15.next()) {
                                final LinkedHashMap rows5 = new LinkedHashMap();
                                rows5.put("FeatureID", rs15.getString("feature_id"));
                                jr10.add(rows5);
                            }
                            final ResultSet rs16 = s5.executeQuery("SELECT id,label FROM pci_sub_channel");
                            while (rs16.next()) {
                                final LinkedHashMap rows6 = new LinkedHashMap();
                                rows6.put("ChannelID", rs16.getString("id"));
                                rows6.put("ChannelName", rs16.getString("label"));
                                jr13.add(rows6);
                            }
                            final String PerCasesql = "SELECT distinct id,(select label from distributor_beat_plan dbp where dbp.id=dbps.id) pjp_label,outlet_id,(select name from common_outlets co where co.id=dbps.outlet_id) outlet_name,(select address from common_outlets co where co.id=dbps.outlet_id) outlet_address,(select region_id from common_outlets co where co.id=dbps.outlet_id) region_idd,(SELECT  region_name FROM common_regions cr where cr.region_id=region_idd) region_name,(SELECT  region_short_name FROM common_regions cr where cr.region_id=region_idd) region_short,(select distributor_id from common_outlets co where co.id=dbps.outlet_id) distributor_idd,(SELECT name FROM common_distributors cd where cd.distributor_id=distributor_idd) distributor_name,(select channel_id from common_outlets co where co.id=dbps.outlet_id) channel_idd,(SELECT label FROM mrd_census_sub_channel mcsc where mcsc.id=channel_idd) channel_name,(SELECT contact_name FROM common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_name,(SELECT contact_number FROM common_outlets_contacts coc where coc.outlet_id=dbps.outlet_id) owner_contact,(select lat from common_outlets co where co.id=dbps.outlet_id) lat,(select lng from common_outlets co where co.id=dbps.outlet_id) lng,(select updated_on from common_outlets co where co.id=dbps.outlet_id) updated_on,(select updated_by from common_outlets co where co.id=dbps.outlet_id) updated_by FROM distributor_beat_plan_schedule dbps where id in (SELECT distinct id FROM distributor_beat_plan_view dbpv where " + UserTypeCheck + "=" + LoginUsername + " ) order by id";
                            final ResultSet rs17 = s2.executeQuery(PerCasesql);
                            while (rs17.next()) {
                                final LinkedHashMap rows8 = new LinkedHashMap();
                                final Date UpdatedOn = rs17.getDate("updated_on");
                                final String UpdatedBy = rs17.getString("updated_by");
                                if (UpdatedOn != null || (UpdatedBy != "0" && UpdatedBy != null)) {
                                    rows8.put("LAT", "NotZero");
                                    rows8.put("LNG", "NotZero");
                                }
                                else {
                                    rows8.put("LAT", "Zero");
                                    rows8.put("LNG", "Zero");
                                }
                                rows8.put("OutletID", rs17.getString("outlet_id"));
                                rows8.put("OutletName", rs17.getString("outlet_name"));
                                rows8.put("OutletAddress", rs17.getString("outlet_address"));
                                rows8.put("OutletPJPID", rs17.getString("id"));
                                rows8.put("OutletPJPLabel", rs17.getString("pjp_label"));
                                rows8.put("DistributorID", rs17.getString("distributor_idd"));
                                rows8.put("DistributorName", rs17.getString("distributor_name"));
                                rows8.put("OwnerName", rs17.getString("owner_name"));
                                rows8.put("OwnerContact", rs17.getString("owner_contact"));
                                rows8.put("Region", rs17.getString("region_idd"));
                                rows8.put("RegionName", rs17.getString("region_name"));
                                rows8.put("RegionShortName", rs17.getString("region_short"));
                                rows8.put("ChannelID", rs17.getString("channel_idd"));
                                rows8.put("ChannelLabel", rs17.getString("channel_name"));
                                jr14.add(rows8);
                            }
                            final ResultSet rs18 = s5.executeQuery("SELECT id,label FROM pci_sub_channel");
                            while (rs18.next()) {
                                final LinkedHashMap rows9 = new LinkedHashMap();
                                rows9.put("ChannelID", rs18.getString("id"));
                                rows9.put("ChannelName", rs18.getString("label"));
                                jr15.add(rows9);
                            }
                            json.put("outletChannelArray", jr13);
                            json.put("promotions_active", jr4);
                            json.put("promotions_products", jr6);
                            json.put("promotions_products_free", jr8);
                            json.put("ActivePriceListRows", jr9);
                            json.put("UserFeatures", jr10);
                            json.put("HandDiscount", jr11);
                            json.put("outletArray", jr14);
                            final Date Today = new Date();
                            final BiProcesses bip = new BiProcesses();
                            bip.CreateOrderBookerStatisticsDaily(LoginUsername, Today);
                            bip.close();
                            final OrderBookerDashboard OrderBooker = new OrderBookerDashboard();
                            final int year = Calendar.getInstance().get(1);
                            final int month = Calendar.getInstance().get(2);
                            final Date MonthToDate = Utilities.getStartDateByMonth(month, year);
                            final int TodayProductivity = OrderBooker.getProductivity(LoginUsername, Today, Today);
                            final int TodayDropSize = OrderBooker.getDropSize(LoginUsername, Today, Today);
                            final double TodaySKUPerBill = OrderBooker.getSKUPerBill(LoginUsername, Today, Today);
                            final int MonthToDateProductivity = OrderBooker.getProductivity(LoginUsername, MonthToDate, Today);
                            final int MonthToDateDropSize = OrderBooker.getDropSize(LoginUsername, MonthToDate, Today);
                            final double MonthToDateSKUPerBill = OrderBooker.getSKUPerBill(LoginUsername, MonthToDate, Today);
                            json.put("TodayProductivity", new StringBuilder(String.valueOf(TodayProductivity)).toString());
                            json.put("TodayDropSize", new StringBuilder(String.valueOf(TodayDropSize)).toString());
                            json.put("TodaySKUPerBill", new StringBuilder(String.valueOf(Utilities.getDisplayCurrencyFormatOneDecimal(TodaySKUPerBill))).toString());
                            json.put("MonthToDateProductivity", new StringBuilder(String.valueOf(MonthToDateProductivity)).toString());
                            json.put("MonthToDateDropSize", new StringBuilder(String.valueOf(MonthToDateDropSize)).toString());
                            json.put("MonthToDateSKUPerBill", new StringBuilder(String.valueOf(Utilities.getDisplayCurrencyFormatOneDecimal(MonthToDateSKUPerBill))).toString());
                            OrderBooker.close();
                            System.out.println("MobileAuthenticateUserSMV4 User:" + LoginUsername + " Step5");
                        }
                        else {
                            LogTypeID = 7;
                            json.put("success", "false");
                            json.put("error_code", "104");
                        }
                    }
                    else {
                        LogTypeID = 5;
                        json.put("success", "false");
                        json.put("error_code", "103");
                    }
                }
                else {
                    LogTypeID = 6;
                    json.put("success", "false");
                    json.put("error_code", "102");
                }
                s4.close();
                s3.close();
                s2.close();
                s.close();
                ds.dropConnection();
                dsr.dropConnection();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (InstantiationException e2) {
                e2.printStackTrace();
            }
            catch (IllegalAccessException e3) {
                e3.printStackTrace();
            }
            catch (SQLException e4) {
                e4.printStackTrace();
            }
        }
        else {
            json.put("success", "false");
            json.put("error_code", "101");
        }
        out.print(json);
        out.close();
    }
}
