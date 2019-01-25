package app.it.hueic.nghiencuukhoahochueic.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.it.hueic.nghiencuukhoahochueic.R;
import app.it.hueic.nghiencuukhoahochueic.interf.ItemClickListener;
import app.it.hueic.nghiencuukhoahochueic.model.Para;

/**
 * Created by KenHoang on 04-Jan-18.
 */
class DateRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    TextView tvTietItem, tvTenMonHoc, tvTenGiaoVien, tvTenPhong;
    private ItemClickListener itemClickListener;
    public DateRecycleViewHolder(View itemView) {
        super(itemView);
        tvTietItem = (TextView) itemView.findViewById(R.id.tvTietItem);
        tvTenMonHoc = (TextView) itemView.findViewById(R.id.tvTenMonHoc);
        tvTenGiaoVien = (TextView) itemView.findViewById(R.id.tvTenGiaoVien);
        tvTenPhong = (TextView) itemView.findViewById(R.id.tvPhongItem);
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
public class DateRecycleViewAdapter extends RecyclerView.Adapter<DateRecycleViewHolder>{
    private List<Para> paraList;
    private Context context;

    public DateRecycleViewAdapter(List<Para> paraList, Context context) {
        this.paraList = paraList;
        this.context = context;
    }

    @Override
    public DateRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_para_list, parent, false);

        return new DateRecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DateRecycleViewHolder holder, int position) {
        Para para = this.paraList.get(position);
        holder.tvTietItem.setText(para.tietbatdau + " - " + para.tietkethuc + " ");
        holder.tvTenMonHoc.setText(para.tenmonhoc);
        holder.tvTenGiaoVien.setText(para.tengiaovien);
        holder.tvTenPhong.setText(para.phonghoc);
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
        return paraList.size();
    }
}
