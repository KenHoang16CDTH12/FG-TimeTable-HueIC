package app.it.hueic.nghiencuukhoahochueic.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;
import android.view.View;


import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.common.Common;
import app.it.hueic.nghiencuukhoahochueic.database.TimeTableDB;
import app.it.hueic.nghiencuukhoahochueic.model.TimeNotification;
import app.it.hueic.nghiencuukhoahochueic.receiver.AlarmReceiver;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class HomeActivity extends AppCompatActivity {
    NotificationBadge mBadge;
    GridLayout mainGrid;
    TimeTableDB TimeTableDB;
    List<TimeNotification> timeNotificationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TimeTableDB = new TimeTableDB(this);
        setStatusReceiver();//Receiver
        //FindViewById & Events
        getWidGetFunction();
    }

    /**
     * Get Widget function
     */
    private void getWidGetFunction() {
        mainGrid = findViewById(R.id.gridLayout);
        mBadge = findViewById(R.id.badge);
        //setCountBadge();//Set count badge
        //Set Events
        //setSingleEvents();
        setToggleEvent(mainGrid);
    }

    /**
     * Set number count badge
     */
    private void setCountBadge() {
        timeNotificationList = TimeTableDB.getListNotification();
        Common.NOTIFICATION_COUNT = timeNotificationList.size();
        mBadge.setNumber(timeNotificationList.size());
    }

    /**
     * set status receiver mode
     */
    private void setStatusReceiver() {
        int modeNotification = TimeTableDB.getSettingNotificationMode();
        int modeVibration = TimeTableDB.getSettingNotificationVibrateMode();
        if (modeNotification == 1 && modeVibration == 1) { //On On
            Common.VIBRATIONTIME = new long[]{1000, 1000, 1000, 1000, 1000};
            registerAlarm(); //Receiver
        } else if (modeNotification == 1 && modeVibration == 0){ //On Off
            Common.VIBRATIONTIME = new long[]{0L};
            registerAlarm(); //Receiver
        } else if (modeNotification == 0 && modeVibration == 1){ //Off On
            Common.VIBRATIONTIME = new long[]{1000, 1000, 1000, 1000, 1000};
        } else if (modeNotification == 0 && modeVibration == 0){ // Off Off
            Common.VIBRATIONTIME = new long[]{0L};
        }
    }

    /**
     * Register broadcast
     */
    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);


        Intent intent = new Intent(HomeActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


    }

    /**
     * handle Toogle events
     * @param mainGrid
     */
    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child Item
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            // You can see, all child is CardView, so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            /*        if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background Color
                        cardView.setCardBackgroundColor(Color.parseColor("#BBDEFB"));
                    } else {
                        //Change background Color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    }*/
                    switch (finalI) {
                        case 0:
                            Intent intentTimeTable = new Intent(HomeActivity.this, TimeTableActivity.class);
                            startActivity(intentTimeTable);
                            finish();
                            break;
                        case 1:
                            if (!Common.TITLE.equals("")) {
                                TimeNotification timeNotification = new TimeNotification(Common.TITLE, Common.CONTENT, Common.IS_NOTIFICAITON);
                                TimeTableDB.insertDataTimeNotification(timeNotification);
                            }
                            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            break;
                        case 2:
                            Intent intentPreferences = new Intent(HomeActivity.this, SettingActivity.class);
                            startActivity(intentPreferences);
                            break;
                        case 3:
                            Intent aboutIntent = new Intent(HomeActivity.this, AboutActivity.class);
                            startActivity(aboutIntent);
                            break;
                        case 5:
                            startActivity(new Intent(HomeActivity.this, TutorialsActivity.class));
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCountBadge();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
