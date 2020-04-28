package cn.ppqing.accountskeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                int costs=cursor.getInt(cursor.getColumnIndex("costs"));
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
    public static int getMonthCost(Context context){
        int cost=0;
        List<Data> list = readFromDB(context);
        Date endDate=new Date();
        Date startDate = new Date(endDate.getYear(),endDate.getMonth()-1,endDate.getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate)>=0&&date.compareTo(endDate)<=0){
                    cost+=d.costs;
                }
            }catch (Exception e){
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }

        }
        return cost;
    }

    public static int getYearCost(Context context){
        int cost=0;
        List<Data> list = readFromDB(context);
        Date endDate=new Date();
        Date startDate = new Date(endDate.getYear()-1,endDate.getMonth(),endDate.getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate)>=0&&date.compareTo(endDate)<=0){
                    cost+=d.costs;
                }
            }catch (Exception e){
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }

        }
        return cost;
    }

    public static int get3MonthsCost(Context context){
        int cost=0;
        List<Data> list = readFromDB(context);
        Date endDate=new Date();
        Date startDate = new Date(endDate.getYear(),endDate.getMonth()-3,endDate.getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate)>=0&&date.compareTo(endDate)<=0){
                    cost+=d.costs;
                }
            }catch (Exception e){
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }

        }
        return cost;
    }

    public static int getTodayCost(Context context){
        int cost=0;
        List<Data> list = readFromDB(context);
        Date endDate=new Date();
        Date startDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate)>=0&&date.compareTo(endDate)<=0){
                    cost+=d.costs;
                }
            }catch (Exception e){
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }

        }
        return cost;
    }

}
