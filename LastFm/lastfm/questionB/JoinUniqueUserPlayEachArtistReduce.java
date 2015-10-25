package lastfm.questionB;

import java.io.IOException;
import java.util.HashSet;

import lastfm.IterationState;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class JoinUniqueUserPlayEachArtistReduce extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		HashSet<String> hash = new HashSet<String>();
		
		for(Text uid : values){
			String str = uid.toString();
			if (str.indexOf(" ") > 0) continue; // I do not count the record which user id is a datetime;
			if (str.indexOf("|") == -1) {
				hash.add(str);
				continue;
			}
			for(String s : str.split("\\|")) {
				if(s.isEmpty()) continue;
				hash.add(s);
			}
		}
		context.write(key, new Text(combineHash(hash)));
		context.getCounter(IterationState.numberofReduce).increment(1L);
	}
	
	private String combineHash(HashSet<String> hash) {
		StringBuilder sb = new StringBuilder();
		for(String s : hash) {
			sb.append(s);
			sb.append("|");
		}
		return sb.toString();
	}
}
