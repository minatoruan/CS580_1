package lastfm.questionC;

import java.io.IOException;

import lastfm.ArtistAgeImmediateData;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AvgAgeByArtistReduce extends Reducer<Text, Text, Text, Text> {
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		ArtistAgeImmediateData collector = ArtistAgeImmediateData.parse(key.toString(), values.iterator().next().toString());
		if (collector.isSetAge()) {
			context.write(key, new Text(String.valueOf(collector.getAgeAvg())));
		} else {
			context.write(key, new Text("N/A"));
		}
	}
}