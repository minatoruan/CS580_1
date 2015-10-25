package lastfm.questionB;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ComputeNumberOfUniqueUserPlayEachArtistReduce extends Reducer<Text, Text, Text, IntWritable> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String str = values.iterator().next().toString();
		int numOfUsers = str.substring(0, str.length() - 1).split("\\|").length;
		context.write(key, new IntWritable(numOfUsers));
	}
}