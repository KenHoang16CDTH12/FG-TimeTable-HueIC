package app.it.hueic.nghiencuukhoahochueic.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.model.TimeNotification;
import app.it.hueic.nghiencuukhoahochueic.view.NotificationActivity;

/**
 * Created by kenhoang on 1/1/18.
 */

public class TimeTableDB extends SQLiteAssetHelper {
	private static final String DB_NAME = "TimeTable.db";
	private static final int DB_VERSION = 1;
	public TimeTableDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public int getSettingNotificationMode() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = {"Mode"};
		String sqlTable = "SettingNoTification";
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, sqlSelect,null, null, null, null, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("Mode"));
	}

	public int getSettingNotificationVibrateMode() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = {"Vibrate"};
		String sqlTable = "SettingNoTification";
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, sqlSelect,null, null, null, null, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("Vibrate"));
	}

	public void saveSettingMode(int value) 	{
		SQLiteDatabase db = getReadableDatabase();
		String query = "UPDATE SettingNoTification SET Mode = " + value;
		db.execSQL(query);
	}

	public void saveSettingVibrateMode(int value, int id) 	{
		SQLiteDatabase db = getReadableDatabase();
		String query = "UPDATE SettingNoTification SET Vibrate = " + value + " WHERE Mode = " + id;
		db.execSQL(query);
	}

	/**
	 * INSERT INTO TimeNotification (TITLE, CONTENT, STATUS)
	 * VALUES ("22/02/2017", "Hom nay nghi hoc!", 0);
	 * Notification
	 */

	public void insertDataTimeNotification(TimeNotification timeNotification) {
		SQLiteDatabase db = getReadableDatabase();
		String query = String.format("INSERT INTO TimeNotification (TITLE, CONTENT, STATUS)" +
		" VALUES ('%s', '%s', '%d');", timeNotification.getTITLE(), timeNotification.getCONTENT(), timeNotification.getSTATUS());
		try {
			db.execSQL(query);
		} catch (Exception ex) {
			Log.d("DEBUG", "ERROR SAVE DATA TIME NOTIFICATION");
		}
	}

	/**
	 * INSERT INTO TimeNotification (TITLE, CONTENT, STATUS)
	 * VALUES ("22/02/2017", "Hom nay nghi hoc!", 1);
	 * Notes
	 */

	public void insertNoteData(TimeNotification timeNotification, Context context) {
		SQLiteDatabase db = getReadableDatabase();
		String query = String.format("INSERT INTO TimeNotification (TITLE, CONTENT, STATUS)" +
				" VALUES ('%s', '%s', '%d');", timeNotification.getTITLE(), timeNotification.getCONTENT(), timeNotification.getSTATUS());
		try {
			db.execSQL(query);
			MDToast.makeText(context, "Đã thêm một ghi chú mới!", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
			Intent intent = new Intent(context, NotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(intent);
		} catch (Exception ex) {
			MDToast.makeText(context, "Thêm ghi chú thất bại! Tiêu đề này đang tồn tại!", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
			Log.d("DEBUG", "ERROR SAVE DATA TIME NOTIFICATION");
		}
	}

	/**
	 * SELECT ID, TITLE, CONTENT, STATUS
	 * FROM TimeNotification
	 */

	public List<TimeNotification> getListNotification() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = {"ID", "TITLE", "CONTENT", "STATUS"};
		String sqlTable = "TimeNotification";
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, sqlSelect,null, null, null, null, null);

		List<TimeNotification> result = new ArrayList<>();
		if (c.moveToFirst()) {
			do {
				TimeNotification timeNotification = new TimeNotification();
				timeNotification.setID(c.getInt(c.getColumnIndex("ID")));
				timeNotification.setTITLE(c.getString(c.getColumnIndex("TITLE")));
				timeNotification.setCONTENT(c.getString(c.getColumnIndex("CONTENT")));
				timeNotification.setSTATUS(c.getInt(c.getColumnIndex("STATUS")));
				result.add(timeNotification);
			} while (c.moveToNext());
		}
		return result;
	}

	/**
	 * DELETE FROM TimeNotification
	 */
	public void clearAllNotification(Context context) {
		SQLiteDatabase db = getReadableDatabase();
		String query = "DELETE FROM TimeNotification;";
		try {
			db.execSQL(query);
			MDToast.makeText(context, "Xóa tất cả thành công!", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
		} catch (Exception ex) {
			MDToast.makeText(context, "Danh sách trống!", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
			Log.d("DEBUG", "ERROR CLEAR ALL");
		}
	}

	/**
	 * DELETE FROM TimeNotification WHERE ID = ?
	 */

	public void removeNoteNotification(int value, Context context) {
		SQLiteDatabase db = getReadableDatabase();
		String query = "DELETE FROM TimeNotification WHERE ID = " + value + " ;";
		try {
			db.execSQL(query);
			MDToast.makeText(context, "Xóa thành công!", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
			Intent intent = new Intent(context, NotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(intent);
		} catch (Exception ex) {
			MDToast.makeText(context, "Xoá thất bại!", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
			Log.d("DEBUG", "REMOVE A");
		}
	}

	/**
	 * UPDATE TimeNotification SET CONTENT = new Value
	 * WHERE ID = ?
	 *
	 * UPDATE TimeNotification SET CONTENT = 555 WHERE ID = 1
	 */
	public void updateNotificationContent(int id, String content, Context context) {
		SQLiteDatabase db = getReadableDatabase();
		String query = "UPDATE TimeNotification SET CONTENT = '" + content + "' WHERE ID = " + id + " ;";
		try {
			db.execSQL(query);
			MDToast.makeText(context, "Cập nhật thành công!", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
		} catch (Exception ex) {
			MDToast.makeText(context, "Cập nhật thất bại!", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
			Log.d("DEBUG", "REMOVE A");
		}
	}
}
