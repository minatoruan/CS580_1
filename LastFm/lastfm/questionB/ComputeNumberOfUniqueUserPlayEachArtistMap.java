package lastfm.questionB;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ComputeNumberOfUniqueUserPlayEachArtistMap extends Mapper<LongWritable, Text, Text, Text> {
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {	
		String[] str = value.toString().split("\\t");
		context.write(new Text(str[0].trim()), new Text(str[1].trim()));
	}
}