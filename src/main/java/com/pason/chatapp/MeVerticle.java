package com.pason.chatapp;
import java.util.Date;
import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * 
 */

/**
 * @author wtruong
 *
 */
public class MeVerticle extends Verticle {
	
	@Override
	public void start() {
		Handler<Message<JsonObject>> sendMe = new Handler<Message<JsonObject>>() {

			@Override
			public void handle(Message<JsonObject> m) {
				Map<String, Object> jsonMap = m.body().toMap();
				JsonObject me = new JsonObject();
				try {
					me.putString("message", jsonMap.get("name").toString() + " " + jsonMap.get("parameters").toString());
					me.putString("sender", "");
					me.putString("received", new Date().toString());
					for(Object id : vertx.sharedData().getMap(jsonMap.get("chatroom").toString() + "." + "id-name").keySet())
						vertx.eventBus().send((String) id, me.toString());
				}
				catch (NullPointerException e) {
					me.putString("message", "Provide a message.");
					me.putString("sender", "SYSTEM");
					me.putString("received", new Date().toString());
					vertx.eventBus().send(jsonMap.get("id").toString(), me.toString());					
				}
			}
			
		};
		vertx.eventBus().registerHandler("me", sendMe);
	}
}
