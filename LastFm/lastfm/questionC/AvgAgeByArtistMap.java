package lastfm.questionC;

import java.io.IOException;

import lastfm.ArtistAgeImmediateData;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AvgAgeByArtistMap extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String data = value.toString();
		ArtistAgeImmediateData aai = ArtistAgeImmediateData.parse(data);
		context.write(new Text(aai.artistname), new Text(aai.toString()));
	}	
}