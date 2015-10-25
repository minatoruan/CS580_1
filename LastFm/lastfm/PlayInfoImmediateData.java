package lastfm;

public class PlayInfoImmediateData {
	public static boolean isPlayInfo(String playline) {
		return !playline.isEmpty() && playline.split("\\t").length == 2;
	}
	
	public static PlayInfoImmediateData parse(String playline) {
		String[] splits = playline.split("\\t");
		if(splits.length != 2) {
			throw new ArrayIndexOutOfBoundsException("Data PlayInfoImmediateData format ?" + playline);
		}
		PlayInfoImmediateData pi = new PlayInfoImmediateData();
		pi.artistname = splits[0].trim();
		pi.numPlays = Integer.parseInt(splits[1].trim());
		return pi;
	}
	
	public String artistname = "";
	public int numPlays = -1;
}