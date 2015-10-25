#!/bin/bash
OUTPUT="lastfminput"

for k in `~/hadoop/bin/hadoop fs -ls $OUTPUT | grep $OUTPUT | awk '{print $8}'` 
do 
	echo $k
done




