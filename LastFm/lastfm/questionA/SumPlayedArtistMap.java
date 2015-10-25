package lastfm.questionA;

import java.io.IOException;

import lastfm.PlayInfo;
import lastfm.PlayInfoImmediateData;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SumPlayedArtistMap extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (PlayInfo.IsPlayInfo(value.toString())) {
			PlayInfo pi = PlayInfo.parse(value.toString());
			context.write(new Text(pi.artistname), new IntWritable(pi.numPlays));
			return;
		}
		if (PlayInfoImmediateData.isPlayInfo(value.toString())) {
			PlayInfoImmediateData pi = PlayInfoImmediateData.parse(value.toString());
			context.write(new Text(pi.artistname), new IntWritable(pi.numPlays));
			return;
		}
	}
}
