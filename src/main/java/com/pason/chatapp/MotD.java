package com.pason.chatapp;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

//sends a message of the day every time a user connects
public class MotD extends Verticle {
	final String original = "sup man, the time is " + new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime()) + ". it's %CONDITION% and %TEMP% degrees right now";
	final String noweather = "sup bro, the time is " + new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime()) + ". i'm still figuring out the weather, ask me later with /motd";
	String motd = original;
	boolean weatherReceived = false;

	@Override
	public void start() {
		weatherRequest();
		vertx.setPeriodic(2000, new Handler<Long>() {
			public void handle(Long event) {
				weatherRequest();
				if(weatherReceived) {
					vertx.setPeriodic(30000l, new Handler<Long>() {
						public void handle(Long event) {
							weatherRequest();
						}
					});
					vertx.cancelTimer(event);										
				}					
			}
		});
		Handler sendMotd = new Handler<Message>() {
			@Override
			public void handle(Message message) {
				String content = message.body().toString();
//				System.out.println(content);
				String greeting = motd;
				if(!weatherReceived)
					greeting = noweather;
				if(!vertx.sharedData().getSet("chat.room." + content).isEmpty()) {
					for (Object chatter : vertx.sharedData().getSet("chat.room." + content)) {	
//						System.out.println("sending motd to " + (String)chatter);
						String address = (String)chatter;
						vertx.eventBus().send("test.address", "{\"message\":\"" + greeting + "\",\"sender\":\"MOTD\",\"received\":\"" + new Date().toString() + "\"}" + address);								
					}
				}
				else
					vertx.eventBus().send("test.address", "{\"message\":\"" + greeting + "\",\"sender\":\"MOTD\",\"received\":\"" + new Date().toString() + "\"}" + content);
			}
		};
		vertx.eventBus().registerHandler("incoming.user", sendMotd);
		vertx.eventBus().registerHandler("motd", sendMotd);
	}
	
	public void weatherRequest() {
		vertx.eventBus().send("request.weather", "", new Handler<Message>() {

			@Override
			public void handle(Message event) {
				//System.out.println("received weather data");
				motd = original;
				String html = (String) event.body();
				String condition = html.split(",")[0];
				String temp = html.split(",")[1];
				motd = motd.replace("%CONDITION%", condition.toLowerCase());
				motd = motd.replace("%TEMP%", temp);
				weatherReceived = true;
			}
			 
		});
		//System.out.println("sent weather request");
	}
}