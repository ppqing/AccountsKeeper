package cn.ppqing.accountskeeper.ui.list;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.mViewHolder> {
    private  List<Data> data;

    class mViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCosts;
        TextView textViewKind;
        TextView textViewMethod;
        TextView textViewDate;
        TextView textViewRe;
        ImageView listImage;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCosts=itemView.findViewById(R.id.list_item_costs);
            textViewKind=itemView.findViewById(R.id.list_item_kind);
            textViewMethod=itemView.findViewById(R.id.list_item_method);
            textViewDate=itemView.findViewById(R.id.list_item_date);
            textViewRe=itemView.findViewById(R.id.list_item_remarks);
            listImage=itemView.findViewById(R.id.list_item_image);
        }
    }

    public ListAdapter(List<Data> data){
        this.data=data;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate( R.layout.list_item,parent,false);
        mViewHolder viewHolder=new mViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
            Log.e("Date",data.get(position).date);
            Context mContext=holder.itemView.getContext();
            holder.textViewMethod.setText(mContext.getString(R.string.payment)+": "+data.get(position).method);
            holder.textViewKind.setText(mContext.getString(R.string.kind)+": "+data.get(position).kind);
            holder.textViewDate.setText(mContext.getString(R.string.Date)+": "+data.get(position).date);
            holder.textViewRe.setText(mContext.getString(R.string.note)+": "+data.get(position).remarks);
            if(data.get(position).costs>=0){
                holder.textViewCosts.setText(mContext.getString(R.string.Cost)+": +"+data.get(position).costs);
                holder.textViewCosts.setTextColor(Color.RED);
                holder.listImage.setImageResource(R.drawable.ic_income_black_24dp);
            }else {
                holder.textViewCosts.setText(mContext.getString(R.string.Cost)+": "+data.get(position).costs);
                holder.textViewCosts.setTextColor(Color.GREEN);
                holder.listImage.setImageResource(R.drawable.ic_outcome_black_24dp);
            }
    }

    @Override
    public int getItemCount() {

        return data.size();

    }

}
