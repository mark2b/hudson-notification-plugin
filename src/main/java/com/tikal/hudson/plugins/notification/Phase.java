/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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