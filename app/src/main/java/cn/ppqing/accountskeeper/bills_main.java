package cn.ppqing.accountskeeper;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.FrameLayout;
import android.widget.TextView;

public class bills_main extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private FrameLayout mFrame;
    private TabLayout mTab;
    private OutcomeFragment out;
    private IncomeFragment in;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_main);
        initView();
        initTab();      // 设置TabLayout的标题
       // initToolBar();  // 设置ToolBar
        initFragmentReplace();  // 设置开启页面时fragment的显示和影藏
        mTab.addOnTabSelectedListener(this);    // 设置TabLayout选择监听
    }

    private void initTab() {

        mTab.addTab(mTab.newTab().setText(R.string.out));
        mTab.addTab(mTab.newTab().setText(R.string.in));

    }

    private void initFragmentReplace() {

        // 获取到fragment碎片管理器
        FragmentManager manager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = manager.beginTransaction();

        // 获取到fragment的对象
        out = new OutcomeFragment();
        in = new IncomeFragment();

        // 设置要显示的fragment 和 影藏的fragment
        transaction.add(R.id.frame, out, "home").show(out);
        transaction.add(R.id.frame, in, "upload").hide(in);

        // 提交事务
        transaction.commit();

    }

    private void initView() {
        // 获取控件对象
        mTab = (TabLayout) findViewById(R.id.tab);
        mFrame = (FrameLayout) findViewById(R.id.frame);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        // 设置标题
       // mTvTitle.setText(tab.getText());

        // 设置选中时fragment的显示和影藏
        switch (tab.getPosition()) {
            case 0:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.show(out).hide(in).commit();

                break;
            case 1:
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                transaction1.show(in).hide(out).commit();

                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}