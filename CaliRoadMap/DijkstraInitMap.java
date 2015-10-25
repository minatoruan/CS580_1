import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DijkstraInitMap extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String edge = value.toString();
		String[] splits = edge.split("\\t");
		
		if (edge.startsWith("#") || splits.length != 2) {
			return;
		}
		context.write(new Text(splits[0]), new Text(splits[1]));
		context.write(new Text(splits[1]), new Text(""));
	}
}