package com.pason.chatapp;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;



public interface MessageFilter
{
public String filterMessage(List badwords, String data);


}

class MessageFilterRegular implements MessageFilter {

	@Override
	public String filterMessage(List badwords, String data) {
		
    	
    	
    	data = data.replaceAll("\"", "");
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