package com.tikal.hudson.plugins.notification;

import hudson.Extension;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractProject;
import hudson.model.Job;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class HudsonNotificationProperty extends JobProperty<AbstractProject<?, ?>> {

	private List<Target> targets = new ArrayList<Target>();

	@DataBoundConstructor
	public HudsonNotificationProperty() {
		super();
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
	}

	@XStreamAlias(value = "target")
	public static class Target {

		private Protocol protocol;

		private String url;

		@DataBoundConstructor
		public Target(Protocol protocol, String url) {
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

	@Extension
	public static final class DescriptorImpl extends JobPropertyDescriptor {

		public DescriptorImpl() {
			super(HudsonNotificationProperty.class);
			load();
		}

		private List<Target> targets = new ArrayList<Target>();

		public boolean isEnabled() {
			return !targets.isEmpty();
		}

		public List<Target> getTargets() {
			return targets;
		}

		public void setTargets(List<Target> targets) {
			this.targets = targets;
		}

		@Override
		public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
			return true;
		}

		public String getDisplayName() {
			return "Hudson Job Notification";
		}

		@Override
		public HudsonNotificationProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {
			System.out.println(formData.toString(0));

			HudsonNotificationProperty notificationProperty = new HudsonNotificationProperty();
			if (formData != null && !formData.isNullObject()) {
				JSON targetsData = (JSON) formData.get("targets");
				if (targetsData != null && !targetsData.isEmpty()) {
					if (targetsData.isArray()) {
						JSONArray targetsArrayData = (JSONArray) targetsData;
						notificationProperty.setTargets(req.bindJSONToList(Target.class, targetsArrayData));
					} else {
						JSONObject targetsObjectData = (JSONObject) targetsData;
						notificationProperty.getTargets().add(req.bindJSON(Target.class, targetsObjectData));
					}
				}
			}
			return notificationProperty;
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) {
			save();
			return true;
		}

	}

	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}
}