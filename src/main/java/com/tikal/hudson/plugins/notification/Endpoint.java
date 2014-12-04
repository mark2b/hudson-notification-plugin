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

import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class Endpoint {

    public static final Integer DEFAULT_TIMEOUT = 30000;

    private Protocol protocol;

    /**
     * json as default
     */
    private Format format = Format.JSON;

    private String url;

    private String event = "all";

    private Integer timeout = DEFAULT_TIMEOUT;

    private Integer loglines = 0;

    @DataBoundConstructor
    public Endpoint(Protocol protocol, String url, String event, Format format, Integer timeout, Integer loglines) {
        setProtocol( protocol );
        setUrl( url );
        setEvent( event );
        setFormat( format );
        setTimeout( timeout );
        setLoglines( loglines );
    }

    public int getTimeout() {
        return timeout == null ? DEFAULT_TIMEOUT : timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout =  timeout;
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

    public String getEvent (){
        return event;
    }

    public void setEvent ( String event ){
        this.event = event;
    }

    public Format getFormat() {
        if (this.format==null){
            this.format = Format.JSON;
        }
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Integer getLoglines() {
        return this.loglines;
    }

    public void setLoglines(Integer loglines) {
        this.loglines = loglines;
    }

    public FormValidation doCheckURL(@QueryParameter(value = "url", fixEmpty = true) String url) {
        if (url.equals("111"))
            return FormValidation.ok();
        else
            return FormValidation.error("There's a problem here");
    }

    public boolean isJson() {
        return getFormat() == Format.JSON;
    }

    @Override
    public String toString() {
        return protocol+":"+url;
    }
}
