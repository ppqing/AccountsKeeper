package cn.ppqing.accountskeeper.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.R;
import cn.ppqing.accountskeeper.db.DataOperator;


public class ListFragment extends Fragment {

    private ListViewModel listViewModel;
    mRecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;
    RadioGroup radioGroup;
    RadioButton radioButtonDefault,radioButtonS2L,radioButtonL2S;
    List<Data> data;
    List<Data> dataSortS2L;
    List<Data> dataSortL2S;



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
        radioGroup=root.findViewById(R.id.radio_group);
        radioButtonDefault=root.findViewById(R.id.radio_default);
        radioButtonS2L=root.findViewById(R.id.radio_small_to_large);
        radioButtonL2S=root.findViewById(R.id.radio_large_to_small);

        radioGroup.setOnCheckedChangeListener(new MyRadioButtonListener());

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(root.getContext());
        registerForContextMenu(recyclerView);



        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        data= DataOperator.readFromDB(getContext());

        adapter=new ListAdapter(data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.list_item_context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
//            case R.id.context_edit:
//                break;
            case R.id.context_delete:
                DataOperator.deleteFromDB(getContext(),data.get(menuInfo.position).id);
                data.remove(menuInfo.position);
                adapter.notifyItemRemoved(menuInfo.position);
                adapter.notifyItemRangeChanged(menuInfo.position,adapter.getItemCount());
                break;
        }
        adapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_default:
                    data = DataOperator.readFromDB(getContext());
                    adapter = new ListAdapter(data);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    break;
                case R.id.radio_small_to_large:
                    dataSortS2L = DataOperator.readFromDB(getContext());
                    Collections.sort(dataSortS2L, new Comparator<Data>() {
                        @Override
                        public int compare(Data o1, Data o2) {
                            if (o1.costs > o2.costs) {
                                return 1;
                            } else if (o1.costs == o2.costs) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }
                    });
                    adapter = new ListAdapter(dataSortS2L);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    break;
                case R.id.radio_large_to_small:
                    dataSortL2S = DataOperator.readFromDB(getContext());
                    Collections.sort(dataSortL2S, new Comparator<Data>() {
                        @Override
                        public int compare(Data o1, Data o2) {
                            if (o1.costs > o2.costs) {
                                return -1;
                            } else if (o1.costs == o2.costs) {
                                return 0;
                            } else {
                                return 1;
                            }
                        }
                    });
                    adapter = new ListAdapter(dataSortL2S);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    }
}
