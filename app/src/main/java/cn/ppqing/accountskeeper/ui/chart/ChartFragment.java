package cn.ppqing.accountskeeper.ui.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.MainActivity;
import cn.ppqing.accountskeeper.R;
import cn.ppqing.accountskeeper.db.DataOperator;

public class ChartFragment extends Fragment {


    private ChartViewModel chartViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chartViewModel =
                ViewModelProviders.of(this).get(ChartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chart, container, false);
        /*final TextView textView = root.findViewById(R.id.text_notifications);
        chartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        //设置checkbox
        final CheckBox hkd = root.findViewById(R.id.HKD);
        hkd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on clicks
                if (hkd.isChecked()) {
                    Toast.makeText(getActivity(), "Convert to HKD ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Convert to RMB", Toast.LENGTH_LONG).show();
                }
            }
        });

        //设置spinner
        final String[] dates = getResources().getStringArray(R.array.time_array);
        Spinner mSpinner = root.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, dates);
        mSpinner.setAdapter(adapter);

        final List<Data> dataList= DataOperator.readFromDB(getContext());
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        Toast.makeText(getActivity(), "selected " + dates[position], Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "selected " + dates[position], Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "selected " + dates[position], Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "selected " + dates[position], Toast.LENGTH_LONG).show();;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //画图
        PieChart pieChart = root.findViewById(R.id.pieChart);
        //数据库读取数据
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            entries.add(new PieEntry(dataList.get(i).costs, dataList.get(i).kind));
        }
        PieDataSet dataSet = new PieDataSet(entries,"Label");

        //添加色彩数据
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Integer> colorList = new ArrayList<>(Arrays.asList(0xff6633ff,0xffff3333,
                0xff333399,0xff33cc66,0xff9933cc,0xff330000));
        for (int i = 0; i < dataList.size(); i++) {
            colors.add(colorList.get(i));
        }
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);

        //设置百分比，添加文字注释
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(16f);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);

        String descriptionStr = "Consumption Kind Proportion";
        Description description = new Description();
        description.setText(descriptionStr);
        description.setTextSize(14f);

        // 对齐方式：居中对齐
        description.setTextAlign(Paint.Align.CENTER);
        // 获取屏幕中间x轴dp值坐标
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        Paint paint = new Paint();
        paint.setTextSize(18f);
        float x = Utils.convertPixelsToDp(dm.widthPixels / 3);
        // 设置y的dp值为文本高度+12
        float y = Utils.calcTextHeight(paint, descriptionStr) + 12;
        // 设置x和y的偏移量
        description.setXOffset(x);
        description.setYOffset(-y); // 反方向偏移为负值

        pieChart.setDescription(description);
        pieChart.invalidate();

        //饼图实心
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);

        return root;
    }
}
