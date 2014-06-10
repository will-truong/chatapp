package com.pason.chatapp;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vertx.java.core.eventbus.impl.DefaultEventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class MessageFilterVerticle extends Verticle {
	
	@Override
	public void start() {
		JsonArray badwords = container.config().getArray("buzz");
		final List buzz =  badwords.toList();
	
           
		vertx.eventBus().registerHandler("test.address",new Handler<Message<String>>() {
		    public void handle(Message<String> message) {
		    	message.reply();
		    	JsonObject obj =  new JsonObject(message.body());
			     
			        MessageFilter filter = new MessageFilterRegular();
			        String message1 = obj.getString("message");
			       
			        String filteredMess = filter.filterMessage(buzz, message1);
			        
			    	obj.putString("message",filteredMess);
			    	 
			    	String address = obj.getString("address");
			    	 
			    	obj.removeField("address");
			    	 
			    	vertx.eventBus().send(address,obj.toString());
		    	
		    	
		    }
		});

		
	}
	
}
