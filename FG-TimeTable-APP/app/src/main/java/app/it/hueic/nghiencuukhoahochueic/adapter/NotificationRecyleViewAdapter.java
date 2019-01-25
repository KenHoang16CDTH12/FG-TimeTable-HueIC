package app.it.hueic.nghiencuukhoahochueic.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.common.Common;
import app.it.hueic.nghiencuukhoahochueic.database.SQLiteController;
import app.it.hueic.nghiencuukhoahochueic.interf.ItemClickListener;
import app.it.hueic.nghiencuukhoahochueic.model.Para;
import app.it.hueic.nghiencuukhoahochueic.model.TimeNotification;
import app.it.hueic.nghiencuukhoahochueic.util.Config;
import app.it.hueic.nghiencuukhoahochueic.view.DetailNotificationActivity;
import app.it.hueic.nghiencuukhoahochueic.view.InforOfDateActivity;

/**
 * Created by kenhoang on 1/2/18.
 */

class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
	public ImageView img_status;
	public TextView title, content;
	private ItemClickListener itemClickListener;

	public RecycleViewHolder(View itemView) {
		super(itemView);
		img_status = (ImageView) itemView.findViewById(R.id.img_status);
		title = (TextView) itemView.findViewById(R.id.txt_title);
		content = (TextView) itemView.findViewById(R.id.txt_content);
		itemView.setOnClickListener(this);
		itemView.setOnLongClickListener(this);
	}

	public void setItemClickListener(ItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}


	@Override
	public void onClick(View view) {
		itemClickListener.onClick(view, getAdapterPosition());
	}

	@Override
	public boolean onLongClick(View v) {
		itemClickListener.onLongClick(v, getAdapterPosition());
		return true;
	}
}

public class NotificationRecyleViewAdapter extends RecyclerView.Adapter<RecycleViewHolder>{
	SQLiteController sqLiteController;
	private List<Para> paraList;
	private List<TimeNotification> notificationList;
	private Context context;

	public NotificationRecyleViewAdapter(List<TimeNotification> notificationList, Context context) {
		this.notificationList = notificationList;
		this.context = context;
	}

	@Override
	public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.item_notification_data, parent, false);

		return new RecycleViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecycleViewHolder holder, int position) {
		sqLiteController = new SQLiteController();
		final String title = notificationList.get(position).getTITLE();
		final String content = notificationList.get(position).getCONTENT();
		holder.title.setText(title);
		holder.content.setText(content);
		if (notificationList.get(position).getSTATUS() == Common.IS_NOTIFICAITON) {
				holder.img_status.setImageResource(R.drawable.calendar_star);
		} else {
			holder.img_status.setImageResource(R.drawable.notepad);
		}
		holder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position) {
				//Call to new Activity
				String tittleToolbar = "";
				int type = 0;
				Intent intent = new Intent(context, DetailNotificationActivity.class);
				if (notificationList.get(position).getSTATUS() == Common.IS_NOTIFICAITON) {
					tittleToolbar = " THÔNG BÁO";
					type = R.drawable.smartphone;
					intent.putExtra("TITLETOOLBAR", tittleToolbar);
					intent.putExtra("TYPE", type);
					intent.putExtra("STATUS", Common.IS_NOTIFICAITON);
					intent.putExtra("TITTLE", title);
					intent.putExtra("CONTENT", content);
					intent.putExtra("STATE_EDIT", Common.NO_EDIT);
					intent.putExtra("ID", notificationList.get(position).getID());
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				} else {
					tittleToolbar = " GHI CHÚ";
					type = R.drawable.notepad;
					intent.putExtra("TITLETOOLBAR", tittleToolbar);
					intent.putExtra("TYPE", type);
					intent.putExtra("STATUS", Common.IS_NOTE);
					intent.putExtra("TITTLE", title);
					intent.putExtra("CONTENT", content);
					intent.putExtra("STATE_EDIT", Common.NO_EDIT);
					intent.putExtra("ID", notificationList.get(position).getID());
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}

			}

			@Override
			public void onLongClick(View view, int position) {

				if (notificationList.get(position).getSTATUS() == Common.IS_NOTIFICAITON) {
					Intent intentPara = new Intent(context, InforOfDateActivity.class);
					String[] dateTittle = notificationList.get(position).getTITLE().split("/");
					Bundle bundle = new Bundle();
					bundle.putInt(Config.PARAM_YEAR, Integer.parseInt(dateTittle[2]));
					bundle.putInt(Config.PARAM_MONTH, Integer.parseInt(dateTittle[1]) - 1);
					bundle.putInt(Config.PARAM_DAY, Integer.parseInt(dateTittle[0]));
					intentPara.putExtras(bundle);
					context.startActivity(intentPara);
				} else {
					Intent intent = new Intent(context, DetailNotificationActivity.class);
					String tittleToolbar = " GHI CHÚ";
					int type = R.drawable.notepad;
					intent.putExtra("TITLETOOLBAR", tittleToolbar);
					intent.putExtra("TYPE", type);
					intent.putExtra("STATUS", Common.IS_NOTE);
					intent.putExtra("TITTLE", title);
					intent.putExtra("CONTENT", content);
					intent.putExtra("STATE_EDIT", Common.NO_EDIT);
					intent.putExtra("ID", notificationList.get(position).getID());
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}

			}
		});
	}
	@Override
	public int getItemCount() {
		return notificationList.size();
	}
}
