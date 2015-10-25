package lastfm;

public class PlayInfoAgeImmediateData {
	
	private static String _splitPattern = "\\t";
	
	public static boolean isPlayInfo(String playline) {
		return !playline.isEmpty() && playline.split(_splitPattern).length == 3;
	}
	
	public static PlayInfoAgeImmediateData parse(String dataline) {
		String[] splits = dataline.split(_splitPattern);
		if(splits.length != 3) {
			throw new ArrayIndexOutOfBoundsException("Data PlayInfoAgeImmediateData format ?" + dataline);
		}
		PlayInfoAgeImmediateData pi = new PlayInfoAgeImmediateData();
		pi.uid = splits[0].trim();
		pi.artistname = splits[1].trim();
		String str = splits[2].trim();
		pi.age = str.isEmpty() ? -2 : Integer.parseInt(str);
		
		return pi;
	}
	
	public static PlayInfoAgeImmediateData parse(String uid, String dataline) {
		String[] splits = dataline.split(_splitPattern);
		if(splits.length != 2) {
			throw new ArrayIndexOutOfBoundsException("Data PlayInfoAgeImmediateData format ?" + dataline);
		}
		PlayInfoAgeImmediateData pi = new PlayInfoAgeImmediateData();
		pi.uid = uid;
		pi.artistname = splits[0].trim();
		String str = splits[1].trim();
		pi.age = str.isEmpty() ? -2 : Integer.parseInt(str);

		return pi;
	}
	
	public static PlayInfoAgeImmediateData parse(PlayInfo playInfo) {
		PlayInfoAgeImmediateData pi = new PlayInfoAgeImmediateData();
		pi.uid = playInfo.uid;
		pi.artistname = playInfo.artistname;
		return pi;
	}
	
	public String uid = "";
	public String artistname = "";
	public int age = -2; // -2: not set; -1: NA; >0: age
	
	public boolean isSetAge() {
		return age != -2;
	}
	
	public String toString() {
		return String.format("%s\t%d", artistname, age);
	}
}
