package cn.ppqing.accountskeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import cn.ppqing.accountskeeper.Data;

public class DBHelper extends SQLiteOpenHelper {
    //表的结构 id cost kind method text
    public static final String CREATE_DB="create table List ("
            +"id integer primary key autoincrement,"
            +"costs integer,"
            +"kind text,"
            +"method text,"
            +"date text,"
            +"remarks text)";

    private Context mContext;
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB);
        Toast.makeText(mContext,"初次启动，成功创建数据库",Toast.LENGTH_SHORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(mContext,"数据库已更新",Toast.LENGTH_SHORT);
    }

    public void writeToFile(List<Data> list){
        FileOutputStream fileOutputStream=null;
        BufferedWriter writer=null;
        try {
            fileOutputStream =mContext.openFileOutput("data.json",Context.MODE_PRIVATE);
            Gson gson=new Gson();
            String json=gson.toJson(list);
            writer=new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(json);
            Log.d("json",json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("file","打开文件失败");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("file","写入文件失败");
        }finally {
            try {
                if(writer!=null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToFile(Context context){
        try {
            context.openFileOutput("data.json",Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("file","打开文件失败");
        }
    }

    public void readFromFile(){

    }


}
