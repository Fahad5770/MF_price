package com.pbc.employee;

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
@WebServlet(description = "Uploads a file", urlPatterns = { "/employee/UploadFilePJPSschedule" })
public class UploadFilePJPSschedule extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadFilePJPSschedule() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		String UserID = null;
		
		
		PrintWriter out = response.getWriter();

		System.out.println("in PJP file Upload Employee ");
		
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
			Statement sb = null;
			Statement sp = null;
			Statement s2 = null;
			Statement s3 = null;
			long RequestID = 123;
			int processType=0;
			String description  = "";
			
			try {
				ds = new Datasource();
				ds.createConnection();
				ds.startTransaction();
				s = ds.createStatement();
				s2 = ds.createStatement();
				s3 = ds.createStatement();
				sb = ds.createStatement();
				sp = ds.createStatement();
				
				
				
				//sb.executeUpdate("truncate table mrd_markematics_prices_4;");
				
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
							
							System.out.println("RequestID"+RequestID);
						}
						
						else if (name.equals("processType")){
							processType = Integer.parseInt(value);
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
						 
						 
						  
						  
						  ////////////////////////////////
						  //New Addition
						 
						  
						  
						  ////////////////////////////////////
						  
						  SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
						  
						  
						  String tempPJPID="";
						  
						  while ((thisLine = myInput.readLine()) != null)
						{
							  
							  String PJPID = "";
							  String OutletID = "";
							  String isalternative = "0";
							  String Days[]=new String[7];
							  
							
							  int ISPJPChanged=0;
							  
							  if(i>1) {
								  String strar[] = thisLine.split(",");
								  		//System.out.println("strar[0]"+strar[0]);
								  		PJPID=strar[0];
										if(!strar[1].equals("")) {
											OutletID=strar[1];}
										
										//Monday
										if(strar[2].equals("1")) {
											Days[0]="2";}
										else if(strar[2].equals("0"))  {
											Days[0]="0";
										}
										
										//Tuesday
										if(strar[3].equals("1")) {
											Days[1]="3";}
										else if(strar[3].equals("0"))  {
											Days[1]="0";
										}
										
										//Wednesday
										if(strar[4].equals("1")) {
											Days[2]="4";}
										else if(strar[4].equals("0"))  {
											Days[2]="0";
										}
										
										System.out.println("WED"+ Days[2]);
										//Thursday
										if(strar[5].equals("1")) {
											Days[3]="5";}
										else if(strar[5].equals("0"))  {
											Days[3]="0";
										}
										
										//Friday
										if(strar[6].equals("1")) {
											Days[4]="6";}
										else if(strar[6].equals("0"))  {
											Days[4]="0";
										}
										
										//Saturday
										if(strar[7].equals("1")) {
											Days[5]="7";}
										else if(strar[7].equals("0"))  {
											Days[5]="0";
										}
										
										//Sunday
										if(strar[8].equals("1")) {
											Days[6]="1";}
										else if(strar[8].equals("0")) {
											Days[6]="0";
										}
										
										
										if(!strar[9].equals("")) {
											isalternative=strar[9];
											}
							
								//try {
									
									String q="";
									if(!PJPID.equals(tempPJPID)) {
										
										System.out.println("IF PART ============>   "+tempPJPID+" CHANGE TO  "+PJPID);
										
										tempPJPID=PJPID;
										ISPJPChanged=1;
									}else {
										System.out.println("ELSE PART ============>   "+tempPJPID+" CHANGE TO  "+PJPID);
										
										ISPJPChanged=0;
									}
									
									
									/*To Append*/
									if(processType==1) {
										
										
											int checkForZeroFrequencyOutlet=0;
											for(int k=0;k<Days.length;k++) {
												System.out.println("Days[k]"+Days[k]);
												if(!Days[k].equals("0")) {
												
													s3.executeUpdate("insert into distributor_beat_plan_schedule(id,outlet_id,day_number,is_alternative)values ("+PJPID+","+OutletID+","+Days[k]+", "+isalternative+")");	
												
													checkForZeroFrequencyOutlet++;
												}
											}
											/*if(checkForZeroFrequencyOutlet==0) {
												
												s3.executeUpdate("insert into distributor_beat_plan_schedule(id,outlet_id,day_number)values ("+PJPID+","+OutletID+",0)");
											}*/
																				
									/*To Overwrite*/	
									}else if(processType==2) {
										
										
										/* delete all the previous records of particular pjp and outlet*/
										if(ISPJPChanged==1) {
											s3.executeUpdate("delete from distributor_beat_plan_schedule where id="+PJPID);	
										}
											
										int checkForZeroFrequencyOutlet=0;
											for(int k=0;k<Days.length;k++) {
												System.out.println("Days[k]"+Days[k]);
												if(!Days[k].equals("0")) {
													
													
													s3.executeUpdate("insert into distributor_beat_plan_schedule(id,outlet_id,day_number,is_alternative)values ("+PJPID+","+OutletID+","+Days[k]+","+isalternative+")");	
												
													checkForZeroFrequencyOutlet++;
												}
												
											}
										
										/*if(checkForZeroFrequencyOutlet==0) {
											
											s3.executeUpdate("insert into distributor_beat_plan_schedule(id,outlet_id,day_number)values ("+PJPID+","+OutletID+",0)");
										}*/
										
									}
									
									
							/*	}catch(Exception e) {
									
									System.out.println("Error111 "+ e);
									
									try {
										ds.rollback();
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								   // e.printStackTrace();
									out.print("{\"Success\": \"false\"}");
									//out.print("{\"Error\": \"'asdasdasds\'"}");
								}*/
							}
							i++;
						
						} 
						
						
						out.print("{\"Success\": \"true\"}");
							
						}

					
				}
				
				
				
				 ds.commit();
			}catch (Exception e){
				try {
					ds.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			   e.printStackTrace();
			  // \"" + message + "\"}";
			   out.print("{\"Success\": \"" + e + "\"}");
			 
			   //out.print("{\"ErrorMsg\": "+e+"}");
			   
			   
			   } finally{
			    try {
			     if (s != null){
			      s.close();
			      ds.dropConnection();
			     }
			    } catch (SQLException e) {
			     e.printStackTrace();
			    }   
			    out.close();
			   }
			   
			  }

			 }

			}