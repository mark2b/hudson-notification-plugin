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

import org.kohsuke.stapler.DataBoundConstructor;

public class Endpoint {

    public static final Integer DEFAULT_TIMEOUT = 30000;
    
    public static final Integer DEFAULT_RETRIES = 0;

    private Protocol protocol;
    
    /**
     * json as default
     */
    private Format format = Format.JSON;
    
    private UrlInfo urlInfo;
    
    // For backwards compatbility
    @Deprecated
    private transient String url;

    private String event = "all";

    private Integer timeout = DEFAULT_TIMEOUT;

    private Integer loglines = 0;
    
    private Integer retries = DEFAULT_RETRIES;

    @DataBoundConstructor
    public Endpoint(Protocol protocol, UrlInfo urlInfo, String event, Format format, Integer timeout, Integer loglines, Integer retries) {
        setProtocol( protocol );
        setEvent( event );
        setFormat( format );
        setTimeout( timeout );
        setUrlInfo ( urlInfo );
        setLoglines( loglines );
        setRetries( retries );
    }

    public UrlInfo getUrlInfo() {
        if (this.urlInfo == null) {
            this.urlInfo = new UrlInfo(UrlType.PUBLIC, "");
        }
        return this.urlInfo;
    }

    public void setUrlInfo(UrlInfo urlInfo) {
        this.urlInfo = urlInfo;
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

    public boolean isJson() {
        return getFormat() == Format.JSON;
    }
    
    public Integer getRetries() {
        return this.retries == null ? DEFAULT_RETRIES : this.retries;
    }
    
    public void setRetries(Integer retries) {
        this.retries = retries;
    }
    
    protected Object readResolve() {
        if (url != null) {
           // Upgrade, this is a public URL
           this.urlInfo = new UrlInfo(UrlType.PUBLIC, url);
        }
        return this;
    }

    @Override
    public String toString() {
        return protocol+":"+urlInfo.getUrlOrId();
    }
}
