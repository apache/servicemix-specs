/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
