package com.tikal.hudson.plugins.notification;

import hudson.model.Run;

import java.util.List;

import com.tikal.hudson.plugins.notification.HudsonNotifierProperty.Target;

public enum Phase {
	STARTED, COMPLETED, FINISHED;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status) {
		HudsonNotifierProperty property = (HudsonNotifierProperty) run.getParent().getProperty(HudsonNotifierProperty.class);
		if (property != null) {
			List<Target> targets = property.getTargets();
			for (Target target : targets) {
				target.getProtocol().sendNotification(target.getUrl(), run.getParent(), run.getNumber(), this, status);
			}
		}
	}
}