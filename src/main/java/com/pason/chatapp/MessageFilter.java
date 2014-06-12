package com.pason.chatapp;

import java.util.List;

public interface MessageFilter
{
public String filterMessage(List<String> badwords, String data);


}

class MessageFilterRegular implements MessageFilter {

	@Override
	public String filterMessage(List<String> badwords, String data) {
    	String [] words = data.split(" ");
    	String newmessage = "";
    		
    		
    	
    	for(int x = 0; x < words.length; x++){
    		String messageWord = words[x];
    	if(badwords.contains(messageWord)){
    		messageWord = "@$^&^@#";
    		
    	}
    	newmessage += messageWord + " ";
    }
    	
    	
    	
    	
		return newmessage;
    	
    	
   
	}
	
}