package com.tikal.hudson.plugins.notification.model;

public class JobState {

	private String name;

	private String url;

	private BuildState build;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BuildState getBuild() {
		return build;
	}

	public void setBuild(BuildState build) {
		this.build = build;
	}
}
