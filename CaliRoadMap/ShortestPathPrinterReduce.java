import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ShortestPathPrinterReduce extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values,	Context context) throws IOException, InterruptedException {	
		String node = key.toString();
		SSSPState main = null;
		ArrayList<SSSPState> minors = new ArrayList<SSSPState>();
		
		for(Text data : values) {
			SSSPState state = SSSPState.parse(node, data.toString());
			if (state.wasVisited) {	
				main = state;
				continue;
			}
			if (state.canSwap()) {
				state.swap();
			}
			minors.add(state);
		}
		
		if (main != null) {
			context.write(new Text(main.node), new Text(main.toString()));
			for(SSSPState state : minors) {
				state.addPathsFromStart(main.fromNodes);
			}
		}
		
		for(SSSPState state : minors) {
			context.write(new Text(state.node), new Text(state.toString()));
			System.out.println(String.format("%s(%s)", state.node, state.toString()));
		}
	}
}