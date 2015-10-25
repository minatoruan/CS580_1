package lastfm.questionC;

import java.io.IOException;

import lastfm.IterationState;
import lastfm.PlayInfo;
import lastfm.PlayInfoAgeImmediateData;
import lastfm.UserAgeInfo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class JoinUserAndPlayMap extends Mapper<LongWritable, Text, Text, Text> {
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String str = value.toString();

		if (PlayInfo.IsPlayInfo(str)) {
			PlayInfo pi = PlayInfo.parse(str);
			PlayInfoAgeImmediateData piai = PlayInfoAgeImmediateData.parse(pi);
			
			context.write(new Text(piai.uid), new Text(piai.toString()));
			context.getCounter(IterationState.numberofReduce).increment(1L);
			return;
		} 
		if (UserAgeInfo.isUserAgeInfo(str)) {
			UserAgeInfo uai = UserAgeInfo.parse(str);
			context.write(new Text(uai.uid), new Text(String.valueOf(uai.age)));
			return;
		}
		if (PlayInfoAgeImmediateData.isPlayInfo(str)) {
			PlayInfoAgeImmediateData piai = PlayInfoAgeImmediateData.parse(str);
			context.write(new Text(piai.uid), new Text(piai.toString()));
			
			if(!piai.isSetAge()) {
				context.getCounter(IterationState.numberofReduce).increment(1L);
			}
			return;
		}
		
	}
}
