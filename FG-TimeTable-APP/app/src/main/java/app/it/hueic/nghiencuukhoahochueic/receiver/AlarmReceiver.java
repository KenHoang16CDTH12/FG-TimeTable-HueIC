package app.it.hueic.nghiencuukhoahochueic.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.common.Common;
import app.it.hueic.nghiencuukhoahochueic.database.SQLiteController;
import app.it.hueic.nghiencuukhoahochueic.model.Para;
import app.it.hueic.nghiencuukhoahochueic.util.Config;
import app.it.hueic.nghiencuukhoahochueic.util.DayConfig;
import app.it.hueic.nghiencuukhoahochueic.view.InforOfDateActivity;

/**
 * Created by KenHoang on 28-Dec-17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    SQLiteController sqLiteController;
    Calendar calendar;
    int year = 2017;
    int month = 1;
    int date = 1;
    String weekDay = "";
    List<Para> listPara;
    String title = "";
    String msg = "";

    public static final String ANDROID_CHANNEL_ID = "app.it.hueic.nghiencuukhoahochueic.receiver.ANDROID";
    @Override
    public void onReceive(Context context, Intent intent) {
        //Database
        getDatabaseByCalendar();
        //Check
        checkListPara();
        //
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, InforOfDateActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putInt(Config.PARAM_YEAR, year);
        bundle.putInt(Config.PARAM_MONTH, month);
        bundle.putInt(Config.PARAM_DAY, date);
        notificationIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(Common.VIBRATIONTIME);
        notificationManager.notify(0, builder.build());
        Common.TITLE = title;
        Common.CONTENT = msg;
        //


    }

    /**
     * Check List
     */
    private void checkListPara() {
        title = date + "/" + (month + 1) + "/" + year;
        String  dateMonth = date + "/" + (month + 1);
        String dateMonthYear = date + "/" + (month + 1) + "/" + year;
        String festivalDay = getFestivalDate(dateMonth);
        String festivalDateLunar = getFestivalDayLunar(dateMonthYear);
        System.out.println(festivalDay + " && " + festivalDateLunar);
        if (festivalDay.equals("")) {
            if (festivalDateLunar.equals("")) {
                msg = getMessageInfo();
            } else {
                msg = festivalDateLunar;
            }
        } else {
            if (dateMonth.equals(DayConfig.VALENTINE_DAY) ||
                    dateMonth.equals(DayConfig.CHRISTMAS_DAY)||
                    dateMonth.equals(DayConfig.INTERNATIONAL_WORKER_S_DAY) ||
                    dateMonth.equals(DayConfig.CHILDREN_S_DAY) ||
                    dateMonth.equals(DayConfig.WOMEN_DAY)
                    ) {
                msg = festivalDay + "\n" +
                        getMessageInfo();

            } else {
                msg = festivalDay;
            }
        }
    }

    private String getMessageInfo() {
        String msgInfor = "";
        if (!listPara.isEmpty()) {
            int tongTietDay = 0;
            msgInfor = "Xin chào! Lịch học hôm nay của bạn là: \n";
            for (Para para: listPara) {
                int tietbatdau = para.tietbatdau;
                int tietketthuc = para.tietkethuc;
                String tenmonhoc = para.tenmonhoc;
                String phonghoc = para.phonghoc;
                String tengiaovien = para.tengiaovien;
                int dayPara = para.day;
                int monthPara = para.month;
                int yearPara = para.year;
                int tongTiet = para.tongTiet();

                String tietBatDauVaTietKetThuc = tietbatdau + " - " + tietketthuc + ": ";
                msgInfor += "Tiết " + tietBatDauVaTietKetThuc
                        + tenmonhoc
                        + ".\nPhòng: " + tengiaovien + ".\n";
                tongTietDay += tongTiet;
            }
            msgInfor += "Tổng số tiết: " + tongTietDay;
            msgInfor += "\nChúc bạn một ngày may mắn!";
        } else {
            if (!weekDay.equals("Sunday")) {
                msgInfor = "Theo lịch học chính thức thì hôm nay bạn không có tiết học nào."
                        + "\nChúc bạn một ngày may mắn.";
            } else {
                msgInfor = "Hôm nay là chủ nhật." +
                        "\nChúc bạn cuối tuần vui vẻ!";
            }
        }
        return msgInfor;
    }
    /**
     *   Lunar Calendar
     *   date + "/" + month + "/" + year
     */
    private String getFestivalDayLunar(String festivalDay) {
        String result = "";
        switch (festivalDay) {
            case DayConfig.NEW_YEAR_EVE_S_DAY:
                result =  "Happy New Year!" + "\n"
                        + "Chiềng làng chiềng xã," +
                        " thượng hạ đông tây," +
                        " xa gần đó đây," +
                        " vểnh tai nghe chúc:" +
                        "Tân niên sung túc," +
                        " lắm phúc nhiều duyên, " +
                        "trong túi nhiều tiền," +
                        " tâm hồn vui sướng.";
                break;
            case DayConfig.NEW_YEAR_LUNAR_THE_FIRST:
                result = "Happy New Year!" + "\n" +
                        "2018, 8 thì quá phát, 1 năm may mắn, 0 gì cản trở, 2 mình tiến lên.";
                break;
            case DayConfig.NEW_YEAR_LUNAR_THE_SECOND:
                result = "Happy New Year!" + "\n" +
                        "An Khang Thịnh Vượng.";
                break;
            case DayConfig.NEW_YEAR_LUNAR_THE_THIRD:
                result = "Happy New Year!" + "\n" +
                        "Sức khỏe cường tráng.";
                break;
            default:
                result = "";
        }
        return result;
    }

    /**
     * Get data
     */
    private void getDatabaseByCalendar() {
        sqLiteController = new SQLiteController();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        weekDay = dayFormat.format(calendar.getTime());
        listPara = Common.paraList = sqLiteController.getParaByDate(year, month, date);
    }

    private String getFestivalDate(String festivalDay) {
        System.out.println("Festival Day " + festivalDay);
        String result = "";
        switch (festivalDay) {
            case DayConfig.HAPPYNEWYEAR_DAY:
                result = "Happy New Year!";
                break;
            case DayConfig.VALENTINE_DAY:
                result = "Happy Valentine Day!";
                break;
            case DayConfig.WOMEN_DAY:
                result = "Happy Women's Day!";
                break;
            case DayConfig.INTERNATIONAL_WORKER_S_DAY:
                result = "Happy International Worker's Day!";
                break;
            case DayConfig.CHILDREN_S_DAY:
                result = "Happy Children's Day!";
                break;
            case DayConfig.CHRISTMAS_DAY:
                result = "Merry Christmas!";
                break;
            default:
                result = "";
        }
        return result;
    }
}
