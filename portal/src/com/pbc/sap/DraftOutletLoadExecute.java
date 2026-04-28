package com.pbc.sap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Util;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

@WebServlet(description = "Get Sales Order Status Json", urlPatterns = { "/sap/DraftOutletLoadExecute" })

public class DraftOutletLoadExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DraftOutletLoadExecute() {
        super();
        // TODO Auto-generated constructor stub
        //System.out.println("contructor() ...");
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Hello ");
		
		HttpSession session = request.getSession();
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long DraftID =0;
		
		response.setContentType("application/json");
		JSONObject JsonObj = new JSONObject();
		PrintWriter out = response.getWriter();
		
		try{
		
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			Statement s4 = ds.createStatement();
			Statement s5 = ds.createStatement();
			
			long SaleOrderNo = Utilities.parseLong(request.getParameter("SaleOrderNo"));
			
			long UVID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
			
			
			//boolean isEditCase = Utilities.parseBoolean(request.getParameter("isEditCase"));
			boolean isDuplicate = false;
			boolean isPartial = false;
			
			ResultSet rs_check = s.executeQuery("select idn.delivery_id, idn.sap_order_no, ( select sap_order_no from inventory_delivery_note_partial_orders where sap_order_no=idn.sap_order_no) partial from inventory_delivery_note idn where idn.sap_order_no="+SaleOrderNo);
			if(rs_check.first()){
				isDuplicate = false;
				if(rs_check.getLong("partial") != 0){
					isDuplicate = false;
					isPartial = true;
				}
			}
			
			//if (isEditCase){
			//	isDuplicate = false;
			//}
			
			if(isDuplicate == true){
				JsonObj.put("error", "2");
			}else{
 
				try {
					SAPUtilities obj = new SAPUtilities();
					//obj.connectPRD();
					
					String SaleOrderStr = SaleOrderNo+"";
					if(SaleOrderStr.length() < 10){
						
						int MissingDigits = 10 - SaleOrderStr.length();
						for(int i = 0; i < MissingDigits; i++){
							SaleOrderStr = "0"+SaleOrderStr;
						}
						
					}
					
					

					JCoTable tab[] = obj.getSalesOrder(SaleOrderStr);
					
					
					if (tab[0].getNumRows() > 0){
					System.out.println("bindas ");
					
					//Hello
						tab[0].firstRow();
						
						String DistributorName = "";
						ResultSet rsDistributor = s.executeQuery("SELECT name FROM common_distributors where distributor_id="+Utilities.parseLong(tab[0].getString("KNKLI"))+" and is_delivery_blocked = 0 and is_central_blocked  = 0");
						if(rsDistributor.first()){
							DistributorName = Utilities.parseLong(tab[0].getString("KNKLI"))+"-"+rsDistributor.getString("name");
						}
						
						long InvoiceVal = 0;
						double InvoiceAmount = 0;
						boolean InvoicePaid = false;
						Date InvoiceDate = null;
						
						if (DistributorName.length() > 0){
							JsonObj.put("exists", "true");
						}else{
							JsonObj.put("exists", "false");
						}
						JsonObj.put("DistrubutorID", tab[0].getString("KNKLI"));
						JsonObj.put("DistrubutorName", DistributorName);
						
						int rows = tab[2].getNumRows();
						if(rows > 0 ){
							
							tab[2].firstRow();
							for (int i = 0; i < rows; i++) {
								String VBTYP_N = tab[2].getString("VBTYP_N");
								
								if(VBTYP_N.equals("M")){
									JCoTable InvoiceHeader = obj.getSalesInvoice(tab[2].getString("VBELN"));
									
									if (InvoiceHeader.getNumRows() > 0){
										//System.out.println(InvoiceHeader.getString("FKSTO"));
										if (!InvoiceHeader.getString("FKSTO").equals("X")){ // FKSTO is X when invoice is cancelled
											InvoiceVal = tab[2].getLong("VBELN");
											
											
											InvoiceDate = InvoiceHeader.getDate("FKDAT");
											
											
											Date switchover = Utilities.parseDateYYYYMMDDWithoutSeparator("20001125");
											
											if (InvoiceDate.after(switchover)){
												ResultSet rsCheckInvoice = s.executeQuery("SELECT invoice_no from gl_invoice_posting where invoice_no = "+InvoiceVal);
												if(rsCheckInvoice.first()){
													InvoicePaid = true;
												}
												ResultSet rsCheckInvoice1 = s.executeQuery("SELECT invoice_no from gl_invoice_posting_forced where invoice_no = "+InvoiceVal);
												if(rsCheckInvoice1.first()){
													InvoicePaid = true;
												}
												
											}else{
												if (InvoiceHeader.getString("ZZCDS_STATUS").equals("X")){
													InvoicePaid = true;
												}
											}
											
											//System.out.println(InvoiceAmount);
											
											
											InvoiceAmount = InvoiceHeader.getDouble("NETWR") + InvoiceHeader.getDouble("MWSBK");
											//System.out.println(InvoiceAmount);
											break;
										}
										
									}
								}
								
								tab[2].setRow(i+1);
								
							}
							
						}
						
						if (InvoicePaid == false){
							InvoiceVal = 0;
							InvoiceAmount = 0;
						}
						
						JsonObj.put("InvoiceNo", InvoiceVal);
						
						
						
						//obj.dropConnection();
						
						JsonObj.put("InvoiceAmount", InvoiceAmount);
						//System.out.print(InvoiceAmount);
						
						rows = tab[1].getNumRows();
						
						if(rows > 0 ){
						
							
							
							JSONArray JsonArray = new JSONArray();
							
							s.executeUpdate("create temporary table temp_sales_order_status (category_id int(11), package_id int(11), qty_KI int(11) default 0, qty_BOT int(11) default 0, product_sap_code int(11) )");
							
							tab[1].firstRow();
							
							for (int i = 0; i < rows; i++) {
												
								
								
								String SALES_UNIT = tab[1].getString("VRKME");
								
								String UnitColumn = SALES_UNIT;
								
								long MATERIAL = tab[1].getLong("MATNR");
								int REQ_QTY = tab[1].getInt("KWMENG");
								
								int PackageID = 0;
								int CategoryID = 0;
								
								ResultSet rs = s.executeQuery("SELECT package_id, category_id FROM inventory_products where sap_code="+MATERIAL);
								if(rs.first()){
									PackageID = rs.getInt("package_id");
									CategoryID = rs.getInt("category_id");
								}else{
									System.out.println("missing code : " + MATERIAL);
								}
								
								if (!SALES_UNIT.equals("KI") && !SALES_UNIT.equals("BOT")){
									if(SALES_UNIT.equals("KAR")){
										UnitColumn = "KI";
									}else if(SALES_UNIT.equals("EA")){
										UnitColumn = "BOT";
									}else{
										UnitColumn = "KI";
									}
								}
								if(MATERIAL == 2011){
									UnitColumn = "KI";
								}
								
								if(CategoryID != 2){
									s2.executeUpdate("insert into temp_sales_order_status (category_id, package_id, qty_"+UnitColumn+", product_sap_code) values("+CategoryID+", "+PackageID+", "+REQ_QTY+", "+MATERIAL+")");
									//System.out.println("insert into temp_sales_order_status (category_id, package_id, qty_"+UnitColumn+", product_sap_code) values("+CategoryID+", "+PackageID+", "+REQ_QTY+", "+MATERIAL+")");
								}
								
								tab[1].setRow(i+1);
							}
							
							
								
								// ResultSet rs =
								
								// s2.executeUpdate("insert into temp_sales_order_status (category_id, package_id, qty_"+SALES_UNIT+", product_sap_code) values("+CategoryID+", "+PackageID+", -"+REQ_QTY+", "+MATERIAL+")");
								
							 				 		
							//inserting into draft outlet master table
							
							
							ResultSet rs24 = s5.executeQuery("select * from inventory_draft_outlet_load where sap_order_no="+SaleOrderNo);
							if(rs24.first()){//if already inserted mean already printed
								JsonObj.put("success", "false");
							}else{
								s4.executeUpdate("insert into inventory_draft_outlet_load (created_on,created_by,distributor_id,sap_order_no,uvid,invoice_no) values(now(),"+UserID+","+Utilities.parseLong(tab[0].getString("KNKLI"))+","+SaleOrderNo+","+UVID+","+InvoiceVal+")");
								
								
								
								//inserting into child table
								
								ResultSet rs10 = s.executeQuery("select LAST_INSERT_ID()");
								if(rs10.first()){
									DraftID = rs10.getInt(1);
								}
								
								
								ResultSet rs2 = s2.executeQuery("select category_id, package_id, sum(qty_KI) as qty_KI, sum(qty_BOT) as qty_BOT, (select label from inventory_packages where id=package_id) package_name, product_sap_code, (select brand_id from inventory_products where sap_code = product_sap_code) brand_id, (select label from inventory_brands where id=brand_id) brand_name, (select id from inventory_products where sap_code=product_sap_code) product_id from temp_sales_order_status where package_id != 0 group by category_id, package_id");
								while( rs2.next() ){
									
									int PartialRawCase = 0;
									int PartialUnits = 0;
									
									if (isPartial == true ){
										ResultSet rs = s.executeQuery("select ip.package_id, sum(idnp.raw_cases) as raw_cases, sum(idnp.units) as units from inventory_delivery_note_products idnp, inventory_delivery_note idn, inventory_products ip where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and idn.sap_order_no = "+SaleOrderNo+" and ip.package_id="+rs2.getInt("package_id")+" and ip.category_id != 2 group by ip.package_id;");
										if(rs.first()){
											PartialRawCase = rs.getInt("raw_cases");
											PartialUnits = rs.getInt("units");
										}
									}
									
									/*LinkedHashMap rows_packages = new LinkedHashMap();
									
									rows_packages.put("ProductSAPCode", rs2.getString("product_sap_code"));
									rows_packages.put("ProductID", rs2.getInt("product_id"));
									rows_packages.put("BrandName", rs2.getString("brand_name"));
									
									rows_packages.put("CategoryID", rs2.getInt("category_id"));
									rows_packages.put("PackageID", rs2.getInt("package_id"));
									rows_packages.put("PackageName", rs2.getString("package_name"));
									rows_packages.put("QuantityRawCases", (rs2.getInt("qty_KI")-PartialRawCase));
									rows_packages.put("QuantityUnits", (rs2.getInt("qty_BOT")-PartialUnits));
									
									JsonArray.add(rows_packages);
									
									*/
									
									//System.out.println("Product SAP Code "+rs2.getString("product_sap_code"));
									
									//Inserting into child table of Draft Outlet Load
									
									
									long Rawcases = rs2.getInt("qty_KI")-PartialRawCase;
									long Units = rs2.getInt("qty_BOT")-PartialUnits;
									
									
									s4.executeUpdate("INSERT INTO inventory_draft_outlet_load_products (draft_id,product_id,raw_cases,units,total_units) values("+DraftID+","+rs2.getInt("product_id")+","+Rawcases+","+Units+","+Units+")");
									
									
								}
								
								JsonObj.put("success", "true");
							}
							
							
								
							
							
							//JsonObj.put("rows", JsonArray);
							
							//s.executeUpdate("drop TEMPORARY table if exists temp_sales_order_status ");
						}
					
					}else{
						//obj.dropConnection();
						JsonObj.put("exists", "false");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("exception = "+e);
				}
			}
			
			s2.close();
			s.close();
			ds.dropConnection();
		
		}catch(Exception e){
			System.out.println(e);
		}
		out.print(JsonObj);
		out.close();
		
	}

}
