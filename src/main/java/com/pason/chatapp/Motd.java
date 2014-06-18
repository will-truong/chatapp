package com.pason.chatapp;
import java.util.HashMap;

public interface Motd {
	boolean weatherReceived = false;
	String motdOriginalWeather = "Hi, no message of the day set.";
	String motdNoWeather = "Hi, no message of the day set.";
	HashMap<String, Object> variables = new HashMap<>();
	public static final String MOTD_NAME = "name";
	public static final String MOTD_TEMP = "temp";
	public static final String MOTD_COND = "cond";
	public static final String MOTD_TIME = "time";
	
	public String getMotd();
	public void updateMotd(HashMap<String, Object> updates);
	public void setMotd(String weather, String noWeather);
	public boolean isWeatherReceived();
}

//sends a message of the day every time a user connects
