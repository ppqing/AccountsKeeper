package cn.ppqing.accountskeeper.ui.dashboard;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.MainActivity;
import cn.ppqing.accountskeeper.R;
import cn.ppqing.accountskeeper.db.DataOperator;

public class DashboardFragment extends Fragment {
    private String[] times;
    private Switch mswitch;
    private TextView rate_num;
    private DashboardViewModel dashboardViewModel;
    private String rates;
    private TextView date;
    private TextView num;
    private TextView in;
    private TextView out;
    private Switch CNY;
    private int[] res;
    private Boolean flag_cny = false;
    private Spinner spinner_date;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        date = root.findViewById(R.id.Date);
        date.setText(Data.getDate());
        res = new int[3];
        in = root.findViewById(R.id.in);
        out = root.findViewById(R.id.out);
        num = root.findViewById(R.id.number);
        CNY = root.findViewById(R.id.ai_switch);
        Bundle bundle = this.getArguments();
        spinner_date = root.findViewById(R.id.times);
        times = getResources().getStringArray(R.array.time_array);
        rate_num = root.findViewById(R.id.rate_num);
        LinearLayout l = root.findViewById(R.id.dash_layout);
        l.getBackground().setAlpha(200);
        //setnumber();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, times);
        spinner_date.setAdapter(adapter1);
        CNY.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("switch", "CNY!!!!!!!");
                    flag_cny = true;
                } else {
                    flag_cny = false;
                }
                setnumber();
            }
        });
        spinner_date.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int arg2, long arg3) {
                        int index = arg0.getSelectedItemPosition();

                        if (index == 0) {

                            res = DataOperator.getMonthCost(getContext());
                            Toast.makeText(getActivity(), times[index],
                                    Toast.LENGTH_LONG).show();
                        } else if (index == 1) {//this dat
                            res = DataOperator.getTodayCost(getContext());
                            Toast.makeText(getActivity(), times[index],
                                    Toast.LENGTH_LONG).show();
                        } else if (index == 2) {//3 months
                            res = DataOperator.get3MonthsCost(getContext());
                            Toast.makeText(getActivity(), times[index],
                                    Toast.LENGTH_LONG).show();
                        } else if (index == 3) {  //this year
                            res = DataOperator.getYearCost(getContext());
                            Toast.makeText(getActivity(), times[index],
                                    Toast.LENGTH_LONG).show();
                        }
                        setnumber();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });


        String rate;
        rate_num.setText("CNY:" + String.valueOf(Data.rate));
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //rates = ((MainActivity)context).toValue();
        //Log.e("dh rate:",rates);

    }
    private void setnumber(){
        double f = Data.rate;
        if (flag_cny) {
            Log.e("switch", "ok");
            num.setText(String.valueOf((int)(res[0] / f)) + "CNY");
            in.setText(String.valueOf((int) (res[1] / f)) + "CNY");
            out.setText(String.valueOf(Math.abs((int) (res[2] / f))) + "CNY");
        } else {
            Log.e("switch", "ok");
            num.setText(String.valueOf((int) res[0]) + "HKD");
            in.setText(String.valueOf((int) res[1]) + "HKD");
            out.setText(String.valueOf(Math.abs((int) res[2])) + "HKD");
        }
    }
}