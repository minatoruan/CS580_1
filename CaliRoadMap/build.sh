#!/bin/bash
         
INPUT="caliinput"
OUTPUT="calioutput"
JARFN="Dijkstra"
HADOOP=~/hadoop/bin/hadoop
CLASSPATH="/home/admin/hadoop/hadoop-core-1.2.1.jar"

mkdir -p ../build/bin
mkdir -p ../build/jar

javac -classpath $CLASSPATH -d ../build/bin/ *.java
jar -cvf ../build/jar/$JARFN.jar -C ../build/bin/ .

echo $HADOOP fs -rmr $OUTPUT
$HADOOP jar ../build/jar/$JARFN.jar $JARFN $INPUT $OUTPUT 0

rm ./output.txt
$HADOOP fs -getmerge $OUTPUT/3_format ./output.txt
gedit output.txt






