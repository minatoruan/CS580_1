import lastfm.IterationState;
import lastfm.OrderMap;
import lastfm.OrderReduce;
import lastfm.questionA.*;
import lastfm.questionB.*;
import lastfm.questionC.*;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class LastFm {
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		String plays = args[0];
		String users = args[1];
		String output = args[2];
		/*question 1*/
		Order(computeSumPlayedArtists(plays, output), output);
		/*question 2*/
		computeUniqueUserPlays(joinUniqueUserPlays(plays, output), output);
		/*question3*/
		avgAgeByArtistMapReduce(computeAgeByArtistMapReduce(joinUserAndPlayMapReduce(plays, users, output), output), output);
	}
	
	private static void avgAgeByArtistMapReduce(String input, String output) throws IOException, InterruptedException, ClassNotFoundException {	
		Job job = getDefaultConf();
		
		job.setMapperClass(AvgAgeByArtistMap.class);
		job.setReducerClass(AvgAgeByArtistReduce.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output + "/q3result"));
		
		job.setSortComparatorClass(Text.Comparator.class);
		
		job.waitForCompletion(true);
		deletePreviousPath(job.getConfiguration(), input);
	}
	
	private static String computeAgeByArtistMapReduce(String plays, String output) throws IOException, InterruptedException, ClassNotFoundException {
		long numOfReduce = 0;
		int index = 0;
		Job job = null;
		String prefix = "/q3_2_";
		String[] inputoutputPath;
		do {
			if(job != null) {
				numOfReduce = getNumberOfReduces(job);
			}			
			job = getDefaultConf();
			
			job.setMapperClass(ComputeAgeByArtistMap.class);
			job.setReducerClass(ComputeAgeByArtistReduce.class);
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			
			inputoutputPath = generateInputOutputPath(index++, prefix, plays, output);
			FileInputFormat.setInputPaths(job, new Path(inputoutputPath[0]));
			FileOutputFormat.setOutputPath(job, new Path(inputoutputPath[1]));
			
			job.setSortComparatorClass(Text.Comparator.class);
			
			job.waitForCompletion(true);
			deletePreviousPath(job.getConfiguration(), inputoutputPath[0]);
		} while (numOfReduce != getNumberOfReduces(job));
		return inputoutputPath[1];
	}
	
	private static String joinUserAndPlayMapReduce(String plays, String users, String output) throws IOException, InterruptedException, ClassNotFoundException {
		long numOfReduce = 0;
		int index = 0;
		Job job = null;
		String prefix = "/q3_1_";
		String[] inputoutputPath;
		do {		
			if(job != null) {
				numOfReduce = getNumberOfReduces(job);
			}
			
			job = getDefaultConf();
			
			job.setMapperClass(JoinUserAndPlayMap.class);
			job.setReducerClass(JoinUserAndPlayReduce.class);
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			
			inputoutputPath = generateInputOutputPath(index++, prefix, plays, output);
			FileInputFormat.setInputPaths(job, new Path(inputoutputPath[0]), new Path(users));
			FileOutputFormat.setOutputPath(job, new Path(inputoutputPath[1]));
			
			job.waitForCompletion(true);
			if (index > 1) {
				deletePreviousPath(job.getConfiguration(), inputoutputPath[0]);
			}
		} while (numOfReduce != getNumberOfReduces(job) || getNumberOfReduces(job) == 0);
		return inputoutputPath[1];
	}
	
	private static void computeUniqueUserPlays(String input, String output) throws IOException, InterruptedException, ClassNotFoundException {
		Job job = getDefaultConf();
		
		job.setMapperClass(ComputeNumberOfUniqueUserPlayEachArtistMap.class);
		job.setReducerClass(ComputeNumberOfUniqueUserPlayEachArtistReduce.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output + "/q2result"));
		
		job.waitForCompletion(true);
		deletePreviousPath(job.getConfiguration(), input);
	}	
	
	private static String joinUniqueUserPlays(String plays, String output) throws IOException, InterruptedException, ClassNotFoundException {
		long numOfReduce = 0;
		int index = 0;
		Job job = null;
		String prefix = "/q2_";
		String[] inputoutputPath;
		do {
			if(job != null) {
				numOfReduce = getNumberOfReduces(job);
			}
			
			job = getDefaultConf();
			
			job.setMapperClass(JoinUniqueUserPlayEachArtistMap.class);
			job.setReducerClass(JoinUniqueUserPlayEachArtistReduce.class);
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			
			inputoutputPath = generateInputOutputPath(index++, prefix, plays, output);
			FileInputFormat.setInputPaths(job, new Path(inputoutputPath[0]));
			FileOutputFormat.setOutputPath(job, new Path(inputoutputPath[1]));
			
			job.waitForCompletion(true);
			if (index > 1) {
				deletePreviousPath(job.getConfiguration(), inputoutputPath[0]);
			}
		} while (numOfReduce != getNumberOfReduces(job));
		return inputoutputPath[1];
	}	

	private static void Order(String input, String output) throws IOException, InterruptedException, ClassNotFoundException {
		Job job = getDefaultConf();
		
		job.setMapperClass(OrderMap.class);
		job.setReducerClass(OrderReduce.class);
		
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		job.setSortComparatorClass(LongWritable.DecreasingComparator.class);
		
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output + "/q1result"));
		
		job.waitForCompletion(true);
		deletePreviousPath(job.getConfiguration(), input);
	}	
	
	private static String computeSumPlayedArtists(String plays, String output) throws IOException, InterruptedException, ClassNotFoundException {
		long numOfReduce = 0;
		int index = 0;
		Job job = null;
		String prefix = "/q1_";
		String[] inputoutputPath;
		do {
			if(job != null) {
				numOfReduce = getNumberOfReduces(job);
			}
			job = getDefaultConf();
			
			job.setMapperClass(SumPlayedArtistMap.class);
			job.setReducerClass(SumPlayedArtistReduce.class);
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			
			inputoutputPath = generateInputOutputPath(index++, prefix, plays, output);
			FileInputFormat.setInputPaths(job, new Path(inputoutputPath[0]));
			FileOutputFormat.setOutputPath(job, new Path(inputoutputPath[1]));
			
			job.waitForCompletion(true);
			if (index > 1) {
				deletePreviousPath(job.getConfiguration(), inputoutputPath[0]);
			}
		} while (numOfReduce != getNumberOfReduces(job));
		return inputoutputPath[1];
	}
	
	private static String[] generateInputOutputPath(int index, String prefix, String input, String output) {
		String si = null;
		String so = "";
		if(index == 0) {
			si = input;
		} else {
			si = output + prefix + (index - 1);
		}
		so = output + prefix + (index);
		return new String[] {si, so};
	}
	
	private static long getNumberOfReduces(Job job) throws IOException {
		Counters jobCntrs = job.getCounters();
		return jobCntrs.findCounter(IterationState.numberofReduce).getValue();
	}	

	private static Job getDefaultConf() throws IOException {
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "SumPlayedArtists");
		job.setJarByClass(LastFm.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		return job;
	}
	
	private static void deletePreviousPath(Configuration conf, String path) throws IOException {
		FileSystem dfs = FileSystem.get(conf);
		dfs.delete(new Path(path), true);
	}
}
