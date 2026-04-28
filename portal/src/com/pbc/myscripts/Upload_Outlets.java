package com.pbc.myscripts;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.io.*;
@WebServlet(description = "Uploads a file", urlPatterns = { "/myscripts/Upload_Outlets" })
public class Upload_Outlets extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Upload_Outlets() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String UserID = null;		
		if (session.getAttribute("UserID") != null) {
		UserID = (String) session.getAttribute("UserID");
		}

		if (UserID == null) {
		response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart == true) {

			Datasource ds = null;
			Statement s = null;
			Statement s1 = null;
			Statement s2 = null;
			Statement s3 = null;
			Statement s4 = null;
			Statement s5 = null;
			
			
			long Distributor=0;
			long City=0;
			long RequestID = 123;
			long created_by=0;
			String description  = "";
		
			try {
				ds = new Datasource();
				ds.createConnection();
				//ds.startTransaction();
				s = ds.createStatement();
				s1 = ds.createStatement();
				s2= ds.createStatement();
				s3 = ds.createStatement();
				s4 = ds.createStatement();
				s5 = ds.createStatement();
				
				
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();
						
						if (name.equals("DistributorID"))
						{
							
							Distributor=Long.parseLong(value);
						}
						else if (name.equals("CityID"))
						{
							
							City=Long.parseLong(value);
						}
					}
				}
	
				
				
				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {
						String fileName = item.getName();

						File path = new File(
								Utilities.getWorkflowAttachmentsPath());
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}

						File uploadedFile = new File(path + "/" + RequestID + "_" + fileName);
						item.write(uploadedFile);
		
							
						  String fName = uploadedFile.getPath();
						  String thisLine; 
						 
						  FileInputStream fis = new FileInputStream(fName);
						  DataInputStream myInput = new DataInputStream(fis);
						  int i=1; 
						 
						 
						  
						  
						  
						  SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
						  
						  System.out.println("Start");
						  
						  
						  while ((thisLine = myInput.readLine()) != null)
						{
							  if(i>1) {
								  
								  String strar[] = thisLine.split(",");
								  
								  String OutletName=strar[0];
								  String OutletAddress=strar[1];
								  String OwnerName=strar[2];
								  String MobNo=strar[3];
								  long RegionID=0;
								  String DistributorName="";
								  
								  System.out.println("City "+City);
								  long maxID=0;
								     ResultSet rs1=s1.executeQuery("select max(id) from common_outlets where city_id="+City);
								     
								     if(rs1.first()){
								    	 maxID=rs1.getLong(1);
								    	 
								     };
								     
								   //if maxid=1 mean there no no outlet against this city then go to range table and get starting code
								     if(maxID==0) {
								    	 
								    	 ResultSet RsRange = s2.executeQuery("SELECT * FROM common_outlet_code_range where city_id="+City);
								    	 if(RsRange.first()) {
								    		 maxID = RsRange.getLong("start_code");
								    	 }
								    	 
								     }else {
								    	 maxID+=1; 
								     }
								     
								     
								     
								     //System.out.println("INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,cache_contact_name,cache_contact_number,cache_contact_nic,cache_snd_id,cache_rsm_id,category_id,cache_beat_plan_id,channel_id,vpo_classification_id,kpo_request_id)VALUES("+maxID+",'"+OutletName+"', '"+OutletAddress+"',"+RouteAreaID+","+DistributorID+",now(), "+UserID+", '"+OwnerName+"', '"+OutletContactNum+"','"+CnicNum+"', "+CRNameAndSapID+","+RMID+","+Shopcategory+", "+BeatDaysPlan+", "+SubChannel+","+VpoClass+","+LastEneteredRequestID+")");
								     
								     //Region Query
								     ResultSet rs7=s2.executeQuery("select region_id from common_distributors where distributor_id="+Distributor);
								     while(rs7.next()){
								    	 RegionID=rs7.getLong("region_id");
								     }
								     
								     // common_outlets Query
								     s2.executeUpdate("INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,category_id,city_id,cache_distributor_id,is_active)VALUES("+maxID+",'"+OutletName+"', '"+OutletAddress+"',"+RegionID+","+Distributor+",now(), "+UserID+",1,"+City+","+Distributor+",1)");
								  
								  
								     // common_outlets_contacts Query
									 s3.executeUpdate("INSERT INTO common_outlets_contacts(outlet_id,contact_name,contact_number,type_id,is_primary)values ("+maxID+",'"+OwnerName+"','"+MobNo+"',5,1)");
									
								  
									 
									//table common_distributors data fetching query
								     String query3="select * from common_distributors where distributor_id="+Distributor;
								    
								     ResultSet rs4 = s4.executeQuery(query3);
								     while( rs4.next() ){
								    	 DistributorName=rs4.getString("name");
								    	 
								     }
								     
								    //variables of table common_regions
									    String RegionShortName="";
									    String RegionName="";
								      
									//table common_regions data fetching query
									 String query4="select * from common_regions where region_id="+RegionID;
									    
									 ResultSet rs6 = s3.executeQuery(query4);
									 while( rs6.next() ){
										 RegionShortName=rs6.getString("region_short_name");
										 RegionName=rs6.getString("region_name");
									 }
										
									 
									 //table outletmaster insertion query
									 s5.executeUpdate("INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,city_id) VALUES(0,"+maxID+",'"+OutletName+"',"+Distributor+",'"+DistributorName+"','"+RegionShortName+"','"+RegionName+"','"+OwnerName+"','"+OutletAddress+"','"+MobNo+"','PBCIT',now(), 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"+City+")");
									 
								  
								  
							  }				
										
							i++;

						
						} 
						
						
			            //s3.executeUpdate("insert into employee_targets_packages(id,package_id,quantity) select id,package_id,sum(quantity) from employee_targets_packages_brands where id in (select id from employee_targets where created_by="+created_by+" and year="+Year+" and month="+Month+") group by id,package_id;");
						
						PrintWriter out = response.getWriter();
						//out.print("{\"ErrorMsg\": \"Successfully Uploaded\"}");
						out.print("{\"Success\": \"true\"}");
						out.close();
						
							
						}

					
				}
				
				
				
				 //ds.commit();
			}catch (Exception e){
			    
			    e.printStackTrace();
			   } finally{
			    try {
			     if (s != null){
			      s.close();
			      ds.dropConnection();
			     }
			    } catch (SQLException e) {
			     e.printStackTrace();
			    }    
			   }
			   
			  }

			 }

			}