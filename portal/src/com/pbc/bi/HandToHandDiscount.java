package com.pbc.bi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
@WebServlet(description = "Bi Hand to hand discount", urlPatterns = { "/bi/HandToHandDiscount" })
public class HandToHandDiscount  extends HttpServlet {
	
	public HandToHandDiscount() {
        super();
    }
	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		try {
			BiProcesses bip = new BiProcesses();
			if(bip.createHandToHandDiscount()){
				obj.put("success", "true");
			}else{
				//obj.put("success", "true");
			}
			bip.close();
		}catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.print(obj);
		out.close();
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException { 
		
		BiProcesses bip = new BiProcesses();
		bip.createHandToHandDiscount();
		bip.close();
		
	}

}
