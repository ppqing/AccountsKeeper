package cn.ppqing.accountskeeper;
import java.util.*;
import java.text.SimpleDateFormat;
public class Data {
    public  static double rate=1.094;
    public int id=-1;
    public int costs;
    public String kind;
    public String method;
    public String date;
    public String remarks;//备注
    public Data(int costs,String kind,String method,String date,String remarks){
        this.costs=costs;
        this.kind = kind;
        this.method = method;
        this.date = date;
        this.remarks = remarks;
    }
    public Data(int id,int costs,String kind,String method,String date,String remarks){
        this.id=id;
        this.costs=costs;
        this.kind = kind;
        this.method = method;
        this.date = date;
        this.remarks = remarks;
    }
    public Data(int costs,String method){
        this.costs=costs;
        this.method = method;
    }

    public void getmonth(){
    }
    public void getYear(){

    }

    public static String getDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
}

   /* public void setDate(Date d){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        this.date = dateformat.format(d);
    }*/

}
