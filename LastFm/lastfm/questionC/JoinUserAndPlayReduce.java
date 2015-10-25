package lastfm.questionC;

import java.io.IOException;
import java.util.ArrayList;

import lastfm.PlayInfoAgeImmediateData;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class JoinUserAndPlayReduce extends Reducer<Text, Text, Text, Text> {
	
	public static boolean isNumeric(String str) {
		try  {  
			Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe)  {  
			return false;  
		}  
		return true;
	}
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		int age = -2; // age not set
		ArrayList<PlayInfoAgeImmediateData> playInfoAgeList = new ArrayList<PlayInfoAgeImmediateData>();
		
		for(Text data : values) {
			String str = data.toString();
			if (isNumeric(str)) {
				age = Integer.parseInt(str);
				continue;
			}
			playInfoAgeList.add(PlayInfoAgeImmediateData.parse(key.toString(), str));
		}
		
		for(PlayInfoAgeImmediateData pi : playInfoAgeList) {
			pi.age = age;
			context.write(key, new Text(pi.toString()));
		}
	}
}
