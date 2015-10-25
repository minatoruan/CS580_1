import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ShortestPathPrinterMap extends Mapper<LongWritable, Text, Text, Text> {
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		SSSPState state = SSSPState.parse(value.toString());
		
		if (state.canSwap()) {
			context.getCounter(Session.IterationState.numberOfDijkstraMap).increment(1L);
			state.swap();
		} 
		
		context.write(new Text(state.node), new Text(state.toString()));
	}
}