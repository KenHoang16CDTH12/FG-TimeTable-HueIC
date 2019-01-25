package app.it.hueic.nghiencuukhoahochueic.util;

import org.apache.poi.hssf.util.CellReference;

/**
 * Created by kenhoang on 5/12/17.
 */

public class Config {
    public static final String TAG = "ParseExcelActivity";
    //POSITION
    public static int POSITION = 0;
    //PARAM
    public static final String PARAM_YEAR = "YEAR";
    public static final String PARAM_MONTH = "MONTH";
    public static final String PARAM_DAY = "DAY";
    //DAY_OF_WEEK
    public static final String T2 = "T2";
    public static final String T3 = "T3";
    public static final String T4 = "T4";
    public static final String T5 = "T5";
    public static final String T6 = "T6";
    public static final String T7 = "T7";
    //
    public static final int BEGIN_ROW = 6;
    public static final int END_COL = CellReference.convertColStringToIndex("AC");

    //DEFAULT TIME
    public static int HOUR = 6;
    public static int MINUTE = 0;
    public static int SECOND = 0;
    //TURN ON/OFF NOTIFICATION
    public static int NOTIFICATION_STATUS = 1; //0 = off && 1 == on

}
