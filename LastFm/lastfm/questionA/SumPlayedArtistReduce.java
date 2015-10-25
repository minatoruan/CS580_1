package lastfm.questionA;

import java.io.IOException;

import lastfm.IterationState;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SumPlayedArtistReduce extends Reducer<Text, IntWritable, Text, LongWritable> {
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		long sum = 0L;
		
		for(IntWritable numPlays : values) {
			sum += numPlays.get();
		}
		context.write(key, new LongWritable(sum));
		context.getCounter(IterationState.numberofReduce).increment(1L);
	}
}
