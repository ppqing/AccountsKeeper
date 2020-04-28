package cn.ppqing.accountskeeper.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCosts=itemView.findViewById(R.id.list_item_costs);
            textViewKind=itemView.findViewById(R.id.list_item_kind);
            textViewMethod=itemView.findViewById(R.id.list_item_method);
            textViewDate=itemView.findViewById(R.id.list_item_date);
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

            holder.textViewCosts.setText("Cost: "+data.get(position).costs);
            holder.textViewMethod.setText("Method: "+data.get(position).method);
            holder.textViewKind.setText("Kind: "+data.get(position).kind);
            holder.textViewDate.setText("Date: "+data.get(position).date);

    }

    @Override
    public int getItemCount() {

        return data.size();

    }


}
