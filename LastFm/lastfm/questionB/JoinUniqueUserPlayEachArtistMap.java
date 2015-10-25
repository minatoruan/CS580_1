package lastfm.questionB;

import java.io.IOException;

import lastfm.PlayInfo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class JoinUniqueUserPlayEachArtistMap extends Mapper<LongWritable, Text, Text, Text> {
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String s = value.toString();
		if (s.isEmpty()) return;
		
		if (PlayInfo.IsPlayInfo(s)) {
			PlayInfo pi = PlayInfo.parse(s);
			context.write(new Text(pi.artistname), new Text(pi.uid));
			return;
		} 
			
		String[] str = s.split("\\t");
		if(str.length > 1) {
			context.write(new Text(str[0].trim()), new Text(str[1].trim()));
		} else {
			context.write(new Text(str[0].trim()), new Text(""));
		}
		
	}
}