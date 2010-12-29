package com.tikal.hudson.plugins.notification;

import hudson.model.Run;

import java.util.List;

import com.tikal.hudson.plugins.notification.HudsonNotificationProperty.Target;

public enum Phase {
	STARTED, COMPLETED, FINISHED;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status) {
		HudsonNotificationProperty property = (HudsonNotificationProperty) run.getParent().getProperty(HudsonNotificationProperty.class);
		if (property != null) {
			List<Target> targets = property.getTargets();
			for (Target target : targets) {
				target.getProtocol().sendNotification(target.getUrl(), run.getParent(), run.getNumber(), this, status);
			}
		}
	}
}