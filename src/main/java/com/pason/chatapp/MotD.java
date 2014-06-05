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
	String motd = "sup man, the time is " + new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());

	@Override
	public void start() {		
		vertx.eventBus().registerHandler("incoming.user", new Handler<Message>() {
			@Override
			public void handle(Message message) {
				String chatter = message.body().toString();
				System.out.println("bot.motd received incoming user: " + chatter + ", gonna reply with motd");
				vertx.eventBus().send(chatter, "{\"message\":\"" + motd + "\",\"sender\":\"MOTD\",\"received\":\"" + new Date().toString() + "\"}");				
			}
		});
	}
}