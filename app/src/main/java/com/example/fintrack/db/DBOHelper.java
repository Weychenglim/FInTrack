package com.example.fintrack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.fintrack.R;

public class DBOHelper extends SQLiteOpenHelper {
    int id;
    int imageId; // selected
    int simageId; // not selected

    int kind;

    public DBOHelper(@Nullable Context context){
        super(context, "FinTrack.db", null, 2);
    }

//  Database will be created when we run it the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE table typetb(id integer primary key autoincrement, typename varchar(10), imageId integer, sImageId integer, kind integer)";
        db.execSQL(sql);
        insertType(db);
        sql = "CREATE table accounttb(id integer primary key autoincrement, typename varchar(10), sImageId integer,  remark varchar(80), money double, time varchar(60), year integer, month integer, day integer, kind integer)";
        db.execSQL(sql);
        sql = "CREATE table savingtb(id integer primary key autoincrement, goaltitle varchar(50), amount double, duration integer, priority integer, creation_date DATE, amountleft double, percentage double, status varchar(20),image_uri TEXT)";
        db.execSQL(sql);
        sql = "CREATE table savingtransactiontb(id integer primary key autoincrement, amount double,  transaction_date DATETIME,saving_id integer, FOREIGN KEY (saving_id) REFERENCES savingtb (id))";
        db.execSQL(sql);
        sql = "CREATE table tipstb(id integer primary key autoincrement, type varchar(20), title varchar (80) , imageId integer, webLink TEXT)";
        db.execSQL(sql);
        insetTip(db);
    }

    private void insetTip(SQLiteDatabase db) {
        String sql = "insert into tipstb(type, title, imageID, webLink) values (?,?,?,?)";
        db.execSQL(sql,new Object[]{"Budgeting Tips" , "Budgeting Tips: The 50/30/20 Rule", R.drawable.budgettingtips,"https://www.britannica.com/money/debt-management-program"});
        db.execSQL(sql,new Object[]{"Budgeting Tips" , "Budgeting Tips: Track Every Expense", R.drawable.budgettingtips,"https://www.getsmarteraboutmoney.ca/learning-path/budgeting/tracking-your-expenses/"});
        db.execSQL(sql,new Object[]{"Budgeting Tips" , "Budgeting Tips: Adjust Your Budget Monthly", R.drawable.budgettingtips,"https://www.incharge.org/financial-literacy/budgeting-saving/how-to-make-a-budget/"});
        db.execSQL(sql,new Object[]{"Saving Tips" , "Saving Tips: Build an Emergency Fund", R.drawable.savingtips,"https://www.principal.com.my/en/why-you-need-emergency-fund-and-how-build-it"});
        db.execSQL(sql,new Object[]{"Saving Tips" , "Saving Tips: Automate Savings Contributions", R.drawable.savingtips,"https://www.investopedia.com/terms/a/automatic_savings_plan.asp"});
        db.execSQL(sql,new Object[]{"Saving Tips" , "Saving Tips: Save on Daily Expenses", R.drawable.savingtips,"https://www.nerdwallet.com/article/finance/how-to-save-money"});
        db.execSQL(sql,new Object[]{"Debt Management" , "Debt Management: The Debt Snowball Method", R.drawable.debtmanagement,"https://www.nerdwallet.com/article/finance/what-is-a-debt-snowball#:~:text=With%20the%20debt%20snowball%20method,a%20snowball%20down%20a%20hill."});
        db.execSQL(sql,new Object[]{"Debt Management" , "Debt Management: Negotiate Lower Interest Rates", R.drawable.debtmanagement,"https://www.experian.com/blogs/ask-experian/can-i-negotiate-a-lower-interest-rate-on-my-credit-card/"});
        db.execSQL(sql,new Object[]{"Debt Management" , "Debt Management: Consolidate Debt", R.drawable.debtmanagement,"https://www.investopedia.com/terms/d/debtconsolidation.asp"});
        db.execSQL(sql,new Object[]{"Investing Tips" , "Investing Tips: Start Small but Start Now", R.drawable.investmenttips,"https://www.frankbyocbc.com/blog/dont-sleep-on-investing-why-you-should-invest-now-and-not-later"});
        db.execSQL(sql,new Object[]{"Investing Tips" , "Investing Tips: Diversify Your Investments", R.drawable.investmenttips,"https://www.investopedia.com/articles/03/072303.asp"});
        db.execSQL(sql,new Object[]{"Investing Tips" , "Investing Tips: Understand Risk Tolerance", R.drawable.investmenttips,"https://corporatefinanceinstitute.com/resources/wealth-management/risk-tolerance/#:~:text=What%20is%20Risk%20Tolerance%3F,investor%20can%20afford%20to%20take."});
        db.execSQL(sql,new Object[]{"Shopping Smart" , "Shopping Smart: Use Cashback Apps or Coupons", R.drawable.shoppingsmart,"https://www.deliberatedirections.com/unlock-cash-back-shop-smart-save-big/"});
        db.execSQL(sql,new Object[]{"Shopping Smart" , "Shopping Smart: Compare Prices Before Purchasing", R.drawable.shoppingsmart,"https://www.digitalhill.com/blog/the-importance-of-comparison-shopping/"});
        db.execSQL(sql,new Object[]{"Shopping Smart" , "Shopping Smart: Delay Non-Essential Purchases", R.drawable.shoppingsmart,"https://inews.co.uk/inews-lifestyle/money/how-to-save-money-financial-expert-delete-card-details-1553026?srsltid=AfmBOopIPBDc_xZ2KWZEni_coQy4-y3A0uG6Mhua54xmzwxXZoa3LfgX"});

    }

    private void insertType(SQLiteDatabase db) {
        String sql = "insert into typetb(typename, imageId, sImageId, kind) values (?,?,?,?)";
        db.execSQL(sql,new Object []{"Other", R.mipmap.ic_other, R.mipmap.ic_other_fs, 0});
        db.execSQL(sql,new Object []{"Dining", R.mipmap.ic_dining, R.mipmap.ic_dining_fs, 0});
        db.execSQL(sql,new Object []{"Transportation", R.mipmap.ic_transportation, R.mipmap.ic_transportation_fs, 0});
        db.execSQL(sql,new Object []{"Shopping", R.mipmap.ic_shopping, R.mipmap.ic_shopping_fs, 0});
        db.execSQL(sql,new Object []{"Clothing", R.mipmap.ic_clothing, R.mipmap.ic_clothing_fs, 0});
        db.execSQL(sql,new Object []{"Essentials", R.mipmap.ic_essentials, R.mipmap.ic_essentials_fs, 0});
        db.execSQL(sql,new Object []{"Entertainment", R.mipmap.ic_entertainment, R.mipmap.ic_entertainment_fs, 0});
        db.execSQL(sql,new Object []{"Snacks", R.mipmap.ic_snacks, R.mipmap.ic_snacks_fs, 0});
        db.execSQL(sql,new Object []{"Lifestyle", R.mipmap.ic_lifestyle, R.mipmap.ic_lifestyle_fs, 0});
        db.execSQL(sql,new Object []{"Education ", R.mipmap.ic_education, R.mipmap.ic_education_fs, 0});
        db.execSQL(sql,new Object []{"Housing", R.mipmap.ic_housing, R.mipmap.ic_housing_fs, 0});
        db.execSQL(sql,new Object []{"Utilities", R.mipmap.ic_utilities, R.mipmap.ic_utilities_fs, 0});
        db.execSQL(sql,new Object []{"Communication", R.mipmap.ic_communication, R.mipmap.ic_communication_fs, 0});
        db.execSQL(sql,new Object []{"Gifts", R.mipmap.ic_gifts, R.mipmap.ic_gifts_fs, 0});
        db.execSQL(sql,new Object []{"Ingredients", R.mipmap.ic_ingredients, R.mipmap.ic_ingredients_fs, 0});

        db.execSQL(sql,new Object []{"Other", R.mipmap.in_other2, R.mipmap.in_other2_fs, 1});
        db.execSQL(sql,new Object []{"Salary", R.mipmap.in_salary, R.mipmap.in_salary_fs, 1});
        db.execSQL(sql,new Object []{"Bonus", R.mipmap.in_bonus, R.mipmap.in_bonus_fs, 1});
        db.execSQL(sql,new Object []{"Loan", R.mipmap.in_loan, R.mipmap.in_loan_fs, 1});
        db.execSQL(sql,new Object []{"Collection", R.mipmap.in_collection, R.mipmap.in_collection_fs, 1});
        db.execSQL(sql,new Object []{"Interest", R.mipmap.in_interest, R.mipmap.in_interest_fs, 1});
        db.execSQL(sql,new Object []{"Investment", R.mipmap.in_invesment, R.mipmap.in_investment_fs, 1});
        db.execSQL(sql,new Object []{"Resale", R.mipmap.in_resale, R.mipmap.in_resale_fs, 1});
        db.execSQL(sql,new Object []{"Windfall", R.mipmap.in_windfall, R.mipmap.in_windfall_fs, 1});
    }

    // Used when updating the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create the tipstb table
            String createTipsTable = "CREATE table tipstb(" +
                    "id integer primary key autoincrement, " +
                    "type varchar(20), " +
                    "title varchar(80), " +
                    "imageId integer, " +
                    "webLink TEXT)";
            db.execSQL(createTipsTable);

            // Insert initial tips data
            insetTip(db);
        }

    }
}
