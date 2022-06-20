package backend.util;

public class Tool {
	
	public static String getNumberFromString(String name) {
		return name.replaceAll("[^0-9]+", "");
	}

	public static String processTime(String time) {
		return time.substring(11, 16);
	}

}
