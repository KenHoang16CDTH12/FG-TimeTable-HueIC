package app.it.hueic.nghiencuukhoahochueic.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.valdesekamdem.library.mdtoast.MDToast;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.common.Common;
import app.it.hueic.nghiencuukhoahochueic.database.TimeTableDB;
import app.it.hueic.nghiencuukhoahochueic.view.custom.AppCompatPreferenceActivity;

public class SettingActivity extends AppCompatPreferenceActivity {
	private static final String TAG = SettingActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// load settings fragment
		getVersionInfo();
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();

	}


	public static class MainPreferenceFragment extends PreferenceFragment {

		Preference vibrate_key, notification_key, feedback_key, version_key;
		TimeTableDB TimeTableDB;
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_main);
			TimeTableDB = new TimeTableDB(getActivity());
			initKey();

			initEvents();

		}

		private void initEvents() {
			//notification preference change listener
			notification_key.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					boolean isNotification = (boolean) newValue;
					if (isNotification) {
						TimeTableDB.saveSettingMode(1);
						vibrate_key.setEnabled(true);
					} else {
						TimeTableDB.saveSettingMode(0);
						vibrate_key.setEnabled(false);
					}
					return true;
				}
			});
			//vibrate preference change listener
			vibrate_key.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					boolean isVibration = (boolean) newValue;
					if (isVibration) {
						TimeTableDB.saveSettingVibrateMode(1, TimeTableDB.getSettingNotificationMode());
					} else {
						TimeTableDB.saveSettingVibrateMode(0, TimeTableDB.getSettingNotificationMode());
					}
					return true;
				}
			});
			// version preference click listener
			version_key.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					MDToast.makeText(getActivity(),"Phiên bản hiện tại là " + version_key.getSummary() + ".", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO ).show();
					return true;
				}
			});
			// feedback preference click listener
			feedback_key.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					sendFeedback(getActivity());
					return true;
				}
			});
		}

		private void initKey() {
			notification_key = findPreference(getString(R.string.notification_key));
			vibrate_key = findPreference(getString(R.string.key_vibrate));
			version_key = findPreference(getString(R.string.version_key));
			feedback_key = findPreference(getString(R.string.key_send_feedback));
			int modeNotification = TimeTableDB.getSettingNotificationMode();
			int modeVibration = TimeTableDB.getSettingNotificationVibrateMode();
			setSwitchMode(modeNotification, modeVibration);
			version_key.setSummary(Common.VERSION_CODE);
		}

		private void setSwitchMode(int modeNotification, int modeVibration) {
			if (modeNotification == 1 && modeVibration == 1) // On On
			{
				notification_key.setDefaultValue(true);
				vibrate_key.setDefaultValue(true);
			} else if (modeNotification == 1 && modeVibration == 0) // On Off
			{
				notification_key.setDefaultValue(true);
				vibrate_key.setDefaultValue(false);
			} else if (modeNotification == 0 && modeVibration == 1) // Off On
			{
				notification_key.setDefaultValue(false);
				vibrate_key.setEnabled(false);
				vibrate_key.setDefaultValue(true);
			} else if (modeNotification == 0 && modeVibration == 0) // Off Off
			{
				notification_key.setDefaultValue(false);
				vibrate_key.setEnabled(false);
				vibrate_key.setDefaultValue(false);
			}
		}


	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Get version
	 */
	//get the current version number and name
	private void getVersionInfo() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			Common.VERSION_NAME = packageInfo.versionName;
			Common.VERSION_CODE = String.valueOf(packageInfo.versionCode) + ".0";
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Email client intent to send support mail
	 * Appends the necessary device information to email body
	 * useful when providing support
	 */
	public static void sendFeedback(Context context) {
		String body = null;
		try {
			body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
					Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
					"\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
		} catch (PackageManager.NameNotFoundException e) {
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		//intent.setType("text/email");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Common.EMAIL});
		intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
		intent.putExtra(Intent.EXTRA_TEXT, body);
		context.startActivity(Intent.createChooser(intent, "Send Feedback:"));
	}
}
