package com.pbc.crm;

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

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Iterator;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Uploads a file", urlPatterns = { "/crm/TaskWebUploadImage" })
public class TaskWebUploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TaskWebUploadImage() {
		super();
	}
	/*
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}
	*/
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//System.out.println("here2");
		
		HttpSession session = request.getSession();
		
		Datasource ds = new Datasource();
		
		//System.out.println("ServletFileUpload.isMultipartContent(request) = "+ServletFileUpload.isMultipartContent(request));

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		

		if (isMultipart == true) {
			Statement s = null;
			long RequestID = 0;
			int IsResolved = 0;
			int IsWebRequest = 0;
			String description  = "";
			
			try {
				
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				
				//System.out.println("items size = "+items.size());
				
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
							IsWebRequest = Integer.parseInt(value);
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
						String fileName = item.getName();
						//System.out.println("filename = "+fileName);
						if( !fileName.equals("") ){
						
							File path = new File(
									Utilities.getComplaintAttachmentsPath());
							if (!path.exists()) {
								boolean status = path.mkdirs();
							}
	
							File uploadedFile = new File(path + "/CRM_" + RequestID + "_" + fileName);
							item.write(uploadedFile);
							
							//System.out.println( "SELECT id, created_by FROM crm_complaints where complaint_id="+RequestID );
							
							String SQL = "SELECT id, created_by FROM crm_tasks where id="+RequestID;
							
							ResultSet rs = s.executeQuery(SQL);
							if( rs.first() ){
								s2.executeUpdate("INSERT INTO `crm_tasks_files`(`id`,`filename`,`uri`,`created_on`,`created_by`, `is_resolved`)VALUES("+rs.getString("id")+",'"+ ( "CRM_"+RequestID + "_" + fileName ) +"','"+ ( path + "/CRM_" + RequestID + "_" + fileName ) +"',now(), "+rs.getString("created_by")+", "+IsResolved+" )");
							}
							
						}
					}// end if !item.isFormField()
				}
				
				s2.close();
				s.close();
				
				response.sendRedirect(request.getServletContext().getContextPath()+"/CRMTaskResolveWeb.jsp");
				
				
				/*
				PrintWriter out = response.getWriter();
				out.print("{\"success\": true}");
				out.close();
				*/
				
				
			}catch (Exception e){
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
