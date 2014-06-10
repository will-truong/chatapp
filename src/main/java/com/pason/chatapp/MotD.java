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
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

//sends a message of the day every time a user connects
public class MotD extends Verticle {
	boolean weatherReceived = false;
	String motdOriginalWeather;
	String motdNoWeather;
	String motd;

	@Override
	public void start() {
		JsonObject config = container.config();		
		motdOriginalWeather = config.getString("motd");
		motdNoWeather = config.getString("motdNoWeather");
		motd = motdOriginalWeather;
		
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
		Handler<Message> sendMotd = new Handler<Message>() {
			@Override
			public void handle(Message message) {
				String content = message.body().toString();
				JsonObject map = new JsonObject();
				System.out.println(message.body() + "       message body from motd");
				String name = map.getString("name");
				String greeting = motd;
				if(!weatherReceived)
					greeting = motdNoWeather;
				greeting = greeting.replace("%time%", new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime())).replace("%name%", name);
				JsonObject motdMessage = new JsonObject().putString("message", greeting).putString("sender", "motd").putString("received", new Date().toString());
				vertx.eventBus().send("test.address", motdMessage.toString());
			}
		};
		vertx.eventBus().registerHandler("incoming.user", sendMotd);
		vertx.eventBus().registerHandler("motd", sendMotd);
	}
	
	public void weatherRequest() {
		vertx.eventBus().send("request.weather", "", new Handler<Message>() {

			@Override
			public void handle(Message event) {
				motd = motdOriginalWeather;
				String html = (String) event.body();
				String condition = html.split(",")[0];
				String temp = html.split(",")[1];
				motd = motd.replace("%condition%", condition.toLowerCase());
				motd = motd.replace("%temp%", temp);
				weatherReceived = true;
			}
			 
		});
	}
}