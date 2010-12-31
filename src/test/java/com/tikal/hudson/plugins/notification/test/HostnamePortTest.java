package com.tikal.hudson.plugins.notification.test;

import junit.framework.Assert;

import org.junit.Test;

import com.tikal.hudson.plugins.notification.HostnamePort;

public class HostnamePortTest {
	
	@Test
	public void parseUrlTest() {
		HostnamePort hnp = HostnamePort.parseUrl("111");
		Assert.assertNull(hnp);
		hnp = HostnamePort.parseUrl(null);
		Assert.assertNull(hnp);
		hnp = HostnamePort.parseUrl("localhost:123");
		Assert.assertEquals("localhost", hnp.hostname);
		Assert.assertEquals(123, hnp.port);
		hnp = HostnamePort.parseUrl("localhost:123456");
		Assert.assertEquals(12345, hnp.port);
	}
}
