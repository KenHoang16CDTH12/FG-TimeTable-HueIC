package app.it.hueic.nghiencuukhoahochueic.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.interf.ItemClickListener;
import app.it.hueic.nghiencuukhoahochueic.model.FileModel;

/**
 * Created by kenhoang on 11/01/2018.
 */
class FileRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    ImageView img;
    TextView tvTitle, tvSubTitle;
    private ItemClickListener itemClickListener;
    public FileRecycleViewHolder(View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.imgFolder);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onLongClick(v, getAdapterPosition());
        return true;
    }
}
    public class FileRecycleViewAdapter extends RecyclerView.Adapter<FileRecycleViewHolder>{
        private List<FileModel> fileList;
        private Context context;

        public FileRecycleViewAdapter(List<FileModel> fileList, Context context) {
            this.fileList = fileList;
            this.context = context;
        }

        @Override
        public FileRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View itemView = inflater.inflate(R.layout.item_folder, parent, false);

            return new FileRecycleViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(FileRecycleViewHolder holder, int position) {
            FileModel model = this.fileList.get(position);
            holder.img.setImageResource(model.getImg());
            holder.tvTitle.setText(model.getTitle());
            holder.tvSubTitle.setText(model.getSubTitle());
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //handle after
                }

                @Override
                public void onLongClick(View view, int position) {
                    //handle after
                }
            });
        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }

    }


