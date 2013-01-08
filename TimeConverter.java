

public class TimeConverter {

	public static String convertToString(long millis) {
		int shift = 8;
		if(millis >= 3000)
			shift = -4;
		long hours = ((millis / 10) / 60) + shift;
		long minutes = (millis / 10) % 60;
		
		String pad = "";
		if(minutes < 10)
			pad += "0";
		
		return hours + ":" + pad + minutes;
	}
	
	public static long convertToLong(long hour, long minute) {
		int shift = -4800;
		if(hour <= 5)
			shift = 2400;
		long hours = (hour * 10) * 60;
		long minutes = (minute * 10);
		
		return hours + minutes + shift;
	}
	
	public static void main(String args[]) {
		System.out.println(convertToLong(8, 0));
		System.out.println(convertToLong(9, 0));
		System.out.println(convertToLong(10, 0));
		System.out.println(convertToLong(11, 0));
		System.out.println(convertToLong(12, 0));
		System.out.println(convertToLong(1, 0));
		System.out.println(convertToLong(2, 0));
		System.out.println(convertToLong(3, 0));
		System.out.println(convertToLong(4, 0));
		System.out.println(convertToLong(5, 0));
		System.out.println(convertToString(0));
		System.out.println(convertToString(600));
		System.out.println(convertToString(1200));
		System.out.println(convertToString(1800));
		System.out.println(convertToString(2400));
		System.out.println(convertToString(3000));
		System.out.println(convertToString(3600));
		System.out.println(convertToString(4200));
		System.out.println(convertToString(4800));
		System.out.println(convertToString(5400));
	}
	
}
