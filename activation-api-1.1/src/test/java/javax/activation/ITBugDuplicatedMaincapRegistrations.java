/*
 * Copyright 2020, MP Objects, http://www.mp-objects.com
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
