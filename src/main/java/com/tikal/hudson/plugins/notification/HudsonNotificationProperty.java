package com.tikal.hudson.plugins.notification;

import hudson.model.JobProperty;
import hudson.model.AbstractProject;

import java.util.ArrayList;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

public class HudsonNotificationProperty extends JobProperty<AbstractProject<?, ?>> {

	private List<Endpoint> endpoints = new ArrayList<Endpoint>();

	@DataBoundConstructor
	public HudsonNotificationProperty() {
		super();
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public static class Endpoint {

		private Protocol protocol;

		private String url;

		@DataBoundConstructor
		public Endpoint(Protocol protocol, String url) {
			this.protocol = protocol;
			this.url = url;
		}

		public Protocol getProtocol() {
			return protocol;
		}

		public void setProtocol(Protocol protocol) {
			this.protocol = protocol;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	public HudsonNotificationPropertyDescriptor getDescriptor() {
		return (HudsonNotificationPropertyDescriptor) super.getDescriptor();
	}
}