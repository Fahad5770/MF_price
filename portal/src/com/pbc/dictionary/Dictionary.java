package com.pbc.dictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class Dictionary {

	Connection c;
	Datasource ds;
	PreparedStatement s;
	
	public Dictionary() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
		s = c.prepareStatement("select urdu_unicode from common_dictionary_urdu where english = ?");
	}
	
	
	public String translateToUrdu(String val) throws SQLException{
		
		String words[] = val.split(" ");
		String ret = "";
		
		for (String word: words){
			
			if (word.indexOf(",") == -1){
				s.setString(1, word);
				ResultSet rs = s.executeQuery();
				if (rs.first()){
					word = rs.getString(1);
				}
				ret += word + " ";
			}else{
				
				String iwords[] = word.split(",");
				for (String iword: iwords){
					s.setString(1, iword);
					ResultSet rs = s.executeQuery();
					if (rs.first()){
						iword = rs.getString(1);
					}
					ret += iword + ", ";
				}
				
			}
			
		}
		
		return ret;
	}
	
	
	public void close() throws SQLException{
		if (s != null){
			s.close();
		}
		ds.dropConnection();
		
	}
	
}
