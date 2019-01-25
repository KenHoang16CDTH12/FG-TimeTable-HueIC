package app.it.hueic.nghiencuukhoahochueic.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.model.FileModel;

/**
 * Created by kenhoang on 11/01/2018.
 */

public class ListFileAdapter extends ArrayAdapter<FileModel> {
    Activity context;
    int resource;
    List<FileModel> list;
    public ListFileAdapter(@NonNull Activity context, int resource, List<FileModel> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_folder, null);
        }
        TextView tvTitle = v.findViewById(R.id.tvTitle);
        TextView tvSubTitle = v.findViewById(R.id.tvSubTitle);
        ImageView img = v.findViewById(R.id.imgFolder);

        FileModel model = this.list.get(position);
        img.setImageResource(model.getImg());
        tvTitle.setText(model.getTitle());
        tvSubTitle.setText(model.getSubTitle());


        return v;
    }
}
