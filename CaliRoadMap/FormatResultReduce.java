import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class FormatResultReduce extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values,	Context context) throws IOException, InterruptedException {
		SSSPState state = SSSPState.parse(key.toString(), values.iterator().next().toString());
		context.write(new Text(state.node), new Text(state.toStringWithoutTag()));
	}
}