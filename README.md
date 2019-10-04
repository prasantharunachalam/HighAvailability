# HighAvailability
Problems related to HA &amp; Replication

Input1:  Map of HostServer to Files Maping
Input2:  HostName which is down
Replication Factor: 2


Assumptions:
All Hosts have equal capacity
All Hosts have equal distribution of files across them say 
Host 1 has 2 files
Host 2 will also have 2 files

Output:
Solution will propose replication fix as below summary of instructions

------------------------Replication Fix Summary---------------------------------

File4 to be copied from Host4 to Host3

File3 to be copied from Host4 to Host1


