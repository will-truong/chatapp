package com.pason.chatapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.shareddata.ConcurrentSharedMap;
import org.vertx.java.platform.Verticle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WebserverVerticle extends Verticle {
	
	@Override
	public void start() {		
		JsonObject config = container.config();
		final Pattern chatUrlPattern = Pattern.compile("/chat/(\\w+)/(\\w+)");
		final EventBus eventBus = vertx.eventBus();
		final Logger logger = container.logger();
		ConcurrentSharedMap<Object, Object> idNameMap = vertx.sharedData().getMap("id-name");
		ConcurrentSharedMap<Object, Object> nameIdMap = vertx.sharedData().getMap("name-id"); 
		
		
		RouteMatcher httpRouteMatcher = new RouteMatcher().get("/", new Handler<HttpServerRequest>() {
			@Override
			public void handle(final HttpServerRequest request) {
				request.response().sendFile("web/chat.html");
			}
		}).get(".*\\.(css|js)$", new Handler<HttpServerRequest>() {
			@Override
			public void handle(final HttpServerRequest request) {
				request.response().sendFile("web/" + new File(request.path()));
			}
		});

		vertx.createHttpServer().requestHandler(httpRouteMatcher).listen(config.getInteger("port"), "localhost");
		
		eventBus.registerHandler("finalsend", new Handler<Message<String>>() {
		    public void handle(Message<String> message) {
		    	
		    	int index = message.body().lastIndexOf("}");		    	
		    	String address = message.body().substring(index + 1);
		    	String data = message.body().substring(0, index + 1);
		    	eventBus.send(address, data);
		    }
		});

		vertx.createHttpServer().websocketHandler(new Handler<ServerWebSocket>() {
			@Override
			public void handle(final ServerWebSocket ws) {
				final Matcher m = chatUrlPattern.matcher(ws.path());
				if (!m.matches()) {
					ws.reject();
					return;
				}
				final String chatRoom = m.group(1);
				final String name = m.group(2);
				final String id = ws.textHandlerID();
				
				
				logger.info("registering name: " + name + " with id: " + id + " for chat-room: " + chatRoom);
				vertx.sharedData().getSet("chat.room." + chatRoom).add(id);
				vertx.sharedData().getMap(chatRoom + ".name-id").put(name, id);
				vertx.sharedData().getMap(chatRoom + ".id-name").put(id, name);


				eventBus.send("incoming.user", new JsonObject().putString("id", id).putString("name", name).putString("chatroom", chatRoom));

				ws.closeHandler(new Handler<Void>() {
					@Override
					public void handle(final Void event) {
						logger.info("un-registering connection with id: " + id + " from chat-room: " + chatRoom);
						vertx.sharedData().getSet("chat.room." + chatRoom).remove(id);
					}
				});

				ws.dataHandler(new Handler<Buffer>() {
					@Override
					public void handle(final Buffer data) {
						ObjectMapper m = new ObjectMapper();
						try {
							JsonNode rootNode = m.readTree(data.toString());
							((ObjectNode) rootNode).put("received", new Date().toString());
							String jsonOutput = m.writeValueAsString(rootNode);
							logger.info("json generated: " + jsonOutput);
							final String backupjson =  jsonOutput;
							String message = ((ObjectNode) rootNode).get("message").asText();
							JsonObject user = new JsonObject().putString("id", (String) vertx.sharedData().getMap(chatRoom + ".name-id").get(rootNode.get("sender").asText())).putString("name", rootNode.get("sender").asText()).putString("chatroom", chatRoom);
							if(message.length() > 0) {
								if(message.charAt(0) == '/') {
									if(message.contains(" "))
										eventBus.send(message.substring(1, message.indexOf(" ")), user.putString("parameters", message.substring(message.indexOf(" "))));
									else
										eventBus.send(message.substring(1), user);
								}
								else {
									for (Object chatter : vertx.sharedData().getSet("chat.room." + chatRoom)) {
										final String address = (String)chatter;
										vertx.eventBus().sendWithTimeout("test.address", jsonOutput + address,20,new Handler<AsyncResult<Message<String>>>() {
										    public void handle(AsyncResult<Message<String>> result) {
										        if (result.succeeded()) {
										           
										        } else {									        	
		                                                   vertx.eventBus().send(address, backupjson);									            
										        }
										    }
										});										
									}
								}
							}
						} catch (IOException e) {
							ws.reject();
						}
					}
				});

			}
		}).listen(8090);
	}
}
