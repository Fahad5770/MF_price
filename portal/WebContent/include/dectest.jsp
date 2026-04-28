<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%!
public String decrypt(String qry){
	
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
		
		int intVal = Integer.parseInt(arr2[i]);
		intVal = (intVal + 21) / 5;
		url2 += (char) (Byte.parseByte(""+ intVal));

	}
	
	return url2;
}

public String getParameter(String qry, String parameter){
	
	java.util.StringTokenizer st = new java.util.StringTokenizer(qry, "&");
	
	String ret = "";
	
	if (st != null){
		while(st.hasMoreElements()){
			
			String param = st.nextToken();
			if (param != null){
				String name = param.substring(0, param.indexOf("="));
				String value = param.substring((param.indexOf("=")+1), param.length());
				
				if (parameter.equals(name)){
					ret = value;
					break;
				}
			}
		}
	}
	
	return ret;
}
%>
<code>
<%
String url = decrypt(request.getParameter("SessionID"));

out.print(url);

out.println(getParameter(url, "user"));
out.println(getParameter(url, "timestamp"));


java.util.Date RequestDate = com.pbc.util.Utilities.parseDateTime((getParameter(url, "timestamp")));
java.util.Date NowDate = new java.util.Date();

long difference =  NowDate.getTime() - RequestDate.getTime();

double minutes = (difference / 1000) / 60;

out.print(minutes);


%>
</code>
