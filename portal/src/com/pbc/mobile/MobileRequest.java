package com.pbc.mobile;

import com.pbc.util.Utilities;

import java.util.ArrayList;
import java.util.Date;

public class MobileRequest {
	
	
	public String URL = "";
	
	public MobileRequest(String URL){
		
		this.URL = decrypt(URL);
		//System.out.println(this.URL);
	}
	
	public boolean isExpired(){
		
		boolean ret = true;
		
		String TimeStamp = getParameter("timestamp");
		
		Date RequestDate = Utilities.parseDateTime(TimeStamp);
		Date CurrentDate = new Date();
		
		long difference =  CurrentDate.getTime() - RequestDate.getTime();
		double minutes = (difference / 1000) / 60;
		
		if (minutes >= 0 && minutes < 25){
			return false;
		}
		
		return ret;
	}
	
	public String getParameter(String parameter){
		
		java.util.StringTokenizer st = new java.util.StringTokenizer(URL, "&");
		
		String ret = "";
		
		if (st != null){
			while(st.hasMoreElements()){
				
				String param = st.nextToken();
				if (param != null){
					
					if (param.indexOf("=") != -1){
						String name = param.substring(0, param.indexOf("="));
						String value = param.substring((param.indexOf("=")+1), param.length());
						
						if (parameter.equals(name)){
							ret = value;
							break;
						}
					}
					
				}
			}
		}
		
		return ret;
	}
	
	public String[] getParameterValues(String parameter){
		
		java.util.StringTokenizer st = new java.util.StringTokenizer(URL, "&");
		
		//String ret = "";
		ArrayList<String> ret = new ArrayList<String>();
		
		
		boolean isEmpty = true;
		
		if (st != null){
			while(st.hasMoreElements()){
				
				String param = st.nextToken();
				if (param != null){
					
					if (param.indexOf("=") != -1){
						String name = param.substring(0, param.indexOf("="));
						String value = param.substring((param.indexOf("=")+1), param.length());
						
						if (parameter.equals(name)){
							isEmpty = false;
							ret.add(value);
						}
					}
				}
			}
		}
		
		if (isEmpty == false){
			return ret.toArray(new String[ret.size()]);
		}else{
			return null;
		}
	}
	
	private String decrypt(String qry){
		
		String arr[] = qry.split("0a");
		
		String url = "";
		
		for (int i = 0; i < arr.length; i++){
			
			int intVal = Integer.parseInt(arr[i]);
			intVal = (intVal + 21) / 5;
			
			url += (char) (Byte.parseByte(""+ intVal));
		}
		
		String arr2[] = url.split(",");
		String url2 = "";
		
		for (int i = 0; i < arr2.length; i++){
			try{
				int intVal = Integer.parseInt(arr2[i]);
				intVal = (intVal + 21) / 5;
				url2 += (char) (Byte.parseByte(""+ intVal));
			}catch(Exception e){e.printStackTrace();}
			//System.out.print((char) (Byte.parseByte(""+ intVal)));
		}
		
		return url2;
	}
	
}
