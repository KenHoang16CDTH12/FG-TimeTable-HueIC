package app.it.hueic.nghiencuukhoahochueic.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.adapter.DateRecycleViewAdapter;
import app.it.hueic.nghiencuukhoahochueic.database.SQLiteController;
import app.it.hueic.nghiencuukhoahochueic.model.Para;
import app.it.hueic.nghiencuukhoahochueic.util.Config;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class InforOfDateActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView lstPara;
    DateRecycleViewAdapter adapter;
    private SQLiteController sqLiteController;
    private Toolbar toolbar;
    List<Para> paraList;

    //Horizontal Calendar material
    private HorizontalCalendar horizontalCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_of_date);
        Intent intent = getIntent();
        int year = intent.getIntExtra(Config.PARAM_YEAR, 0);
        int month = intent.getIntExtra(Config.PARAM_MONTH, 0);
        int day = intent.getIntExtra(Config.PARAM_DAY, 0);
        //findViewByID
        addControls(year, month, day);
    }

    private void addControls( int year, int month, int day) {
        //Setup toolbar
        toolbar = findViewById(R.id.tbListTimeTable);
        toolbar.setTitle(R.string.time_table);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //Setup Horizontal Calendar
        // Default Date set to Today.
        /* end after 1 months from now */
        Calendar endDate = Calendar.getInstance();
        endDate.set(year, month, day);
        endDate.add(Calendar.MONTH, 2);
        /* start 1 months ago from now */
        Calendar startDate = Calendar.getInstance();
        startDate.set(year, month, day);
        startDate.add(Calendar.MONTH, -2);
        final Calendar defaultSelectedDate = Calendar.getInstance();

        defaultSelectedDate.set(year, month, day);
        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                    .range(startDate, endDate)
                    .datesNumberOnScreen(5)
                    .configure()
                        .formatTopText("MMM")
                        .formatMiddleText("dd")
                        .formatBottomText("EEE")
                        .showTopText(true)
                        .showBottomText(true)
                        .textColor(Color.LTGRAY, Color.WHITE)
                        .colorTextMiddle(Color.LTGRAY, Color.parseColor("#ffd54f"))
                    .end()
                    .defaultSelectedDate(defaultSelectedDate)
                    .build();

        Log.i("Default Date", DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString());
        Log.i("Default Date", + day + "/" + (month + 1) + "/" + year);
        //   horizontalCalendar.goToday(false);
        /*
        tvNgayClick.setText(dateformat); */
        lstPara = findViewById(R.id.listPara);
        sqLiteController = new SQLiteController();
        paraList = sqLiteController.getParaByDate(year, month, day);
        adapter = new DateRecycleViewAdapter(paraList, this);
        layoutManager = new LinearLayoutManager(this);
        lstPara.setLayoutManager(layoutManager);
        lstPara.setAdapter(adapter);
        //  Events
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                String selectedDateStr = DateFormat.format("EEE, MMM d, yyyy", date).toString();
                paraList.clear();
                paraList = sqLiteController.getParaByDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
                adapter = new DateRecycleViewAdapter(paraList, InforOfDateActivity.this);
                adapter.notifyDataSetChanged();
                lstPara.setAdapter(adapter);
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
                super.onCalendarScroll(calendarView, dx, dy);
            }
        });


    }

    private String formatDay(int day, int month, int year) {
        String dayStr = "";
        String monthStr = "";

        if (day < 10)
            dayStr = "0" + day;
        else
            dayStr = String.valueOf(day);

        if (month < 9)
            monthStr = "0" + (month + 1);
        else
            monthStr = String.valueOf(month + 1);
        return String.format(" %s/%s/%d", dayStr, monthStr, year);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
