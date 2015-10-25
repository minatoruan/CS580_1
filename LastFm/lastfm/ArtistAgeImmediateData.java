package lastfm;

public class ArtistAgeImmediateData {
	
	private static String _splitPattern = "\\t";
	
	public static ArtistAgeImmediateData parse(PlayInfoAgeImmediateData piai) {
		ArtistAgeImmediateData pi = new ArtistAgeImmediateData();
		
		pi.artistname = piai.artistname;
		pi.sumOfAge = piai.age;
		pi.numberOfAge = pi.sumOfAge < 0 ? 0 : 1; //age is not defined, then do not count

		return pi;
	}
	
	public static boolean isArtistAgeImmediateData(String dataline) {
		return !dataline.isEmpty() && dataline.split(_splitPattern).length == 4 && dataline.split(_splitPattern)[3] == "0";
	}	
	
	public static ArtistAgeImmediateData parse(String dataline) {
		String[] splits = dataline.split(_splitPattern);
		if(splits.length != 4) {
			throw new ArrayIndexOutOfBoundsException("Data ArtistAgeImmediateData format ?" + dataline);
		}
		
		ArtistAgeImmediateData pi = new ArtistAgeImmediateData();
		pi.artistname = splits[0].trim();
		pi.sumOfAge = Integer.parseInt(splits[1].trim());
		pi.numberOfAge = Integer.parseInt(splits[2].trim());
		return pi;
	}		
	
	public static ArtistAgeImmediateData parse(String artistname, String dataline) {
		String[] splits = dataline.split(_splitPattern);
		if(splits.length != 3) {
			throw new ArrayIndexOutOfBoundsException("Data ArtistAgeImmediateData format ?" + dataline);
		}
		ArtistAgeImmediateData pi = new ArtistAgeImmediateData();
		pi.artistname = artistname;
		pi.sumOfAge = Integer.parseInt(splits[0].trim());
		pi.numberOfAge = Integer.parseInt(splits[1].trim());
		
		return pi;
	}	
	
	public String artistname = "";
	public int sumOfAge = 0; 
	public int numberOfAge = 0;
	
	public void computeSum(ArtistAgeImmediateData aai) {
		if(!aai.isSetAge()) {//age is not defined, then do not count
			return;
		}
		sumOfAge += aai.sumOfAge;
		numberOfAge += aai.numberOfAge;
	}
	
	public boolean isSetAge() {
		return numberOfAge > 0;
	}
	
	public String toString() {
		return String.format("%d\t%d\t0", sumOfAge, numberOfAge);
	}
	
	public int getAgeAvg() {
		return sumOfAge / numberOfAge;
	}
}
