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
public class PmVerticle extends Verticle {
	@Override
	public void start() {
		Handler<Message<JsonObject>> sendPm = new Handler<Message<JsonObject>>() {

			@Override
			public void handle(Message<JsonObject> m) {
				Map<String, Object> jsonMap = m.body().toMap();
				JsonObject pm = new JsonObject();
				try {
					pm.putString("message", jsonMap.get("parameters").toString().split(" ",2)[1]);
					String recipient = jsonMap.get("parameters").toString().split(" ",2)[0];
					pm.putString("sender", jsonMap.get("name") +  " ---> " + recipient);
					pm.putString("received", new Date().toString());
					vertx.eventBus().send(jsonMap.get("id").toString(), pm.toString());
					vertx.eventBus().send(vertx.sharedData().getMap(jsonMap.get("chatroom").toString() + "." + "name-id").get(recipient).toString(), pm.toString());
				}
				catch (IndexOutOfBoundsException e) {
					pm.putString("message", "Provide a message.");
					pm.putString("sender", "SYSTEM");
					pm.putString("received", new Date().toString());
					vertx.eventBus().send(jsonMap.get("id").toString(), pm.toString());					
				}
				catch (NullPointerException e) {
					pm.putString("message", "Provide a valid recipient.");
					pm.putString("sender", "SYSTEM");
					pm.putString("received", new Date().toString());
					vertx.eventBus().send(jsonMap.get("id").toString(), pm.toString());					
				}
			}
			
		};
		vertx.eventBus().registerHandler("msg", sendPm);
	}
	
}
