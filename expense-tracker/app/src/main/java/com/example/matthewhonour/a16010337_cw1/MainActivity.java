package com.example.matthewhonour.a16010337_cw1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //constants to be used in the activity code:
    final static    int     REQUEST_IF_NEW_EXPENSE_WAS_ADDED_CODE   = 0;
    final int               SHOW_ALL_EXPENSES                       = 0;
    final int               ONLY_SHOW_PAID_EXPENSES                 = 1;
    final int               ONLY_SHOW_UNPAID_EXPENSES               = 2;
    final int               EXPENSE_UNPAID                          = 0;
    final int               EXPENSE_PAID                            = 1;
    final static    String  EMPTY_STRING                            ="";
    //stores expenses from database
    public ArrayList<Expense> expenses                  = new ArrayList<Expense>();
    ExpensesDatabaseManager expensesDatabaseManager;
    CustomExpenseAdapter expenseAdapter;
    //Objects to store activity views.
    ListView listOfExpenses;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //populates the spinner with the options from the strings.xml
        spinner = (Spinner) findViewById(R.id.sFilters);
        this.populateFilterSpinner();

        //listens for a spinner item to be selected, when an item in the spinner is selected then the populateListView method is run
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                populateListView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        //this is run when the add expense activity returns its RESULT_OK flag.
        if(resultCode == RESULT_OK)
        {
            switch(requestCode)
            {
                case(REQUEST_IF_NEW_EXPENSE_WAS_ADDED_CODE):
                    //if this case statement is run then that means a new expense was added in add_new_expense activity
                    //re-populates the list_view
                    expenses.clear();
                    expenses = expensesDatabaseManager.getExpenses(getQuery(spinner.getSelectedItemPosition()));
                    expenseAdapter = new CustomExpenseAdapter(this,expenses);
                    //set the list view's adapter
                    listOfExpenses.setAdapter(expenseAdapter);
                    break;
            }
        }
    }
    public void btnAddExpense_onClick(View view)
    {
        Intent addExpense = new Intent(this, AddExpense.class);
        //The request code specifies which case is run in onActivityResult
        startActivityForResult(addExpense,REQUEST_IF_NEW_EXPENSE_WAS_ADDED_CODE);
    }
    public void btnMarkAsPaid_onClick(View view)
    {
        TableLayout expenseTable = (TableLayout) view.getParent();
        ListView listViewOfExpenses = (ListView)expenseTable.getParent();
        CustomExpenseAdapter expenseAdapter = (CustomExpenseAdapter) listViewOfExpenses.getAdapter();
        //get the id of the record we are going to update:
        int expensePositionInListView =listViewOfExpenses.getPositionForView(expenseTable);
        Expense updatedExpense = expenseAdapter.getItem(expensePositionInListView);

        TextView dateClaimed = expenseTable.findViewById(R.id.etDateClaimed);
        Button markAsPaid = expenseTable.findViewById(R.id.btnMarkAsPaid);
        //check the input from the user
        if(dateClaimed.getText().toString().matches(EMPTY_STRING))
        {
            dateClaimed.setError(getString(R.string.empty_field_error));
        }
        else if (updatedExpense != null) {
            if(!UserInputChecks.checkDateInput(dateClaimed.getText().toString()))
            {
                dateClaimed.setError(getString(R.string.date_format_error));
            }
            else
            {
                //update the specified record in the database
                updatedExpense.setHasExpenseBeenPaid(true);
                updatedExpense.setDatePaid(dateClaimed.getText().toString());
                expensesDatabaseManager.updateExpense(updatedExpense);
                Toast toast = Toast.makeText(this,"The expense at position: "+expensePositionInListView+" has been updated",Toast.LENGTH_SHORT);
                toast.show();

                //set the controls that need to be disabled
                dateClaimed.setEnabled(false);
                markAsPaid.setEnabled(false);
            }
        }
    }
    public void populateListView(int position)
    {
        //determine list view
        listOfExpenses = (ListView)findViewById(R.id.lvExpenses);
        //populate the array list of expenses
        expensesDatabaseManager = new ExpensesDatabaseManager(MainActivity.this);
        expenses = expensesDatabaseManager.getExpenses(this.getQuery(position));
        //create an array adapter object
        expenseAdapter = new CustomExpenseAdapter(MainActivity.this,expenses);
        //set the list view's adapter
        listOfExpenses.setAdapter(expenseAdapter);
    }
    public void populateFilterSpinner()
    {
        //populates the filter spinner based on strings stored in the strings.xml file
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filter_spinner_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }
    public String getQuery(int index)
    {
        //determines the query to send to the db
        String query = "";
        if(index == SHOW_ALL_EXPENSES)
        {
            query = "SELECT * FROM "+ExpensesDatabaseManager.DATABASE_TABLE_EXPENSE;
        }

        if(index == ONLY_SHOW_PAID_EXPENSES)
        {
            query = "SELECT * FROM "+ExpensesDatabaseManager.DATABASE_TABLE_EXPENSE +" WHERE "+ ExpensesDatabaseManager.EXPENSE_HAS_IT_BEEN_PAID_COLUMN + " = "+EXPENSE_PAID;
        }

        if(index == ONLY_SHOW_UNPAID_EXPENSES)
        {
            query = "SELECT * FROM "+ExpensesDatabaseManager.DATABASE_TABLE_EXPENSE +" WHERE "+ ExpensesDatabaseManager.EXPENSE_HAS_IT_BEEN_PAID_COLUMN + " = "+EXPENSE_UNPAID;
        }
        return query;
    }
}

