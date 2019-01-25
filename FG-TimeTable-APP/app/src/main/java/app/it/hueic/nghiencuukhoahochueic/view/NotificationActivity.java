package app.it.hueic.nghiencuukhoahochueic.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.adapter.NotificationRecyleViewAdapter;
import app.it.hueic.nghiencuukhoahochueic.common.Common;
import app.it.hueic.nghiencuukhoahochueic.database.TimeTableDB;
import app.it.hueic.nghiencuukhoahochueic.model.TimeNotification;

public class NotificationActivity extends AppCompatActivity {
	List<TimeNotification> notificationList = new ArrayList<>();
	RecyclerView.LayoutManager layoutManager;
	RecyclerView recyclerView;
	NotificationRecyleViewAdapter adapter;
	TimeTableDB timeTableDB;
	BottomNavigationView bottomNavigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		setupToolbar();
		timeTableDB = new TimeTableDB(this);
		initData();
		initView();
		initEvents();
	}

	private void initEvents() {
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_add:
						handleAddNote();
						break;
					case R.id.action_clear:
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which){
									case DialogInterface.BUTTON_POSITIVE:
										//Yes button clicked
										timeTableDB.clearAllNotification(getApplicationContext());
										//Update
										updateDataRecycleview();
										//Toast
										MDToast.makeText(NotificationActivity.this, "Xóa tất cả thành công!", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
										break;

									case DialogInterface.BUTTON_NEGATIVE:
										//No button clicked
										break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
						builder.setMessage("Bạn có chắc chắn muốn xóa hết thông báo ?").setPositiveButton("Yes", dialogClickListener)
								.setNegativeButton("No", dialogClickListener).show();
						break;
				}
				return true;
			}

		});
	}

	/**
	 * Handle add note
	 */
	private void handleAddNote() {
		Intent intent = new Intent(NotificationActivity.this, DetailNotificationActivity.class);
		String tittle = " GHI CHÚ";
		int type = R.drawable.notepad;
		intent.putExtra("TITLETOOLBAR", tittle);
		intent.putExtra("TYPE", type);
		intent.putExtra("TITTLE", "");
		intent.putExtra("CONTENT", "");
		intent.putExtra("STATE_EDIT", Common.OK_EDIT);
		intent.putExtra("ID", 0);
		startActivity(intent);
		finish();
	}

	/**
	 * Update
	 */
	private void updateDataRecycleview() {
		notificationList.clear();
		notificationList = timeTableDB.getListNotification();
		adapter.notifyDataSetChanged();
		recyclerView.setAdapter(adapter);
	}
	private void initView() {
		recyclerView = (RecyclerView) findViewById(R.id.listNotification);
		bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigtaion);
		adapter = new NotificationRecyleViewAdapter(notificationList, getBaseContext());
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);
	}

	private void initData() {
		notificationList = timeTableDB.getListNotification();
	}

	private void setupToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.pref_title_notifications);
		toolbar.setTitleTextColor(Color.WHITE);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!Common.TITLE.equals("")) {
			TimeNotification timeNotification = new TimeNotification(Common.TITLE, Common.CONTENT, Common.IS_NOTIFICAITON);
			timeTableDB.insertDataTimeNotification(timeNotification);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}


}
