package com.pbc.distributor;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Iterator;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Uploads a file", urlPatterns = { "/distributor/DistributorAreaAllocationUploadFile" })
public class DistributorAreaAllocationUploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DistributorAreaAllocationUploadFile() {
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
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		
		Datasource ds = new Datasource();
		
		//System.out.println("ServletFileUpload.isMultipartContent(request) = "+ServletFileUpload.isMultipartContent(request));

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		String CurrentYear="";
		String CurrentMonth="";
		String LastYear="";
		
		
		
		

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
						
						if (name.equals("CurrentYear1")){
							CurrentYear = Utilities.filterString(value,2,100);
						}
						
						if (name.equals("CurrentMonth1")){
							CurrentMonth = Utilities.filterString(value,2,100);
							
						}
						
						if (name.equals("LastYear1")){
							LastYear = Utilities.filterString(value,2,100);
							
						}
					}
				}
				
				//System.out.println("Hello");
				
				String fileName="";
				File path=null;
				
				
				
				ds.createConnection();
				s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {
						
						
						
						 fileName = item.getName();
						//System.out.println("filename = "+fileName);
						if( !fileName.equals("") ){
						
							 path = new File(
									Utilities.getComplaintAttachmentsPath());
							if (!path.exists()) {
								boolean status = path.mkdirs();
							}
							
							//System.out.println();
						//System.out.println(fileName);	
							
							
	
							File uploadedFile = new File(path + "/Distributor_"+ CurrentYear+"_"+CurrentMonth+"."+fileName.substring(fileName.lastIndexOf(".") + 1));
							item.write(uploadedFile);
							
							
							
						}
					}// end if !item.isFormField()
				}
				
				
				//reading CSV file
				String FileNamePath = path + "/Distributor_"+ CurrentYear+"_"+CurrentMonth+"."+fileName.substring(fileName.lastIndexOf(".") + 1);
				
				//System.out.println(FileNamePath);
				
				Reader in = new FileReader(FileNamePath);
				Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
				for (CSVRecord record : records) {
				    long CurrentYearDistID = Utilities.parseLong(record.get("CurrentYear"));
				    long PreviousYearDistID = Utilities.parseLong(record.get("LastYear"));
				    int Vol = Utilities.parseInt(record.get("volume"));
				    
				    
				    s.executeUpdate("insert into common_distributors_area_allocation (year_previous,year_current,month,distributor_id_previous,distributor_id_current,volume_percentage,created_on,created_by) values("+LastYear+","+CurrentYear+","+CurrentMonth+","+PreviousYearDistID+","+CurrentYearDistID+","+Vol+",now(),"+UserID+")");
				    
				}
				
				//System.out.println("insert into common_distributors_area_allocation (year_previous,year_current,month,distributor_id_previous,distributor_id_current,volume_percentage,created_on,created_by) values("+LastYear+","+CurrentYear+","+CurrentMonth+",100662,100662,10,now(),"+UserID+")");
				
				
				
				
				s2.close();
				s.close();
				
				response.sendRedirect(request.getServletContext().getContextPath()+"/DistributorAreaAllocation.jsp");
				
				
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
