#!/bin/bash

INPUT1="lastfmplaysinput"
INPUT2="lastfmusersinput"
HADOOP=~/hadoop/bin/hadoop

$HADOOP fs -rmr $INPUT1
$HADOOP fs -mkdir $INPUT1
$HADOOP fs -copyFromLocal ./input.tsv $INPUT1
#$HADOOP fs -copyFromLocal ./usersha1-artmbid-artname-plays.tsv $INPUT1

$HADOOP fs -rmr $INPUT2
$HADOOP fs -mkdir $INPUT2
$HADOOP fs -copyFromLocal ../test/data/usersha1-profile.tsv $INPUT2
