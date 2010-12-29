package com.tikal.hudson.plugins.notification;

import hudson.Extension;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractProject;
import hudson.model.Job;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class HudsonNotifierProperty extends JobProperty<AbstractProject<?, ?>> {

	@DataBoundConstructor
	public HudsonNotifierProperty() {
		super();
	}

	private List<Target> targets = new ArrayList<Target>();

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

		private List<Target> targets = new ArrayList<Target>();

		public List<Target> getTargets() {
			return targets;
		}

		public void setTargets(List<Target> targets) {
			this.targets = targets;
		}

		public DescriptorImpl() {
			super(HudsonNotifierProperty.class);
			load();
		}

		@Override
		public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
			return true;
		}

		public String getDisplayName() {
			return "Hudson Notifier";
		}

		@Override
		public HudsonNotifierProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {
			HudsonNotifierProperty notifierProperty = new HudsonNotifierProperty();
			if (formData != null && !formData.isNullObject()) {
				JSONObject targetsData = (JSONObject) formData.get("targets");
				if (targetsData != null && !targetsData.isNullObject()) {
					notifierProperty.setTargets(req.bindJSONToList(Target.class, targetsData));
				}
			}
			return notifierProperty;
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