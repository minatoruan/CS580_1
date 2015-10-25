import org.apache.hadoop.conf.Configuration;

public class Session {
	
	public static enum IterationState {
		numberOfDijkstraMap
	}
	
	private static String startNode = "startNode";
	
	public static void setStartNode(Configuration conf, String value) {
		conf.set(startNode, value);
	}
	
	public static String getStartNode(Configuration conf) {
		return conf.get(startNode);
	}
}