package com.pason.chatapp.test.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import com.pason.chatapp.MotdRegular;

import org.junit.Test;


public class MotDTest {

	@Test
	public void testWeatherRequest() {
		MotdRegular vert1 = new MotdRegular();
		assertEquals("Hi, no message of the day set.", vert1.getMotd()); //this should be the motd by default
		vert1.setMotd("1: %name% %time% %cond% %temp%", "2: %name% %time%"); //sets motd's with vars (1st motd w/ weather info, 2nd motd w/o weather info)
		assertFalse(vert1.isWeatherReceived()); //weather info should not be present
		assertEquals("2: %name% %time%", vert1.getMotd()); //motd w/o weather info should be sent
		HashMap<String, Object> updates = new HashMap<>(); //prepare an update map
		updates.put("name", "will"); //prepare to update "name" variable with "will"
		updates.put("time", "12p"); //prepare to update "time" variable with "12p"
		vert1.updateMotd(updates); //execute the update
		assertEquals("2: will 12p", vert1.getMotd()); //motd with variables plugged in
		updates = new HashMap<>(); //prepare a new update
		updates.put("temp", 67); //prepare to update "temp" variable with 67
		updates.put("cond", "really foggy"); //prepare to update "cond" variable with "really foggy"
		vert1.updateMotd(updates); //execute the update
		assertTrue(vert1.isWeatherReceived()); //weather info should be present
		assertEquals("1: will 12p really foggy 67", vert1.getMotd()); //should retrieve the motd w/ weather info, variables replaced
		updates = new HashMap<>(); //prepare a new update
		updates.put("temp", null); //prepare to remove "temp" variable
		updates.put("cond", null); //prepare to remove "cond" variable
		vert1.updateMotd(updates); //execute the update
		assertFalse(vert1.isWeatherReceived()); //weather info should not be present
		assertEquals("2: will 12p", vert1.getMotd()); //motd w/o weather info should be executed
	}
}