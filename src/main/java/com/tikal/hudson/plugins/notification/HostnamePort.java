package com.tikal.hudson.plugins.notification;

import java.util.Scanner;
import java.util.regex.MatchResult;

public class HostnamePort {

	final public String hostname;

	final public int port;

	public HostnamePort(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	static public HostnamePort parseUrl(String url) {
		try {
			Scanner scanner = new Scanner(url);
			scanner.findInLine("(.+):(\\d{1,5})");
			MatchResult result = scanner.match();
			if (result.groupCount() != 2) {
				return null;
			}
			String hostname = result.group(1);
			int port = Integer.valueOf(result.group(2));
			return new HostnamePort(hostname, port);
		} catch (Exception e) {
			return null;
		}
	}
}