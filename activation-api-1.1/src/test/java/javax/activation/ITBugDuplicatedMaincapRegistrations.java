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
package javax.activation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * SM-4383. Filter out duplicate registrations of a command.
 */
public class ITBugDuplicatedMaincapRegistrations {

    private static final String MIMETYPE = "example/sm-4383";

    private static final String MAILCAP = MIMETYPE+";; x-java-content-handler=some.value";

    @Test
    public void testFilterDuplicates() {
        MailcapCommandMap mc = new MailcapCommandMap();

        assertEquals(0, mc.getAllCommands(MIMETYPE).length);

        mc.addMailcap(MAILCAP);
        mc.addMailcap(MAILCAP);
        assertEquals(1, mc.getAllCommands(MIMETYPE).length);

        mc.addMailcap(MAILCAP+".alt");
        assertEquals(2, mc.getAllCommands(MIMETYPE).length);
	}

}
