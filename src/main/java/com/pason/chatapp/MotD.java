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
	String motd = original;

	@Override
	public void start() {
		vertx.setPeriodic(1000, new Handler<Long>() {
			public void handle(Long event) {
				weatherRequest();
			}
		});
		vertx.eventBus().registerHandler("incoming.user", new Handler<Message>() {
			@Override
			public void handle(Message message) {
				String chatter = message.body().toString();
				vertx.eventBus().send(chatter, "{\"message\":\"" + motd + "\",\"sender\":\"MOTD\",\"received\":\"" + new Date().toString() + "\"}");				
			}
		});
	}
	
	public void weatherRequest() {
		vertx.eventBus().send("request.weather", "", new Handler<Message>() {

			@Override
			public void handle(Message event) {
				motd = original;
				String html = (String) event.body();
				String condition = html.split(",")[0];
				String temp = html.split(",")[1];
				motd = motd.replace("%CONDITION%", condition.toLowerCase());
				motd = motd.replace("%TEMP%", temp);
			}
			
		});
	}
}