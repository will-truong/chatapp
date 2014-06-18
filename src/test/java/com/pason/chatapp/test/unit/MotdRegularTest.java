/**
 * 
 */
package com.pason.chatapp.test.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.pason.chatapp.Motd;
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
		updates.put(Motd.MOTD_NAME, "will"); //insert variable Motd.MOTD_NAME
		motdRegular.updateMotd(updates); //execute update
		assertEquals("hey will", motdRegular.getMotd());
	}
	
	@Test
	public void weatherTest() {
		MotdRegular motdRegular = new MotdRegular();
		motdRegular.setMotd("its %cond% and %temp% degrees", "hi");
		HashMap<String, Object> updates = new HashMap<>(); //mapping of updated variables
		updates.put(Motd.MOTD_COND, "rainy"); //insert variable Motd.MOTD_COND
		updates.put(Motd.MOTD_TEMP, "-5"); //insert variable Motd.MOTD_TEMP
		motdRegular.updateMotd(updates); //execute update
		assertEquals("its rainy and -5 degrees", motdRegular.getMotd());
	}
	
	@Test
	public void weatherFailTest() {
		MotdRegular motdRegular = new MotdRegular();
		motdRegular.setMotd("its %cond% and %temp% degrees", "hi");
		HashMap<String, Object> updates = new HashMap<>(); //mapping of updated variables
		updates.put(Motd.MOTD_COND, null); //insert variable Motd.MOTD_COND
		updates.put(Motd.MOTD_TEMP, null); //insert variable Motd.MOTD_TEMP
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
		updates.put(Motd.MOTD_NAME, null);
		motdRegular.updateMotd(updates);
		assertEquals("%name%", motdRegular.getMotd());
		updates.put(Motd.MOTD_COND, "raining");
		updates.put(Motd.MOTD_TEMP, "63");
		motdRegular.updateMotd(updates);
		assertEquals("%name% raining 63", motdRegular.getMotd());
		updates.put(Motd.MOTD_COND, null);
		updates.put(Motd.MOTD_TEMP, null);
		motdRegular.updateMotd(updates);
		assertEquals("%name%", motdRegular.getMotd());
	}
}
