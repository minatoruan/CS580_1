import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class Dijkstra {

	private static String _deletePath = "";
	
	public static void main(String[] args) throws Exception {
		String output="";
		output = runDijstraMapReduceInit(args[2], args[0], args[1]);
		output = runDijstraMapReduce(output, args[1]);
		output = runShortestPathMapReduce(output, args[1]);
		runFormatMapReduce(output, args[1]);
	}
	
	private static void runFormatMapReduce(String input, String output) throws IOException, InterruptedException, ClassNotFoundException {
		String prefix = "3_format";
		Job	job = getDefaultConf();
					
		job.setMapperClass(FormatResultMap.class);
		job.setReducerClass(FormatResultReduce.class);
		
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output + "/" +  prefix));
		
		job.waitForCompletion(true);
		
		_deletePath = input;
		deletePreviousPath(job.getConfiguration());
	}
	
	private static String runShortestPathMapReduce(String input, String output) throws IOException, InterruptedException, ClassNotFoundException {
		Job job = null;
		int index = 0;
		String prefix = "2_shortest";
		do {
			job = getDefaultConf();
						
			job.setMapperClass(ShortestPathPrinterMap.class);
			job.setReducerClass(ShortestPathPrinterReduce.class);
			
			if (index == 0) { 
				FileInputFormat.setInputPaths(job, new Path(input));
				_deletePath = input;
			} else {
				FileInputFormat.setInputPaths(job, new Path(output + "/" + prefix +  (index - 1)));
				_deletePath = output + "/" + prefix +  (index - 1);
			}
			
			FileOutputFormat.setOutputPath(job, new Path(output + "/" + prefix +  (index++)));
			
			job.waitForCompletion(true);
			deletePreviousPath(job.getConfiguration());
			
		} while(job != null && needMoreIterations(job));
		return output + "/" + prefix +  (index - 1);
	}
	
	private static String runDijstraMapReduce(String input, String output) throws IOException, InterruptedException, ClassNotFoundException {
		Job job = null;
		int index = 0;
		String prefix = "1_dijstra";
		do {
			job = getDefaultConf();
						
			job.setMapperClass(DijkstraMap.class);
			job.setReducerClass(DijkstraReduce.class);
			
			if (index == 0) { 
				FileInputFormat.setInputPaths(job, new Path(input));
				_deletePath = input;
			} else {
				FileInputFormat.setInputPaths(job, new Path(output + "/" + prefix +  (index - 1)));
				_deletePath = output + "/" + prefix +  (index - 1);
			}
			FileOutputFormat.setOutputPath(job, new Path(output + "/" + prefix +  (index++)));
			
			job.waitForCompletion(true);
			deletePreviousPath(job.getConfiguration());
			
		} while(job != null && needMoreIterations(job));
		return output + "/" + prefix +  (index - 1);
	}

	private static String runDijstraMapReduceInit(String startNode, String input, String output) throws IOException, InterruptedException, ClassNotFoundException {
		String prefix = "0_init";
		
		Job job = getDefaultConf();

		Session.setStartNode(job.getConfiguration(), startNode);
				
		job.setMapperClass(DijkstraInitMap.class);
		job.setReducerClass(DijkstraInitReduce.class);
		
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output + "/" + prefix));
		
		job.waitForCompletion(true);
		
		return output + "/" + prefix;
	}
	
	private static Job getDefaultConf() throws IOException {
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "CaliSSSP");
		job.setJarByClass(Dijkstra.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		return job;
	}
	
	private static void deletePreviousPath(Configuration conf) throws IOException {
		FileSystem dfs = FileSystem.get(conf);
		dfs.delete(new Path(_deletePath), true);
	}
	
	private static boolean needMoreIterations(Job job) throws IOException {
		Counters jobCntrs = job.getCounters();
		return jobCntrs.findCounter(Session.IterationState.numberOfDijkstraMap).getValue() > 0L;
	}
	
}
