package app.it.hueic.nghiencuukhoahochueic.common;

import java.util.HashSet;
import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.database.SQLiteController;
import app.it.hueic.nghiencuukhoahochueic.model.CustomDate;
import app.it.hueic.nghiencuukhoahochueic.model.Para;

/**
 * Created by kenhoang on 12/28/17.
 */

public class Common {
    public static List<Para> paraList;

    public static String VERSION_NAME = "";
    public static String VERSION_CODE = "1.0";

    public static final String EMAIL = "hoang.duongminh0221@gmail.com";

    //DEFAULT TIME
    public static int HOUR = 6;
    public static int MINUTE = 0;
    public static int SECOND = 0;
    //VIBRATION TIME
    public static long[] VIBRATIONTIME = new long[]{1000, 1000, 1000, 1000, 1000};
    //COUNT NOTIFICATION
    public static int NOTIFICATION_COUNT = 0;
    //NOTIFICATION INFO
    public static String TITLE = "";
    public static String CONTENT = "";
    public static final int IS_NOTE = 1;
    public static final int IS_NOTIFICAITON = 0;

    public static final int NO_EDIT = 0;
    public static final int OK_EDIT = 1;
    //REQUEST_PERMISSIONS
    public static final int REQUEST_PERMISSIONS = 20;
}
