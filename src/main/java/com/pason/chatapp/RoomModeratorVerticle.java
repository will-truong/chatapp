package com.pason.chatapp;

import java.util.HashMap;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;


public class RoomModeratorVerticle extends Verticle {
HashMap<String,Integer> badWordTracker =  new HashMap<String,Integer>();
JsonObject obj = new JsonObject();
static String date =  "";
      @Override
      public void start() {
    	  vertx.eventBus().registerHandler("moderator.address",new Handler<Message<String>>() {
  		    public void handle(Message<String> message) {
  		    	System.out.println(message.body());
  		    	String [] separate = message.body().split(" ");
  		    	
  		    	
  		    	Integer count = badWordTracker.get(separate[0]);
  		    	obj = new JsonObject().putString("name", separate[0]);
  		    	
  		    	if(count == null && !date.equals(separate[4])){
  		    	badWordTracker.put(separate[0], 1);
  		    	count = 1;
  		    	obj.putNumber("badCount", count);
  		    	System.out.println("here2       " + date + "      " + separate[1]);
  		    	vertx.eventBus().send("notifier.address", obj.toString());
  		    	}
  		    	else{
  		    		if(!date.equals(separate[4])){
  		    		badWordTracker.put(separate[0], count++);
  		    		obj.putNumber("badCount", count);
  		    		System.out.println("here       " + date + "      " + separate[4]);
  		    		vertx.eventBus().send("notifier.address", obj.toString());
  		    		}
  		    	}
  		    	date = separate[4];
  		    }
  		    
    	  });  
      }

}
