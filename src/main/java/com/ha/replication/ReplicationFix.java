package com.ha.replication;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReplicationFix {

	private String description;
	
	private Map<String, String> fileNameToHost;
	
	private Set<String> runningHosts;
	
	private Set<String> filesToBeReplicated;
	
	private Set<String> hostsWithPrimaryResponsibility;
	
	private List<String> hostsOrderedOnPriority;
	
	private Map<String, String> fileToBeCopiedFromHost;
	
	private String replicationFixSummary;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getFileNameToHost() {
		return fileNameToHost;
	}

	public void setFileNameToHost(Map<String, String> fileNameToHost) {
		this.fileNameToHost = fileNameToHost;
	}

	public Set<String> getRunningHosts() {
		return runningHosts;
	}

	public void setRunningHosts(Set<String> runningHosts) {
		this.runningHosts = runningHosts;
	}

	public Set<String> getFilesToBeReplicated() {
		return filesToBeReplicated;
	}

	public void setFilesToBeReplicated(Set<String> filesToBeReplicated) {
		this.filesToBeReplicated = filesToBeReplicated;
	}

	public Set<String> getHostsWithPrimaryResponsibility() {
		return hostsWithPrimaryResponsibility;
	}

	public void setHostsWithPrimaryResponsibility(Set<String> hostsWithPrimaryResponsibility) {
		this.hostsWithPrimaryResponsibility = hostsWithPrimaryResponsibility;
	}

	public List<String> getHostsOrderedOnPriority() {
		return hostsOrderedOnPriority;
	}

	public void setHostsOrderedOnPriority(List<String> hostsOrderedOnPriority) {
		this.hostsOrderedOnPriority = hostsOrderedOnPriority;
	}

	public Map<String, String> getFileToBeCopiedFromHost() {
		return fileToBeCopiedFromHost;
	}

	public void setFileToBeCopiedFromHost(Map<String, String> fileToBeCopiedFromHost) {
		this.fileToBeCopiedFromHost = fileToBeCopiedFromHost;
	}
	

	@Override
	public String toString() {
		return "ReplicationFix [description=" + description + ", fileNameToHost=" + fileNameToHost + ", runningHosts="
				+ runningHosts + ", filesToBeReplicated=" + filesToBeReplicated + ", hostsWithPrimaryResponsibility="
				+ hostsWithPrimaryResponsibility + ", hostsOrderedOnPriority=" + hostsOrderedOnPriority
				+ ", fileToBeCopiedFromHost=" + fileToBeCopiedFromHost + "]";
	}

	public String getReplicationFixSummary() {
		return replicationFixSummary;
	}

	public void setReplicationFixSummary(String replicationFixSummary) {
		this.replicationFixSummary = replicationFixSummary;
	}
	
	
}
