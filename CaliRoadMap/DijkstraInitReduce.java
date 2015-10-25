import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DijkstraInitReduce extends Reducer<Text, Text, Text, Text> {
	
	public void reduce(Text key, Iterable<Text> values,	Context context) throws IOException, InterruptedException {
		ArrayList<String> adjacents = new ArrayList<String>();
		
		for(Text t : values) {
			String str = t.toString();
			if(!str.isEmpty()) {
				adjacents.add(t.toString());
			}
		}
		
		SSSPState state = new SSSPState();
		state.node = key.toString();
		state.wasVisited = false;
		
		state.adjacents = new String[adjacents.size()];
		state.adjacents = adjacents.toArray(state.adjacents);
		
		String startNode = Session.getStartNode(context.getConfiguration());
		state.weight = startNode.equals(key.toString()) ? 0 : Integer.MAX_VALUE;
		state.fromNodes[0] = startNode.equals(key.toString()) ? startNode : state.fromNodes[0];
		
		context.write(key, new Text(state.toString()));
	}
}