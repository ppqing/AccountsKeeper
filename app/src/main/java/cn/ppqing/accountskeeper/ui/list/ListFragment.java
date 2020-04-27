package cn.ppqing.accountskeeper.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.R;
import cn.ppqing.accountskeeper.db.DataOperator;

public class ListFragment extends Fragment {

    private ListViewModel listViewModel;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listViewModel =
                ViewModelProviders.of(this).get(ListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        listViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        recyclerView=root.findViewById(R.id.recyclerview_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(root.getContext());

        List<Data> data= DataOperator.readFromDB(getContext());
        adapter=new ListAdapter(data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }
}
