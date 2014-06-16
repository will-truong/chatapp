package com.pason.chatapp;

import java.io.File;
import java.io.IOException;
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
import org.vertx.java.platform.Verticle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WebserverVerticle extends Verticle {
	
	@Override
	public void start() {

		final Pattern chatUrlPattern = Pattern.compile("/chat/(\\w+)/(\\w+)");
		
		JsonObject config = container.config();
		HashMap<String, Object> configMap = (HashMap<String, Object>) config.toMap();
		Integer port = null;
		if(!(configMap.get("port") instanceof Integer)) {
			System.out.println("no integer port, using 2014");
			port = 2014;
		}
		else {
			port = (Integer)configMap.get("port");
			System.out.println("port suggested: " + port);
		}
		if(port <= 0) {
			System.out.println("invalid integer port, using 2014");
			port = 2014;
		}
		
		final EventBus eventBus = vertx.eventBus();
		final Logger logger = container.logger();
		
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
		
		vertx.createHttpServer().requestHandler(httpRouteMatcher).listen(port, "localhost");

		

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
				
				final JsonObject newUser = new JsonObject();
				String entry = name + " has joined the channel.";
				newUser.putString("message", entry);
				newUser.putString("sender", "SYSTEM");
				newUser.putString("received", new Date().toString());
				for(Object address : vertx.sharedData().getMap(chatRoom + "." + "id-name").keySet())
					vertx.eventBus().send((String) address, newUser.toString());


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
							String dataStr = data.toString();

							// String pat = "\\{\"message\":\"(.+)\", \"sender\":\".+?"; also works but use matches() instead of find() and use group(1) instead of group(), also change the replaceFirst to sth else. keep in case this one is actually better than pat2
                            String pat2 = "(?<=\\{\"message\":\").+(?=\", \"sender\":\".+?)";
							Pattern msgPattern = Pattern.compile(pat2);
							Matcher matcher = msgPattern.matcher(dataStr);
							matcher.find();
							String msgstr = matcher.group();
							dataStr = dataStr.replaceFirst(pat2, "");

							JsonNode rootNode = m.readTree(dataStr);
							((ObjectNode) rootNode).put("received", new Date().toString());
							((ObjectNode) rootNode).put("message", msgstr);


                             if(!vertx.sharedData().getMap(chatRoom + ".name-id").containsKey(((ObjectNode) rootNode).get("sender").toString().replaceAll("\"", ""))){
                            	 
                             }
                             else{


							final JsonObject obj = new JsonObject().putString("message", ((ObjectNode) rootNode).get("message")
							.asText()).putString("sender", ((ObjectNode) rootNode).get("sender").asText())
							.putString("received", ((ObjectNode) rootNode).get("received").asText());

							logger.info("json generated: " + obj.toString());
						
							
						
							
							String message = ((ObjectNode) rootNode).get("message").asText();
							JsonObject user = new JsonObject().putString("id", (String) vertx.sharedData().getMap(chatRoom + ".name-id").get(rootNode.get("sender").asText())).putString("name", rootNode.get("sender").asText()).putString("chatroom", chatRoom);
							if(message.length() > 0) {
								if(message.charAt(0) == '/') {
									eventBus.send(user.getString("id"), obj.toString());
									if(message.contains(" "))
										eventBus.send(message.substring(1, message.indexOf(" ")), user.putString("parameters", message.substring(message.indexOf(" ") + 1)));
									else
										eventBus.send(message.substring(1), user);
								}
								else {
									for (Object chatter : vertx.sharedData().getSet("chat.room." + chatRoom)) {
										
										final String address = (String)chatter;
										obj.putString("address", address);
										vertx.eventBus().sendWithTimeout("test.address", obj.toString(),700,new Handler<AsyncResult<Message<Byte>>>() {
										    public void handle(AsyncResult<Message<Byte>> result) {
										        if (result.succeeded()) {
										           
										        } else {	
										        	System.out.println("sending backup msg");
		                                                   vertx.eventBus().send(address, obj.toString());									            
										        }
										    }
										});										
									}
								}
							}
                             }
						} catch (IOException e) {
<<<<<<< HEAD
							System.err.println("messed up");
							ws.reject();



=======
							ws.reject();
>>>>>>> fa1ae5b1940c06288ac5272b20049bbb488ac9b4
						}
						
					}
				});
				vertx.eventBus().registerHandler("notifier.address",new Handler<Message<String>>() {
		  		    public void handle(Message<String> message) {
		  		    	JsonObject obj = new JsonObject(message.body());
		  		    	
		  		    	
                             
                             
		  		    		if(obj.getInteger("badCount") == 1){
		  		    			newUser.putString("message", "You have just typed an unapproved word. One more violation and you will no longer be allowed to talk.");
		  		    			vertx.eventBus().send(vertx.sharedData().getMap(chatRoom + ".name-id").get(obj.getString("name")).toString(), newUser.toString());
		  		    		}
		  		    		else{
		  		    			newUser.putString("message", "That's it. I warned you, you are done.");
		  		    			vertx.eventBus().send(vertx.sharedData().getMap(chatRoom + ".name-id").get(obj.getString("name")).toString(), newUser.toString());
		  		    			vertx.sharedData().getMap(chatRoom + ".name-id").remove(obj.getString("name"));
		  		    			vertx.sharedData().getSet("chat.room." + chatRoom).remove(vertx.sharedData().getMap(chatRoom + ".name-id").get(obj.getString("name")).toString());
		  		    		}
		  		    	
		  		    }
		  		    
			 });    

			}
		}).listen(8090);
	}
}
