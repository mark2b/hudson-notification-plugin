package com.tikal.hudson.plugins.notification;

import hudson.model.Job;
import hudson.model.Run;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tikal.hudson.plugins.notification.model.BuildState;
import com.tikal.hudson.plugins.notification.model.JobState;

@SuppressWarnings("rawtypes")
public enum Protocol {

	UDP {
		@Override
		protected void send(String url, byte[] data) throws IOException {
            HostnamePort hostnamePort = HostnamePort.parseUrl(url);
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(hostnamePort.hostname), hostnamePort.port);
            socket.send(packet);
		}

		@Override
		public void validateUrl(String url) {
			try {
				HostnamePort hnp = HostnamePort.parseUrl(url);
				if (hnp == null) {
					throw new Exception();
				}
			} catch (Exception e) {
				throw new RuntimeException("Invalid Url: hostname:port");
			}
		}
	},
	TCP {
		@Override
		protected void send(String url, byte[] data) throws IOException {
            HostnamePort hostnamePort = HostnamePort.parseUrl(url);
            SocketAddress endpoint = new InetSocketAddress(InetAddress.getByName(hostnamePort.hostname), hostnamePort.port);
            Socket socket = new Socket();
            socket.connect(endpoint);
            OutputStream output = socket.getOutputStream();
            output.write(data);
            output.flush();
            output.close();
		}
	},
	HTTP {
		@Override
		protected void send(String url, byte[] data) throws IOException {
            URL targetUrl = new URL(url);
            URLConnection connection = targetUrl.openConnection();
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            output.write(data);
            output.flush();
            output.close();
		}

		public void validateUrl(String url) {
			try {
				new URL(url);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid Url: http://hostname:port/path");
			}
		}
	};

	private Gson gson = new GsonBuilder().create();

	public void sendNotification(String url, Job job, Run run, Phase phase, String status) throws IOException {
		send(url, buildMessage(job, run, phase, status));
	}

	private byte[] buildMessage(Job job, Run run, Phase phase, String status) {
		JobState jobState = new JobState();
		jobState.setName(job.getName());
		jobState.setUrl(job.getUrl());
		BuildState buildState = new BuildState();
		buildState.setNumber(run.number);
		buildState.setUrl(run.getUrl());
		buildState.setPhase(phase);
		buildState.setStatus(status);
		jobState.setBuild(buildState);
		return gson.toJson(jobState).getBytes();
	}

	abstract protected void send(String url, byte[] data) throws IOException;

	public void validateUrl(String url) {
		try {
			HostnamePort hnp = HostnamePort.parseUrl(url);
			if (hnp == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new RuntimeException("Invalid Url: hostname:port");
		}
	}
}