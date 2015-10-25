package lastfm.questionC;

import java.io.IOException;

import lastfm.ArtistAgeImmediateData;
import lastfm.IterationState;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ComputeAgeByArtistReduce extends Reducer<Text, Text, Text, Text> {
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		ArtistAgeImmediateData collector = new ArtistAgeImmediateData();
		collector.artistname = key.toString();

		for(Text dataline : values) {
			ArtistAgeImmediateData aai = ArtistAgeImmediateData.parse(key.toString(), dataline.toString());
			collector.computeSum(aai);
		}
		context.write(key, new Text(collector.toString()));
		context.getCounter(IterationState.numberofReduce).increment(1L);
	}
}