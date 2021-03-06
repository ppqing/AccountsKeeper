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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;
import cn.ppqing.accountskeeper.db.DataOperator;

public class OutcomeFragment extends Fragment  {

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
    private String get_type;
    private String get_method;
    private int get_costs;
    private EditText cos_num;
    private EditText remarks_Text;
    private  String re;
    private int year, month,day;
    private StringBuffer date_s;
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
        initDateTime();
        date_s = new StringBuffer();
        HKD = (CheckBox)getActivity().findViewById(R.id.check_hkd);
        CNY = (CheckBox)getActivity().findViewById(R.id.check_cny);
        cur = (ImageView)getActivity().findViewById(R.id.cur);
        Back = (Button)getActivity().findViewById(R.id.Cancel);
        Submit = (Button)getActivity().findViewById(R.id.submit);
        date = (TextView) getActivity().findViewById(R.id.Date);
        cos_num=(EditText)getActivity().findViewById(R.id.cost_num);
        remarks_Text=(EditText)getActivity().findViewById(R.id.Remarks_text);
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
        date.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date_s.length() > 0) { //清除上次记录的日期
                            date_s.delete(0, date.length());
                        }
                       // Log.e("Date:",date_s.append(String.valueOf(day)).append("-").append(String.valueOf(month)).append("-").append(year).toString());
                        month+=1;
                        date.setText(date_s.append(String.valueOf(day)).append("-").append(String.valueOf(month)).append("-").append(year));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(getContext(), R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month -1, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
                        year = Year;
                        month = monthOfYear;
                        day = dayOfMonth;
                    }
                });
            }
        });

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

                    alterDiaglog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alterDiaglog.show();


                }
                else{

                    int cos = 0-Integer.parseInt(cos_num.getText().toString());
                    re= remarks_Text.getText().toString();
                    String d = date.getText().toString();

                    Data input = new Data(cos,get_type,get_method,d,re);
                    Log.e("checked",input.date);
                    DataOperator.addToDB(getContext(),input);
                    Toast.makeText(getContext(), "added", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    });
        Back.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Log.e("OK","finish");
                getActivity().finish();}

        });
       spinner_type_outcome.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int arg2, long arg3) {
                        int index = arg0.getSelectedItemPosition();
                        get_type = types_outcome[index];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        spinner_method_outcome.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int arg2, long arg3) {
                        int index = arg0.getSelectedItemPosition();
                        get_method = methods[index];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });



}
    public void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) ;
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }



}



