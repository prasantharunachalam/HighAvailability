package com.ha.replication;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class HAReplicationTest {

	@Test
	public void getReplicationFix_GivenHostToFileMappingAndFailedHostWithHostSizeEqualsFileSize_ShouldReturnReplicationFixSummary() {
		final Map<String, Set<String>> hostNameToFiles = new HashMap<>();
		final Set<String> filesListOne = new HashSet<>();
		filesListOne.add("File1");
		filesListOne.add("File2");
		final Set<String> filesListTwo = new HashSet<>();
		filesListTwo.add("File3");
		filesListTwo.add("File4");
		hostNameToFiles.put("Host1", filesListOne);
		hostNameToFiles.put("Host2", filesListTwo);
		hostNameToFiles.put("Host3", filesListOne);
		hostNameToFiles.put("Host4", filesListTwo);	
		final ReplicationFix replicationFix = HAReplication.getReplicationFix(hostNameToFiles, "Host2");
		assertNotNull(replicationFix);
		assertNotNull(replicationFix.getReplicationFixSummary());
		final String outputTextOne = "File4 to be copied from Host4 to Host3";
		final String outputTextTwo = "File3 to be copied from Host4 to Host1";
		assertTrue(replicationFix.getReplicationFixSummary().contains(outputTextOne));
		assertTrue(replicationFix.getReplicationFixSummary().contains(outputTextTwo));
	}
	
	@Test
	public void getReplicationFix_GivenHostToFileMappingAndFailedHostWithHostSizeGreaterThanFileSize_ShouldReturnReplicationFixSummary() {
		final Map<String, Set<String>> hostNameToFiles = new HashMap<>();
		final Set<String> filesListOne = new HashSet<>();
		filesListOne.add("File1");
		filesListOne.add("File2");
		final Set<String> filesListTwo = new HashSet<>();
		filesListTwo.add("File3");
		filesListTwo.add("File4");
		final Set<String> filesListThree = new HashSet<>();
		filesListThree.add("File5");
		filesListThree.add("File6");
		hostNameToFiles.put("Host1", filesListOne);
		hostNameToFiles.put("Host2", filesListTwo);
		hostNameToFiles.put("Host3", filesListOne);
		hostNameToFiles.put("Host4", filesListTwo);
		hostNameToFiles.put("Host5", filesListThree);	
		hostNameToFiles.put("Host6", filesListThree);	
		final ReplicationFix replicationFix = HAReplication.getReplicationFix(hostNameToFiles, "Host2");
		assertNotNull(replicationFix);
		assertNotNull(replicationFix.getReplicationFixSummary());
		final String outputTextOne = "File4 to be copied from Host4 to Host3";
		final String outputTextTwo = "File3 to be copied from Host4 to Host1";
		assertTrue(replicationFix.getReplicationFixSummary().contains(outputTextOne));
		assertTrue(replicationFix.getReplicationFixSummary().contains(outputTextTwo));
	}
	
	@Test
	public void getReplicationFix_GivenHostToFileMappingAndFailedHostWithHostSizeLessThanFileSize_ShouldReturnReplicationFixSummary() {
		final Map<String, Set<String>> hostNameToFiles = new HashMap<>();
		final Set<String> filesListOne = new HashSet<>();
		filesListOne.add("File1");
		filesListOne.add("File2");
		filesListOne.add("File3");
		filesListOne.add("File4");
		final Set<String> filesListTwo = new HashSet<>();
		filesListTwo.add("File1");
		filesListTwo.add("File2");
		filesListTwo.add("File3");
		filesListTwo.add("File4");
		hostNameToFiles.put("Host1", filesListOne);
		hostNameToFiles.put("Host2", filesListTwo);
		hostNameToFiles.put("Host3", null);
		final ReplicationFix replicationFix = HAReplication.getReplicationFix(hostNameToFiles, "Host2");
		assertNotNull(replicationFix);
		assertNotNull(replicationFix.getReplicationFixSummary());
		final String outputTextOne = "File2 to be copied from Host1 to Host3";
		final String outputTextTwo = "File3 to be copied from Host1 to Host3";
		final String outputTextThree = "File1 to be copied from Host1 to Host3";
		assertTrue(replicationFix.getReplicationFixSummary().contains(outputTextOne));
		assertTrue(replicationFix.getReplicationFixSummary().contains(outputTextTwo));
		assertTrue(replicationFix.getReplicationFixSummary().contains(outputTextThree));
	}	
}
