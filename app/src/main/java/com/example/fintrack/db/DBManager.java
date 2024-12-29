package com.example.fintrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.fintrack.utils.doubleUtils;
import com.example.fintrack.overview.OverviewItemType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        // Adjust the SQL query to sort by kind ASC, then time DESC, and finally id ASC
        String sql = "select * from accounttb where year=? and month=? and day=? order by kind asc, time desc, id asc";

        // Execute the query
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String remark = cursor.getString(cursor.getColumnIndexOrThrow("remark"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));

            // Construct AccountItem object and add it to the list
            AccountItem accountItem = new AccountItem(id, typename, sImageId, remark, time, money, year, month, day, kind);
            list.add(accountItem);
        }

        cursor.close();
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

    public static void deleteFromSavingTbById(int savingId) {
        String sql = "DELETE FROM savingtb WHERE id = ?";
        db.execSQL(sql, new Object[]{savingId});
    }

    public static void deleteFromSavingTransactionTbById(int savingId) {
        String sql = "DELETE FROM savingtransactiontb WHERE saving_id = ?";
        db.execSQL(sql, new Object[]{savingId});
    }

    public static void deleteFromSavingTransactionTbBySavingTransactionId(int savingTransactionId) {
        String sql = "DELETE FROM savingtransactiontb WHERE id = ?";
        db.execSQL(sql, new Object[]{savingTransactionId});
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

    public static List<TipsItem> getTipsList() {
        List<TipsItem> list = new ArrayList<>();
        String query = "SELECT type, title, imageId, webLink FROM tipstb";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int imageID = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
                String webLink = cursor.getString(cursor.getColumnIndexOrThrow("webLink"));

                TipsItem tipsItem = new TipsItem(title, type, webLink, imageID);
                list.add(tipsItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }


    public static double getTotalTransactionsForSaving(int savingId) {
        String sql = "SELECT SUM(amount) AS total FROM savingtransactiontb WHERE saving_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(savingId)});
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        cursor.close();
        return total;
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

    public static List<AccountItem> getSortedAccountList() {
        List<AccountItem> accountList = new ArrayList<>();

        // Query to fetch and sort data
        String query = "SELECT typename, remark, money, time, year, month, day, kind " +
                "FROM accounttb ORDER BY kind, year, month, day, time";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                AccountItem item = new AccountItem();
                item.setTypename(cursor.getString(0));
                item.setRemark(cursor.getString(1));
                item.setMoney(cursor.getDouble(2));
                item.setTime(cursor.getString(3));
                item.setYear(cursor.getInt(4));
                item.setMonth(cursor.getInt(5));
                item.setDay(cursor.getInt(6));
                item.setKind(cursor.getInt(7));
                accountList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accountList;
    }

    public static void saveSavingGoal(Context context, String title, String amountStr,String durationStr, String selectedPriority, String imageURI) {

        if (title.isEmpty() || amountStr.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(context , "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }else if(selectedPriority == null){
            Toast.makeText(context , "Please select a priority for your goal!", Toast.LENGTH_SHORT).show();
            return;
        } else if (amountStr.equals(0) || durationStr.equals(0)) {
            Toast.makeText(context , "Please enter a valid amount/duration for your goal!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Parse the values
            double amount = Double.parseDouble(amountStr);
            int duration = Integer.parseInt(durationStr);

            int priority = selectedPriority.equals("High") ? 1 : selectedPriority.equals("Normal") ? 2 : 3;

            String sql = "INSERT INTO savingtb (goaltitle, amount, duration, priority, creation_date, amountleft, percentage,image_uri) " +
                    "VALUES (?, ?, ?, ?, date('now'), ?, 0.0,?)";
            db.execSQL(sql, new Object[]{title, amount, duration, priority, amount,imageURI});
            Toast.makeText(context, "Saving goal created!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            // Show a Toast message for invalid input
            Toast.makeText(context, "Please enter valid numerical values for amount and duration.", Toast.LENGTH_SHORT).show();
        }

    }

    public static List<SavingItem> getSavingGoals() {
        List<SavingItem> list = new ArrayList<>();
        String sql = "SELECT id, goaltitle, amount, amountleft, duration, priority, creation_date, status,image_uri " +
                "FROM savingtb " +
                "ORDER BY " +
                "CASE status " +
                "   WHEN 'Expired' THEN 1 " +
                "   WHEN 'Active' THEN 2 " +
                "   WHEN 'Completed' THEN 3 " +
                "   ELSE 4 " + // Fallback for unexpected statuses
                "END, " +
                "priority ASC, id DESC";
        Cursor cursor = db.rawQuery(sql, null);

        long currentTimeMillis = System.currentTimeMillis();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String goalTitle = cursor.getString(cursor.getColumnIndexOrThrow("goaltitle"));
            double goalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            double amountLeft = cursor.getDouble(cursor.getColumnIndexOrThrow("amountleft"));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority"));
            String creationDateStr = cursor.getString(cursor.getColumnIndexOrThrow("creation_date"));
            String currentStatus = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_uri"));

            // Calculate amount completed
            double amountCompleted = goalAmount - amountLeft;

            // Parse creation_date
            Calendar creationDate = Calendar.getInstance();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                creationDate.setTime(sdf.parse(creationDateStr));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            // Calculate days left
            long durationMillis = duration * 24L * 60 * 60 * 1000; // Duration in milliseconds
            long endDateMillis = creationDate.getTimeInMillis() + durationMillis;
            long timeLeftMillis = endDateMillis - currentTimeMillis;

            // Adding a half-day offset to round up if there is a partial day
            long daysLeft = (timeLeftMillis + (12 * 60 * 60 * 1000)) / (24 * 60 * 60 * 1000);

            // Determine status
            String newStatus = currentStatus;
            if (amountCompleted >= goalAmount) {
                newStatus = "Completed";
            } else if (daysLeft <= 0) {
                newStatus = "Expired";
            } else {
                newStatus = "Active";
            }

            // Update status in database if it has changed
            if (!newStatus.equals(currentStatus)) {
                String updateStatusSql = "UPDATE savingtb SET status = ? WHERE id = ?";
                db.execSQL(updateStatusSql, new Object[]{newStatus, id});
            }

            // Calculate percentage
            double percentage = (goalAmount > 0) ? (amountCompleted / goalAmount) * 100 : 0;

            // Create SavingItem instance
            SavingItem savingItem = new SavingItem(
                    id, goalTitle, amountCompleted, String.valueOf(daysLeft),
                    priority, percentage, goalAmount, amountLeft, newStatus,imageUri
            );
            list.add(savingItem);
        }

        cursor.close(); // Always close the cursor after use
        return list;
    }


    public static void addTransactionRecord(double amount, int savingId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());


        String sql = "INSERT INTO savingtransactiontb ( amount, transaction_date, saving_id) VALUES (?, ?, ?)";

        try {
            db.execSQL(sql, new Object[]{amount, currentDateTime, savingId});
            Log.d("DBManager", "Transaction record added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBManager", "Error while adding transaction record: " + e.getMessage());
        }
    }


    public static List<SavingTransactionItem> getSavingTransaction(int saving_idcurrent) {
        List<SavingTransactionItem> list = new ArrayList<>();
        String sql = "SELECT id, amount, transaction_date FROM savingtransactiontb WHERE saving_id = ? ORDER BY transaction_date DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(saving_idcurrent)});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            String transactionDate = cursor.getString(cursor.getColumnIndexOrThrow("transaction_date"));

            // Create a new SavingTransactionItem and add it to the list
            SavingTransactionItem transaction = new SavingTransactionItem(transactionDate, amount,id);
            list.add(transaction);
        }
        cursor.close(); // Always close the cursor to release resources
        return list;
    }

    public static void updateSavingGoal(int savingId) {
        // Query the total sum of transactions for the given saving_id
        String transactionSumSql = "SELECT SUM(amount) AS totalTransactions FROM savingtransactiontb WHERE saving_id = ?";
        Cursor transactionCursor = db.rawQuery(transactionSumSql, new String[]{String.valueOf(savingId)});

        if (transactionCursor.moveToFirst()) {
            double totalTransactions = transactionCursor.getDouble(transactionCursor.getColumnIndexOrThrow("totalTransactions"));

            // Query the original amount and current amount left for the saving goal
            String savingGoalSql = "SELECT amount FROM savingtb WHERE id = ?";
            Cursor savingGoalCursor = db.rawQuery(savingGoalSql, new String[]{String.valueOf(savingId)});

            if (savingGoalCursor.moveToFirst()) {
                double totalAmount = savingGoalCursor.getDouble(savingGoalCursor.getColumnIndexOrThrow("amount"));

                // Calculate the new amount left
                double updatedAmountLeft = totalAmount - totalTransactions;
                updatedAmountLeft = Math.max(0, updatedAmountLeft); // Ensure it doesn't go below 0

                // Calculate the updated percentage
                double updatedPercentage = ((totalAmount - updatedAmountLeft) / totalAmount) * 100;

                // Update the savingtb table with the new values
                String updateSql = "UPDATE savingtb SET amountleft = ?, percentage = ? WHERE id = ?";
                db.execSQL(updateSql, new Object[]{updatedAmountLeft, updatedPercentage, savingId});
            }

            savingGoalCursor.close(); // Close the cursor for savingtb
        }

        transactionCursor.close(); // Close the cursor for savingtransactiontb
    }



}

