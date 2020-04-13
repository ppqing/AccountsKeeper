package cn.ppqing.accountskeeper.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.ppqing.accountskeeper.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.mViewHolder> {
    private  String[] data;

    class mViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.list_item);
        }
    }

    public ListAdapter(String[] data){
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
            holder.textView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }


}
