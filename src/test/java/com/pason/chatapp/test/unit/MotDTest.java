package com.pason.chatapp.test.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import com.pason.chatapp.MotdRegular;

import org.junit.Test;


public class MotDTest {

	@Test
	public void testWeatherRequest() {
		MotdRegular vert1 = new MotdRegular();
		assertEquals("Hi, no message of the day set.", vert1.getMotd());
		vert1.setMotd("%name% %time% %cond% %temp%", "%name% %time%");
		assertFalse(vert1.isWeatherReceived());
		assertEquals("%name% %time%", vert1.getMotd());
		HashMap<String, Object> updates = new HashMap<>();
		updates.put("name", "will");
		updates.put("time", "12p");
		vert1.updateMotd(updates);
		assertEquals("will 12p", vert1.getMotd());
		updates = new HashMap<>();
		updates.put("temp", 67);
		updates.put("cond", "really foggy");
		vert1.updateMotd(updates);
		assertTrue(vert1.isWeatherReceived());
		assertEquals("will 12p really foggy 67", vert1.getMotd());
		updates = new HashMap<>();
		updates.put("temp", null);
		updates.put("cond", null);
		vert1.updateMotd(updates);
		assertFalse(vert1.isWeatherReceived());
		assertEquals("will 12p", vert1.getMotd());
	}
}