package lastfm;

public class PlayInfo {
	public static boolean isPlayInfo(String playline) {
		return !playline.isEmpty() && playline.split("\\t").length == 4;
	}
	
	public static PlayInfo parse(String playline) {	
		String[] splits = playline.split("\\t");
		if(splits.length != 4) {
			throw new ArrayIndexOutOfBoundsException("Data PlayInfo format ?" + playline);
		}
		PlayInfo pi = new PlayInfo();
		pi.uid = splits[0].trim();
		pi.artistid = splits[1].trim();
		pi.artistname = splits[2].trim();
		pi.numPlays = Integer.parseInt(splits[3].trim());
		return pi;
	}
	
	public static boolean IsPlayInfo(String playline) {
		String[] splits = playline.split("\\t");
		return splits.length == 4;
	}
	
	public String uid = "";
	public String artistid = "";
	public String artistname = "";
	public int numPlays = -1;
}