package cn.ppqing.accountskeeper.ui.dashboard;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import cn.ppqing.accountskeeper.MainActivity;
import cn.ppqing.accountskeeper.R;

public class DashboardFragment extends Fragment {
    private String[] times;
    private Switch mswitch;
    private  TextView rate_num;
    private DashboardViewModel dashboardViewModel;
    private  String rates;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Bundle bundle = this.getArguments();
        Spinner spinner_date = root.findViewById(R.id.times);
        times = getResources().getStringArray(R.array.time_array);
        rate_num = root.findViewById(R.id.rate_num) ;
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item , times);
        spinner_date.setAdapter(adapter1);
        spinner_date.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int arg2, long arg3) {
                        int index = arg0.getSelectedItemPosition();

                        Toast.makeText(getActivity(), times[index],
                                Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        String rate ;
        rate_num.setText("CNY:"+rates);
        return root;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rates = ((MainActivity)context).toValue();
        Log.e("dh rate:",rates);

    }

}
