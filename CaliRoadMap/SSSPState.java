import java.util.ArrayList;

public class SSSPState {
	public static String NullString = "NA";
	
	public static SSSPState parse(String state) {
		String[] splits = state.split("\\|");	
		return parse(splits[0].trim(), state);
	}
	
	public static SSSPState parse(String node, String data) {
		String[] splits = data.split("\\|");
		
		SSSPState result = new SSSPState();
		result.node = node;
		result.wasVisited = splits[1].equals("1");
		result.adjacents = splits[2].split("\\s");
		if (result.adjacents[0].isEmpty()) {
			result.adjacents = new String[0];
		}		
		result.weight = Integer.parseInt(splits[3]);
		result.fromNodes = splits[4].split("\\s");
		return result;
	}
	
	public String node;
	public String[] adjacents;
	public int weight;
	public boolean wasVisited;
	public String[] fromNodes = new String[] { NullString };
	
	public String toString() {
		return String.format("|%s|%s|%d|%s",
										wasVisited ? "1" : "0",
										concat(adjacents), 
										weight,
										concat(fromNodes));
	}
	
	public String toStringWithoutTag() {
		return String.format(":[%s]|%d|[%s]",
										concat(adjacents), 
										weight == Integer.MAX_VALUE ? -1 : weight,
										concat(fromNodes));
	}
	
	public boolean canSwap() {
		return !fromNodes[0].equals(node) && !fromNodes[0].equals(NullString) && fromNodes.length < weight;
	}
	
	public void swap() {
		String firstFromNode = fromNodes[0];
		String _node = node;
		fromNodes[0] = _node;
		node = firstFromNode;
		wasVisited = !wasVisited;
	}

	private String concat(String[] adjacents) {
		StringBuilder sb = new StringBuilder();
		
		for(String t : adjacents) {
			sb.append(t.toString());
			sb.append(" ");
		}
		return sb.toString().trim();
	}

	public void addPathsFromStart(String[] fromNodes2) {
		ArrayList<String> newFromNodes = new ArrayList<String>();
		
		for(String node : fromNodes2) {
			newFromNodes.add(node);
		}
		
		for(String node : fromNodes) {
			newFromNodes.add(node);
		}
		fromNodes = new String[newFromNodes.size()];
		fromNodes = newFromNodes.toArray(fromNodes);
	}
	
	public void addPathsFromEnd(String end) {
		ArrayList<String> newFromNodes = new ArrayList<String>();
	
		for(String node : fromNodes) {
			newFromNodes.add(node);
		}
		newFromNodes.add(end);
		fromNodes = new String[newFromNodes.size()];
		fromNodes = newFromNodes.toArray(fromNodes);
	}	
}