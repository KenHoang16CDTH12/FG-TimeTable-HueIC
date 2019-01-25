package app.it.hueic.nghiencuukhoahochueic.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.HashSet;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.model.CustomDate;
import app.it.hueic.nghiencuukhoahochueic.util.Config;
import app.it.hueic.nghiencuukhoahochueic.view.custom.CalendarView;

public class TimeTableActivity extends AppCompatActivity {
    private Button btnImport;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        //findViewById
        addControls();
        HashSet<CustomDate> events = new HashSet<>();
        Calendar calendar = Calendar.getInstance();
        events.add(new CustomDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)));

        CalendarView cv = findViewById(R.id.calendar_view);
        cv.updateCalendar(events);

        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(CustomDate date)
            {
                Intent intent = new Intent(TimeTableActivity.this, InforOfDateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Config.PARAM_YEAR, date.year);
                bundle.putInt(Config.PARAM_MONTH, date.month);
                bundle.putInt(Config.PARAM_DAY, date.day);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnImport = findViewById(R.id.btnImport);
        toolbar = findViewById(R.id.tbTimeTable);
        toolbar.setTitle(R.string.time_table);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeTableActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeTableActivity.this, ParseExcelActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TimeTableActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
