package lastfm.questionC;

import java.io.IOException;

import lastfm.ArtistAgeImmediateData;
import lastfm.PlayInfoAgeImmediateData;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ComputeAgeByArtistMap extends Mapper<LongWritable, Text, Text, Text> {
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String data = value.toString();
		ArtistAgeImmediateData aai = null;
		
		if(PlayInfoAgeImmediateData.isPlayInfo(data)) {
			PlayInfoAgeImmediateData piai = PlayInfoAgeImmediateData.parse(data);
			aai = ArtistAgeImmediateData.parse(piai);
		} else {
			aai = ArtistAgeImmediateData.parse(data);
		}
		
		context.write(new Text(aai.artistname), new Text(aai.toString()));
	}
}