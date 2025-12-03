package com.pbc.mobile;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
import java.util.Iterator;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Uploads Census file", urlPatterns = { "/mobile/MobileUploadCensusImage" })
public class MobileUploadCensusImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileUploadCensusImage() {
		
		super();
		
		System.out.println("CensusImage constructor()");
		
	}
	/*
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}
	*/
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("image upload service()");
		
		HttpSession session = request.getSession();
		
		Datasource ds = new Datasource();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		boolean success = false;
		
		if (isMultipart == true) {
			Statement s = null;
			long RequestID = 0;
			int IsResolved = 0;
			String description  = "";
			
			try {
				
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				
				/*iterator = items.iterator();
				while (iterator.hasNext()) {
					
					FileItem item = (FileItem) iterator.next();
					
					if (!item.isFormField()) {
						
						String fileName = item.getName();
						
						System.out.println("filename = "+fileName);
						
					}
					
				}*/
				
				
				System.out.println("items size = "+items.size());
				
				while (iterator.hasNext()) {
					
					//System.out.println("iterator");
					
					FileItem item = (FileItem) iterator.next();
					
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();
						
						//System.out.println(name + " " +value);
						
						if (name.equals("value1")){
							RequestID = Long.parseLong(value);
						}
						
						if (name.equals("value2")){
							IsResolved = Integer.parseInt(value);
						}
					}
				}
				
				
				
				
				ds.createConnection();
				s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {
						
						int FileTypeID = 1;
						/*if( item.getContentType().toString().equals("audio/mpeg3") ){
							FileTypeID = 2;
						}*/
						
						String fileName = item.getName();
						System.out.println(fileName);
						
						File path = new File(
								Utilities.getOrderImagesPath());
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}

						System.out.println("case 1");
						File uploadedFile = new File(path + "/UO_" + RequestID + "_" + fileName);
						item.write(uploadedFile);
						System.out.println("case 2 = "+uploadedFile.getName());
						//System.out.println( "SELECT id, created_by FROM crm_complaints where complaint_id="+RequestID );
						
						String SQL = "SELECT id, created_by FROM mrd_census where census_id="+RequestID;
						
						ResultSet rs = s.executeQuery(SQL); 
						if( rs.first() ){
							
							success = true;
							
							try{
								
								System.out.println("INSERT INTO `mrd_census_files`(`id`,`filename`,`uri`,`created_on`,`created_by`)VALUES("+rs.getString("id")+",'"+ ( "UO_"+RequestID + "_" + fileName ) +"','"+ ( path.getPath() + "/UO_" + RequestID + "_" + fileName ) +"',now(), "+rs.getString("created_by")+" )");
								s2.executeUpdate("INSERT INTO `mrd_census_files`(`id`,`filename`,`uri`,`created_on`,`created_by`)VALUES("+rs.getString("id")+",'"+ ( "UO_"+RequestID + "_" + fileName ) +"','"+ ( path.getPath() + "/UO_" + RequestID + "_" + fileName ) +"',now(), "+rs.getString("created_by")+" )");
							}catch(SQLException e){
								System.out.println("Census ID: "+rs.getString("id")+" File Upload Exception: ");
								e.printStackTrace();
							}
							
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
