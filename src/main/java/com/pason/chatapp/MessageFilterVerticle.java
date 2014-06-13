package com.pason.chatapp;

import java.util.*;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class MessageFilterVerticle extends Verticle {
	
	@Override
	public void start() {
		JsonArray badwords = container.config().getArray("buzz");
		@SuppressWarnings("unchecked")
		final List<String> buzz =  badwords.toList();
	
           
		vertx.eventBus().registerHandler("test.address",new Handler<Message<String>>() {
		    public void handle(Message<String> message) {
		    	    message.reply();
		    	    int badWordCount = 0;
		    	    JsonObject obj =  new JsonObject(message.body());
			     
			        MessageFilter filter = new MessageFilterRegular();
			        String message1 = obj.getString("message");
			       
			        String filteredMess = filter.filterMessage(buzz, message1);
			        
			        
			    	obj.putString("message",filteredMess);
			    	 
			    	String address = obj.getString("address");
			    	 
			    	obj.removeField("address");
			    	
			    	if(!filteredMess.equals(message1)){
			        	badWordCount++;
			        	
			        }
			    	 
			    	vertx.eventBus().send(address,obj.toString());
		    	
		    	
		    }
		});

		
	}
	
}
