package com.tikal.hudson.plugins.notification;

import hudson.model.JobProperty;
import hudson.model.AbstractProject;

import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

public class HudsonNotificationProperty extends JobProperty<AbstractProject<?, ?>> {

	final public List<Endpoint> endpoints;

	@DataBoundConstructor
	public HudsonNotificationProperty(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public HudsonNotificationPropertyDescriptor getDescriptor() {
		return (HudsonNotificationPropertyDescriptor) super.getDescriptor();
	}
}