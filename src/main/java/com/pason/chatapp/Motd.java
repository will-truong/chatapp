package com.pason.chatapp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface Motd {
	boolean weatherReceived = false;
	String motdOriginalWeather = "Hi, no message of the day set.";
	String motdNoWeather = "Hi, no message of the day set.";
	HashMap<String, Object> variables = new HashMap<>();
	
	String getMotd();
	void updateMotd(HashMap<String, Object> updates);
	void setMotd(String weather, String noWeather);
	String substVars(String str);
}

//sends a message of the day every time a user connects
