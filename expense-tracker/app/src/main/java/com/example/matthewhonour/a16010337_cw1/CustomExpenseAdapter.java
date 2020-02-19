package com.example.matthewhonour.a16010337_cw1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class CustomExpenseAdapter extends ArrayAdapter<Expense>
{
    //class to hold the views on the list view object
    private static class ViewHolder
    {
        TextView    tvExpenseID;
        TextView    tvHasExpenseBeenPaid;
        TextView    tvDateAdded;
        EditText    etDateClaimed;
        TextView    tvDatePurchased;
        TextView    tvSummary;
        TextView    tvTotalCost;
        TextView    tvDoesItemValueIncludeVAT;
        TextView    tvVAT;
        Button      btnMarkAsPaid;
    }
    public CustomExpenseAdapter(Context context, ArrayList<Expense> expenses)
    {
        super(context,0,expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //constants
        final String EMPTY_STRING       = "";
        final String ENGLISH_LANGUAGE   ="English";
        final Locale englishLocale      = new Locale(ENGLISH_LANGUAGE);

        //sets the value of the expense
        Expense expense = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) //if no view is being recycled and a new list object needs to be made then:
        {
            //this is run when there are not enough list view objects to fill the screen.
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.expense_list_object,parent,false);

            viewHolder.tvExpenseID                = (TextView)convertView.findViewById(R.id.tvExpenseID);
            viewHolder.tvHasExpenseBeenPaid       = (TextView) convertView.findViewById(R.id.tvHasExpenseBeenPaid);
            viewHolder.tvDateAdded                = (TextView)convertView.findViewById(R.id.tvDateAdded);
            viewHolder.etDateClaimed              = (EditText)convertView.findViewById(R.id.etDateClaimed);
            viewHolder.tvDatePurchased            = (TextView)convertView.findViewById(R.id.tvDatePurchased);
            viewHolder.tvSummary                  = (TextView)convertView.findViewById(R.id.tvExpenseSummary);
            viewHolder.tvTotalCost                = (TextView)convertView.findViewById(R.id.tvTotalCost);
            viewHolder.tvDoesItemValueIncludeVAT  = (TextView)convertView.findViewById(R.id.tvDoesItemValueIncludeVAT);
            viewHolder.tvVAT                      = (TextView)convertView.findViewById(R.id.tvVAT);
            viewHolder.btnMarkAsPaid              = (Button)convertView.findViewById(R.id.btnMarkAsPaid);

            convertView.setTag(viewHolder);
        }
        else    //if there is a view that can be recycled:
        {
            //i.e. if there is a view Holder to use.
            viewHolder = (ViewHolder)convertView.getTag();
        }

        //adding the values from the expense object into the viewHolder object
        viewHolder.tvExpenseID.setText(String.valueOf(expense.getExpenseID()));
        viewHolder.tvHasExpenseBeenPaid.setText(Boolean.toString(expense.getHasExpenseBeenPaid()));
        viewHolder.tvDateAdded.setText(expense.getDateAddedToSystem());
        viewHolder.etDateClaimed.setText(expense.getDatePaid());
        viewHolder.tvDatePurchased.setText(expense.getDateOfPurchase());
        viewHolder.tvSummary.setText(expense.getSummaryOfExpense());

        viewHolder.tvTotalCost.setText(String.format(englishLocale,"%.2f",expense.getTotalCost()));
        viewHolder.tvDoesItemValueIncludeVAT.setText(Boolean.toString(expense.getDoesTotalCostIncludeVAT()));
        viewHolder.tvVAT.setText(String.format(englishLocale,"%.2f",expense.getVatComponent()));

        String dateClaimed = viewHolder.etDateClaimed.getText().toString();

        //setting the views to be enabled/ disabled depending on the contents of date claimed.
        if(dateClaimed.matches(EMPTY_STRING))
        {
            viewHolder.etDateClaimed.setEnabled(true);
            viewHolder.btnMarkAsPaid.setEnabled(true);
        }
        else
        {
            viewHolder.etDateClaimed.setEnabled(false);
            viewHolder.btnMarkAsPaid.setEnabled(false);
        }
        return convertView;
    }
}
