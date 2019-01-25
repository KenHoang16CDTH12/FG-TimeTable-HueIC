package app.it.hueic.nghiencuukhoahochueic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.database.SQLiteController;
import app.it.hueic.nghiencuukhoahochueic.model.CustomDate;
import app.it.hueic.nghiencuukhoahochueic.model.Para;

/**
 * Created by kenhoang on 5/20/17.
 */

public class CalendarAdapter extends ArrayAdapter<CustomDate> {
    // days with events
    private HashSet<CustomDate> eventDays;

    // for view inflation
    private LayoutInflater inflater;

    private SQLiteController sqLiteController;

    public static List<Para> paraList;

    private Calendar monthToView;

    public CalendarAdapter(Context context, Calendar monthToView, ArrayList<CustomDate> days, HashSet<CustomDate> eventDays)
    {
        super(context, R.layout.control_calendar_day, days);
        this.monthToView = monthToView;
        this.eventDays = eventDays;
        // this.paraList = paraList;
        inflater = LayoutInflater.from(context);
        sqLiteController = new SQLiteController();

        if(days.size() > 0) {
            CustomDate customDate = days.get(0);
            paraList = sqLiteController.getParaByMonth(customDate.year, customDate.month);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        // day in question
        CustomDate date = getItem(position);
        Calendar today = Calendar.getInstance();

        // inflate item if it does not exist yet
        if (view == null)
            view = inflater.inflate(R.layout.control_calendar_day, parent, false);

        // if this day has an event, specify event image
        view.setBackgroundResource(0);
        if (eventDays != null)
        {
            for (CustomDate eventDate : eventDays)
            {
                if (eventDate.day == monthToView.get(Calendar.DATE) &&
                        eventDate.month == monthToView.get(Calendar.MONTH) &&
                        eventDate.year == monthToView.get(Calendar.YEAR))
                {
                    // mark this day for event
                    //view.setBackgroundResource(R.drawable.reminder);
                    break;
                }
            }
        }

        // clear styling
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvTiet = (TextView) view.findViewById(R.id.tvTiet);
        tvDate.setTypeface(null, Typeface.NORMAL);
        tvDate.setTextColor(Color.BLACK);

        if (date.month != monthToView.get(Calendar.MONTH) ||
                date.year != monthToView.get(Calendar.YEAR) )
        {
            // if this day is outside current month, grey it out
            tvDate.setTextColor(ContextCompat.getColor(getContext(), R.color.greyed_out));
            tvDate.setOnClickListener(null);
        }
        else
        {
            // if it is today, set it to blue/bold
            tvDate.setTypeface(null, Typeface.BOLD);
        }
        if (date.day  == today.get(Calendar.DATE) && date.month  == today.get(Calendar.MONTH)
                && date.year  == today.get(Calendar.YEAR)){
            tvDate.setTextColor(ContextCompat.getColor(getContext(), R.color.today));
        }

        // set text
        tvDate.setText(String.valueOf(date.day));
        int tongSoTiet = getTongSotiet(date.year, date.month, date.day);
        if (date.month != monthToView.get(Calendar.MONTH) ||
                date.year != monthToView.get(Calendar.YEAR))
        {
            // if this day is outside current month, grey it out
            tvTiet.setVisibility(View.GONE);
        } else {
            if(tongSoTiet > 0){
                tvTiet.setText(tongSoTiet + "");
                tvTiet.setVisibility(View.VISIBLE);
            } else {
                tvTiet.setVisibility(View.GONE);
            }
        }

        return view;
    }

    private int getTongSotiet(int year, int month, int day){
        int tongso = 0;
        List<Para> paraList = sqLiteController.getParaByDate(year, month, day);
        for (Para para : paraList){
            tongso += para.tongTiet();
        }
        return tongso;
    }
}
