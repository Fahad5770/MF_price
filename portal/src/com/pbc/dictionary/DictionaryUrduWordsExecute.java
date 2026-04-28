package com.pbc.dictionary;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

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

@WebServlet(description = "Dictonary Urdu Add Words", urlPatterns = { "/dictionary/DictionaryUrduWordsExecute" })
public class DictionaryUrduWordsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DictionaryUrduWordsExecute() {
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
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			//List<String> list = new ArrayList<String>();
			s.executeUpdate("delete from common_dictionary_urdu_words"); //first deleting old records
			ResultSet rs = s.executeQuery("select * from common_outlets");			
			String[] splited;
			String[] splited1;
			String[] splited2;
			while(rs.next()){
				String st = rs.getString("name");
				String st1 = rs.getString("address");
				
				splited = st.split(" "); //splited name array
				splited1 = st1.split(" "); //splited address array 
				
				for(int i=0;i<splited.length;i++){
					if(Pattern.matches("[a-zA-Z]+",splited[i]) == true){ //checking for alphabet
						//System.out.println(splited[i]);
						s1.executeUpdate("insert into common_dictionary_urdu_words(name) values('"+splited[i]+"')");
					}
				}
				
				//address
				for(int j=0;j<splited1.length;j++){
					if(Pattern.matches("[a-zA-Z]+",splited1[j]) == true){ //checking for alphabet
						//System.out.println(splited[i]);
						s1.executeUpdate("insert into common_dictionary_urdu_words(name) values('"+splited1[j]+"')");
					}
				}
			}
			
			//for distributors
			
			ResultSet rs1 = s.executeQuery("select * from common_distributors");
			while(rs1.next()){
				String st2 = rs1.getString("name");
				splited2 = st2.split(" ");
				for(int k=0;k<splited2.length;k++){
					if(Pattern.matches("[a-zA-Z]+",splited2[k]) == true){ //checking for alphabet
						//System.out.println(splited[i]);
						s1.executeUpdate("insert into common_dictionary_urdu_words(name) values('"+splited2[k]+"')");
					}
				}
			}
			//s.executeUpdate("insert into common_dictionary_urdu (english,urdu) values('"+EnglishWord+"','"+UrduWord+"')");
			//System.out.println("done");
			obj.put("success", "true");
			s.close();
			s1.close();
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
