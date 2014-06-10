package com.pason.chatapp;

import java.util.HashMap;


public class MotdRegular implements Motd {
	boolean weatherReceived = false;
	String motdOriginalWeather = "Hi, no message of the day set.";
	String motdNoWeather = "Hi, no message of the day set.";
	HashMap<String, Object> variables = new HashMap<>();

	public MotdRegular() {
	}

	@Override
	public String getMotd() {
		if(weatherReceived) {
			return substVars(motdOriginalWeather);
		}
		return substVars(motdNoWeather);
	}
	
	@Override
	public void updateMotd(HashMap<String, Object> updates) {
		for(String field : updates.keySet()) {
			variables.put(field, updates.get(field));
		}
		weatherReceived = variables.keySet().contains("condition") && variables.keySet().contains("temp");
	}
	
	@Override
	public void setMotd(String weather, String noWeather) {
		if(weather != null)
			motdOriginalWeather = weather;
		if(motdNoWeather != null)
			motdNoWeather = noWeather;
	}

	@Override
	public String substVars(String str) {
		for(String var : variables.keySet()) {
			str = str.replaceAll("%" + var + "%", (String) variables.get(var));
		}
		return str;
	}
}