package com.pason.chatapp;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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
	String motd = "sup dude";
	

	@Override
	public void start() {
		System.out.println("STARTING MOTD");
		
		vertx.eventBus().registerHandler("bot.motd", new Handler<Message>() {
			public void handle(Message message) {
				System.out.println("received incoming user: " + message.body().toString().split("\\.")[2]);
				vertx.eventBus().send("localhost", motd);
			}
		});
	}
}