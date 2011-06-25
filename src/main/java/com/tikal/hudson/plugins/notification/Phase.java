package com.tikal.hudson.plugins.notification;

import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.List;


public enum Phase {
	STARTED, COMPLETED, FINISHED;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
		HudsonNotificationProperty property = (HudsonNotificationProperty) run.getParent().getProperty(HudsonNotificationProperty.class);
		if (property != null) {
			List<Endpoint> targets = property.getEndpoints();
			for (Endpoint target : targets) {
                try {
                    target.getProtocol().sendNotification(target.getUrl(), run.getParent(), run, this, status);
                } catch (IOException e) {
                    e.printStackTrace(listener.error("Failed to notify "+target));
                }
            }
		}
	}
}