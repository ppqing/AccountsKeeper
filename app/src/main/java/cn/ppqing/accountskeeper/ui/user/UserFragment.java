package cn.ppqing.accountskeeper.ui.user;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.R;
import cn.ppqing.accountskeeper.db.DataOperator;

public class UserFragment extends Fragment {

    private UserViewModel mViewModel;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        // TODO: Use the ViewModel

        Button btn_download =getActivity().findViewById(R.id.button_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data d=new Data(1,"2","3","4");
                DataOperator.addToDB(getContext(),d);
                Toast.makeText(getContext(), "added", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
