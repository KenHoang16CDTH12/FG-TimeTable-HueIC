package app.it.hueic.nghiencuukhoahochueic.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import com.valdesekamdem.library.mdtoast.MDToast;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.common.Common;
import app.it.hueic.nghiencuukhoahochueic.database.TimeTableDB;
import app.it.hueic.nghiencuukhoahochueic.model.TimeNotification;

public class DetailNotificationActivity extends AppCompatActivity {
	EditText edTittle, edContent;
	BottomNavigationView bottomNavigationView;
	Toolbar toolbar;
	String titleToolbar = "";
	int logoToolbar = R.drawable.notepad;
	int status = Common.IS_NOTE;
	String tittle = "";
	String content = "";
	int isEdit = 0;
	TimeTableDB timeTableDB;
	int id = 0;
	boolean isOnclick = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_notification);
		timeTableDB = new TimeTableDB(this);
		getDataIntent();
		setUpToolbar();
		initView();//findViewById
		initEvents();
	}

	private void getDataIntent() {
		if (getIntent() != null) {
			Intent intent = getIntent();
			titleToolbar = intent.getStringExtra("TITLETOOLBAR");
			logoToolbar = intent.getIntExtra("TYPE", R.drawable.notepad);
			status = intent.getIntExtra("STATUS", Common.IS_NOTE);
			tittle = intent.getStringExtra("TITTLE");
			content = intent.getStringExtra("CONTENT");
			isEdit = intent.getIntExtra("STATE_EDIT", Common.NO_EDIT);
			id = intent.getIntExtra("ID", 0);
		}
	}

	/**
	 * Handle events
	 */
	private void initEvents() {
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_save:
						handleAddNewNote();
						break;
					case R.id.action_edit:
						handleEdit();
						break;
					case R.id.action_remove:
						handleRemove();
						break;
				}
				return true;
			}

		});
	}

	private void handleRemove() {
		if (id <= 0) {
			MDToast.makeText(getApplicationContext(), "Xóa không được! Vì dữ liệu này chưa tồn tại", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
		} else {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							timeTableDB.removeNoteNotification(id, getBaseContext());
						break;

						case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						break;
				}
			}
		} ;

		AlertDialog.Builder builder = new AlertDialog.Builder(DetailNotificationActivity.this);
		builder.setMessage("Bạn có chắc muốn xóa dữ liệu này?").setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}
	}

	private void handleEdit() {
		if (id <= 0) {
			MDToast.makeText(getApplicationContext(), "Cập nhật không được! Vì dữ liệu này chưa tồn tại", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
		} else {
			if (isOnclick == true) {
				edTittle.setEnabled(false);
				edContent.setEnabled(true);
				MDToast.makeText(getApplicationContext(), "Bắt đầu chỉnh sửa. Bấm chỉnh sửa thêm lần nữa để cập nhật!", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
			} else {
				edTittle.setEnabled(false);
				edContent.setEnabled(false);
				timeTableDB.updateNotificationContent(id, edContent.getText().toString().trim(), getBaseContext());
			}
			isOnclick = !isOnclick;
		}
	}

	/**
	 * Handle add a new  note
	 */
	private void handleAddNewNote() {
		String title = edTittle.getText().toString().trim();
		String content = edContent.getText().toString().trim();
		if (title.isEmpty()) {
			MDToast.makeText(getBaseContext(), "Vui lòng nhập tiêu đề!", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
		} else {
			TimeNotification timeNotification = new TimeNotification(title, content, Common.IS_NOTE);
			timeTableDB.insertNoteData(timeNotification, getBaseContext());
		}
	}

	private void initView() {
		edTittle = (EditText) findViewById(R.id.edTittle);
		edContent = (EditText) findViewById(R.id.edContent);
		bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigtaion);

	}

	private void setUpToolbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(titleToolbar);
		toolbar.setTitleTextColor(Color.WHITE);
		toolbar.setLogo(logoToolbar);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailNotificationActivity.this, NotificationActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isEdit == Common.OK_EDIT) {
			edTittle.setEnabled(true);
			edContent.setEnabled(true);

		} else {
			edTittle.setEnabled(false);
			edContent.setEnabled(false);
			edTittle.setText(tittle);
			edContent.setText(content);
		}
	}
}
