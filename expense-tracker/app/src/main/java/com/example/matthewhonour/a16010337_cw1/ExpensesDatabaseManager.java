package com.example.matthewhonour.a16010337_cw1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ExpensesDatabaseManager extends SQLiteOpenHelper
{
    //database naming constants:
    private static String DATABASE_NAME                 = "expense_database.db";
    private static final int DATABASE_VERSION           = 1;                        //needed as the super class requires a version number of the db to run its constructor.
    public static final String DATABASE_TABLE_EXPENSE   = "expenses";

    //database column constants
    private static final String EXPENSE_ID_COLUMN                           = "EXPENSE_ID";
    public static final String EXPENSE_HAS_IT_BEEN_PAID_COLUMN              = "EXPENSE_HAS_IT_BEEN_PAID";
    private static final String EXPENSE_DATE_OF_PURCHASE_COLUMN             = "EXPENSE_DATE_OF_PURCHASE";
    private static final String EXPENSE_DATE_ADDED_TO_SYSTEM_COLUMN         = "EXPENSE_DATE_ADDED_TO_SYSTEM";
    private static final String EXPENSE_DATE_CLAIMED_COLUMN                 = "EXPENSE_DATE_CLAIMED";
    private static final String EXPENSE_SUMMARY_COLUMN                      = "EXPENSE_SUMMARY";
    private static final String EXPENSE_DOES_TOTAL_COST_INCLUDE_VAT_COLUMN  = "EXPENSE_DOES_TOTAL_COST_INCLUDE_VAT";
    private static final String EXPENSE_VAT_COMPONENT_COLUMN                = "EXPENSE_VAT_COMPONENT";
    private static final String EXPENSE_TOTAL_COST_COLUMN                   = "EXPENSE_TOTAL_COST";
    private static final String EXPENSE_ITEM_VALUE_COLUMN                   = "EXPENSE_ITEM_VALUE";

    //SQL query to create the database table for the expenses:
    private static final String CREATE_DATABASE_TABLE_EXPENSE = "CREATE TABLE "
            + DATABASE_TABLE_EXPENSE + "("
            + EXPENSE_ID_COLUMN                             + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EXPENSE_HAS_IT_BEEN_PAID_COLUMN               + " INTEGER,"
            + EXPENSE_DATE_OF_PURCHASE_COLUMN               + " TEXT,"
            + EXPENSE_DATE_ADDED_TO_SYSTEM_COLUMN           + " TEXT,"
            + EXPENSE_DATE_CLAIMED_COLUMN                   + " TEXT,"
            + EXPENSE_SUMMARY_COLUMN                        + " TEXT,"
            + EXPENSE_DOES_TOTAL_COST_INCLUDE_VAT_COLUMN    + " INTEGER,"
            + EXPENSE_VAT_COMPONENT_COLUMN                  + " FLOAT,"
            + EXPENSE_TOTAL_COST_COLUMN                     + " FLOAT,"
            + EXPENSE_ITEM_VALUE_COLUMN                     + " FLOAT);";

    public ExpensesDatabaseManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_DATABASE_TABLE_EXPENSE);  //executes the SQL query that is not a SELECT.
    }//onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS '" + DATABASE_TABLE_EXPENSE + "'");
        onCreate(db);
    }//onUpgrade

    //passing in the query so we can re-use this method depending on what data we need from the db.
    public ArrayList<Expense> getExpenses(String queryBasedOnFilters)
    {
        //readable db method as we are reading from the db
        SQLiteDatabase expenseDatabase = this.getReadableDatabase();
        ArrayList<Expense> listOfExpensesFromDB = new ArrayList<Expense>();
        Cursor recordInFocus = expenseDatabase.rawQuery(queryBasedOnFilters,null);

        while(recordInFocus.moveToNext())   //while there is another record to inspect
        {

            //converting the integer values for the boolean variables back to boolean:
            boolean BOOLEAN_doesTotalCostIncludeVAT = Conversion.convertIntegerToBoolean(recordInFocus.getInt(recordInFocus.getColumnIndex(EXPENSE_DOES_TOTAL_COST_INCLUDE_VAT_COLUMN)));
            boolean BOOLEAN_hasExpenseBeenPaid      = Conversion.convertIntegerToBoolean(recordInFocus.getInt(recordInFocus.getColumnIndex(EXPENSE_HAS_IT_BEEN_PAID_COLUMN)));

            //gets a record from the db and then set it to a expense object
            Expense expenseFromDb = new Expense(
                    BOOLEAN_hasExpenseBeenPaid,
                    recordInFocus.getString(recordInFocus.getColumnIndex(EXPENSE_DATE_ADDED_TO_SYSTEM_COLUMN)),
                    recordInFocus.getString(recordInFocus.getColumnIndex(EXPENSE_DATE_OF_PURCHASE_COLUMN)),
                    recordInFocus.getString(recordInFocus.getColumnIndex(EXPENSE_DATE_CLAIMED_COLUMN)),
                    recordInFocus.getString(recordInFocus.getColumnIndex(EXPENSE_SUMMARY_COLUMN)),
                    BOOLEAN_doesTotalCostIncludeVAT,
                    recordInFocus.getFloat(recordInFocus.getColumnIndex(EXPENSE_VAT_COMPONENT_COLUMN)),
                    recordInFocus.getFloat(recordInFocus.getColumnIndex(EXPENSE_TOTAL_COST_COLUMN)),
                    recordInFocus.getFloat(recordInFocus.getColumnIndex(EXPENSE_ITEM_VALUE_COLUMN)));

            //set the expense id separately as it is not explicitly set in the Expense Constructor.
            //so when a new expense is added in the add expense activity, this method will be called first afterwards.
            //Once the method is called the array list of expenses will have this new expense in it and it will have an id in the database.
            expenseFromDb.setExpenseID(recordInFocus.getInt(recordInFocus.getColumnIndex(EXPENSE_ID_COLUMN)));

            //add the expense just read into the ArrayList:
            listOfExpensesFromDB.add(expenseFromDb);
        }
        return listOfExpensesFromDB;
    }

    public void addExpense(Expense expenseToAdd)
    {
        //writable db method as we are writing to the db
        SQLiteDatabase expenseDatabase = this.getWritableDatabase();
        //this will contain the new expense values
        ContentValues newExpense = new ContentValues();

        //converting the boolean values stored in the new expense object into an integer as the data will be stored as an integer in the database.
        int INTEGER_hasExpenseBeenPaid              = Conversion.convertBooleanToInteger(expenseToAdd.getHasExpenseBeenPaid());
        int INTEGER_doesTotalCostIncludeVAT         = Conversion.convertBooleanToInteger(expenseToAdd.getDoesTotalCostIncludeVAT());

        //setting the new expense values
        newExpense.put(EXPENSE_HAS_IT_BEEN_PAID_COLUMN              ,INTEGER_hasExpenseBeenPaid);
        newExpense.put(EXPENSE_DATE_ADDED_TO_SYSTEM_COLUMN          ,expenseToAdd.getDateAddedToSystem());
        newExpense.put(EXPENSE_DATE_OF_PURCHASE_COLUMN              ,expenseToAdd.getDateOfPurchase());
        newExpense.put(EXPENSE_DATE_CLAIMED_COLUMN                  ,expenseToAdd.getDatePaid());
        newExpense.put(EXPENSE_SUMMARY_COLUMN                       ,expenseToAdd.getSummaryOfExpense());
        newExpense.put(EXPENSE_DOES_TOTAL_COST_INCLUDE_VAT_COLUMN   ,INTEGER_doesTotalCostIncludeVAT);
        newExpense.put(EXPENSE_VAT_COMPONENT_COLUMN                 ,expenseToAdd.getVatComponent());
        newExpense.put(EXPENSE_TOTAL_COST_COLUMN                    ,expenseToAdd.getTotalCost());
        newExpense.put(EXPENSE_ITEM_VALUE_COLUMN                    ,expenseToAdd.getItemValue());
        //insert the new expense into the database
        expenseDatabase.insert(DATABASE_TABLE_EXPENSE,null,newExpense);
    }

    public void updateExpense(Expense expenseToUpdate)
    {
        //writable db method as we are writing to the db
        SQLiteDatabase expenseDatabase = this.getWritableDatabase();
        ContentValues updatedExpense = new ContentValues();

        //converting the boolean values stored in the new expense object into an integer as the data will be stored as an integer in the database.
        int INTEGER_hasExpenseBeenPaid               = Conversion.convertBooleanToInteger(expenseToUpdate.getHasExpenseBeenPaid());
        int INTEGER_doesTotalCostIncludeVAT          = Conversion.convertBooleanToInteger(expenseToUpdate.getDoesTotalCostIncludeVAT());

        //setting the new expense values
        updatedExpense.put(EXPENSE_HAS_IT_BEEN_PAID_COLUMN              ,INTEGER_hasExpenseBeenPaid);
        updatedExpense.put(EXPENSE_DATE_ADDED_TO_SYSTEM_COLUMN          ,expenseToUpdate.getDateAddedToSystem());
        updatedExpense.put(EXPENSE_DATE_OF_PURCHASE_COLUMN              ,expenseToUpdate.getDateOfPurchase());
        updatedExpense.put(EXPENSE_DATE_CLAIMED_COLUMN                  ,expenseToUpdate.getDatePaid());
        updatedExpense.put(EXPENSE_SUMMARY_COLUMN                       ,expenseToUpdate.getSummaryOfExpense());
        updatedExpense.put(EXPENSE_DOES_TOTAL_COST_INCLUDE_VAT_COLUMN   ,INTEGER_doesTotalCostIncludeVAT);
        updatedExpense.put(EXPENSE_VAT_COMPONENT_COLUMN                 ,expenseToUpdate.getVatComponent());
        updatedExpense.put(EXPENSE_TOTAL_COST_COLUMN                    ,expenseToUpdate.getTotalCost());
        updatedExpense.put(EXPENSE_ITEM_VALUE_COLUMN                    ,expenseToUpdate.getItemValue());

        String where        = EXPENSE_ID_COLUMN + " = ?";
        String whereArgs[]  = new String[]{String.valueOf(expenseToUpdate.getExpenseID())};
        //update the record in the database with the new values
        expenseDatabase.update(DATABASE_TABLE_EXPENSE,updatedExpense,where,whereArgs);
    }
}
