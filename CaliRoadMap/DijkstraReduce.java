import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DijkstraReduce extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values,	Context context) throws IOException, InterruptedException {
		String fromNode = SSSPState.NullString;
		int min = Integer.MAX_VALUE;	
		
		SSSPState currentState = null;
		SSSPData processData = null;

		for(Text data : values) {
			String str = data.toString();
			
			if(!SSSPData.isSSSPData(str)) {
				currentState = SSSPState.parse(key.toString(), str);
				min = Math.min(currentState.weight, min);
				fromNode = (min == currentState.weight ? currentState.fromNodes[0] : fromNode);
				continue;
			} 
			
			processData = SSSPData.parse(str);
			min = Math.min(processData.distance, min);
			fromNode = min == processData.distance ? processData.fromNode : fromNode;
		}
		currentState.weight = min;
		currentState.fromNodes[0] = fromNode;
		context.write(key, new Text(currentState.toString()));
	}
}