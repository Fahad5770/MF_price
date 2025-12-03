package com.pbc.inventory;

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
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.io.*;

@WebServlet(description = "Uploads a file", urlPatterns = { "/inventory/UploadFileBrandBulkDiscount" })
public class UploadFileBrandBulkDiscount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadFileBrandBulkDiscount() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("Brand Bulk Discount");

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
			Statement sb = null;
			Statement sp = null;
			Statement s2 = null;
			Statement s3 = null;
			long RequestID = 123;
			String DiscountLabel = "", validFrom = "", validTo = " ";
			int active = 1, brand=0, channel=0;// is_SKU=0;
			String created_by = "";
			String description = "";

			try {
				ds = new Datasource();
				ds.createConnection();
				ds.startTransaction();
				s = ds.createStatement();
				s2 = ds.createStatement();
				s3 = ds.createStatement();
				sb = ds.createStatement();
				sp = ds.createStatement();
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();

						if (name.equals("RequestID")) {
							RequestID = Long.parseLong(value);

						} else if (name.equals("DiscountLabel")) {
							DiscountLabel = value;
						} else if (name.equals("DiscountValidFrom")) {
							validFrom = value;
						}else if (name.equals("DiscountValidTo")) {
								validTo = value;
						}else if (name.equals("Activeness")) {
							active = Utilities.parseInt(value);
						}else if (name.equals("BrandSelect")) {
							brand = Utilities.parseInt(value);
						}else if (name.equals("PCIChannelSelect")) {
							channel = Utilities.parseInt(value);
						} else if (name.equals("UserID")) {
							created_by = value;
						}
//						else if (name.equals("is_SKU")) {
//							is_SKU = Utilities.parseInt(value);
//						}
					}
				}

				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {
						String fileName = item.getName();

						File path = new File(Utilities.getWorkflowAttachmentsPath());
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}

						File uploadedFile = new File(path + "/" + RequestID + "_" + fileName);
						item.write(uploadedFile);

						String fName = uploadedFile.getPath();
						String thisLine;
						int count = 0;
						FileInputStream fis = new FileInputStream(fName);
						DataInputStream myInput = new DataInputStream(fis);
					
//						 System.out.println("------------------------------");
//						 System.out.println("Brand Label : "+DiscountLabel);
//						 System.out.println("valiD From : "+validFrom);
//						 System.out.println("valiD To : "+validTo);
//						 System.out.println("active : "+active);
//						 System.out.println("Brand : "+brand);
//						 System.out.println("channel : "+channel);
//						 System.out.println("created_by : "+created_by);
//						 System.out.println("------------------------------");
						Date ValidFromDate = Utilities.parseDate(validFrom);
						Date ValidToDate = Utilities.parseDate(validTo);
						System.out.println("insert into inventory_hand_to_hand_discount_brand(label,valid_from,valid_to,is_active,created_on,created_by,activated_by,activated_on,pci_channel_id,brand_id) values('"+DiscountLabel+"',"+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+","+active+",now(),"+UserID+","+UserID+",now(),"+channel+","+brand+")");
						s.executeUpdate("insert into inventory_hand_to_hand_discount_brand(label,valid_from,valid_to,is_active,created_on,created_by,activated_by,activated_on,pci_channel_id,brand_id) values('"+DiscountLabel+"',"+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+","+active+",now(),"+UserID+","+UserID+",now(),"+channel+","+brand+")");
						int MasterTableSpotDiscountID=0;
						ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
						if(rs.first()){
							MasterTableSpotDiscountID = rs.getInt(1); 
						}
						 int i=0;
						while ((thisLine = myInput.readLine()) != null) {

							String strar[] = thisLine.split(",");
//							 System.out.println("No. "+i);
//							 System.out.println("------------------------------");
							int from = 0, to=0; double discount=0;
							if(i>=1) {
							
							for (int j = 0; j < strar.length; j++) {
								
								if(j == 0) {
								//	System.out.println("from : "+strar[j]);
									from = Utilities.parseInt(strar[j]);
								}else if(j == 1) {
								//	System.out.println("to : "+strar[j]);
									to = Utilities.parseInt(strar[j]);
								}else if(j == 2) {
								//	System.out.println("discount : "+strar[j]);
								//	discount = (is_SKU == 0) ? Utilities.parseInt(strar[j]) : Utilities.parseInt(strar[j].replace("%", ""));
									// discount = Utilities.parseInt(strar[j].replace("%", ""));
									discount = Utilities.parseDouble(strar[j]);
								}
							}
							
					
										if(from !=0 && to !=0) {
											//System.out.println("insert into inventory_hand_to_hand_discount_brand_details(hand_discount_id,from_qty,to_qty,discount,pci_channel_id) values("+MasterTableSpotDiscountID+","+from+","+to+","+discount+","+channel+")");
											s2.executeUpdate("insert into inventory_hand_to_hand_discount_brand_details(hand_discount_id,from_qty,to_qty,discount,pci_channel_id) values("+MasterTableSpotDiscountID+","+from+","+to+","+discount+","+channel+")");
										}
							}
							
//							System.out.println("From : "+from);
//							System.out.println("To : "+to);
//							System.out.println("Discount : "+discount);
//							 System.out.println("------------------------------");
i++;
						}
						PrintWriter out = response.getWriter();
						// out.print("{\"ErrorMsg\": \"Successfully Uploaded\"}");
						out.print("{\"Success\": \"true\"}");
						out.close();

					}

				}

				ds.commit();
			} catch (Exception e) {
				try {
					ds.rollback();
					System.out.println("RollBack");
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				try {
					if (s != null) {
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