#!/bin/bash
         
INPUT1="lastfmplaysinput"
INPUT2="lastfmusersinput"
OUTPUT="lastfmoutput"
JARFN="LastFm"
HADOOP=~/hadoop/bin/hadoop
CLASSPATH="/home/admin/hadoop/hadoop-core-1.2.1.jar"

mkdir -p ../build/bin
mkdir -p ../build/jar

javac -classpath $CLASSPATH -d ../build/bin/ ./*.java ./lastfm/*.java ./lastfm/questionA/*.java ./lastfm/questionB/*.java ./lastfm/questionC/*.java
jar -cvf ../build/jar/$JARFN.jar -C ../build/bin/ .

$HADOOP fs -rmr $OUTPUT
$HADOOP jar ../build/jar/$JARFN.jar $JARFN $INPUT1 $INPUT2 $OUTPUT

rm ../test/output1.txt
$HADOOP fs -getmerge $OUTPUT/q1result ../test/output1.txt

rm ../test/output2.txt
$HADOOP fs -getmerge $OUTPUT/q2result ../test/output2.txt

rm ../test/output3.txt
$HADOOP fs -getmerge $OUTPUT/q3result ../test/output3.txt





