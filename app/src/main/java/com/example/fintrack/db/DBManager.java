package com.example.fintrack.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fintrack.doubleUtils;
import com.example.fintrack.overview.OverviewItemType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static SQLiteDatabase db;

    public static void initDB(Context context) {
        DBOHelper helper = new DBOHelper(context);
        db = helper.getWritableDatabase();
    }


    public static List<TypeItem> getTypeList(int kind) {
        List<TypeItem> list = new ArrayList<>();

        String sql = "select * from typetb where kind =" + kind;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int simageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            TypeItem typeItem = new TypeItem(id, typename, imageId, simageId, kind);
            list.add(typeItem);
        }

        return list;
    }

    /*
     * Insert a tuple into the database
     * */

    public static void insertItemToAccounttb(AccountItem item) {
        ContentValues values = new ContentValues();
        values.put("typename", item.getTypename());
        values.put("sImageId", item.getSimageId());
        values.put("remark", item.getRemark());
        values.put("money", item.getMoney());
        values.put("time", item.getTime());
        values.put("year", item.getYear());
        values.put("month", item.getMonth());
        values.put("day", item.getDay());
        values.put("kind", item.getKind());
        db.insert("accounttb", null, values);

    }

    public static List<AccountItem> getAccountListOneDayFromAccounttb(int year, int month, int day) {
        List<AccountItem> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        /*Each ? is a placeholder for a parameter, and new String[]{year + "", month + "", day + "", kind + ""} provides
        values for each ?. If year = 2023, month = 11, day = 10, and kind = 1, the placeholders will be replaced
        with "2023", "11", "10", and "1", respectively.*/
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String remark = cursor.getString(cursor.getColumnIndexOrThrow("remark"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));
            AccountItem accountItem = new AccountItem(id, typename, sImageId, remark, time, money, year, month, day, kind);
            list.add(accountItem);
        }
        return list;
    }

    public static double getSumMoneyPerDay(int year, int month, int day, int kind) {
        double total = 0.0;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        if (cursor.moveToFirst()) {
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("sum(money)"));
            total = Double.parseDouble(String.format("%.2f", money));
        }

        return total;
    }

    public static double getSumMoneyPerMonth(int year, int month, int kind) {
        double total = 0.0;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("sum(money)"));
            total = Double.parseDouble(String.format("%.2f", money));
        }

        return total;
    }

    public static double getSumMoneyPerYear(int year, int kind) {
        double total = 0.0;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        if (cursor.moveToFirst()) {
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("sum(money)"));
            total = Double.parseDouble(String.format("%.2f", money));
        }

        return total;
    }

    public static int deleteFromAccountTbById(int id) {
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }

    public static List<AccountItem> getAccountListOneDayFromAccounttbType(int year, int month, int day, int type) {
        List<AccountItem> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? and kind=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", type + ""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String remark = cursor.getString(cursor.getColumnIndexOrThrow("remark"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));
            AccountItem accountItem = new AccountItem(id, typename, sImageId, remark, time, money, year, month, day, kind);
            list.add(accountItem);
        }
        return list;
    }

    public static void deleteAllRecord(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    public static int getCountItemOneMonth(int year, int month, int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[] {year + "", +month + "", + kind + ""});
        if (cursor.moveToFirst()){
            total = cursor.getInt(cursor.getColumnIndexOrThrow("count(money)"));  //COUNT(money) counts how many rows have a non-null value in the money column
        }
        return total;
    }

    public static List<OverviewItemType>getOverviewListFromAccounttb(int year,int month,int type){
        List<OverviewItemType>list = new ArrayList<>();
        double sumMoneyPerMonth = getSumMoneyPerMonth(year,month,type);
        String sql = "select typename,sImageId,sum(money)as total,kind from accounttb where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", type + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            double total = cursor.getFloat(cursor.getColumnIndexOrThrow("total"));
            // total /sumMonth
            double percentage = doubleUtils.div(total,sumMoneyPerMonth);
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            OverviewItemType type1 = new OverviewItemType(sImageId, typename, percentage, total,kind);
            list.add(type1);
        }
        return list;
    }

    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            return money;
        }
        return 0;
    }

    public static List<OverviewItem>getSumMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<OverviewItem>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            OverviewItem itemBean = new OverviewItem(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }


}

