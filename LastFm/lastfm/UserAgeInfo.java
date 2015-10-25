package lastfm;

public class UserAgeInfo {
	public static boolean isUserAgeInfo(String dataline) {
		return !dataline.isEmpty() && dataline.split("\\t").length == 5; 
	}
	
	public static UserAgeInfo parse(String dataline) {	
		String[] splits = dataline.split("\\t");
		if(splits.length != 5) {
			throw new ArrayIndexOutOfBoundsException("Data incorrected format ?" + dataline);
		}
		UserAgeInfo uai = new UserAgeInfo();
		uai.uid = splits[0];
		uai.age = splits[2].isEmpty() ? -1 : Integer.parseInt(splits[2].trim());
		return uai;		
	}
	
	public String uid = "";
	public int age = -1; 
}
