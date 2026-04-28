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
@WebServlet(description = "Uploads a file", urlPatterns = { "/myscripts/Upload_CSV_General" })
public class Upload_CSV_General extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Upload_CSV_General() {
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
						
						/*if (name.equals("DistributorID"))
						{
							
							Distributor=Long.parseLong(value);
						}
						else if (name.equals("CityID"))
						{
							
							//City=Long.parseLong(value);
						}*/
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

						File uploadedFile = new File(path + "/Revised_Discount_List_" + fileName);
						item.write(uploadedFile);
		
							
						  String fName = uploadedFile.getPath();
						  String thisLine; 
						 
						  FileInputStream fis = new FileInputStream(fName);
						  DataInputStream myInput = new DataInputStream(fis);
						  int i=1; 
						 
						 
						  
						  
						  
						  SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
						  
						  System.out.println("Start");
						  
						  int counter=1;
						  while ((thisLine = myInput.readLine()) != null)
						{
							  if(i>1) {
								  
								  String strar[] = thisLine.split(",");
								  
								  String _OutletID=strar[0];
								//  String CNIC=strar[1];
								 
								  System.out.println("Outlet ID :"+_OutletID);
								//  System.out.println("update common_outlets_contacts set contact_nic='"+CNIC+"' where outlet_id="+_OutletID);
								//  s.executeUpdate("update common_outlets_contacts set contact_nic='"+CNIC+"' where outlet_id="+_OutletID);
								  
							  }				
										
							i++;
							counter++;

						
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