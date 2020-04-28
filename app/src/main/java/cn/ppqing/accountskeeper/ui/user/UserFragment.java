package cn.ppqing.accountskeeper.ui.user;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.MainActivity;
import cn.ppqing.accountskeeper.R;
import cn.ppqing.accountskeeper.db.DBHelper;
import cn.ppqing.accountskeeper.db.DataOperator;
import cn.ppqing.accountskeeper.db.WebDAV;

import static android.content.Context.MODE_PRIVATE;


public class UserFragment extends Fragment {

    private UserViewModel mViewModel;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextServer;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        // TODO: Use the ViewModel

        editTextUsername=getActivity().findViewById(R.id.edittext_username);
        editTextPassword=getActivity().findViewById(R.id.edittext_password);
        editTextServer=getActivity().findViewById(R.id.edittext_server);

        loadPreferences();

        final Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Toast.makeText(getContext(), "download success", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getContext(), "download failed", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getContext(), "upload success", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getContext(), "upload failed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        Button btn_download =getActivity().findViewById(R.id.button_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg=new Message();
                        WebDAV dav=new WebDAV(getContext(),editTextUsername.getText().toString(),editTextPassword.getText().toString(),editTextServer.getText().toString());
                        String jsonStr=dav.download();
                        if(jsonStr!=""){
                            DBHelper dbHelper=new DBHelper(getContext(),"List.db",null,1);
                            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                            sqLiteDatabase.execSQL("DELETE FROM List");
                            Gson gson=new Gson();
                            List<Data> list=gson.fromJson(jsonStr,new TypeToken<List<Data>>(){}.getType());
                            for(int i=0;i<list.size();i++){
                                Data e=list.get(i);
                                DataOperator.addToDB(getContext(),e);
                            }
                            msg.what=1;
                            handler.sendMessage(msg);
                        }else {
                            msg.what=2;
                            handler.sendMessage(msg);
                        };
                    }
                }).start();
            }
        });

        Button btn_upload =getActivity().findViewById(R.id.button_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg=new Message();
                        WebDAV dav=new WebDAV(getContext(),editTextUsername.getText().toString(),editTextPassword.getText().toString(),editTextServer.getText().toString());
                        if(dav.upload(DataOperator.readFromDB(getContext()))){
                            msg.what=3;
                            handler.sendMessage(msg);

                        }else {
                            msg.what=4;
                            handler.sendMessage(msg);

                        };
                    }
                }).start();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        savePreferences(editTextUsername.getText().toString(),editTextPassword.getText().toString(),editTextServer.getText().toString());
    }

    public void savePreferences(String username, String password,String server) {
        SharedPreferences pref = getContext().getSharedPreferences("User", MODE_PRIVATE);
        pref.edit().putString("username", username).apply();
        pref.edit().putString("password", password).apply();
        pref.edit().putString("server", server).apply();
    }
    public void loadPreferences() {
        SharedPreferences pref = getContext().getSharedPreferences("User", MODE_PRIVATE);
        editTextUsername.setText(pref.getString("username", ""));
        editTextPassword.setText(pref.getString("password", ""));
        editTextServer.setText(pref.getString("server", ""));
    }


}
