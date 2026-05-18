package com.pbc.bi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;


@WebServlet(description = "Bi Batch", urlPatterns = { "/bi/PromotionsCache" })
public class PromotionsCache extends HttpServlet {
	
       
    public PromotionsCache() {
        super();
    }
    
    public static void main(String args[]){
    	
		try {
			BiProcesses bip = new BiProcesses();
			bip.createPromotionsCache();
			bip.close();
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		//System.out.println("Hello");
		
		
		try {
			BiProcesses bip = new BiProcesses();
			if(bip.createPromotionsCache()){
				obj.put("success", "true");
			}
			bip.close();
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		out.print(obj);
		out.close();
		
	}
	
}
