package com.pbc.dictionary;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class UserRight */

@WebServlet(description = "Dictonary Urdu Add Words Json", urlPatterns = { "/dictionary/DictionaryUrduAddWordsJson" })
public class DictionaryUrduAddWordsJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DictionaryUrduAddWordsJson() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		JSONArray jr = new JSONArray();	
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();		
		
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			String JsonLiString ="";
			
			ResultSet rs = s.executeQuery("select * FROM common_dictionary_urdu");
			while(rs.next()){
				JsonLiString+="<li><a href='#' style='font-size:16pt;font-family: Tahoma;font-weight:normal;' onClick=EditCase("+rs.getString("id")+",'"+rs.getString("english")+"','"+rs.getString("urdu_unicode")+"')>"+rs.getString("english")+" - "+rs.getString("urdu_unicode")+"</a></li>";
			}
			obj.put("jsonstring", JsonLiString);
			obj.put("success", "true");				
			out.print(obj);
			out.close();				
			ds.dropConnection();
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
	}

}
