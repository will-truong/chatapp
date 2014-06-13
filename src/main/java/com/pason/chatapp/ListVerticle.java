package com.pason.chatapp;

import java.util.Date;
import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class ListVerticle extends Verticle {
	
	@Override
	public void start() {
		Handler<Message<JsonObject>> sendList = new Handler<Message<JsonObject>>() {

			@Override
			public void handle(Message<JsonObject> m) {
				Map<String, Object> jsonMap = m.body().toMap();
				JsonObject list = new JsonObject();
				try {
					String users = "Users in " + jsonMap.get("chatroom").toString() + ": ";
					for(Object name : vertx.sharedData().getMap(jsonMap.get("chatroom").toString() + "." + "name-id").keySet()) {
						users += (String)name + "\t";
					}					
					list.putString("message", users);
					list.putString("sender", "SYSTEM");
					list.putString("received", new Date().toString());
					vertx.eventBus().send(jsonMap.get("id").toString(), list.toString());					
				}
				catch (NullPointerException e) {
					list.putString("message", "nullpointer");
					list.putString("sender", "SYSTEM");
					list.putString("received", new Date().toString());
					vertx.eventBus().send(jsonMap.get("id").toString(), list.toString());					
				}
			}
			
		};
		vertx.eventBus().registerHandler("list", sendList);
	}
	
}
