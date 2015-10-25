import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DijkstraMap extends Mapper<LongWritable, Text, Text, Text> {
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		SSSPState state = SSSPState.parse(value.toString());
	
		if (state.wasVisited || state.weight == Integer.MAX_VALUE) {
			context.write(new Text(state.node), new Text(state.toString()));
			return;
		} 
		
		state.wasVisited = true;
		context.getCounter(Session.IterationState.numberOfDijkstraMap).increment(1L);
		context.write(new Text(state.node), new Text(state.toString()));
		
		if (state.adjacents.length == 0) {
			return;
		}
		
		for(String neighbour : state.adjacents) {
			SSSPData data = new SSSPData();
			data.fromNode = state.node;
			data.distance = state.weight + 1;
			context.write(new Text(neighbour), new Text(data.toString()));
		}
	}
}