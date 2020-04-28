package cn.ppqing.accountskeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                String remarks=cursor.getString(cursor.getColumnIndex("remarks"));
                Data d=new Data(id,costs,kind,method,date,remarks);
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
        values.put("date",data.date);
        values.put("remarks",data.remarks);
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
        values.put("remarks",data.remarks);
        sqLiteDatabase.update("List",values,"id=?",new String[]{String.valueOf(id)});
    }

    public static void deleteFromDB(Context context,int id){
        DBHelper dbHelper=new DBHelper(context,"List.db",null,1);
        SQLiteDatabase sqLiteDatabase =dbHelper.getWritableDatabase();
        sqLiteDatabase.delete("List","id=?",new String[]{String.valueOf(id)});
    }

    public static int [] getMonthCost(Context context){
        int cost[]=new int[3];
        List<Data> list = readFromDB(context);
        Date today=new Date();
        Calendar endDate=Calendar.getInstance();;
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.MONTH,-1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    cost[0]+=d.costs;
                    if(d.costs<0){
                        cost[2]+=d.costs;
                    }
                    else {
                        cost[1]+=d.costs;
                    }
                }
            }catch (Exception e){
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }

        }
        return cost;
    }

    public static List<Data> ThisMonthMethodCost(Context context){
        int cost_cash =0, cost_wechat =0, cost_alipay =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date today=new Date();
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.MONTH,-1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    if (d.method.equals("现金") && d.costs<0) {
                        cost_cash += -(d.costs);
                    }
                    if (d.method.equals("微信") && d.costs<0) {
                        cost_wechat += -(d.costs);
                    }
                    if (d.method.equals("支付宝") && d.costs<0) {
                        cost_alipay += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(cost_cash, "现金");
        Data b=new Data(cost_wechat, "微信");
        Data c=new Data(cost_alipay, "支付宝");
        if(cost_cash!=0){
            cost.add(a);
        }
        if(cost_wechat!=0){
            cost.add(b);
        }
        if(cost_alipay!=0){
            cost.add(c);
        }
        return cost;
    }

    public static List<Data> ThisMonthKindCost(Context context){
        int cost_cash =0, cost_wechat =0, cost_alipay =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date today=new Date();
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.MONTH,-1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    if (d.kind.equals("饮食") && d.costs<0) {
                        cost_cash += -(d.costs);
                    }
                    if (d.kind.equals("理财") && d.costs<0) {
                        cost_wechat += -(d.costs);
                    }
                    if (d.kind.equals("出行") && d.costs<0) {
                        cost_alipay += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(cost_cash, "饮食");
        Data b=new Data(cost_wechat, "理财");
        Data c=new Data(cost_alipay, "出行");
        if(cost_cash!=0){
            cost.add(a);
        }
        if(cost_wechat!=0){
            cost.add(b);
        }
        if(cost_alipay!=0){
            cost.add(c);
        }
        return cost;
    }

    public static int [] getYearCost(Context context){
        int[] cost=new int[3];
        List<Data> list = readFromDB(context);
        Date today=new Date();
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.YEAR,-1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    cost[0]+=d.costs;
                    if(d.costs<0){
                        cost[2]+=d.costs;
                    }
                    else {
                        cost[1]+=d.costs;
                    }
                }
            }catch (Exception e){
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }

        }
        return cost;
    }

    public static List<Data> YearMethodCost(Context context){
        int cost_cash =0, cost_wechat =0, cost_alipay =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date today=new Date();
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.YEAR,-1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    if (d.method.equals("现金") && d.costs<0) {
                        cost_cash += -(d.costs);
                    }
                    if (d.method.equals("微信") && d.costs<0) {
                        cost_wechat += -(d.costs);
                    }
                    if (d.method.equals("支付宝") && d.costs<0) {
                        cost_alipay += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(cost_cash, "现金");
        Data b=new Data(cost_wechat, "微信");
        Data c=new Data(cost_alipay, "支付宝");
        if(cost_cash!=0){
            cost.add(a);
        }
        if(cost_wechat!=0){
            cost.add(b);
        }
        if(cost_alipay!=0){
            cost.add(c);
        }
        return cost;
    }

    public static List<Data> YearKindCost(Context context){
        int cost_cash =0, cost_wechat =0, cost_alipay =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date today=new Date();
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.YEAR,-1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    if (d.kind.equals("饮食") && d.costs<0) {
                        cost_cash += -(d.costs);
                    }
                    if (d.kind.equals("理财") && d.costs<0) {
                        cost_wechat += -(d.costs);
                    }
                    if (d.kind.equals("出行") && d.costs<0) {
                        cost_alipay += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(cost_cash, "饮食");
        Data b=new Data(cost_wechat, "理财");
        Data c=new Data(cost_alipay, "出行");
        if(cost_cash!=0){
            cost.add(a);
        }
        if(cost_wechat!=0){
            cost.add(b);
        }
        if(cost_alipay!=0){
            cost.add(c);
        }
        return cost;
    }

    public static int [] get3MonthsCost(Context context){
        int [] cost=new int[3];
        List<Data> list = readFromDB(context);
        Date today=new Date();
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.MONTH,-3);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    cost[0]+=d.costs;
                    if(d.costs<0){
                        cost[2]+=d.costs;
                    }
                    else {
                        cost[1]+=d.costs;
                    }
                }
            }catch (Exception e){
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }

        }
        return cost;
    }

    public static List<Data> TMonthsMethodCost(Context context){
        int cost_cash =0, cost_wechat =0, cost_alipay =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date today=new Date();
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.MONTH,-3);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    if (d.method.equals("现金") && d.costs<0) {
                        cost_cash += -(d.costs);
                    }
                    if (d.method.equals("微信") && d.costs<0) {
                        cost_wechat += -(d.costs);
                    }
                    if (d.method.equals("支付宝") && d.costs<0) {
                        cost_alipay += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(cost_cash, "现金");
        Data b=new Data(cost_wechat, "微信");
        Data c=new Data(cost_alipay, "支付宝");
        if(cost_cash!=0){
            cost.add(a);
        }
        if(cost_wechat!=0){
            cost.add(b);
        }
        if(cost_alipay!=0){
            cost.add(c);
        }
        return cost;
    }

    public static List<Data> TMonthsKindCost(Context context){
        int cost_cash =0, cost_wechat =0, cost_alipay =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date today=new Date();
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(today);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(today);
        startDate.add(Calendar.MONTH,-3);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(startDate.getTime())>=0&&date.compareTo(endDate.getTime())<=0){
                    if (d.kind.equals("饮食") && d.costs<0) {
                        cost_cash += -(d.costs);
                    }
                    if (d.kind.equals("理财") && d.costs<0) {
                        cost_wechat += -(d.costs);
                    }
                    if (d.kind.equals("出行") && d.costs<0) {
                        cost_alipay += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(cost_cash, "饮食");
        Data b=new Data(cost_wechat, "理财");
        Data c=new Data(cost_alipay, "出行");
        if(cost_cash!=0){
            cost.add(a);
        }
        if(cost_wechat!=0){
            cost.add(b);
        }
        if(cost_alipay!=0){
            cost.add(c);
        }
        return cost;
    }

    public static int [] getTodayCost(Context context){
        int[] cost=new int[3];
        List<Data> list = readFromDB(context);
        Date endDate=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                Date date=formatter.parse(d.date);
                if (date.compareTo(endDate)==0){
                    cost[0]+=d.costs;
                    if(d.costs<0){
                        cost[2]+=d.costs;
                    }
                    else {
                        cost[1]+=d.costs;
                    }
                }
            }catch (Exception e){
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }

        }
        return cost;
    }

    public static List<Data> TodayMethodCost(Context context){
        int cost_cash =0, cost_wechat =0, cost_alipay =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date endDate=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strEndDate=formatter.format(endDate);

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                if (d.date.equals(strEndDate)){
                    if (d.method.equals("现金") && d.costs<0) {
                        cost_cash += -(d.costs);
                    }
                    if (d.method.equals("微信") && d.costs<0) {
                        cost_wechat += -(d.costs);
                    }
                    if (d.method.equals("支付宝") && d.costs<0) {
                        cost_alipay += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(cost_cash, "现金");
        Data b=new Data(cost_wechat, "微信");
        Data c=new Data(cost_alipay, "支付宝");
        if(cost_cash!=0){
            cost.add(a);
        }
        if(cost_wechat!=0){
            cost.add(b);
        }
        if(cost_alipay!=0){
            cost.add(c);
        }
        return cost;
    }

    public static List<Data> TodayKindCost(Context context){
        int cost_cash =0, cost_wechat =0, cost_alipay =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date endDate=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strEndDate=formatter.format(endDate);

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                if (d.date.equals(strEndDate)){
                    if (d.kind.equals("饮食") && d.costs<0) {
                        cost_cash += -(d.costs);
                    }
                    if (d.kind.equals("理财") && d.costs<0) {
                        cost_wechat += -(d.costs);
                    }
                    if (d.kind.equals("出行") && d.costs<0) {
                        cost_alipay += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(cost_cash, "饮食");
        Data b=new Data(cost_wechat, "理财");
        Data c=new Data(cost_alipay, "出行");
        if(cost_cash!=0){
            cost.add(a);
        }
        if(cost_wechat!=0){
            cost.add(b);
        }
        if(cost_alipay!=0){
            cost.add(c);
        }
        return cost;
    }
}