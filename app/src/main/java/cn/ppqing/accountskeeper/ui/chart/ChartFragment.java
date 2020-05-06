package cn.ppqing.accountskeeper.ui.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.MainActivity;
import cn.ppqing.accountskeeper.R;
import cn.ppqing.accountskeeper.db.DataOperator;

public class ChartFragment extends Fragment {


    private ChartViewModel chartViewModel;
    private Boolean cny = false;

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
                    Toast.makeText(getActivity(), "转换为港币", Toast.LENGTH_SHORT).show();
                    cny = false;
                } else {
                    Toast.makeText(getActivity(), "转换为人民币", Toast.LENGTH_SHORT).show();
                    cny = true;
                }

            }
        });

        //初始化饼图
        final PieChart pieChart1 = root.findViewById(R.id.pieChart1);
        final PieChart pieChart2 = root.findViewById(R.id.pieChart2);

        //设置spinner
        final String[] dates = getResources().getStringArray(R.array.time_array);
        Spinner mSpinner = root.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, dates);
        mSpinner.setAdapter(adapter);
        //依据spinner结果画图
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(), "统计" + dates[position] + "消费", Toast.LENGTH_SHORT).show();
                        List<List> datalist;
                        datalist = setnumber();
                        showPieChart((List<PieEntry>) datalist.get(0).get(0), pieChart1, getResources().getString(R.string.figure1));
                        showPieChart((List<PieEntry>) datalist.get(1).get(0), pieChart2, getResources().getString(R.string.figure2));
                        break;
                    case 1:
                        datalist = setnumber();
                        Toast.makeText(getActivity(), "统计" + dates[position] + "消费", Toast.LENGTH_SHORT).show();
                        showPieChart((List<PieEntry>) datalist.get(0).get(1), pieChart1, getResources().getString(R.string.figure1));
                        showPieChart((List<PieEntry>) datalist.get(1).get(1), pieChart2, getResources().getString(R.string.figure2));
                        break;
                    case 2:
                        datalist = setnumber();
                        Toast.makeText(getActivity(), "统计" + dates[position] + "消费", Toast.LENGTH_SHORT).show();
                        showPieChart((List<PieEntry>) datalist.get(0).get(2), pieChart1, getResources().getString(R.string.figure1));
                        showPieChart((List<PieEntry>) datalist.get(1).get(2), pieChart2, getResources().getString(R.string.figure2));
                        break;
                    case 3:
                        datalist = setnumber();
                        Toast.makeText(getActivity(), "统计" + dates[position] + "消费", Toast.LENGTH_SHORT).show();;
                        showPieChart((List<PieEntry>) datalist.get(0).get(3), pieChart1, getResources().getString(R.string.figure1));
                        showPieChart((List<PieEntry>) datalist.get(1).get(3), pieChart2, getResources().getString(R.string.figure2));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return root;
    }

    private void showPieChart(List<PieEntry> entries, PieChart pieChart, String descriptionStr) {

        //根据spinner结果录入统计数据
        PieDataSet dataSet = new PieDataSet(entries, "Label");

        //添加色彩数据
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Integer> colorList = new ArrayList<>(Arrays.asList(0xffff3333,
                0xff333399, 0xff33cc66, 0xff9933cc, 0xff6633ff, 0xFFFF00FF,
                0xFF800000, 0xFF808000, 0xFF000080, 0xFF008080, 0xFFADD8E6));
        for (int i = 0; i < entries.size(); i++) {
            colors.add(colorList.get(i));
        }
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);

        //数字设置百分比，添加文字注释
        /*pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());*/
        pieData.setValueTextSize(14f);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);

        Description description = new Description();
        description.setText(descriptionStr);
        description.setTextSize(18f);

        //设置饼图距view的偏移量
        pieChart.setExtraTopOffset(20f);
        // 获取屏幕中间x轴dp值坐标
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        Paint paint = new Paint();
        paint.setTextSize(18f);
        // x对齐方式：居中对齐
        description.setTextAlign(Paint.Align.CENTER);
        float x = dm.widthPixels / 2;
        // 设置y的dp值为文本高度+12
        float height = Utils.calcTextHeight(paint, descriptionStr);
        float y = Utils.convertDpToPixel(height + 12);
        // 设置x和y的偏移量
        description.setPosition(x, y);
        pieChart.setDescription(description);

        //设置禁用比例块
        Legend mLegend = pieChart.getLegend();
        mLegend.setEnabled(false);
        //饼图实心
        /*pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);*/

        pieChart.invalidate();

    }

    private List<List> setnumber() {
        double f = Data.rate;
        List<PieEntry> entries00 = new ArrayList<>();
        List<PieEntry> entries0 = new ArrayList<>();
        List<PieEntry> entries10 = new ArrayList<>();
        List<PieEntry> entries1 = new ArrayList<>();
        List<PieEntry> entries20 = new ArrayList<>();
        List<PieEntry> entries2 = new ArrayList<>();
        List<PieEntry> entries30 = new ArrayList<>();
        List<PieEntry> entries3 = new ArrayList<>();
        List<Data> cnycost00 = DataOperator.ThisMonthKindCost(getActivity());
        List<Data> cnycost0 = DataOperator.ThisMonthMethodCost(getActivity());
        List<Data> cnycost10 = DataOperator.TodayKindCost(getActivity());
        List<Data> cnycost1 = DataOperator.TodayMethodCost(getActivity());
        List<Data> cnycost20 = DataOperator.TMonthsKindCost(getActivity());
        List<Data> cnycost2 = DataOperator.TMonthsMethodCost(getActivity());
        List<Data> cnycost30 = DataOperator.YearKindCost(getActivity());
        List<Data> cnycost3 = DataOperator.YearMethodCost(getActivity());
        if (cny){

            for (int i = 0; i < cnycost00.size(); i++) {
                entries00.add(new PieEntry((int)(cnycost00.get(i).costs/f), cnycost00.get(i).method));
            }
            for (int i = 0; i < cnycost0.size(); i++) {
                entries0.add(new PieEntry((int)(cnycost0.get(i).costs/f), cnycost0.get(i).method));
            }

            for (int i = 0; i < cnycost10.size(); i++) {
                entries10.add(new PieEntry((int)(cnycost10.get(i).costs/f), cnycost10.get(i).method));
            }
            for (int i = 0; i < cnycost1.size(); i++) {
                entries1.add(new PieEntry((int)(cnycost1.get(i).costs/f), cnycost1.get(i).method));
            }

            for (int i = 0; i < cnycost20.size(); i++) {
                entries20.add(new PieEntry((int)(cnycost20.get(i).costs/f), cnycost20.get(i).method));
            }
            for (int i = 0; i < cnycost2.size(); i++) {
                entries2.add(new PieEntry((int)(cnycost2.get(i).costs/f), cnycost2.get(i).method));
            }

            for (int i = 0; i < cnycost30.size(); i++) {
                entries30.add(new PieEntry((int)(cnycost30.get(i).costs/f), cnycost30.get(i).method));
            }
            for (int i = 0; i < cnycost3.size(); i++) {
                entries3.add(new PieEntry((int)(cnycost3.get(i).costs/f), cnycost3.get(i).method));
            }
        }
        else {
            for (int i = 0; i < cnycost00.size(); i++) {
                entries00.add(new PieEntry(cnycost00.get(i).costs, cnycost00.get(i).method));
            }
            for (int i = 0; i < cnycost0.size(); i++) {
                entries0.add(new PieEntry(cnycost0.get(i).costs, cnycost0.get(i).method));
            }

            for (int i = 0; i < cnycost10.size(); i++) {
                entries10.add(new PieEntry(cnycost10.get(i).costs, cnycost10.get(i).method));
            }
            for (int i = 0; i < cnycost1.size(); i++) {
                entries1.add(new PieEntry(cnycost1.get(i).costs, cnycost1.get(i).method));
            }

            for (int i = 0; i < cnycost20.size(); i++) {
                entries20.add(new PieEntry(cnycost20.get(i).costs, cnycost20.get(i).method));
            }
            for (int i = 0; i < cnycost2.size(); i++) {
                entries2.add(new PieEntry(cnycost2.get(i).costs, cnycost2.get(i).method));
            }

            for (int i = 0; i < cnycost30.size(); i++) {
                entries30.add(new PieEntry(cnycost30.get(i).costs, cnycost30.get(i).method));
            }
            for (int i = 0; i < cnycost3.size(); i++) {
                entries3.add(new PieEntry(cnycost3.get(i).costs, cnycost3.get(i).method));
            }
        }
        Log.e("stingsetnumber", cny.toString());
        List<List> list1 = new ArrayList<>();
        list1.add(entries00);
        list1.add(entries10);
        list1.add(entries20);
        list1.add(entries30);

        List<List> list2 = new ArrayList<>();
        list2.add(entries0);
        list2.add(entries1);
        list2.add(entries2);
        list2.add(entries3);

        List<List> listpack = new ArrayList<>();
        listpack.add(list1);
        listpack.add(list2);
        return listpack;
    }

}


