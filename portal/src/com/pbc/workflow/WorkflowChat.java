package com.pbc.workflow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pbc.util.Datasource;
import com.pbc.common.User;

public class WorkflowChat {
	Connection c;
	Datasource ds;	
	public long RequestID; 
	public ConversationMessage RECENT_MESSAGE;
	
	public WorkflowChat(long RequestID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
		
		this.RequestID = RequestID;
	}
	public WorkflowChat() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public ArrayList<ConversationMessage> getLastMessages(long UserID) throws SQLException{
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		
		ArrayList<ConversationMessage> list = new ArrayList<ConversationMessage>();
		
			ResultSet rs2 = s2.executeQuery("SELECT wrc.user_to, (select display_name from users where id = wrcm.sent_by) sent_by, wrc.request_id, wrcm.message, wrcm.sent_on FROM workflow_requests_chat_messages wrcm, workflow_requests_chat wrc where wrc.conversation_id = wrcm.conversation_id and (wrc.user_to = 0 or wrc.user_to = "+UserID+") and wrc.request_id in (SELECT distinct request_id FROM pep.workflow_requests_steps where user_id = "+UserID+") and wrcm.sent_by != "+UserID+" order by sent_on desc limit 5");
			while(rs2.next()){
				ConversationMessage msg = new ConversationMessage();
				msg.REQUEST_ID = rs2.getLong("request_id");
				msg.MESSAGE = rs2.getString("message");
				if (msg.MESSAGE.length() > 75){
					msg.MESSAGE = msg.MESSAGE.substring(0, 75)+"...";
				}
				msg.SENT_BY_USER_DISPLAY_NAME = rs2.getString(2);
				msg.SENT_ON = rs2.getTimestamp("sent_on");
				list.add(msg);
				
			}
			rs2.close();
			
		
		s2.close();
		s.close();
		
		return list;
	}
	
	
	public long createConversation(long from, long to, String message) throws SQLException{
		Statement s = c.createStatement();
		
		long ConversationID = 0;
		
		s.executeUpdate("insert into workflow_requests_chat (request_id, conversation_id, user_from, user_to, last_message_on, last_message_by) values("+this.RequestID+", null, "+from+", "+to+", now(), "+from+")");
		
		ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
		if (rs.first()){
			ConversationID = rs.getLong(1);
		}			
		
		s.executeUpdate("insert into workflow_requests_chat_messages (conversation_id, message, sent_by, sent_on) values ("+ConversationID+", '"+message+"', "+from+", now())");
		
		s.close();
		return ConversationID;
	}
	
	public void doReply(long ConversationID, long from, String message) throws SQLException{
		Statement s = c.createStatement();
		
		s.executeUpdate("insert into workflow_requests_chat_messages (conversation_id, message, sent_by, sent_on) values ("+ConversationID+", '"+message+"', "+from+", now())");
		
		
		s.executeUpdate("update workflow_requests_chat set last_message_on = now(), last_message_by = "+from+" where conversation_id = "+ConversationID);
		
		s.close();
	}
	
	
	public ArrayList<Conversation> getConversations() throws SQLException{
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		
		ArrayList<Conversation> list = new ArrayList<Conversation>();
		
		ResultSet rs = s.executeQuery("SELECT wrc.conversation_id, wrc.user_from, (select display_name from users where id = wrc.user_from) user_from_display_name, wrc.user_to, (select display_name from users where id = wrc.user_to) user_to_display_name FROM workflow_requests_chat wrc where wrc.request_id = "+this.RequestID+" order by wrc.last_message_on desc");
		while(rs.next()){
			Conversation item = new Conversation();
			item.REQUEST_ID = this.RequestID;
			item.CONVERSATION_ID = rs.getLong(1);
			item.FROM_USER_ID = rs.getLong(2);
			item.FROM_USER_DISPLAY_NAME = rs.getString(3);
			item.TO_USER_ID = rs.getLong(4);
			item.TO_USER_DISPLAY_NAME = rs.getString(5);
			
			ResultSet rs2 = s2.executeQuery("select wrcm.message, wrcm.sent_by, (select display_name from users where id = wrcm.sent_by) sent_by_display_name, wrcm.sent_on from workflow_requests_chat_messages wrcm where conversation_id = "+item.CONVERSATION_ID);
			while(rs2.next()){
				ConversationMessage msg = new ConversationMessage();
				
				msg.CONVERSATION_ID = item.CONVERSATION_ID;
				msg.MESSAGE = rs2.getString(1);
				msg.SENT_BY_USER_ID = rs2.getLong(2);
				msg.SENT_BY_USER_DISPLAY_NAME = rs2.getString(3);
				msg.SENT_ON = rs2.getTimestamp(4);
				
				item.MESSAGES.add(msg);
				
				if (rs.isFirst() && rs2.isLast()){
					this.RECENT_MESSAGE = msg;
				}
				
			}
			rs2.close();
			
			list.add(item);
		}
		
		rs.close();
		s2.close();
		s.close();
		
		return list;
	}
	
	public User[] getStakeholders() throws SQLException{
		Statement s = c.createStatement();
		int num = 0;
		List <User>list = new ArrayList<User>();
		
		ResultSet rs = s.executeQuery("select id, display_name from users where id in (select distinct user_id from workflow_requests_steps where request_id = "+this.RequestID+") order by display_name");
		while(rs.next()){
			num++;
			
			User item = new User();
			
			item.USER_ID = rs.getLong(1);
			item.USER_DISPLAY_NAME = rs.getString(2);
			
			list.add(item);
		}
		
		rs.close();
		s.close();
		
		return list.toArray(new User[num]);
		
	}
	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
}
