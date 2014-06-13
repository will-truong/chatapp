/**
 * 
 */
package com.pason.chatapp.test.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.pason.chatapp.MotdRegular;

/**
 * @author wtruong
 *
 */


public class MotdRegularTest {
	
	@Test
	public void simpleTest() {
		MotdRegular motdRegular = new MotdRegular();
		motdRegular.setMotd("hi %name%", "hey %name%");
		assertEquals("hey %name%", motdRegular.getMotd());
		HashMap<String, Object> updates = new HashMap<>(); //mapping of updated variables
		updates.put("name", "will"); //insert variable "name"
		motdRegular.updateMotd(updates); //execute update
		assertEquals("hey will", motdRegular.getMotd());
	}
	
	@Test
	public void weatherTest() {
		MotdRegular motdRegular = new MotdRegular();
		motdRegular.setMotd("its %cond% and %temp% degrees", "hi");
		HashMap<String, Object> updates = new HashMap<>(); //mapping of updated variables
		updates.put("cond", "rainy"); //insert variable "cond"
		updates.put("temp", "-5"); //insert variable "temp"
		motdRegular.updateMotd(updates); //execute update
		assertEquals("its rainy and -5 degrees", motdRegular.getMotd());
	}
	
	@Test
	public void weatherFailTest() {
		MotdRegular motdRegular = new MotdRegular();
		motdRegular.setMotd("its %cond% and %temp% degrees", "hi");
		HashMap<String, Object> updates = new HashMap<>(); //mapping of updated variables
		updates.put("cond", null); //insert variable "cond"
		updates.put("temp", null); //insert variable "temp"
		motdRegular.updateMotd(updates); //execute update
		assertEquals("hi", motdRegular.getMotd());
	}
	
	@Test
	public void setNullTest() {
		MotdRegular motdRegular = new MotdRegular();
		motdRegular.setMotd(null, null);
		assertEquals("Hi, no message of the day set.", motdRegular.getMotd());
	}
	
	@Test
	public void updateNullTest() {
		MotdRegular motdRegular = new MotdRegular();
		motdRegular.updateMotd(null);
		assertEquals("Hi, no message of the day set.", motdRegular.getMotd());
		motdRegular.setMotd("%name% %cond% %temp%", "%name%");
		HashMap<String, Object> updates = new HashMap<>();
		updates.put("name", null);
		motdRegular.updateMotd(updates);
		assertEquals("%name%", motdRegular.getMotd());
		updates.put("cond", "raining");
		updates.put("temp", "63");
		motdRegular.updateMotd(updates);
		assertEquals("%name% raining 63", motdRegular.getMotd());
		updates.put("cond", null);
		updates.put("temp", null);
		motdRegular.updateMotd(updates);
		assertEquals("%name%", motdRegular.getMotd());
	}
}
