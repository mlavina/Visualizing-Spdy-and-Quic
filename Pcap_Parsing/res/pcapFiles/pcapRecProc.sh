#!/bin/bash
cat 'top200.txt' | while read LINE
do
	FILENAME="$LINE.pcap"
	wireshark -k -a duration:30 -w $FILENAME &
	sleep 7
	for i in {1..5}
	do
		curl -s $LINE > /dev/null
		sleep 2
	done
	sleep 20
	killall Wireshark
done
