package com.pbc.mobile;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "MobileUploadNewOutletImage", urlPatterns = { "/mobile/MobileUploadNewOutletImage" })
public class MobileUploadNewOutletImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileUploadNewOutletImage() {
		
		super();
		
		
		
	}
	/*
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}
	*/
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		System.out.println("MobileUploadNewOutletImage called!!!");
		
		
		//System.out.println("MobileUploadOrdersImage service()");
		
		HttpSession session = request.getSession();
		
		Datasource ds = new Datasource();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		boolean success = false;
		
		if (isMultipart == true) {
			Statement s = null;
			long outlet_request_id = 0;
			int userId = 0;
			double lat = 0, lng = 0, acc = 0;
			String description  = "", mobileTimeStamp="";
			
			try {
				
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				
				
				while (iterator.hasNext()) {
					
					//System.out.println("iterator");
					
					FileItem item = (FileItem) iterator.next();
					
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();
						
					//	System.out.println(name + " " +value);
						
						if (name.equals("value1")){
							outlet_request_id = Long.parseLong(value);
						}
						
						if (name.equals("value2")){
							userId = Integer.parseInt(value);
						}
						if (name.equals("value3")){
							lat = Double.parseDouble(value);
						}
						if (name.equals("value4")){
							lng = Double.parseDouble(value);
						}
						if (name.equals("value5")){
							acc = Double.parseDouble(value);
						}
						if (name.equals("value6")){
							mobileTimeStamp = value;
						}
					}
				}
//				Date d = new Date(mobileTimeStamp);
//				System.out.println(d);
//				System.out.println(Utilities.getSQLDateTime(d));
				
				
				
				
				ds.createConnection();
				s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {
						
						int FileTypeID = 1;
						
						String fileName = item.getName();
						//System.out.println(fileName);
						if (fileName.indexOf("Outlet") ==0){
							FileTypeID=1;
						}else {
							FileTypeID=2;
						}
						File path = new File(Utilities.getOutletImagesPath());
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}

						File uploadedFile = new File(path + "/Outlet_" + outlet_request_id + "_" + fileName);
						item.write(uploadedFile);
						
					
							
							
							//String Ccc="2771";
							try{
								//System.out.println("INSERT INTO pep.mobile_outlets_request_files (`outlet_request_id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`lat`,`lng`,`accuracy`,`mobile_timestamp`) VALUES('"+outlet_request_id+"','"+ ( "Outlet_"+outlet_request_id + "_" + fileName ) +"','"+ ( path + "/Outlet_" + outlet_request_id + "_" + fileName ) +"',now(), "+userId+" , 1, "+lat+", "+lng+", "+acc+", '"+mobileTimeStamp+"')");
								s2.executeUpdate("INSERT INTO pep.mobile_outlets_request_files (`outlet_request_id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`lat`,`lng`,`accuracy`,`mobile_timestamp`) VALUES('"+outlet_request_id+"','"+ ( "Outlet_"+outlet_request_id + "_" + fileName ) +"','"+ ( path + "/Outlet_" + outlet_request_id + "_" + fileName ) +"',now(), "+userId+" , 1, "+lat+", "+lng+", "+acc+", '"+mobileTimeStamp+"')");
								//s2.executeUpdate("INSERT INTO `mobile_order_unedited_files`(`id`,`filename`,`uri`,`created_on`,`created_by`)VALUES("+rs.getString("id")+",'"+ ( "UO_"+RequestID + "_" + fileName ) +"','"+ ( path + "/UO_" + RequestID + "_" + fileName ) +"',now(), "+Ccc+" )");
								success = true;
							}catch(SQLException e){
								
								e.printStackTrace();
							}
							
						
						
					}
				}
				
				s2.close();
				s.close();
				
				
				
				PrintWriter out = response.getWriter();
				if (success == true){
					out.print("{\"success\": true}");
				}else{
					response.sendError(500);
				}
				
				out.close();
				
				
				
			}catch (Exception e){
				response.sendError(500);
				e.printStackTrace();
			} finally{
				
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}

	}

}
