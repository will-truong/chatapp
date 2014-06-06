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
		    	message.reply();
		    	ObjectMapper m = new ObjectMapper();
		    	String data = message.body();
		    	int index = data.lastIndexOf("}");
		    	String addresshold = data.substring(index + 1);
		    	data = data.substring(0, index + 1);
		    	try{
		    		
		    	JsonNode rootNode = m.readTree(data.toString());
		    	
		    	String check = ((ObjectNode)rootNode).get("message").toString();
		    	
		    	check = check.replaceAll("\"","");
		    	
		    	String [] words = check.split(" ");
		    	Set<String> yes = new HashSet<String>(Arrays.asList(buzz));
		    	String newmessage = "";
		    	System.out.println("Made it past set");
		    	
		    	for(int x = 0; x < words.length; x++){
		    		String buzzword = words[x];
		    		System.out.println("In for loop, this is current word      " + buzzword);
		    	if(yes.contains(buzzword)){
		    		buzzword = "@$^&^@#";
		    		System.out.println("it contains a bad word!");
		    		
		    	}
		    	System.out.println("added to new message");
		    	newmessage += buzzword + " ";
		    }
		    	System.out.println("Out of for loop");
		    	((ObjectNode) rootNode).put("message", newmessage);
				String jsonOutput = m.writeValueAsString(rootNode);
				System.out.println("About to be sent");
		    	vertx.eventBus().send(addresshold, jsonOutput); 
		    	
		    }
		    	catch(IOException e){
		    		System.err.println("Uh Oh");
		    	}
		    	
		    }
		});

		
	}
	
}
