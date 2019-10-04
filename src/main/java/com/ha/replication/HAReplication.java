package com.ha.replication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public class HAReplication {

	private HAReplication() {

	}

	public static ReplicationFix getReplicationFix(final Map<String, Set<String>> inputHostNameToFiles,
			final String failedHost) {
		final ReplicationFix replicationFix = new ReplicationFix();
		final Map<String, String> fileToBeCopiedFromHost = findFilesToBeReplicatedToHostMapping(inputHostNameToFiles,
				failedHost, replicationFix);
		replicationFix.setRunningHosts(getCurrentRunningHosts(replicationFix, failedHost));
		final Set<String> hostsToHandleReplication = getHostsDistributionList(fileToBeCopiedFromHost, replicationFix);
		prepareReplicationFix(hostsToHandleReplication, replicationFix);
		prepareReplicationFixSummary(replicationFix, fileToBeCopiedFromHost);
		return replicationFix;
	}

	private static Map<String, String> findFilesToBeReplicatedToHostMapping(
			final Map<String, Set<String>> inputHostNameToFiles, final String failedHost,
			final ReplicationFix replicationFix) {
		final Set<String> filesToBeReplicated = inputHostNameToFiles.get(failedHost);
		final Map<String, String> fileToBeCopiedFromHost = new HashMap<>();
		final Map<String, String> fileToHostName = getFileHostMappingFromInput(inputHostNameToFiles, replicationFix,
				failedHost);
		for (String file : filesToBeReplicated) {
			fileToBeCopiedFromHost.put(file, fileToHostName.get(file));
		}
		replicationFix.setFileToBeCopiedFromHost(fileToBeCopiedFromHost);
		return fileToBeCopiedFromHost;
	}

	private static Map<String, String> getFileHostMappingFromInput(final Map<String, Set<String>> inputHostNameToFiles,
			final ReplicationFix replicationFix, final String failedHost) {
		final Map<String, String> fileToHostName = new HashMap<>();
		final Set<Entry<String, Set<String>>> entrySet = inputHostNameToFiles.entrySet();
		final Set<String> runningHosts = new HashSet<>();
		for (Entry<String, Set<String>> entry : entrySet) {
			if (!entry.getKey().equalsIgnoreCase(failedHost)) {
				runningHosts.add(entry.getKey());
				if(Objects.nonNull(entry.getValue())) {
					final Iterator<String> iterator = entry.getValue().iterator();
					while (iterator.hasNext()) {
						fileToHostName.put(iterator.next(), entry.getKey());
					}
				}
			}
		}
		replicationFix.setRunningHosts(runningHosts);
		return fileToHostName;
	}

	private static Set<String> getHostsDistributionList(final Map<String, String> fileToBeCopiedFromHost,
			final ReplicationFix replicationFix) {
		final Set<Entry<String, String>> entrySet = fileToBeCopiedFromHost.entrySet();
		final Set<String> hostsToBeExcluded = new HashSet<>();
		final Set<String> filesToBeReplicated = new HashSet<>();
		for (Entry<String, String> entry : entrySet) {
			hostsToBeExcluded.add(entry.getValue());
			filesToBeReplicated.add(entry.getKey());
		}
		final Set<String> hostsToHandleReplication = new HashSet<>(replicationFix.getRunningHosts());
		hostsToHandleReplication.removeAll(hostsToBeExcluded);
		replicationFix.setFilesToBeReplicated(filesToBeReplicated);
		return hostsToHandleReplication;
	}

	private static Set<String> getCurrentRunningHosts(final ReplicationFix replicationFix, final String failedHost) {
		final Set<String> hosts = replicationFix.getRunningHosts();
		hosts.remove(failedHost);
		return hosts;
	}

	private static void prepareReplicationFix(final Set<String> hostsToHandleReplication,
			final ReplicationFix replicationFix) {
		final Set<String> filesToBeReplicated = replicationFix.getFilesToBeReplicated();
		int hostsSize = hostsToHandleReplication.size();
		int fileSize = filesToBeReplicated.size();
		final Map<String, String> fileToHostName = new HashMap<>();
		final List<String> filesToBeReplicatedList = new ArrayList<>(filesToBeReplicated);
		final Set<String> hostsWithPrimaryResponsibility = new HashSet<>();
		// consider hosts with primary resp
		setOrderOfHostsToHandleReplication(replicationFix, hostsToHandleReplication);
		Iterator<String> iterator = Objects.isNull(replicationFix.getHostsOrderedOnPriority())
				? hostsToHandleReplication.iterator()
				: replicationFix.getHostsOrderedOnPriority().iterator();
		int i = 0;
		if (hostsSize == fileSize) {
			while (iterator.hasNext()) {
				fileToHostName.put(filesToBeReplicatedList.get(i), iterator.next());
				i++;
			}
		} else if (hostsSize > fileSize) {
			while (iterator.hasNext()) {
				if (i >= fileSize)
					hostsWithPrimaryResponsibility.add(iterator.next());
				else
					fileToHostName.put(filesToBeReplicatedList.get(i), iterator.next());
				i++;
			}
			replicationFix.setHostsWithPrimaryResponsibility(hostsWithPrimaryResponsibility);
		} else if (hostsSize < fileSize) {
			while (iterator.hasNext()) {
				fileToHostName.put(filesToBeReplicatedList.get(i), iterator.next());
				i++;
			}
			iterator = hostsToHandleReplication.iterator();
			while (iterator.hasNext()) {
				if (i < fileSize) {
					fileToHostName.put(filesToBeReplicatedList.get(i), iterator.next());
					if(!iterator.hasNext())
						iterator = hostsToHandleReplication.iterator();
				}
				else
					hostsWithPrimaryResponsibility.add(iterator.next());
				i++;
			}
		}
		replicationFix.setFileNameToHost(fileToHostName);
	}

	private static void setOrderOfHostsToHandleReplication(final ReplicationFix replicationFix,
			final Set<String> hostsToHandleReplication) {
		final Set<String> hostsWithPrimaryResponsibility = replicationFix.getHostsWithPrimaryResponsibility();
		if (Objects.isNull(hostsWithPrimaryResponsibility))
			return;
		final List<String> hostsInOrder = new ArrayList<>();
		for (String host : hostsWithPrimaryResponsibility) {
			hostsInOrder.add(host);
		}
		for (String host : hostsToHandleReplication) {
			if (!hostsInOrder.contains(host))
				hostsInOrder.add(host);
		}
		replicationFix.setHostsOrderedOnPriority(hostsInOrder);
	}

	private static void prepareReplicationFixSummary(final ReplicationFix replicationFix,
			final Map<String, String> fileToBeCopiedFromHost) {
		replicationFix
				.setDescription("------------------------Replication Fix Summary---------------------------------");
		Map<String, String> fileNameToHost = replicationFix.getFileNameToHost();
		Set<Entry<String, String>> entrySet = fileNameToHost.entrySet();
		StringBuilder sb = new StringBuilder();
		sb.append(replicationFix.getDescription()).append("\n");
		for (Entry<String, String> entry : entrySet) {
			sb.append(entry.getKey()).append(" to be copied from ").append(fileToBeCopiedFromHost.get(entry.getKey()))
					.append(" to ").append(entry.getValue()).append("\n");
		}

		System.out.println(sb.toString());
		replicationFix.setReplicationFixSummary(sb.toString());
	}
}
