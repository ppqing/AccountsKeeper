package cn.ppqing.accountskeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.ppqing.accountskeeper.Data;
import cn.ppqing.accountskeeper.R;

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
        List<String> method = Arrays.asList(context.getResources().getStringArray(R.array.method_type));
        int Cash =0, Alipay =0, AlipayHK =0, Wechat =0, CreditCard =0, Octopus =0;
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
                    if (d.method.equals(method.get(0)) && d.costs < 0) {
                        Cash += -(d.costs);
                    }
                    if (d.method.equals(method.get(1)) && d.costs < 0) {
                        Alipay += -(d.costs);
                    }
                    if (d.method.equals(method.get(2)) && d.costs < 0) {
                        AlipayHK += -(d.costs);
                    }
                    if (d.method.equals(method.get(3)) && d.costs < 0) {
                        Wechat += -(d.costs);
                    }
                    if (d.method.equals(method.get(4)) && d.costs < 0) {
                        CreditCard += -(d.costs);
                    }
                    if (d.method.equals(method.get(5)) && d.costs < 0) {
                        Octopus += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(Cash, method.get(0));
        Data b=new Data(Alipay, method.get(1));
        Data c=new Data(AlipayHK, method.get(2));
        Data d=new Data(Wechat, method.get(3));
        Data e=new Data(CreditCard, method.get(4));
        Data f=new Data(Octopus, method.get(5));
        if(Cash!=0){
            cost.add(a);
        }
        if(Alipay!=0){
            cost.add(b);
        }
        if(AlipayHK!=0){
            cost.add(c);
        }
        if(Wechat!=0){
            cost.add(d);
        }
        if(CreditCard!=0){
            cost.add(e);
        }
        if(Octopus!=0){
            cost.add(f);
        }
        return cost;
    }

    public static List<Data> ThisMonthKindCost(Context context){
        List<String> kind = Arrays.asList(context.getResources().getStringArray(R.array.Bill_type));
        int Diet =0, Conduct =0, Trip =0, Clothing =0, entertainment =0, Medical =0, Healthcare =0,
                Recharge =0, Hotel =0, Human =0, Else=0;
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
                    if (d.kind.equals(kind.get(0)) && d.costs < 0) {
                        Diet += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(1)) && d.costs < 0) {
                        Conduct += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(2)) && d.costs < 0) {
                        Trip += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(3)) && d.costs < 0) {
                        Clothing += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(4)) && d.costs < 0) {
                        entertainment += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(5)) && d.costs < 0) {
                        Medical += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(6)) && d.costs < 0) {
                        Healthcare += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(7)) && d.costs < 0) {
                        Recharge += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(8)) && d.costs < 0) {
                        Hotel += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(9)) && d.costs < 0) {
                        Human += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(10)) && d.costs < 0) {
                        Else += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(Diet, kind.get(0));
        Data b=new Data(Conduct, kind.get(1));
        Data c=new Data(Trip, kind.get(2));
        Data d=new Data(Clothing, kind.get(3));
        Data e=new Data(entertainment, kind.get(4));
        Data f=new Data(Medical, kind.get(5));
        Data g=new Data(Healthcare, kind.get(6));
        Data h=new Data(Recharge, kind.get(7));
        Data i=new Data(Hotel, kind.get(8));
        Data j=new Data(Human, kind.get(9));
        Data k=new Data(Else, kind.get(10));
        if(Diet!=0){
            cost.add(a);
        }
        if(Conduct!=0){
            cost.add(b);
        }
        if(Trip!=0){
            cost.add(c);
        }
        if(Clothing!=0){
            cost.add(d);
        }
        if(entertainment!=0){
            cost.add(e);
        }
        if(Medical!=0){
            cost.add(f);
        }
        if(Healthcare!=0){
            cost.add(g);
        }
        if(Recharge!=0){
            cost.add(h);
        }
        if(Hotel!=0){
            cost.add(i);
        }
        if(Human!=0){
            cost.add(j);
        }
        if(Else!=0){
            cost.add(k);
        }
        return cost;
    }

    public static int [] getTodayCost(Context context){
        int[] cost=new int[3];
        List<Data> list = readFromDB(context);
        Date endDate=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strEndDate=formatter.format(endDate);
        for (int i = 0; i < list.size(); i++){
            Data d=list.get(i);
            try {
                //Date date=formatter.parse(d.date);
                if (d.date.equals(strEndDate)){
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
        List<String> method = Arrays.asList(context.getResources().getStringArray(R.array.method_type));
        int Cash =0, Alipay =0, AlipayHK =0, Wechat =0, CreditCard =0, Octopus =0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date endDate=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strEndDate=formatter.format(endDate);

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                if (d.date.equals(strEndDate)){
                    if (d.method.equals(method.get(0)) && d.costs < 0) {
                        Cash += -(d.costs);
                    }
                    if (d.method.equals(method.get(1)) && d.costs < 0) {
                        Alipay += -(d.costs);
                    }
                    if (d.method.equals(method.get(2)) && d.costs < 0) {
                        AlipayHK += -(d.costs);
                    }
                    if (d.method.equals(method.get(3)) && d.costs < 0) {
                        Wechat += -(d.costs);
                    }
                    if (d.method.equals(method.get(4)) && d.costs < 0) {
                        CreditCard += -(d.costs);
                    }
                    if (d.method.equals(method.get(5)) && d.costs < 0) {
                        Octopus += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(Cash, method.get(0));
        Data b=new Data(Alipay, method.get(1));
        Data c=new Data(AlipayHK, method.get(2));
        Data d=new Data(Wechat, method.get(3));
        Data e=new Data(CreditCard, method.get(4));
        Data f=new Data(Octopus, method.get(5));
        if(Cash!=0){
            cost.add(a);
        }
        if(Alipay!=0){
            cost.add(b);
        }
        if(AlipayHK!=0){
            cost.add(c);
        }
        if(Wechat!=0){
            cost.add(d);
        }
        if(CreditCard!=0){
            cost.add(e);
        }
        if(Octopus!=0){
            cost.add(f);
        }
        return cost;
    }

    public static List<Data> TodayKindCost(Context context){
        List<String> kind = Arrays.asList(context.getResources().getStringArray(R.array.Bill_type));
        int Diet =0, Conduct =0, Trip =0, Clothing =0, entertainment =0, Medical =0, Healthcare =0,
                Recharge =0, Hotel =0, Human =0, Else=0;
        List<Data> list = readFromDB(context);
        List<Data> cost = new ArrayList<>();

        Date endDate=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strEndDate=formatter.format(endDate);

        for (int i = 0; i < list.size(); i++) {
            Data d = list.get(i);
            try {
                if (d.date.equals(strEndDate)){
                    if (d.kind.equals(kind.get(0)) && d.costs < 0) {
                        Diet += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(1)) && d.costs < 0) {
                        Conduct += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(2)) && d.costs < 0) {
                        Trip += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(3)) && d.costs < 0) {
                        Clothing += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(4)) && d.costs < 0) {
                        entertainment += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(5)) && d.costs < 0) {
                        Medical += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(6)) && d.costs < 0) {
                        Healthcare += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(7)) && d.costs < 0) {
                        Recharge += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(8)) && d.costs < 0) {
                        Hotel += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(9)) && d.costs < 0) {
                        Human += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(10)) && d.costs < 0) {
                        Else += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(Diet, kind.get(0));
        Data b=new Data(Conduct, kind.get(1));
        Data c=new Data(Trip, kind.get(2));
        Data d=new Data(Clothing, kind.get(3));
        Data e=new Data(entertainment, kind.get(4));
        Data f=new Data(Medical, kind.get(5));
        Data g=new Data(Healthcare, kind.get(6));
        Data h=new Data(Recharge, kind.get(7));
        Data i=new Data(Hotel, kind.get(8));
        Data j=new Data(Human, kind.get(9));
        Data k=new Data(Else, kind.get(10));
        if(Diet!=0){
            cost.add(a);
        }
        if(Conduct!=0){
            cost.add(b);
        }
        if(Trip!=0){
            cost.add(c);
        }
        if(Clothing!=0){
            cost.add(d);
        }
        if(entertainment!=0){
            cost.add(e);
        }
        if(Medical!=0){
            cost.add(f);
        }
        if(Healthcare!=0){
            cost.add(g);
        }
        if(Recharge!=0){
            cost.add(h);
        }
        if(Hotel!=0){
            cost.add(i);
        }
        if(Human!=0){
            cost.add(j);
        }
        if(Else!=0){
            cost.add(k);
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
        List<String> method = Arrays.asList(context.getResources().getStringArray(R.array.method_type));
        int Cash =0, Alipay =0, AlipayHK =0, Wechat =0, CreditCard =0, Octopus =0;
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
                    if (d.method.equals(method.get(0)) && d.costs < 0) {
                        Cash += -(d.costs);
                    }
                    if (d.method.equals(method.get(1)) && d.costs < 0) {
                        Alipay += -(d.costs);
                    }
                    if (d.method.equals(method.get(2)) && d.costs < 0) {
                        AlipayHK += -(d.costs);
                    }
                    if (d.method.equals(method.get(3)) && d.costs < 0) {
                        Wechat += -(d.costs);
                    }
                    if (d.method.equals(method.get(4)) && d.costs < 0) {
                        CreditCard += -(d.costs);
                    }
                    if (d.method.equals(method.get(5)) && d.costs < 0) {
                        Octopus += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(Cash, method.get(0));
        Data b=new Data(Alipay, method.get(1));
        Data c=new Data(AlipayHK, method.get(2));
        Data d=new Data(Wechat, method.get(3));
        Data e=new Data(CreditCard, method.get(4));
        Data f=new Data(Octopus, method.get(5));
        if(Cash!=0){
            cost.add(a);
        }
        if(Alipay!=0){
            cost.add(b);
        }
        if(AlipayHK!=0){
            cost.add(c);
        }
        if(Wechat!=0){
            cost.add(d);
        }
        if(CreditCard!=0){
            cost.add(e);
        }
        if(Octopus!=0){
            cost.add(f);
        }
        return cost;
    }

    public static List<Data> TMonthsKindCost(Context context){
        List<String> kind = Arrays.asList(context.getResources().getStringArray(R.array.Bill_type));
        int Diet =0, Conduct =0, Trip =0, Clothing =0, entertainment =0, Medical =0, Healthcare =0,
                Recharge =0, Hotel =0, Human =0, Else=0;
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
                    if (d.kind.equals(kind.get(0)) && d.costs < 0) {
                        Diet += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(1)) && d.costs < 0) {
                        Conduct += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(2)) && d.costs < 0) {
                        Trip += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(3)) && d.costs < 0) {
                        Clothing += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(4)) && d.costs < 0) {
                        entertainment += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(5)) && d.costs < 0) {
                        Medical += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(6)) && d.costs < 0) {
                        Healthcare += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(7)) && d.costs < 0) {
                        Recharge += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(8)) && d.costs < 0) {
                        Hotel += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(9)) && d.costs < 0) {
                        Human += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(10)) && d.costs < 0) {
                        Else += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(Diet, kind.get(0));
        Data b=new Data(Conduct, kind.get(1));
        Data c=new Data(Trip, kind.get(2));
        Data d=new Data(Clothing, kind.get(3));
        Data e=new Data(entertainment, kind.get(4));
        Data f=new Data(Medical, kind.get(5));
        Data g=new Data(Healthcare, kind.get(6));
        Data h=new Data(Recharge, kind.get(7));
        Data i=new Data(Hotel, kind.get(8));
        Data j=new Data(Human, kind.get(9));
        Data k=new Data(Else, kind.get(10));
        if(Diet!=0){
            cost.add(a);
        }
        if(Conduct!=0){
            cost.add(b);
        }
        if(Trip!=0){
            cost.add(c);
        }
        if(Clothing!=0){
            cost.add(d);
        }
        if(entertainment!=0){
            cost.add(e);
        }
        if(Medical!=0){
            cost.add(f);
        }
        if(Healthcare!=0){
            cost.add(g);
        }
        if(Recharge!=0){
            cost.add(h);
        }
        if(Hotel!=0){
            cost.add(i);
        }
        if(Human!=0){
            cost.add(j);
        }
        if(Else!=0){
            cost.add(k);
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
        List<String> method = Arrays.asList(context.getResources().getStringArray(R.array.method_type));
        int Cash =0, Alipay =0, AlipayHK =0, Wechat =0, CreditCard =0, Octopus =0;
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
                    if (d.method.equals(method.get(0)) && d.costs < 0) {
                        Cash += -(d.costs);
                    }
                    if (d.method.equals(method.get(1)) && d.costs < 0) {
                        Alipay += -(d.costs);
                    }
                    if (d.method.equals(method.get(2)) && d.costs < 0) {
                        AlipayHK += -(d.costs);
                    }
                    if (d.method.equals(method.get(3)) && d.costs < 0) {
                        Wechat += -(d.costs);
                    }
                    if (d.method.equals(method.get(4)) && d.costs < 0) {
                        CreditCard += -(d.costs);
                    }
                    if (d.method.equals(method.get(5)) && d.costs < 0) {
                        Octopus += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(Cash, method.get(0));
        Data b=new Data(Alipay, method.get(1));
        Data c=new Data(AlipayHK, method.get(2));
        Data d=new Data(Wechat, method.get(3));
        Data e=new Data(CreditCard, method.get(4));
        Data f=new Data(Octopus, method.get(5));
        if(Cash!=0){
            cost.add(a);
        }
        if(Alipay!=0){
            cost.add(b);
        }
        if(AlipayHK!=0){
            cost.add(c);
        }
        if(Wechat!=0){
            cost.add(d);
        }
        if(CreditCard!=0){
            cost.add(e);
        }
        if(Octopus!=0){
            cost.add(f);
        }
        return cost;
    }

    public static List<Data> YearKindCost(Context context){
        List<String> kind = Arrays.asList(context.getResources().getStringArray(R.array.Bill_type));
        int Diet =0, Conduct =0, Trip =0, Clothing =0, entertainment =0, Medical =0, Healthcare =0,
                Recharge =0, Hotel =0, Human =0, Else=0;
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
                    if (d.kind.equals(kind.get(0)) && d.costs < 0) {
                        Diet += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(1)) && d.costs < 0) {
                        Conduct += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(2)) && d.costs < 0) {
                        Trip += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(3)) && d.costs < 0) {
                        Clothing += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(4)) && d.costs < 0) {
                        entertainment += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(5)) && d.costs < 0) {
                        Medical += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(6)) && d.costs < 0) {
                        Healthcare += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(7)) && d.costs < 0) {
                        Recharge += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(8)) && d.costs < 0) {
                        Hotel += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(9)) && d.costs < 0) {
                        Human += -(d.costs);
                    }
                    if (d.kind.equals(kind.get(10)) && d.costs < 0) {
                        Else += -(d.costs);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "date parse failed", Toast.LENGTH_SHORT).show();
            }
        }
        Data a=new Data(Diet, kind.get(0));
        Data b=new Data(Conduct, kind.get(1));
        Data c=new Data(Trip, kind.get(2));
        Data d=new Data(Clothing, kind.get(3));
        Data e=new Data(entertainment, kind.get(4));
        Data f=new Data(Medical, kind.get(5));
        Data g=new Data(Healthcare, kind.get(6));
        Data h=new Data(Recharge, kind.get(7));
        Data i=new Data(Hotel, kind.get(8));
        Data j=new Data(Human, kind.get(9));
        Data k=new Data(Else, kind.get(10));
        if(Diet!=0){
            cost.add(a);
        }
        if(Conduct!=0){
            cost.add(b);
        }
        if(Trip!=0){
            cost.add(c);
        }
        if(Clothing!=0){
            cost.add(d);
        }
        if(entertainment!=0){
            cost.add(e);
        }
        if(Medical!=0){
            cost.add(f);
        }
        if(Healthcare!=0){
            cost.add(g);
        }
        if(Recharge!=0){
            cost.add(h);
        }
        if(Hotel!=0){
            cost.add(i);
        }
        if(Human!=0){
            cost.add(j);
        }
        if(Else!=0){
            cost.add(k);
        }
        return cost;
    }

}