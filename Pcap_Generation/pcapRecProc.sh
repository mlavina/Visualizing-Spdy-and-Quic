#!/bin/bash
cat 'top200.txt' | while read LINE
do
	FILENAME="fixed_$LINE.pcap"
	wireshark -k -a duration:20 -w $FILENAME &
	sleep 8
		open -a /Applications/Google\ Chrome.app/ "http://$LINE"
	sleep 15
	killall Google\ Chrome
	killall Wireshark
done
