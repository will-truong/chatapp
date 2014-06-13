package com.pason.chatapp;

import java.util.HashMap;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;


public class RoomModeratorVerticle extends Verticle {
HashMap<String,Integer> badWordTracker =  new HashMap<String,Integer>();

      @Override
      public void start() {
    	  vertx.eventBus().registerHandler("moderator.address",new Handler<Message<String>>() {
  		    public void handle(Message<String> message) {
  		    	Integer count = badWordTracker.get(message.body());
  		    	if(count == null)
  		    	badWordTracker.put(message.body(), 1);
  		    	else{
  		    		badWordTracker.put(message.body(), count++);
  		    	}
  		    }
  		    
    	  });  
      }

}
