package cn.ppqing.accountskeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.ppqing.accountskeeper.Data;

public class DataOperator {
    public static List<Data> readFromDB(Context context){
        List<Data> DataList=new ArrayList<>();
        DBHelper dbHelper=new DBHelper(context,"List.db",null,1);
        SQLiteDatabase sqLiteDatabase =dbHelper.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.query("List",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                int costs=cursor.getInt(cursor.getColumnIndex("cost"));
                String kind=cursor.getString(cursor.getColumnIndex("kind"));
                String method=cursor.getString(cursor.getColumnIndex("method"));
                String date=cursor.getString(cursor.getColumnIndex("date"));
                Data d=new Data(id,costs,kind,method,date);
                DataList.add(d);
            }while (cursor.moveToNext());
        }
        return DataList;
    }
    public static void addToDB(Context context, Data data){
        /**
         * 传入context，自动添加该event到数据库
         */
        DBHelper dbHelper=new DBHelper(context,"List.db",null,1);
        SQLiteDatabase sqLiteDatabase =dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("costs",data.costs);
        values.put("kind",data.kind);
        values.put("method",data.method);
        values.put("date",data.costs);
        sqLiteDatabase.insert("List",null,values);
    }

    public static void updateToDB(Context context, Data data, int id){
        DBHelper dbHelper=new DBHelper(context,"List.db",null,1);
        SQLiteDatabase sqLiteDatabase =dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("costs",data.costs);
        values.put("kind",data.kind);
        values.put("method",data.method);
        values.put("date",data.costs);
        sqLiteDatabase.update("List",values,"id=?",new String[]{String.valueOf(id)});
    }

    public static void deleteFromDB(Context context,int id){
        DBHelper dbHelper=new DBHelper(context,"List.db",null,1);
        SQLiteDatabase sqLiteDatabase =dbHelper.getWritableDatabase();
        sqLiteDatabase.delete("List","id=?",new String[]{String.valueOf(id)});
    }
}
