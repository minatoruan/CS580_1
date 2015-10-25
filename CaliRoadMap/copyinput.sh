#!/bin/bash

INPUT="caliinput"
HADOOP=~/hadoop/bin/hadoop

$HADOOP fs -rmr $INPUT
$HADOOP fs -mkdir $INPUT
$HADOOP fs -copyFromLocal ./input.txt $INPUT
