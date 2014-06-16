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



public class MessageFilterRegular implements MessageFilter {

	@Override
	public String filterMessage(List badwords, String data) {
		
    	
    	
    	String substitute = "*************************";
    	String [] words = data.split(" ");
    	String newmessage = "";
    		
    		
    	
    	for(int x = 0; x < words.length; x++){
    		String messageWord = words[x];
    		
    			if(badwords.contains(messageWord)){
    				messageWord = substitute.substring(0,messageWord.length());
    				
    			}
    			else if(!messageWord.matches("[a-zA-Z]+")){
    				String replaceWord = messageWord.replaceAll("[^a-zA-Z]", "");
    				if(badwords.contains(replaceWord)){
        				messageWord = substitute.substring(0,replaceWord.length());
        				
        			}
    				
    			
    		}
    
    	newmessage += messageWord + " ";
    }
    	
    	
    	
    	
		return newmessage.trim();
    	
    	
   
	}
	
}