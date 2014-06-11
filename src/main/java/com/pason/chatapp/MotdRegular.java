package com.pason.chatapp;

import java.util.HashMap;


public class MotdRegular implements Motd {
	private boolean weatherReceived = false;
	String motdOriginalWeather = "Hi, no message of the day set.";
	String motdNoWeather = "Hi, no message of the day set.";
	HashMap<String, Object> variables = new HashMap<>();

	@Override
	public String getMotd() { //retrieves the appropriate motd based on whether there's weather info or not
		if(isWeatherReceived()) {
			return substVars(motdOriginalWeather);
		}
		return substVars(motdNoWeather);
	}
	
	@Override
	public void updateMotd(HashMap<String, Object> updates) { //updates the motd with a provided mapping and checks if theres weather info
		for(String field : updates.keySet()) {
			variables.put(field, updates.get(field));
		}
		weatherReceived = variables.get("cond") != null && variables.get("temp") != null;
	}
	
	@Override
	public void setMotd(String weather, String noWeather) { //sets the motd's
		if(weather != null)
			motdOriginalWeather = weather;
		if(motdNoWeather != null)
			motdNoWeather = noWeather;
	}

	private String substVars(String str) {
		for(String var : variables.keySet()) {
			if(variables.get(var) != null)
				str = str.replaceAll("%" + var + "%", variables.get(var).toString());
		}
		return str;
	}

	public boolean isWeatherReceived() { //whether or not there's weather info
		return weatherReceived;
	}
}