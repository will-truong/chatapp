package com.pason.chatapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;



//sends a message of the day every time a user connects
public class MotdVerticle extends Verticle {
	final MotdRegular motd = new MotdRegular();

	@Override
	public void start() {
		motd.setMotd((String)((HashMap<String, Object>) container.config().toMap()).get("motd"), (String)((HashMap<String, Object>) container.config().toMap()).get("motdNoWeather"));
		
		weatherRequest();
		vertx.setPeriodic(4000, new Handler<Long>() {
			public void handle(Long event) {
				weatherRequest();
				if(motd.isWeatherReceived()) {
					vertx.setPeriodic(30000l, new Handler<Long>() {
						public void handle(Long event) {
							weatherRequest();
						}
					});
					vertx.cancelTimer(event);										
				}					
			}
		});
		Handler<Message<JsonObject>> sendMotd = new Handler<Message<JsonObject>>() {
			@Override
			public void handle(Message<JsonObject> message) {
				JsonObject jsonMap = (JsonObject)message.body();
				HashMap<String, Object> changes = new HashMap<String, Object>();
				changes.put("id", jsonMap.getString("id"));
				changes.put("name", jsonMap.getString("name"));
				changes.put("time", (new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime())).toString());
				motd.updateMotd(changes);
				String greeting = motd.getMotd();
				JsonObject motdMessage = new JsonObject().putString("message", greeting).putString("sender", "motd").putString("received", new Date().toString());
				vertx.eventBus().send(jsonMap.getString("id"), motdMessage.toString());
			}
		};
		vertx.eventBus().registerHandler("incoming.user", sendMotd);
		vertx.eventBus().registerHandler("motd", sendMotd);
	}
	
	public void weatherRequest() { //requests accuweather.py for weather information, handler edits motd appropriately
		vertx.eventBus().send("request.weather", "", new Handler<Message<String>>() {

			@Override
			public void handle(Message<String> event) { //on successful motd editing, motd = true, and condition, temp replaced appropriately
				HashMap<String, Object> changes = new HashMap<String, Object>();
				changes.put("temp", ((String) event.body()).split(",")[1]);
				changes.put("cond", (((String) event.body()).split(",")[0]).toLowerCase());
				motd.updateMotd(changes);
			}
		});
	}
}