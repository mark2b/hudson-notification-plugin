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
package com.tikal.hudson.plugins.notification.model;

import com.tikal.hudson.plugins.notification.Phase;
import hudson.model.AbstractBuild;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.s3.Entry;
import hudson.plugins.s3.S3BucketPublisher;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildState {

    private String fullUrl;

    private int number;

    private Phase phase;

    private String status;

    private String url;

    private String displayName;

    private ScmState scm;

    private Map<String, String> parameters;

    private Map<String, List<String>> artifacts;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> params) {
        this.parameters = new HashMap<String, String>( params );
    }

    public Map<String, List<String>> getArtifacts () {
        return artifacts;
    }

    public void setArtifacts ( Map<String, List<String>> artifacts )
    {
        this.artifacts = new HashMap<String, List<String>>( artifacts );
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ScmState getScm ()
    {
        return scm;
    }

    public void setScm ( ScmState scmState )
    {
        this.scm = scmState;
    }


    /**
     * Updates artifacts Map with S3 links, if corresponding publisher is available.
     */
    public void updateArtifacts ( Job job, Run run )
    {
        if ( ! ( run instanceof AbstractBuild )){ return; }
        if ( Jenkins.getInstance().getPlugin( "s3" ) == null ) { return; }

        DescribableList   publishers  = (( AbstractBuild ) run ).getProject().getPublishersList();
        S3BucketPublisher s3Publisher = ( S3BucketPublisher ) publishers.get( S3BucketPublisher.class );

        if ( s3Publisher == null ){ return; }

        for ( Entry entry : s3Publisher.getEntries()) {

            if ( isEmpty( entry.sourceFile, entry.selectedRegion, entry.bucket )){ continue; }
            String fileName = new File( entry.sourceFile ).getName();
            if ( isEmpty( fileName )){ continue; }

            // https://s3-eu-west-1.amazonaws.com/evgenyg-temp/
            String bucketUrl = String.format( "https://s3-%s.amazonaws.com/%s",
                                              entry.selectedRegion.toLowerCase().replace( '_', '-' ),
                                              entry.bucket );

            String fileUrl   = entry.managedArtifacts ?
                // https://s3-eu-west-1.amazonaws.com/evgenyg-temp/jobs/notification-plugin/21/notification.hpi
                String.format( "%s/jobs/%s/%s/%s", bucketUrl, job.getName(), run.getNumber(), fileName ) :
                // https://s3-eu-west-1.amazonaws.com/evgenyg-temp/notification.hpi
                String.format( "%s/%s", bucketUrl, fileName );

            if ( artifacts.get( fileName ) == null ) {
                artifacts.put( fileName, Arrays.asList( fileUrl ));
            }
            else {
                artifacts.get( fileName ).add( fileUrl );
            }
        }
    }

    private static boolean isEmpty( String ... strings ) {

        for ( String s : strings ) {
            if (( s == null ) || ( s.trim().length() < 1 )){
                return true;
            }
        }

        return false;
    }
}
