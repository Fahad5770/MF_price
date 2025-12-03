package com.pbc.util;

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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Iterator;

@WebServlet(description = "Uploads a file", urlPatterns = { "/util/UploadFile" })
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadFile() {
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
			long RequestID = 0;
			String description  = "";
			
			try {
				ds = new Datasource();
				ds.createConnection();
				s = ds.createStatement();
				
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();
						
						if (name.equals("RequestID")){
							RequestID = Long.parseLong(value);
						}else if (name.equals("description")){
							description = Utilities.filterString(value, 1, MaxLength.WORKFLOW_REMARKS);
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
						
						s.executeUpdate("insert into workflow_requests_attachments (request_id, filename, attached_by, description, attached_on) values("+RequestID+",'"+RequestID + "_" + fileName+"', "+UserID+", '"+description+"', now())");

					}
				}
				
				PrintWriter out = response.getWriter();
				out.print("{\"success\": true}");
				out.close();
				
				
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
