package com.tikal.hudson.plugins.notification;

import hudson.model.JobProperty;
import hudson.model.AbstractProject;
import hudson.util.FormValidation;

import java.util.ArrayList;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class HudsonNotificationProperty extends JobProperty<AbstractProject<?, ?>> {

	private List<Endpoint> endpoints = new ArrayList<Endpoint>();

	@DataBoundConstructor
	public HudsonNotificationProperty(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public FormValidation doCheckurl(@QueryParameter(value = "url", fixEmpty = true) String url) {
		System.out.println("HudsonNotificationProperty.Endpoint.doCheckURL()");
		if (url.equals("111"))
			return FormValidation.ok();
		else
			return FormValidation.error("There's a problem here");
	}

	// public HudsonNotificationPropertyDescriptor getDescriptor() {
	// return (HudsonNotificationPropertyDescriptor) super.getDescriptor();
	// }
}