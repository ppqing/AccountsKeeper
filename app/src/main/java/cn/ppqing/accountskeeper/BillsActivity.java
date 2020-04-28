package cn.ppqing.accountskeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

public class BillsActivity extends AppCompatActivity {
    private int currency=0;
    private CheckBox HKD;
    private CheckBox CNY;
    private ImageView cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        HKD = (CheckBox)findViewById(R.id.check_hkd);
        CNY = (CheckBox)findViewById(R.id.check_cny);
        cur = (ImageView)findViewById(R.id.cur);
    }
    public  void  onChecked(View view){
       // boolean checked = ((CheckBox)view).isChecked();
        switch (view.getId()){
            case R.id.check_cny:
                if(CNY.isChecked()){
                    currency=1;
                    cur.setImageDrawable(getResources().getDrawable((R.drawable.rmb)));
                    HKD.setChecked(false);
                }
            case R.id.check_hkd:
                if(HKD.isChecked()){
                    currency=2;
                    cur.setImageDrawable(getResources().getDrawable((R.drawable.hk)));
                   CNY.setChecked(false);
                }
        }
    }
    public void Submit_Bills(View view){
        //boolean checked = ((CheckBox)view).isChecked();
        if (!HKD.isChecked()&&!CNY.isChecked()){
            Log.e("ckecked","nononono");
            final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(BillsActivity.this);
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
    public void Back(View view){
       Log.e("OK","finish");
        this.finish();}
};
