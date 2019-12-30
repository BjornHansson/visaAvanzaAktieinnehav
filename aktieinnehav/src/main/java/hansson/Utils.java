package hansson;

public class Utils {
    public static double convertToDouble(String str) {
        if (str.contains("%")) {
            String subStr = str.substring(0, str.length() - 2);
            return Double.parseDouble(subStr.replace(',', '.'));
        }
        return Double.parseDouble(str.replace(',', '.'));
    }
}