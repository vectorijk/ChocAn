package Format;

/**
 * Created by Kai Jiang on 12/4/2014.
 */
public class formatNumber {
    //convert integer(id) to 9-digits String
    public static String ID(int id) {
        String orginal = String.valueOf(id);
        String res = "9";
        if (orginal.length() < 9) {
            for (int i = 0; i < 8 - orginal.length(); i++) {
                res += "0";
            }
            res += orginal;
            return res;
        } else {
            return orginal;
        }
    }

    //convert service code(integer) to 6-digits
    public static String serviceCode(int sc) {
        String orginal = String.valueOf(sc);
        String res = "6";
        if (orginal.length() < 6) {
            for (int i = 0; i < 5 - orginal.length(); i++) {
                res += "0";
            }
            res += orginal;
            return res;
        } else {
            return orginal;
        }
    }
}
