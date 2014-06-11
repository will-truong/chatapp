/**
 * 
 */
package com.pason.chatapp.test.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Assume;
import org.junit.Test;

import com.pason.chatapp.MotdRegular;

/**
 * @author wtruong
 *
 */
public class MotdRegularTest {

	/**
	 * Test method for {@link com.pason.chatapp.MotdRegular#getMotd()}.
	 */
	@Test
	public void testGetMotd() { //getmotd gets the motd without weather variables when no weather info is present, and the motd with weather vars when it is		
		MotdRegular vert1 = new MotdRegular();
		assertEquals("Hi, no message of the day set.", vert1.getMotd()); //this should be the motd by default
		vert1.setMotd("1: %name% %time% %cond% %temp%", "2: %name% %time%"); //sets motd's with vars (1st motd w/ weather info, 2nd motd w/o weather info)
		assertEquals("2: %name% %time%", vert1.getMotd()); //motd w/o weather info should be sent
		HashMap<String, Object> updates = new HashMap<>(); //prepare an update map
		updates.put("name", "will"); //prepare to update "name" variable with "will"
		updates.put("time", "12p"); //prepare to update "time" variable with "12p"
		vert1.updateMotd(updates); //execute the update
		assertEquals("2: will 12p", vert1.getMotd()); //motd with variables plugged in
		updates = new HashMap<>(); //prepare a new update
		updates.put("temp", 67); //prepare to update "temp" variable with 67
		updates.put("cond", "really foggy"); //prepare to update "cond" variable with "really foggy"
		vert1.updateMotd(updates); //execute the update		assertTrue(vert1.isWeatherReceived()); //weather info should be present
		assertEquals("1: will 12p really foggy 67", vert1.getMotd()); //should retrieve the motd w/ weather info, variables replaced
		updates = new HashMap<>(); //prepare a new update
		updates.put("temp", null); //prepare to remove "temp" variable
		updates.put("cond", null); //prepare to remove "cond" variable
		vert1.updateMotd(updates); //execute the update
		assertEquals("2: will 12p", vert1.getMotd()); //motd w/o weather info should be executed
	}

	/**
	 * Test method for {@link com.pason.chatapp.MotdRegular#updateMotd(java.util.HashMap)}.
	 */
	@Test //isn't this test already covered by testGetMotd()??? is this test redundant or is there something else going on
	public void testUpdateMotd() { //updates list of variables with a list of updated variables		
		MotdRegular vert1 = new MotdRegular();
		vert1.setMotd("1: %name% %time% %cond% %temp%", "2: %name% %time%"); //sets motd's with vars (1st motd w/ weather info, 2nd motd w/o weather info)
		HashMap<String, Object> updates = new HashMap<>(); //prepare an update map
		updates.put("name", "will"); //prepare to update "name" variable with "will"
		updates.put("time", "12p"); //prepare to update "time" variable with "12p"
		vert1.updateMotd(updates); //execute the update
		assertEquals("2: will 12p", vert1.getMotd()); //motd with variables plugged in
		updates = new HashMap<>(); //prepare a new update
		updates.put("temp", 67); //prepare to update "temp" variable with 67
		updates.put("cond", "really foggy"); //prepare to update "cond" variable with "really foggy"
		vert1.updateMotd(updates); //execute the update		
		assertEquals("1: will 12p really foggy 67", vert1.getMotd()); //should retrieve the motd w/ weather info, variables replaced
		updates = new HashMap<>(); //prepare a new update
		updates.put("temp", null); //prepare to remove "temp" variable
		updates.put("cond", null); //prepare to remove "cond" variable
		vert1.updateMotd(updates); //execute the update
		assertEquals("2: will 12p", vert1.getMotd()); //motd w/o weather info should be executed
	}

	/**
	 * Test method for {@link com.pason.chatapp.MotdRegular#setMotd(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSetMotd() { //sets the motd w/ weather info and the motd w/o weather info
		MotdRegular vert1 = new MotdRegular();
		assertEquals("Hi, no message of the day set.", vert1.getMotd()); //this should be the motd by default
		vert1.setMotd("1: %name% %time% %cond% %temp%", "2: %name% %time%"); //sets motd's with vars (1st motd w/ weather info, 2nd motd w/o weather info)
		assertEquals("2: %name% %time%", vert1.getMotd()); //motd w/o weather info should be sent
		HashMap<String, Object> updates = new HashMap<>(); //prepare an update map		
		updates.put("temp", 67); //prepare to update "temp" variable with 67
		updates.put("cond", "really foggy"); //prepare to update "cond" variable with "really foggy"
		vert1.updateMotd(updates); //execute update
		assertEquals("1: %name% %time% really foggy 67", vert1.getMotd()); //should be motd w/ weather info that is plugged in 
	}

	/**
	 * Test method for {@link com.pason.chatapp.MotdRegular#isWeatherReceived()}.
	 */
	@Test
	public void testIsWeatherReceived() {
		MotdRegular vert1 = new MotdRegular();
		assertFalse(vert1.isWeatherReceived()); //weather info should not be present
		HashMap<String, Object> updates = new HashMap<>(); //prepare an update map
		updates.put("temp", 67); //prepare to update "temp" variable with 67
		updates.put("cond", "really foggy"); //prepare to update "cond" variable with "really foggy"
		vert1.updateMotd(updates); //execute the update
		assertTrue(vert1.isWeatherReceived()); //weather info should be present
		updates = new HashMap<>(); //prepare a new update
		updates.put("temp", null); //prepare to remove "temp" variable
		updates.put("cond", null); //prepare to remove "cond" variable
		vert1.updateMotd(updates); //execute the update
		assertFalse(vert1.isWeatherReceived()); //weather info should not be present
	}

}