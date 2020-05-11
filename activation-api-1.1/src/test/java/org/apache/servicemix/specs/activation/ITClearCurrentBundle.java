/*
 * Copyright 2020, MP Objects, http://www.mp-objects.com
 */
package org.apache.servicemix.specs.activation;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.net.URL;

import javax.activation.CommandMap;
import javax.activation.DataContentHandler;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;

/**
 * After Activator.rebuildCommandMap the currentBundle of OsgiMailcapCommandMap should be null, so that calls
 * to addMailcap do not register with the last started bundle.
 */
public class ITClearCurrentBundle {

	@After
	public void tearDown() {
		CommandMap.setDefaultCommandMap(null);
	}

	@Test
	public void testClearCurrentBundle() throws Exception {
		Activator activator = new Activator();
		Bundle bundle = createBundle(ITClearCurrentBundle.class.getResource("/mailcap.example"));
		
		activator.register(bundle);
		
		assertTrue(CommandMap.getDefaultCommandMap() instanceof OsgiMailcapCommandMap);
		assertNullCurrentBundle((OsgiMailcapCommandMap) CommandMap.getDefaultCommandMap());
	}

	private void assertNullCurrentBundle(OsgiMailcapCommandMap commandMap) throws Exception {
		Field field = OsgiMailcapCommandMap.class.getDeclaredField("currentBundle");
		boolean oldAccessible = field.isAccessible();
		try {
			field.setAccessible(true);
			assertNull(field.get(commandMap));
		}
		finally {
			field.setAccessible(oldAccessible);
		}
	}

	private Bundle createBundle(URL mailcapResource) throws Exception {
		Bundle result = Mockito.mock(Bundle.class);
		Mockito.when(result.loadClass("javax.activation.DataContentHandler")).thenReturn(DataContentHandler.class);
		Mockito.when(result.getResource("/META-INF/mailcap")).thenReturn(mailcapResource);
		return result;
	}
}
