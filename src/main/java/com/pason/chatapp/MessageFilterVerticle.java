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
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class MessageFilterVerticle extends Verticle {
	
	@Override
	public void start() {
	final String [] buzz = {"bomb", "gun", "terrorist", "kill","destroy","murder"};
           
		vertx.eventBus().registerHandler("test.address",new Handler<Message<String>>() {
		    public void handle(Message<String> message) {
		    	ObjectMapper m = new ObjectMapper();
		    	String data = message.body();
		    	int index = data.lastIndexOf("}");
		    	String addresshold = data.substring(index + 1);
		    	data = data.substring(0, index + 1);
		    	try{
		    		
		    	JsonNode rootNode = m.readTree(data.toString());
		    	
		    	String check = ((ObjectNode)rootNode).get("message").toString();
		    	
		    	check = check.replaceAll("\"","");
		    	
		    	for(int x = 0; x < buzz.length; x++){
		    		String buzzword = buzz[x];
		    	
		    	if(check.equals(buzzword)){
		    		
		    		((ObjectNode) rootNode).put("message", "!#$%^&");
		    	}
		    }
				String jsonOutput = m.writeValueAsString(rootNode);
		    			    	
		    	vertx.eventBus().send("finalsend", jsonOutput + addresshold); /*
		    	jsonOutput = "";
		    	data = "";
		    	addresshold = ""; */
		    }
		    	catch(IOException e){
		    		System.err.println("Uh Oh");
		    	}
		    	
		    }
		});

		
	}
	
}

