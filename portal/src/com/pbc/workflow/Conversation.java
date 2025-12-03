package com.pbc.workflow;

import java.util.ArrayList;
import java.util.Date;

public class Conversation {
	public long CONVERSATION_ID;
	public long REQUEST_ID;
	public long FROM_USER_ID;
	public String FROM_USER_DISPLAY_NAME;
	public long TO_USER_ID;
	public String TO_USER_DISPLAY_NAME;
	public Date LAST_MESSAGE_ON;
	public long LAST_MESSAGE_BY_USER_ID;
	public ArrayList<ConversationMessage> MESSAGES = new ArrayList<ConversationMessage>();
}
