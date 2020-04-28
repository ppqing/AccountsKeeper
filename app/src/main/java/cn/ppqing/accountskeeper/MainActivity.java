package cn.ppqing.accountskeeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;


import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.ppqing.accountskeeper.ui.dashboard.DashboardFragment;



public class MainActivity extends AppCompatActivity {

    private ConnectivityManager mConnMgr;
    private String Rate;
    private Bundle bundle=new Bundle();
    public TextView rec;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        FragmentManager FM = getSupportFragmentManager();
        rec = (TextView)findViewById(R.id.rec);
        DashboardFragment db =(DashboardFragment)FM.findFragmentById(R.id.navigation_dashboard);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_list,R.id.navigation_chart,R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        mConnMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        loadRate();
        Log.v("Get","x"+rec.getText());

    }
    //menu set
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {getMenuInflater().inflate(R.menu.menu_option, menu);
        return true; }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.add_bill:
                Toast.makeText(this, R.string.add_mention,
                        Toast.LENGTH_LONG).show();
               //Intent intent = new Intent(this,BillsActivity.class);
                Intent intent = new Intent(this,bills_main.class);
                startActivity(intent);
        }
        return false;
    }
    public void setRate(String str){
        this.Rate = str;
    }
   /* public  String toValue(){
        return  Data.rate;
    }*/
    private void loadRate(){
        String path = "http://www.floatrates.com/daily/hkd.json";
        String val;
        if (mConnMgr!=null){
            NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected()){
                try{
                DownloadRateTask getrate= new DownloadRateTask() ;
                getrate.execute(path);
                getrate.setOnAsyncResponse(new AsyncResponse() {
                    @Override
                    public void onDataReceivedSuccess(String data) {


                        //Log.e("reveive",Data.rate);
                    }
                    @Override
                    public void onDataReceivedFailed() {

                    }
                });
            }catch (Exception e) {
                    e.printStackTrace();
                }   }
            else{
                Toast.makeText(this, "Network Not Avaialble", Toast.LENGTH_LONG);


            }
        }

    }
    class DownloadRateTask extends AsyncTask<String, Void, String> {
        public AsyncResponse asyncResponse;
        public void setOnAsyncResponse(AsyncResponse asyncResponse)
        {
            this.asyncResponse = asyncResponse;
        }

        public String downloadRate(String path){
            String data="";
            InputStream inStream;
            try {
                //Create a URL Connection object and set its parameters
                Log.v("url:",path);
                URL url = new URL(path);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                //Set connection time out of 5 seconds
                urlConn.setConnectTimeout(5000);
                //Set read connection time out of 2.5 seconds
                urlConn.setReadTimeout(2500);
                //Set HTTP request method
                urlConn.setRequestMethod("GET");
                urlConn.setDoInput(true);

                //Perfrom network request
                urlConn.connect();

                //After the network response, retrieve the input stream
                inStream = urlConn.getInputStream();
                //Convert the input stream to String Bitmap
                data = readStream(inStream);
                Log.v("data:",data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  data;
        }
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                Log.e("HttpGetTask", "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }

        protected String doInBackground(String...urls) {
            return downloadRate(urls[0]);
        }
        protected void onPostExecute(String data){
            if(data!=null){
                try{
                    JSONObject reader= new JSONObject(data);
                    JSONObject rateReader;
                    String mapString, rateString;
                    mapString =  reader.getString("cny");
                    rateReader = new JSONObject(mapString);
                    Log.v("rmb:        ",mapString);
                    rateString = rateReader.getString("inverseRate");
                    //Data.setA(rateString);
                    rec.setText(data);
                    asyncResponse.onDataReceivedSuccess("CNY:"+rateString);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

}


