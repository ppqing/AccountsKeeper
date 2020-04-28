package cn.ppqing.accountskeeper;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;

public class OutcomeFragment extends Fragment {

    private OutcomeViewModel mViewModel;
    private int currency=0;
    private CheckBox HKD;
    private CheckBox CNY;
    private ImageView cur;
    private Button Back;
    private Button Submit;
    private String [] types_outcome;
    private String [] methods;
    private String [] types_income;
    private TextView date;
    public static OutcomeFragment newInstance() {
        return new OutcomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_outcome, container, false);

        return  root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OutcomeViewModel.class);
        HKD = (CheckBox)getActivity().findViewById(R.id.check_hkd);
        CNY = (CheckBox)getActivity().findViewById(R.id.check_cny);
        cur = (ImageView)getActivity().findViewById(R.id.cur);
        Back = (Button)getActivity().findViewById(R.id.Cancel);
        Submit = (Button)getActivity().findViewById(R.id.submit);
        date = (TextView) getActivity().findViewById(R.id.Date);
        date.setText(Data.getDate());
        Spinner spinner_type_outcome = (Spinner)getActivity().findViewById(R.id.type);
        Spinner spinner_method_outcome = (Spinner)getActivity().findViewById(R.id.method);
        types_outcome = getResources().getStringArray(R.array.Bill_type);
        methods = getResources().getStringArray(R.array.method_type);
        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item , types_outcome);
        ArrayAdapter<String> adapter3= new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item , methods);
        spinner_type_outcome.setAdapter(adapter2);
        spinner_method_outcome.setAdapter(adapter3);
        // TODO: Use the ViewModel
        HKD.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                if(HKD.isChecked()){
                    currency=2;
                    cur.setImageDrawable(getResources().getDrawable((R.drawable.hk)));
                    CNY.setChecked(false);
                }
            }
        });
        CNY.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                if(CNY.isChecked()){
                    currency=1;
                    cur.setImageDrawable(getResources().getDrawable((R.drawable.rmb)));
                    HKD.setChecked(false);
                }
            }
        });
        Submit.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                if (!HKD.isChecked()&&!CNY.isChecked()){
                    Log.e("ckecked","nononono");
                    final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(getActivity());
                    //alterDiaglog.setIcon(R.drawable.icon);图标
                    //alterDiaglog.setTitle(R.string.u);//文字
                    alterDiaglog.setMessage(R.string.unchecked);//提示消息

                    //积极的选择
                    alterDiaglog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alterDiaglog.show();


                }
                else{
                    Log.e("checked","yes");
                }
        }
    });
        Back.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Log.e("OK","finish");
                getActivity().finish();}

        });


}}



