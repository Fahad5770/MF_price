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

@WebServlet(description = "Dictonary Urdu Add Words", urlPatterns = { "/dictionary/DictionaryUrduAddWordsExecute" })
public class DictionaryUrduAddWordsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DictionaryUrduAddWordsExecute() {
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
		HttpSession session = request.getSession();
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		PrintWriter out = response.getWriter();
		
		String EnglishWord = Utilities.filterString(request.getParameter("EnglishWord"), 1, 500);
		String UrduWord = request.getParameter("UrduWord");
		String UrduWordUniCode = request.getParameter("UrduWordHidden");
		int IsEditFlag = Utilities.parseInt(request.getParameter("DictionaryIsEditFlag"));
		long DictionaryID = Utilities.parseLong(request.getParameter("DictionaryWordID"));
		//System.out.println(request.getParameter("UrduWordHidden"));
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			
			if(IsEditFlag==0){//means insertion case
				s.executeUpdate("insert into common_dictionary_urdu (english,urdu_unicode, created_on, created_by) values('"+EnglishWord+"','"+UrduWordUniCode+"', now(), "+UserID+")");
				obj.put("success", "true");
			}else if(IsEditFlag==1){//means edit case
				s.executeUpdate("update common_dictionary_urdu set english='"+EnglishWord+"',urdu_unicode='"+UrduWordUniCode+"', created_on = now(), created_by = "+UserID+" where id="+DictionaryID);
				obj.put("success", "true");
			}
			s.close();			
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
