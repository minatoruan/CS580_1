public class SSSPData {
	
	public static boolean isSSSPData(String ssspData) {
		return ssspData.indexOf(":") > 0;
	}
	
	public static SSSPData parse(String ssspData) {
		SSSPData data = new SSSPData();
		data.fromNode = ssspData.split("\\:")[0];
		data.distance = Integer.parseInt(ssspData.split("\\:")[1]);
		return data;
	}
	
	public String fromNode;
	public int distance;
	
	public String toString() {
		return String.format("%s:%d", fromNode, distance);
	}
}