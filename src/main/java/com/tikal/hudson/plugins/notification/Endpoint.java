package com.tikal.hudson.plugins.notification;

import hudson.util.FormValidation;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class Endpoint {

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

	public FormValidation doCheckURL(@QueryParameter(value = "url", fixEmpty = true) String url) {
		if (url.equals("111"))
			return FormValidation.ok();
		else
			return FormValidation.error("There's a problem here");
	}

    @Override
    public String toString() {
        return protocol+":"+url;
    }
}