package app.it.hueic.nghiencuukhoahochueic.view;

import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

import app.it.hueic.nghiencuukhoahochueic.R;

/**
 * Created by kenhoang on 16/01/2018.
 */

public class TutorialsActivity extends TutorialActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder().setTitle("Thời Khóa Biểu")
                .setContent("Đây là thời khóa biểu")
                .setBackgroundColor(R.color.colorPrimary) // int background color
                .setDrawable(R.drawable.ic_event_black_24dp) // int top drawable
                .setSummary("Chức năng")
                .build());

        addFragment(new Step.Builder().setTitle("Thời Khóa Biểu")
                .setContent("Đây là thời khóa biểu")
                .setBackgroundColor(R.color.colorPrimary) // int background color
                .setDrawable(R.drawable.ic_textsms_black_24dp) // int top drawable
                .setSummary("Chức năng")
                .build());

        addFragment(new Step.Builder().setTitle("Thời Khóa Biểu")
                .setContent("Đây là thời khóa biểu")
                .setBackgroundColor(R.color.colorPrimary) // int background color
                .setDrawable(R.drawable.ic_settings_black_24dp) // int top drawable
                .setSummary("Chức năng")
                .build());

        addFragment(new Step.Builder().setTitle("Thời Khóa Biểu")
                .setContent("Đây là thời khóa biểu")
                .setBackgroundColor(R.color.colorPrimary) // int background color
                .setDrawable(R.drawable.ic_account_circle_black_24dp) // int top drawable
                .setSummary("Chức năng")
                .build());

        addFragment(new Step.Builder().setTitle("Thời Khóa Biểu")
                .setContent("Đây là thời khóa biểu")
                .setBackgroundColor(R.color.colorPrimary) // int background color
                .setDrawable(R.drawable.ic_help_outline_black_24dp) // int top drawable
                .setSummary("Chức năng")
                .build());
    }
}
