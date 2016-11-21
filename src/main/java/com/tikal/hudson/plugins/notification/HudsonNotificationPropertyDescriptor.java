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

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.AbstractIdCredentialsListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import hudson.Extension;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.JobPropertyDescriptor;
import hudson.RelativePath;
import hudson.security.ACL;
import hudson.security.Permission;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.AncestorInPath;

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
        this.endpoints = new ArrayList<Endpoint>( endpoints );
    }

    @Override
    public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return "Hudson Job Notification";
    }

    public int getDefaultTimeout(){
        return Endpoint.DEFAULT_TIMEOUT;
    }
    
    @Override
    public HudsonNotificationProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {
        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        if (formData != null && !formData.isNullObject()) {
            JSON endpointsData = (JSON) formData.get("endpoints");
            if (endpointsData != null && !endpointsData.isEmpty()) {
                if (endpointsData.isArray()) {
                    JSONArray endpointsArrayData = (JSONArray) endpointsData;
                    for (int i = 0; i < endpointsArrayData.size(); i++) {
                        JSONObject endpointsObject = endpointsArrayData.getJSONObject(i);
                        endpoints.add(convertJson((JSONObject) endpointsObject));
                    }
                } else {
                    endpoints.add(convertJson((JSONObject) endpointsData));
                }
            }
        }
        HudsonNotificationProperty notificationProperty = new HudsonNotificationProperty(endpoints);
        return notificationProperty;
    }
    
    private Endpoint convertJson(JSONObject endpointObjectData) throws FormException {
        // Transform the data to get the public/secret URL data
        JSONObject urlInfoData = endpointObjectData.getJSONObject("urlInfo");
        UrlInfo urlInfo = null;
        if (urlInfoData.containsKey("publicUrl")) {
            urlInfo = new UrlInfo(UrlType.PUBLIC, urlInfoData.getString("publicUrl"));
        }
        else if (urlInfoData.containsKey("secretUrl")) {
            urlInfo = new UrlInfo(UrlType.SECRET, urlInfoData.getString("secretUrl"));
        }
        else {
            throw new FormException("Expected either a public url or secret url id", "urlInfo");
        }
        Endpoint endpoint = new Endpoint(
            Protocol.valueOf(endpointObjectData.getString("protocol")),
            urlInfo,
            endpointObjectData.getString("event"),
            Format.valueOf(endpointObjectData.getString("format")),
            endpointObjectData.getInt("timeout"),
            endpointObjectData.getInt("loglines"),
            endpointObjectData.getInt("retries")
        );
        return endpoint;
    }
    
    public FormValidation doCheckPublicUrl(
            @QueryParameter(value = "publicUrl", fixEmpty = true) String publicUrl,
            @RelativePath ("..") @QueryParameter(value = "protocol") String protocolParameter) {
        Protocol protocol = Protocol.valueOf(protocolParameter);
        return checkUrl(publicUrl, UrlType.PUBLIC, protocol);
    }
    
    public FormValidation doCheckSecretUrl(
            @QueryParameter(value = "secretUrl", fixEmpty = true) String publicUrl,
            @RelativePath ("..") @QueryParameter(value = "protocol") String protocolParameter) {
        Protocol protocol = Protocol.valueOf(protocolParameter);
        return checkUrl(publicUrl, UrlType.SECRET, protocol);
    }
    
    private FormValidation checkUrl(String urlOrId, UrlType urlType, Protocol protocol) {
        String actualUrl = urlOrId;
        if (urlType == UrlType.SECRET && !StringUtils.isEmpty(actualUrl)) {
            // Get the credentials
            actualUrl = Utils.getSecretUrl(urlOrId);
            if (actualUrl == null) {
                return FormValidation.error("Could not find secret text credentials with id " + urlOrId);
            }
        }
        
        try {
            protocol.validateUrl(actualUrl);
            return FormValidation.ok();
        } catch (Exception e) {
            String message = e.getMessage();
            if (urlType == UrlType.SECRET && !StringUtils.isEmpty(actualUrl)) {
                message = message.replace(actualUrl, "******");
            }
            return FormValidation.error(message);
        }
    }
    
    public ListBoxModel doFillSecretUrlItems(@AncestorInPath Item owner, @QueryParameter String secretUrl) {
        if (owner == null || !owner.hasPermission(Permission.CONFIGURE)) {
            return new StandardListBoxModel();
        }
        
        // when configuring the job, you only want those credentials that are available to ACL.SYSTEM selectable
        // as we cannot select from a user's credentials unless they are the only user submitting the build
        // (which we cannot assume) thus ACL.SYSTEM is correct here.
        AbstractIdCredentialsListBoxModel<StandardListBoxModel, StandardCredentials> model = new StandardListBoxModel()
                .withEmptySelection()
                .withAll(
                    CredentialsProvider.lookupCredentials(
                        StringCredentials.class, owner, ACL.SYSTEM, Collections.<DomainRequirement>emptyList()));
        if (!StringUtils.isEmpty(secretUrl)) {
            // Select current value, add if missing
            for (ListBoxModel.Option option : model) {
                if (option.value.equals(secretUrl)) {
                    option.selected = true;
                    break;
                }
            }
        }
        
        return model;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) {
        save();
        return true;
    }

}