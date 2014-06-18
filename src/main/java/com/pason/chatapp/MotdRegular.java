package com.pason.chatapp;

import java.util.HashMap;


public class MotdRegular implements Motd {
	private boolean weatherReceived = false;
	String motdOriginalWeather = "Hi, no message of the day set.";
	String motdNoWeather = "Hi, no message of the day set.";
	HashMap<String, Object> variables = new HashMap<>();

	@Override
	/**
	 * Returns the message of the day with consideration of the availability of weather information.
	 *
	 * @return motd if there's weather information, motdNoWeather otherwise.
	 */
	public String getMotd() {
		if(isWeatherReceived()) {
			return substVars(motdOriginalWeather);
		}
		return substVars(motdNoWeather);
	}
	
	@Override
	/**
	 * Updates the variables that are substituted into the message of the day and checks if weather information is present.
	 * 
	 * 
	 * @param a mapping of the variables to be updated to their updated values. 
	 */
	public void updateMotd(HashMap<String, Object> updates) {
		if(updates != null)
			variables.putAll(updates);
		weatherReceived = variables.get(Motd.MOTD_COND) != null && variables.get(Motd.MOTD_TEMP) != null;
	}

	@Override
	/**
	 * Sets the messages of the day to provided values.
	 * 
	 * @param The message of the day when there is weather information.
	 * @param The message of the day when there's not weather information.
	 * 
	 */
	public void setMotd(String weather, String noWeather) {
		if(weather != null)
			motdOriginalWeather = weather;
		if(noWeather != null)
			motdNoWeather = noWeather;
	}

	private String substVars(String str) {
		for(String var : variables.keySet()) {
			if(variables.get(var) != null)
				str = str.replaceAll("%" + var + "%", variables.get(var).toString());
		}
		return str;
	}

	/**
	 * Returns whether or not there is weather information.
	 */
	public boolean isWeatherReceived() {
		return weatherReceived;
	}
}