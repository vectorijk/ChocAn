package Format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kai Jiang on 12/4/2014.
 */
public class Time {
    public static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        return df.format(date);
    }

    public static String getCurrentTimestamp() {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = new Date();
        return df.format(date);
    }

    public static String getCurrentTimestampSimple(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        return df.format(date);
    }
}
