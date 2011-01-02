package com.tikal.hudson.plugins.notification;

import hudson.Extension;
import hudson.model.JobPropertyDescriptor;
import hudson.model.Job;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;


@Extension
public final class HudsonNotificationPropertyDescriptor extends JobPropertyDescriptor {

	public HudsonNotificationPropertyDescriptor() {
		super(HudsonNotificationProperty.class);
		load();
	}

	private List<Endpoint> endpoints = new ArrayList<Endpoint>();

	public boolean isEnabled() {
		return !endpoints.isEmpty();
	}

	public List<Endpoint> getTargets() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	@Override
	public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
		return true;
	}

	public String getDisplayName() {
		return "Hudson Job Notification";
	}

//	@Override
//	public HudsonNotificationProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {
//		System.out.println(formData.toString(0));
//
//		HudsonNotificationProperty notificationProperty = new HudsonNotificationProperty();
//		if (formData != null && !formData.isNullObject()) {
//			JSON endpointsData = (JSON) formData.get("endpoints");
//			if (endpointsData != null && !endpointsData.isEmpty()) {
//				if (endpointsData.isArray()) {
//					JSONArray endpointsArrayData = (JSONArray) endpointsData;
//					notificationProperty.setEndpoints(req.bindJSONToList(Endpoint.class, endpointsArrayData));
//				} else {
//					JSONObject endpointsObjectData = (JSONObject) endpointsData;
//					notificationProperty.getEndpoints().add(req.bindJSON(Endpoint.class, endpointsObjectData));
//				}
//			}
//		}
//		return notificationProperty;
//	}

	@Override
	public boolean configure(StaplerRequest req, JSONObject formData) {
		save();
		return true;
	}

}