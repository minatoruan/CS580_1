package lastfm;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class OrderReduce extends Reducer<LongWritable, Text, Text, LongWritable> {
	public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		for(Text text : values) {
			context.write(text, key);
		}
	}
}
